/**
 * General utility routines for use with the framework
 */
package org.devops.framework.core;

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.nio.file.FileVisitResult
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileTime
import java.nio.file.CopyOption
import java.nio.file.SimpleFileVisitor
import java.security.MessageDigest
import java.net.InetAddress;

import java.io.File
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.io.Serializable;
import java.io.FileOutputStream;

import groovy.json.*
import groovy.xml.*

import hudson.FilePath
import hudson.model.TaskListener
import hudson.remoting.VirtualChannel
import hudson.util.Secret
import hudson.FilePath;
import hudson.Launcher;
import hudson.Proc;
import hudson.Launcher.LocalLauncher;
import hudson.model.StreamBuildListener;
import hudson.model.TaskListener;
import hudson.util.StreamTaskListener
import hudson.EnvVars;
import hudson.model.Computer;

import jenkins.model.Jenkins

import java.util.logging.Logger
import java.util.logging.Level

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

@SuppressFBWarnings(value="SE_NO_SERIALVERSIONID")
class Utilities implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( Utilities.class.getName() )
    private static final long serialVersionUID = 1L;

    static String outputInternalStr = null

    /**
     * Get command output
     */
    public static final String getOutput() {
        return this.outputInternalStr
    }

    /**
     * Set command output
     * @param final String - output
     */
    private static void setOutput(final String output) {
        this.outputInternalStr = output
    }

    /**
     * This private class detects if an operating system is Windows or UNIX
     */
    private static class OsDetector {
        private static String OS = System.getProperty("os.name").toLowerCase(ConfigPropertiesConstants.ROOT_LOCALE);

        private static boolean isWindows() {
            return (OS.indexOf("win") >= 0)
        }

        private static boolean isUnix() {
            return (OS.indexOf("mac") >= 0 || OS.indexOf("nix") >= 0 ||
                    OS.indexOf("nux") >= 0 || OS.indexOf("aix") >= 0 ||
                    OS.indexOf("sunos") >= 0)
        }
    }

    /**
     * This private class runs OS commands and is fundamental to the service support
     */
    private static class cmdRunner {
	   /**
		 * Utility routine to run a shell command. It delegates it to OS or
         * Jenkins based on what environment it is invoked from
		 *
		 * @param final String - Command to run
		 * @param StringBuffer - return message
		 * @param final File - workingDir
		 * @param final Launcher - launcher
		 * @param final boolean - stripQuotes
		 * @param final boolean - isFile
     	 * @param final boolean - noParams
		 * @return int - Exit value
		 */
		static int runCmd(final String cmdStr,
						  StringBuffer returnStr,
						  final File workingDir,
						  final Launcher launcher,
						  final boolean stripQuotes,
						  final boolean isFile,
						  final boolean noParams) {
			final Jenkins jenkins = Jenkins.getInstance()
			boolean isJenkins = (jenkins!=null && jenkins.getRootDir() != null && !jenkins.getRootDir().getAbsolutePath().isEmpty())
			int retStatus = -1
			if (isJenkins || launcher!=null) {
                LOGGER.log(Level.FINEST, "Running CDRunner()")
				retStatus = cmdRunner.CDRunner(cmdStr,returnStr,
				                               workingDir,launcher,
				                               stripQuotes,isFile,
				                               noParams)
			} else {
                LOGGER.log(Level.FINEST, "Running OsRunner()")
				retStatus = cmdRunner.OsRunner(cmdStr,returnStr,
				                               workingDir,isFile)
			}
			return retStatus
		}


        /**
         * Utility routine to run a shell command via Jenkins API
         *
         * @param final String - Command to run
         * @param StringBuffer - return message
         * @param final File - workingDir
         * @param final Launcher - launcher
         * @param final boolean - stripQuotes
         * @param final boolean - isFile
     	 * @param final boolean - noParams
         * @return int - Exit value
         */
        static int CDRunner(final String cmdStr,
                            StringBuffer returnStr,
                            final File workingDir,
                            final Launcher procLauncher,
                            final boolean stripQuotes,
                            final boolean isFile,
                            final boolean noParams) {
            int retStatus = 0

            List<String> args = null

            if (!isFile && !noParams) {
            	args = Utilities.parseArgs(cmdStr,stripQuotes)
			} else {
				args = new ArrayList<String>()
				args.add(cmdStr)
			}

            File tempFile = File.createTempFile("devopsFramework", ".tmp")
            File tempFile1 = File.createTempFile("devopsFramework", ".tmp")
            FileOutputStream fos = new FileOutputStream(tempFile);
            StreamBuildListener os = new StreamBuildListener(fos);
            boolean[] masks = new boolean[args.size];

            // This stream will be ignored, but is needed for the interface...
            FileOutputStream fos1 = new FileOutputStream(tempFile1);
            StreamTaskListener listener = new StreamTaskListener(fos1)
            Launcher launcher = ((procLauncher!=null) ? procLauncher : new LocalLauncher(listener))
            boolean secret = false

			if (!isFile && !noParams) {
				int i = 0;
				if (args.size() > 1) {
					for (String astr : args) {
						if (astr.contains("--username") || astr.contains("--password")) {
							masks[i] = true;
							masks[i + 1] = true;
							secret = true
						} else if (astr.contains("//") && astr.contains(":") && astr.contains("@")) {
							masks[i] = true;
							secret = true
						} else if (astr.contains("-u") && astr.contains(":")) {
							masks[i] = true
							secret = true
						}
						i++;
					}
				}
			} else {
                if (noParams) {
                    masks[0] = true
                }
            }

            String outputStr = null

            if (!secret) {
                LOGGER.log(Level.FINEST, "cmd=\"{0}\"",cmdStr)
                LOGGER.log(Level.FINEST, "cmdArgs=\"{0}\"",args.size())
            }

            try {
                Launcher.ProcStarter ps = launcher.launch();
                if (args.size() == 1) {
	                LOGGER.log(Level.FINEST, "Running as single cmd")
                    ps.cmdAsSingleString(cmdStr)
                } else {
	                LOGGER.log(Level.FINEST, "Running as args")
                    ps.cmds(args)
                }
                ps.stdout(os.getLogger());
                ps.stdin(null);
                if (workingDir) {
                    ps.pwd(workingDir)
                }
                if (args.size() > 1 || noParams) {
                    ps.masks(masks);
                }
                Proc p = ps.start()
                retStatus = p.join()
            } catch(IOException ex) {
                retStatus = 1
                outputStr = ex.getMessage()
            } finally {
                os.getLogger().flush();
                listener.getLogger().flush()
                fos.close();
                fos1.close()
            }

            if (outputStr == null || outputStr.isEmpty()) {
                outputStr = new String(readAllBytes(tempFile))
            }
            if (outputStr == null) {
                outputStr = ""
            }

            outputStr = outputStr.trim()
            returnStr.append(outputStr)
            tempFile.delete()
            tempFile1.delete()
            Utilities.setOutput(null)
            Utilities.setOutput(outputStr)
            LOGGER.log(Level.FINEST, "Command output \"{0}\"",outputStr);

            outputStr = null
            if (retStatus > 0) {
                return 1
            } else if (retStatus < 0) {
                return -1
            }
            return retStatus
        }

        /**
         * Utility routine to run a shell command via OS API
         *
         * @param final String - Command to run
         * @param StringBuffer - return message
         * @param final File - workingDir
         * @param final boolean - isFile
         * @return int - Exit value
         */
        static int OsRunner(final String cmdStr,
        					StringBuffer returnStr,
        					final File workingDir,
        					final boolean isFile) {

			if (isFile) {
				File ck = new File(cmdStr)
				if (!ck.exists() && !ck.canRead()) {
					return -1
				}
			}

            File tempFile = File.createTempFile("devopsFramework", ".tmp")
            ProcessBuilder ph = null
            if (isUnix()) {
            	if (isFile) {
                	ph = new ProcessBuilder("sh",cmdStr)
                } else {
                	ph = new ProcessBuilder("sh","-c",cmdStr)
                }
            } else {
            	if (isFile) {
                	ph = new ProcessBuilder("call",cmdStr)
                } else {
                	ph = new ProcessBuilder("cmd","/c",cmdStr)
                }
            }

            if (workingDir != null) {
                if (workingDir.exists() && workingDir.canWrite()) {
                    ph.directory(workingDir)
                }
            }
            ph.redirectErrorStream(true);
            ph.redirectOutput(tempFile)
            Process shell = ph.start()
            shell.waitFor()
            if (returnStr.length() > 0) {
                returnStr.delete(0, returnStr.length())
            }

            String outputStr = new String(readAllBytes(tempFile))
            outputStr = outputStr.trim()
            returnStr.append(outputStr)
            tempFile.delete()
            Utilities.setOutput(null)
            Utilities.setOutput(outputStr)

            LOGGER.log(Level.FINEST, "Command output \"{0}\"",outputStr);
            outputStr = null

            int retStatus = shell.exitValue()
            if (retStatus > 0) {
                return 1
            } else if (retStatus < 0) {
                return -1
            }
            return retStatus
        }
    }

    /**
     * Utility routine to run a shell command string
     *
     * @param final String - Command to run
     * @param StringBuffer - return message
     * @param final File - workingDir
     * @param final Launcher - launcher
     * @param final boolean - stripQuotes
     * @param final boolean - noParams
     * @return int - Exit value
     * @throws IllegalArgumentException
     */
    static int runCmd(final String cmdStr,
                      StringBuffer returnStr,
                      final File workingDir=null,
                      final Launcher launcher=null,
                      final boolean stripQuotes=false,
                      final boolean noParams=false) 
        throws IllegalArgumentException {
        if (cmdStr == null || cmdStr.isEmpty()) {
            throw new IllegalArgumentException("Error: No command specified")
        }
        return cmdRunner.runCmd(cmdStr,
        						returnStr,
        						workingDir,
        						launcher,
        					    stripQuotes,
        					    false,
        					    noParams)
    }

    /**
     * Utility routine to run a shell command file
     * 
     * @param final File - Script to run
     * @param StringBuffer - return message
     * @param final File - workingDir
     * @param final Launcher - launcher
     * @param final boolean - stripQuotes
     * @return int - Exit value
     * @throws IllegalArgumentException
     */
    static int runCmd(final File cmdFile,
                      StringBuffer returnStr,
                      final File workingDir=null,
                      final Launcher launcher=null,
                      final boolean stripQuotes=false) 
        throws IllegalArgumentException {
        if (cmdFile == null) {
            throw new IllegalArgumentException("Error: No command specified")
        }

        return cmdRunner.runCmd(cmdFile.getAbsolutePath(),
        						returnStr,
        						workingDir,
        			  			launcher,
        			  			stripQuotes,
        			  			true,
        			  			false)
    }

    /**
     * This private class provides an implementation for the treeWalker used
     * in copying a directory from one location to another
     */
    public static class CopyDirs extends SimpleFileVisitor<Path> {
        private final Path fromDir;
        private final Path toDir;
        private final CopyOption copyOption;

        private CopyDirs(Path fromDir, Path toDir, CopyOption copyOption) {
            this.fromDir = fromDir;
            this.toDir = toDir;
            this.copyOption = copyOption;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            throws IOException {
            Path targetPath = toDir.resolve(fromDir.relativize(dir));
            if( !Files.exists(targetPath) ) {
                Files.createDirectory(targetPath);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.copy(file, toDir.resolve(fromDir.relativize(file)), copyOption);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
            Path newDir = toDir.resolve(fromDir.relativize(dir));
            FileTime time = Files.getLastModifiedTime(dir);
            Files.setLastModifiedTime(newDir, time);
            return FileVisitResult.CONTINUE;
        }
    }

    /**
     * Utility to detect if unix
     * @return boolean
     */
    static boolean isUnix() {
        return (OsDetector.isUnix())
    }

     /**
     * Utility to detect if windows
     * @return boolean
     */
    static boolean isWindows() {
        return (OsDetector.isWindows())
    }

     /**
     * Utility to parse command line into list
     * @param final String - cmdLine
     * @param final boolean - stripQuotes
     * @return List<String> - parsed arguments
     */
    static List<String> parseArgs(final String cmdLine,
                                  final boolean stripQuotes=false) {
        List<String> args = new ArrayList<String>()

        if (cmdLine.contains("'")) {
            args.add(cmdLine)
        } else {
            Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(cmdLine)
            while (m.find()) {
                String mm = m.group(1)
                if (stripQuotes && mm != null && !mm.isEmpty()) {
                    if (mm.length() >= 2 && mm.charAt(0) == '"' && mm.charAt(mm.length() - 1) == '"') {
                        mm = mm.substring(1, mm.length() - 1)
                    }
                }
                args.add(mm);
            }
        }
        return args
    }

    /**
     * Utility to read property file and convert to a map
     * @param final File - propFile
     * @return Map - Mapped properties
     * @throws FileNotFoundException
     */
    static final Map mapProperties(final File propFile) throws FileNotFoundException {
        if (propFile == null) {
            throw new IllegalArgumentException("Error: Property file is not specified")
        } else if (!propFile.exists() || !propFile.canRead()) {
            throw new FileNotFoundException("Error: Invalid file specified")
        }

        // Read properties from file, put into a map and sort...
        def props = new Properties()
        def mapProp = [:]
        new File(propFile.getAbsolutePath()).withInputStream {
            stream -> props.load(stream)
        }
        props.each {
            mapProp.put(it.key,it.value)
        }
        mapProp = mapProp.sort { 
            a, b -> a.key <=> b.key 
        }
        return mapProp
    }

    /**
     * Utility to read default property file and convert to a map
     * @return Map - Mapped properties
     */
    static final Map getDefaultProperties() {
        String propName = System.getenv("DEFAULT_FRAMEWORK_PROPS")
        if (propName != null && !propName.isEmpty()) {
            File propFile = new File(propName)
            def mapProp = null
            if (propFile.exists && propFile.canRead()) {
                mapProp = mapProperties(propFile)
            }
            propFile = null
            return mapProp
        }
        return null
    }

    /**
     * Utility routine to write file text
     * @param final File - file
     * @param final String - contents
     * @param final String - encoding (default UTF-8)
     * @throws IOException
     */
    static final void writeFile(final File file, final String contents,
                                final String encoding="UTF-8") throws IOException {
        try {
            PrintWriter writer = new PrintWriter(file, encoding);
            writer.println(contents);
            writer.close();
        } catch(Exception ex) {
            throw new IOException(ex.getMessage())
        }
    }

    /**
     * Utility routine to write file text
     * @param final File - file
     * @param final byte[] - contents
     * @throws IOException
     */
    static final void writeFile(final File file, final byte[] contents)
        throws IOException {
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(contents);
            out.close()
        } catch(Exception ex) {
            throw new IOException(ex.getMessage())
        }
    }

    /**
     * Utility routine to read file into memory
     * @param final File - file
     * @return byte[] - File contents
     * @throws IOException
     */
    static final byte[] readAllBytes(final File file) throws IOException {
        long len = file.length();
        if (len > (long) Integer.MAX_VALUE) {
            throw new IOException("File is too long to read entirely");
        }
        byte[] bytes = new byte[(int) len];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int readSoFar = 0;
        try {
            while (readSoFar < (int) len) {
                int readThisTime = bis.read(bytes, readSoFar, (int) len - readSoFar);
                if (readThisTime < 0) {
                    throw new EOFException("File ended before reading expected length");
                }
                readSoFar += readThisTime;
            }
        } finally {
            bis.close();
        }
        return bytes;
    }

    /**
     * Utility routine to convert stacktrace to string
     * @param final Throwable throwable
     * @return String
     */
    static String getStackTraceAsString(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    /**
     * Utility routine to read text file into memory
     * @param final File - file
     * @return String - File contents
     * @throws IOException
     */
    static final String readFile(final File file) throws IOException {
        long len = file.length();
        if (len > (long) Integer.MAX_VALUE) {
            throw new IOException("File is too long to read entirely");
        }
        byte[] bytes = new byte[(int) len];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int readSoFar = 0;
        try {
            while (readSoFar < (int) len) {
                int readThisTime = bis.read(bytes, readSoFar, (int) len - readSoFar);
                if (readThisTime < 0) {
                    throw new EOFException("File ended before reading expected length");
                }
                readSoFar += readThisTime;
            }
        } finally {
            bis.close();
        }
        String outputStr = new String(bytes)
        return outputStr
    }

    /**
     * Find a executable in the path
     * @param final String - exeName
     * @return File - Location
     * @throws FileNotFoundException
     */
    static File getExecutable(final String exeName)
        throws FileNotFoundException {

        // Get the path environment.
        String exec = exeName;
        if (Utilities.isWindows()) {
            exec += ".exe";
        }

        String path = System.getenv("PATH");
        if (path == null) {
            path = System.getenv("path");
        }
        if (path == null) {
            path = System.getenv("Path");
        }
        if (path == null) {
            throw new FileNotFoundException(exec + ": could not be found in the path");
        }

        // Split it into directories.
        String[] pathDirs = path.split(File.pathSeparator);

        // Hunt through the directories to find the file I want.
        File exe = null;
        for (String pathDir : pathDirs) {
            File file = new File(pathDir, exec);
            if (file.isFile()) {
                exe = file;
                break;
            }
        }
        if (exe == null) {
            throw new FileNotFoundException(exec + ": could not be found in the path '"+path+"'");
        }
        return exe;
    }

    /**
     * Find a executable in the path using Jenkins API
     * @param final String - exeName
     * @param final FilePath - remoteArea
     * @return File - Location
     * @throws FileNotFoundException
     */
    static File getExecutable(final String exeName, final FilePath remoteArea)
        throws FileNotFoundException {

        // Get the path environment.
        String exec = exeName;
        if (Utilities.isWindows()) {
            exec += ".exe";
        }

        EnvVars env = remoteArea.toComputer().getEnvironment();

        String path = env.get("PATH");
        if (path == null) {
            path = env.get("path");
        }
        if (path == null) {
            path = env.get("Path");
        }
        if (path == null) {
            throw new FileNotFoundException(exec + ": could not be found in the path");
        }

        // Split it into directories.
        String[] pathDirs = path.split(File.pathSeparator);

        // Hunt through the directories to find the file I want.
        File exe = null;
        for (String pathDir : pathDirs) {
            File file = new File(pathDir, exec);
            FilePath remoteFile = new FilePath(remoteArea.getChannel(),file.getAbsolutePath())
            if (remoteFile.exists()) {
                exe = file;
                break;
            }
        }
        if (exe == null) {
            throw new FileNotFoundException(exec + ": could not be found in the path '"+path+"'");
        }
        return exe;
    }

    /**
     * Utility routine to get a temporary directory
     *
     * @return File - Temp directory
     */
    static File getTmpDir() {
        return new File(System.getProperty("java.io.tmpdir"))
    }

    /**
     * Utility routine to count files in a directory
     *
     * @param final File - Directory to count
     * @param long - counter
     */
    static long countFiles(final File dir) {
        File[] files = dir.listFiles();
        long count = 0;
        for (File f : files) {
            if (f.isDirectory()) {
                count += countFiles(f);
            } else {
                count++;
            }
        }
        return count;
    }

    /**
     * Utility routine to emulate rm -fr
     *
     * @param final File - Directory to delete
     * @throws FileNotFoundException
     */
     static void deleteDirs(final File f) throws FileNotFoundException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                c.setWritable(true)
                deleteDirs(c)
            }
        }
        if (!f.delete()) {
            throw new FileNotFoundException("Failed to delete file: " + f)
        }
    }

    /**
     * Utility to copy files around. Does not work with directories
     * @param final File - srcFile
     * @param final File - targetFile
     * @throws IOException
     */
     static void copyFile(final File srcFile, final File targetFile)
        throws IOException {
            Path srcPath = Paths.get(srcFile.getAbsolutePath())
            Path targetPath = Paths.get(targetFile.getAbsolutePath())

            if (srcFile.isDirectory()) {
                throw new IOException("Error: This function does not support directory copies")
            } else if (!srcFile.exists() || !srcFile.canRead()) {
               throw new IOException("Error: The source file specified '"+srcFile.getAbsolutePath()+"' cannot be read")
            }
            if (targetFile.isDirectory()) {
                String target = targetFile.getAbsolutePath()+File.separator+srcFile.getName()
                targetPath = Paths.get(target)
            }
            Files.copy(srcPath, targetPath,
                    StandardCopyOption.COPY_ATTRIBUTES,
                    StandardCopyOption.REPLACE_EXISTING)
    }

    /**
     * Utility to copy directories around
     * @param final File - srcDirectory
     * @param final File - targetDirectory
     * @param final boolean - isDir
     * @throws IOException
     */
     static void copyDirectories(final File srcDirectory, final File targetDirectory,
                                final boolean isDir=false)
        throws IOException {

            LOGGER.log(Level.FINE, "srcDirectory=\"{0}\" targetDirectory=\"{1}\"",
                        srcDirectory.getAbsolutePath(),targetDirectory.getAbsolutePath());
            LOGGER.log(Level.FINE, "srcDirectory(dir)=\"{0}\" targetDirectory=(dir)\"{1}\" isDir={2}",
                        srcDirectory.isDirectory(),targetDirectory.isDirectory(),
                        isDir);

            if (!srcDirectory.isDirectory() || (!targetDirectory.isDirectory() && !isDir)) {
                throw new IOException("Error: This function only supports directory copies")
            }
            Path srcPath = Paths.get(srcDirectory.getAbsolutePath())
            Path targetPath = Paths.get(targetDirectory.getAbsolutePath())

            Files.walkFileTree(srcPath,
                            new CopyDirs(srcPath, targetPath,
                                         StandardCopyOption.REPLACE_EXISTING))
    }

    /**
     * Utility to get file extension
     * @param final File - fileToCheck
     * @return String
     */
    static String getFileExt(final File fileToCheck) {
        String fileExt = ""
        if (fileToCheck != null) {
            int i = fileToCheck.getAbsolutePath().lastIndexOf('.');
            if (i > 0) {
                fileExt = fileToCheck.getAbsolutePath().substring(i+1);
            }
        }
        return fileExt
    }

    /**
     * Utility to detect if input string is XML or JSON
     * @param final File - fileToCheck
     * @return String - XML or JSON
     */
    static String getMarkUpType(final File fileToCheck) {
        String msgToCheck = new String(readAllBytes(fileToCheck))
        String msgType = getMarkUpType(msgToCheck)
        msgToCheck = null
        return(msgType)
    }

     /**
     * Utility to detect if input string is XML or JSON
     * @param final String - msgToCheck
     * @return String - XML or JSON
     */
    static String getMarkUpType(final String msgToCheck) {
        try {
            def slurper = new JsonSlurper()
            def output = slurper.parseText(msgToCheck)
            return ConfigPropertiesConstants.JSON;
        } catch (Exception e) {
        }

        try {
            def slurper = new XmlSlurper()
            def output = slurper.parseText(msgToCheck)
            return ConfigPropertiesConstants.XML;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Get a Map in JSON format
     * @return final String
     */
    static final String convertMapToJSON(final Map conv) {
        def builder = new JsonBuilder(conv)
        return builder.toString()
    }

     /**
     * Utility to calculate the hash of a file
     * @param final File - fileToCheck
     * @return String - hashcode
     * @throws FileNotFoundException
     */
    static String calcFileMD5(final File fileToCheck) {
        if (fileToCheck == null || !fileToCheck.exists() || !fileToCheck.canRead()) {
            throw new FileNotFoundException("Error: Specified file does not exist or cannot be read")
        }

        byte[] b = Files.readAllBytes(Paths.get(fileToCheck.getAbsolutePath()))
        byte[] hash = MessageDigest.getInstance("MD5").digest(b)
        String hashcode = ""
        for (int i=0; i < hash.length; i++) {
           hashcode += Integer.toString( ( hash[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        b = null
        hash = null
        return hashcode
    }

    /**
     * Get a Machine Details
     * @return final String
     */
    static final String getHostName() {
        try {
            InetAddress netAddr = InetAddress.getLocalHost();
            // Get hostname and compare.
            String hostName = netAddr.getHostName()
            return hostName
        } catch (UnknownHostException e) {
            return ""
        }
    }

    /**
     * Get a Login Details
     * @return final String
     */
    static final String getOSUser() {
        return(System.getProperty("user.name"))
    }
}


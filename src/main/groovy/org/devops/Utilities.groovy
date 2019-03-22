/**
 * General utility routines for use with the framework
 */
package org.devops;

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
import groovy.json.*
import groovy.xml.*

class Utilities implements Serializable {

    static String outputInternalStr = null

    /**
     * Get command output
     */
    public final String getOutput() {
        return this.outputInternalStr
    }

    /**
     * This class detects if an operating system is Windows or UNIX
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
     * This class provides an implementation for the treeWalker used
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
        mapProp = mapProp.sort { a, b -> a.key <=> b.key }
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
            throw new FileNotFoundException(exec + ": could not be found in the path");
        }
        return exe;
    }

    /**
     * Utility routine to run a shell command
     * 
     * @param final String - Command to run
     * @param StringBuffer - return message
     * @param final File - workingDir
     * @return int - Exit value
     */
    static int runCmd(final String cmdStr, StringBuffer returnStr, final File workingDir=null) {
        //
        // Enable this if need to debug commands. Not adding debug facility due
        // to password concerns
        //
        //println "[DEBUG] "+cmdStr
        File tempFile = File.createTempFile("devopsFramework", ".tmp")
        ProcessBuilder ph = null
        if (isUnix()) {
            ph = new ProcessBuilder("sh","-c",cmdStr)
        } else {
            ph = new ProcessBuilder("cmd","/c",cmdStr)
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
        //returnStr.append(shell.text.toString())
        returnStr.append(outputStr)
        tempFile.delete()
        this.outputInternalStr = null
        this.outputInternalStr = outputStr
        outputStr = null
        // Enable this if need to debug commands. Not adding debug facility due
        // to password concerns
        //println "[DEBUG] "+returnStr.toString()
        
        int retStatus = shell.exitValue()
        if (retStatus > 0) {
            return 1
        } else if (retStatus < 0) {
            return -1
        }
        return 0
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
     * @return final File - targetFile
     * @throws IOException
     */    
     static void copyFile(final File srcFile, final File targetFile) 
        throws IOException {
            Path srcPath = Paths.get(srcFile.getAbsolutePath())
            Path targetPath = Paths.get(targetFile.getAbsolutePath())

            if (srcFile.isDirectory()) {
                throw new IOException("Error: This function does not support directory copies")
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
     * @return final File - targetDirectory
     * @throws IOException
     */    
     static void copyDirectories(final File srcDirectory, final File targetDirectory) 
        throws IOException {

            if (!srcDirectory.isDirectory() || !targetDirectory.isDirectory()) {
                throw new IOException("Error: This function only supports directory copies")
            }
            Path srcPath = Paths.get(srcDirectory.getAbsolutePath())
            Path targetPath = Paths.get(targetDirectory.getAbsolutePath())

            Files.walkFileTree(srcPath, 
                            new CopyDirs(srcPath, targetPath, 
                                         StandardCopyOption.REPLACE_EXISTING))
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
     * Utility to calculate the hash of a file
     * @param final File - fileToCheck
     * @return String - hashcode
     * @throws FileNotFoundException
     */   
    static String calcFileHash(final File fileToCheck) {
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
}

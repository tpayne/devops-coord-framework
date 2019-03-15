package org.devops;

/**
 * Utility routines for use with the framework
 */
class Utilities implements Serializable {

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

    // Method for OS detection...
    static boolean isUnix() {
        return (OsDetector.isUnix()) 
    }

    // Method for OS detection...
    static boolean isWindows() {
        return (OsDetector.isWindows()) 
    }

    /** 
     * Utility to read property file and convert to a map
     * @param final File - propFile
     * @return LinkedHashMap - Mapped properties
     * @throws FileNotFoundException, IllegalArgumentException
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
     * @param String - exeName
     * @return File - Location
     * @throws FileNotFoundException
     */
    static File getExecutable(String exeName) 
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
     * @param String - Command to run
     * @param StringBuffer - return message
     * @return int - Exit value
     */
    static int runCmd(final String cmdStr, StringBuffer returnStr) {
        //
        // Enable this if need to debug commands. Not adding debug facility due
        // to password concerns
        //
        //println "[DEBUG] "+cmdStr

        ProcessBuilder ph = new ProcessBuilder("sh","-c",cmdStr)
        ph.redirectErrorStream(true);
        Process shell = ph.start()
        shell.waitFor()
        if (returnStr.length() > 0) {
            returnStr.delete(0, returnStr.length())
        }
        returnStr.append(shell.text.toString())

        // Enable this if need to debug commands. Not adding debug facility due
        // to password concerns
        //println "[DEBUG] "+returnStr.toString()
        
        int retStatus = shell.exitValue()
        if (Utilities.isUnix()) {
            if (retStatus > 0) {
                return 1
            } else if (retStatus < 0) {
                return -1
            }
            return 0
        } else {
            if (retStatus > 0) {
                return 0
            } else if (retStatus < 0) {
                return -1
            }
            return 1            
        }
        return 1
    }

    // Utility function to get temporary directory...
    static File getTmpDir() {
        return new File(System.getProperty("java.io.tmpdir"))
    }

    /**
     * Utility routine to emulate rm -fr
     * 
     * @param final File - Directory to delete
     * @throws IOException
     */   
     static void deleteDirs(final File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                c.setWritable(true)
                deleteDirs(c)
            }
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f)
    }
}

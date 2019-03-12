package org.devops;

/**
 * Utility routines for use with the framework
 */
class Utilities implements Serializable {
    /**
     * Utility routine to read file into memory
     *
     * @return byte[] - File contents
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
     * Utility routine to run a shell command
     * 
     * @param String - Command to run
     * @param StringBuffer - return message
     * @return int - Exit value
     */
    static int runCmd(final String cmdStr, StringBuffer returnStr) {
        ProcessBuilder ph = new ProcessBuilder("sh","-c",cmdStr)
        ph.redirectErrorStream(true);
        Process shell = ph.start()
        shell.waitFor()
        if (returnStr.length() > 0) {
            returnStr.delete(0, returnStr.length())
        }
        returnStr.append(shell.text.toString())
        return shell.exitValue()
    }
}

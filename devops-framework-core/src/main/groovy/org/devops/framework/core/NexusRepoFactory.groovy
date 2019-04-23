/**
 * Repository routines for use with the framework
 */
package org.devops.framework.core;

import groovy.json.*
import org.xml.sax.*

import java.util.logging.Logger
import java.util.logging.Level

class NexusRepoFactory extends RepoFactory {

    private static final Logger LOGGER = Logger.getLogger( NexusRepoFactory.class.getName() )

    /**
     * Utility to pull asset(s) from a repo
     * 
     * @param final URI - srcRepo
     * @param final File - targetAsset
     * @param final String - userName
     * @param final String - userPwd     
     * @param StringBuffer - outputStr
     * @param boolean - isDir     
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    static final boolean pullAssetFromRepo(final URI srcRepo,
                                        final File targetAsset,
                                        final String userName,
                                        final String userPwd,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)                                    
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {
        
        if (srcRepo == null || targetAsset == null || userName == null || userPwd == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        if (srcRepo.toURL().toString().endsWith("/")) {
            throw new IllegalArgumentException("Error: The URI has to refer to a file. Directories are not supported")
        }
        File exeRun = null

        // TODO add jfrog support sometime when it works better
        try {
            exeRun = Utilities.getExecutable("curl")
        } catch (Exception e) {
            throw new FileNotFoundException("Error: Curl has not been located")               
        }

        LOGGER.log(Level.FINE, "pullAssetFromRepo src=\"{0}\" target=\"{1}\"",
                    srcRepo.toURL().toString(),targetAsset.getAbsolutePath());

        def cmdStr = null
        cmdStr = "-v -X GET -u${userName}:${userPwd} -o \""+targetAsset.getAbsolutePath()+"\" -O \""+srcRepo.toURL().toString()+"\""

        // Construct the required command...
        def runCmd = exeRun.getAbsolutePath()+" "+cmdStr
        exeRun = null
        StringBuffer returnStr = new StringBuffer()
        int retStat = Utilities.runCmd(runCmd,returnStr,null,null,true)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null
        LOGGER.log(Level.FINE, "pullAssetFromRepo output=\"{0}\"",returnOutput);

        if (outputStr!=null) {
            outputStr.append(returnOutput)
        }
        returnStr = null    
        
        if (retStat>0 || !targetAsset.exists()) {
            return false
        } else if (retStat==0) {
            // Now we need to test the file to see if it is error message...
            if (returnOutput.contains("HTTP/1.1 404 Not Found") || returnOutput.contains("HTTP/1.1 401 Unauthorized")) {
                targetAsset.delete()
                if (returnOutput.contains("HTTP/1.1 404 Not Found")) {
                    throw new FileNotFoundException("Error: Source file specified was not found on server") 
                }
                if (returnOutput.contains("HTTP/1.1 401 Unauthorized")) {
                    throw new SecurityException("Error: Authorization failed for source") 
                }
                return false
            }
            return true
        } 
        return false
    }

    /**
     * Utility to push asset(s) to a repo
     * 
     * @param final File   - srcAsset
     * @param final URI - targetRepo
     * @param final String - userName
     * @param final String - userPwd          
     * @param StringBuffer - outputStr
     * @param boolean - isDir     
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    static final boolean pushAssetToRepo(final File srcAsset,
                                        final URI targetRepo,
                                        final String userName,
                                        final String userPwd,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)                                    
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {
        
        if (srcAsset == null || targetRepo == null || userName == null || userPwd == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        if (!srcAsset.exists() || !srcAsset.canRead() || !srcAsset.isFile()) {
            throw new FileNotFoundException("Error: Source file '"+srcAsset.getAbsolutePath()+"' cannot be read")
        }

        File exeRun = null

        // TODO add jfrog support sometime when it works better
        try {
            exeRun = Utilities.getExecutable("curl")
        } catch (Exception e) {
            throw new FileNotFoundException("Error: Curl has not been located")               
        }

        LOGGER.log(Level.FINE, "pushAssetToRepo src=\"{0}\" target=\"{1}\"",
                    srcAsset.getAbsolutePath(),targetRepo.toURL().toString());

        def cmdStr = null
        cmdStr = "-v -u${userName}:${userPwd} --upload-file \""+srcAsset.getAbsolutePath()+"\" \""+targetRepo.toURL().toString()+"\""

        // Construct the required command...
        def runCmd = exeRun.getAbsolutePath()+" "+cmdStr
        exeRun = null
        StringBuffer returnStr = new StringBuffer()

        int retStat = Utilities.runCmd(runCmd,returnStr,null,null,true)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null
        LOGGER.log(Level.FINE, "pushAssetToRepo output=\"{0}\"",returnOutput);

        if (retStat==0) {
            // Check the CURL command worked...
            if (returnOutput.contains("HTTP/1.1 404 Not Found") || returnOutput.contains("HTTP/1.1 401 Unauthorized")) {
                if (returnOutput.contains("HTTP/1.1 404 Not Found")) {
                    throw new FileNotFoundException("Error: Target file specified was not found on server") 
                }
                if (returnOutput.contains("HTTP/1.1 401 Unauthorized")) {
                    throw new SecurityException("Error: Authorization failed for target") 
                }
                return false
            } else if (returnOutput.contains("* We are completely uploaded and fine")) {
            } else {
                retStat=1
            }
        }
        if (outputStr!=null) {
            outputStr.append(returnOutput)
        }
        returnStr = null    
        
        if (retStat>0) {
            return false
        } else if (retStat==0) {
            return true
        } 
        return false
    }

}

/**
 * Repository routines for use with the framework
 */
package org.devops.framework.core;

import groovy.json.*

class ArtifactoryRepoFactory extends RepoFactory {

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
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pullAssetFromRepo(final URI srcRepo,
                                        final File targetAsset,
                                        final String userName,
                                        final String userPwd,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)                                    
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
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

        def cmdStr = null
        cmdStr = "-u${userName}:${userPwd} -o \""+targetAsset.getAbsolutePath()+"\" -O \""+srcRepo.toURL().toString()+"\""

        // Construct the required command...
        def runCmd = exeRun.getAbsolutePath()+" "+cmdStr
        exeRun = null
        StringBuffer returnStr = new StringBuffer()
        int retStat = Utilities.runCmd(runCmd,returnStr)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null

        if (outputStr!=null) {
            outputStr.append(returnOutput)
        }
        returnStr = null    
        
        if (retStat>0 || !targetAsset.exists()) {
            return false
        } else if (retStat==0) {
            // Now we need to test the file to see if it is error message...
            try {
                def slurper = new JsonSlurper()
                def result = slurper.parse(targetAsset)
                if (result != null) {
                    if (outputStr != null) {
                        outputStr.delete(0, outputStr.length())
                        outputStr.append(result.toString())
                    }
                    if (result.toString().contains("{errors=[{")) {
                        targetAsset.delete()
                        if (result.errors.status.toString().contains("[404]")) {
                            throw new FileNotFoundException("Error: File specified was not found on server") 
                        }
                        return false
                    }
                }
            } catch (JsonException e) {
                return true
            }
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
     * @throws IllegalArgumentException, Exception
     */
    static final boolean pushAssetToRepo(final File srcAsset,
                                        final URI targetRepo,
                                        final String userName,
                                        final String userPwd,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)                                    
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (srcAsset == null || targetRepo == null || userName == null || userPwd == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        if (!srcAsset.exists() || !srcAsset.canRead() || !srcAsset.isFile()) {
            throw new FileNotFoundException("Error: Source file cannot be read")
        }

        File exeRun = null

        // TODO add jfrog support sometime when it works better
        try {
            exeRun = Utilities.getExecutable("curl")
        } catch (Exception e) {
            throw new FileNotFoundException("Error: Curl has not been located")               
        }

        def cmdStr = null
        cmdStr = "-u${userName}:${userPwd} -T \""+srcAsset.getAbsolutePath()+"\" \""+targetRepo.toURL().toString()+"\""

        // Construct the required command...
        def runCmd = exeRun.getAbsolutePath()+" "+cmdStr
        exeRun = null
        StringBuffer returnStr = new StringBuffer()

        int retStat = Utilities.runCmd(runCmd,returnStr)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null
        if (retStat==0) {
            // Check the CURL command worked...
            if (returnOutput.contains("\"createdBy\"") && returnOutput.contains("\"downloadUri\"")) {
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

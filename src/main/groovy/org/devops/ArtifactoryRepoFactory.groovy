/**
 * Repository routines for use with the framework
 */
package org.devops;

class ArtifactoryRepoFactory extends RepoFactory {

    /**
     * Utility to pull asset(s) from a repo
     * 
     * @param final URI - srcRepo
     * @param final File - targetAsset
     * @param final String - userName
     * @param final String - userPwd     
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pullAssetFromRepo(final URI srcRepo,
                                        final File targetAsset,
                                        final String userName,
                                        final String userPwd,
                                        StringBuffer outputStr=null)                                    
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (srcRepo == null || targetAsset == null || userName == null || userPwd == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        File exeRun = null

        // TODO add jfrog support sometime when it works better
        try {
            exeRun = Utilities.getExecutable("curl")
        } catch (Exception e) {
            throw new FileNotFoundException("Error: Curl has not been located")               
        }

        def cmdStr = null
        cmdStr = "-u${userName}:${userPwd} -o \""+targetAsset.getAbsolutePath()+"\" \""+srcRepo.toURL().toString()+"\""

        // Construct the required command...
        def runCmd = exeRun.getAbsolutePath()+" "+cmdStr
        
        StringBuffer returnStr = new StringBuffer()

        int retStat = Utilities.runCmd(runCmd,returnStr)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null
        
        if (retStat>0) {
            return false
        } else if (retStat==0) {
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
     * @return boolean 
     * @throws IllegalArgumentException, Exception
     */
    static final boolean pushAssetToRepo(final File srcAsset,
                                        final URI targetRepo,
                                        final String userName,
                                        final String userPwd,
                                        StringBuffer outputStr=null)
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
        
        StringBuffer returnStr = new StringBuffer()

        int retStat = Utilities.runCmd(runCmd,returnStr)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null
        
        if (retStat>0) {
            return false
        } else if (retStat==0) {
            return true
        } 
        return false
    }

}

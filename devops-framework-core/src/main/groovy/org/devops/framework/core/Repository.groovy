/**
 * Repository routines for use with the framework
 */
package org.devops.framework.core;

class Repository implements Serializable {

    /**
     * Utility to pull asset(s) from a repo
     * 
     * @param final String - repoType
     * @param final String - srcAsset
     * @param final String - targetAsset
     * @param final String - userName
     * @param final String - userPwd
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pullAssetFromRepo(final String repoType,
                                        final String srcAsset,
                                        final String targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null)
        throws IllegalArgumentException, FileNotFoundException, Exception {

        if (repoType == null || srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }
        File srcFile = new File(srcAsset)            
        File targetFile = new File(targetAsset)
        return pullAssetFromRepo(repoType,srcFile,targetFile,
                                userName,userPwd,outputStr)
    }

    /**
     * Utility to pull asset(s) from a repo
     * 
     * @param final String - repoType
     * @param final File - srcAsset
     * @param final File - targetAsset
     * @param final String - userName
     * @param final String - userPwd
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pullAssetFromRepo(final String repoType,
                                        final File srcAsset,
                                        final File targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (repoType == null || srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        }

        boolean retStat = repo.pullAssetFromRepo(srcAsset,targetAsset,
                                                userName,userPwd,outputStr)

        return retStat
    }

    /**
     * Utility to pull asset(s) from a repo
     * 
     * @param final String - repoType
     * @param final URI - srcAsset
     * @param final File - targetAsset
     * @param final String - userName
     * @param final String - userPwd
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pullAssetFromRepo(final String repoType,
                                        final URI srcAsset,
                                        final File targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (repoType == null || srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        }

        boolean retStat = repo.pullAssetFromRepo(srcAsset,targetAsset,
                                                userName,userPwd,outputStr)

        return retStat
    }

    /**
     * Utility to push asset(s) to a repo
     * 
     * @param final String - repoType
     * @param final String   - srcAsset
     * @param final String   - targetRepo
     * @param final String - userName
     * @param final String - userPwd
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pushAssetToRepo(final String repoType,
                                        final String srcAsset,
                                        final String targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null)
        throws IllegalArgumentException, FileNotFoundException, Exception {
            
        if (repoType == null || srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }
        File srcFile = new File(srcAsset)            
        File targetFile = new File(targetAsset)
        return pushAssetToRepo(repoType,srcFile,targetFile,
                               userName,userPwd,outputStr)
    }

    /**
     * Utility to push asset(s) to a repo
     * 
     * @param final String - repoType
     * @param final File   - srcAsset
     * @param final File   - targetRepo
     * @param final String - userName
     * @param final String - userPwd
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static boolean pushAssetToRepo(final String repoType,
                                        final File srcAsset,
                                        final File targetRepo,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (repoType == null || srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        }

        boolean retStat = repo.pushAssetToRepo(srcAsset,targetRepo,
                                            userName,userPwd,outputStr)

        return retStat
    }

    /**
     * Utility to push asset(s) to a repo
     * 
     * @param final String - repoType
     * @param final File   - srcAsset
     * @param final URI   - targetRepo
     * @param final String - userName
     * @param final String - userPwd
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static boolean pushAssetToRepo(final String repoType,
                                        final File srcAsset,
                                        final URI targetRepo,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (repoType == null || srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        }

        boolean retStat = repo.pushAssetToRepo(srcAsset,targetRepo,
                                            userName,userPwd,outputStr)

        return retStat
    }
}

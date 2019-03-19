/**
 * Repository routines for use with the framework
 */
 package org.devops;

class Repository implements Serializable {

    /**
     * Utility to pull asset(s) from a repo
     * 
     * @param final String - repoType
     * @param final String - srcAsset
     * @param final String - targetAsset
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pullAssetFromRepo(final String repoType,
                                        final String srcAsset,
                                        final String targetAsset,
                                        StringBuffer outputStr=null)
        throws IllegalArgumentException, FileNotFoundException, Exception {
            File srcFile = new File(srcAsset)            
            File targetFile = new File(targetAsset)
            return pullAssetFromRepo(repoType,srcFile,targetFile,outputStr)
    }

    /**
     * Utility to pull asset(s) from a repo
     * 
     * @param final String - repoType
     * @param final File - srcAsset
     * @param final File - targetAsset
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pullAssetFromRepo(final String repoType,
                                        final File srcAsset,
                                        final File targetAsset,
                                        StringBuffer outputStr=null)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (repoType == null || srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        }

        boolean retStat = repo.pullAssetFromRepo(srcAsset,targetAsset)

        return retStat
    }

    /**
     * Utility to push asset(s) to a repo
     * 
     * @param final String - repoType
     * @param final String   - srcAsset
     * @param final String   - targetRepo
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pushAssetToRepo(final String repoType,
                                        final String srcAsset,
                                        final String targetAsset,
                                        StringBuffer outputStr=null)
        throws IllegalArgumentException, FileNotFoundException, Exception {
            File srcFile = new File(srcAsset)            
            File targetFile = new File(targetAsset)
            return pushAssetToRepo(repoType,srcFile,targetFile,outputStr)
    }

    /**
     * Utility to push asset(s) to a repo
     * 
     * @param final String - repoType
     * @param final File   - srcAsset
     * @param final File   - targetRepo
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static boolean pushAssetToRepo(final String repoType,
                                        final File srcAsset,
                                        final File targetRepo,
                                        StringBuffer outputStr=null)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (repoType == null || srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        }

        boolean retStat = repo.pushAssetToRepo(srcAsset,targetRepo)

        return retStat
    }
}

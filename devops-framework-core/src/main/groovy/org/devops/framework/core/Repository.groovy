/**
 * Repository routines for use with the framework
 */
package org.devops.framework.core;

import java.util.logging.Logger
import java.util.logging.Level

class Repository implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( Repository.class.getName() )

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
        boolean isDir = targetAsset.endsWith(File.separator)

        LOGGER.log(Level.FINE, "pullAssetFromRepo src=\"{0}\" target=\"{1}\"",srcAsset,targetAsset);
        LOGGER.log(Level.FINE, 
            "pullAssetFromRepo srcFile=\"{0}\" targetFile=\"{1}\" srcFile(dir)=\"{2}\" targetFile(dir)=\"{3}\"",
            srcFile.getAbsolutePath(),targetFile.getAbsolutePath(),
            srcFile.isDirectory(),targetFile.isDirectory());
        LOGGER.log(Level.FINE, 
            "pullAssetFromRepo srcFileName=\"{0}\" targetFileName=\"{1}\"",
            srcFile.getName(),targetFile.getName());

        return pullAssetFromRepo(repoType,srcFile,targetFile,
                                userName,userPwd,outputStr,isDir)
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
     * @param boolean - isDir     
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pullAssetFromRepo(final String repoType,
                                        final File srcAsset,
                                        final File targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (repoType == null || srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        LOGGER.log(Level.FINER, "pullAssetFromRepo srcFile=\"{0}\" targetFile=\"{1}\"",srcAsset.getAbsolutePath(),
                                                targetAsset.getAbsolutePath());
        LOGGER.log(Level.FINER, "pullAssetFromRepo srcFile(dir)=\"{0}\" targetFile=(dir)\"{1}\"",srcAsset.isDirectory(),
                                                targetAsset.isDirectory());

        RepoFactory repo = null

        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        }

        boolean retStat = repo.pullAssetFromRepo(srcAsset,targetAsset,
                                                userName,userPwd,outputStr,
                                                isDir)

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
     * @param boolean - isDir     
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pullAssetFromRepo(final String repoType,
                                        final URI srcAsset,
                                        final File targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (repoType == null || srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        LOGGER.log(Level.FINER, "pullAssetFromRepo srcURI=\"{0}\" targetFile=\"{1}\"",srcAsset.toString(),
                                                targetAsset.toString());

        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        }

        boolean retStat = repo.pullAssetFromRepo(srcAsset,targetAsset,
                                                userName,userPwd,outputStr,
                                                isDir)

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
        boolean isDir = targetAsset.endsWith(File.separator)

        LOGGER.log(Level.FINE, "pushAssetToRepo src=\"{0}\" target=\"{1}\"",srcAsset,targetAsset);
        LOGGER.log(Level.FINE, 
            "pushAssetToRepo srcFile=\"{0}\" targetFile=\"{1}\" srcFile(dir)=\"{2}\" targetFile(dir)=\"{3}\"",
                        srcFile.getAbsolutePath(),targetFile.getAbsolutePath(),
                        srcFile.isDirectory(),targetFile.isDirectory());
        LOGGER.log(Level.FINE, 
            "pushAssetToRepo srcFileName=\"{0}\" targetFileName=\"{1}\"",
            srcFile.getName(),targetFile.getName());

        return pushAssetToRepo(repoType,srcFile,targetFile,
                               userName,userPwd,outputStr,isDir)
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
     * @param boolean - isDir     
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static boolean pushAssetToRepo(final String repoType,
                                        final File srcAsset,
                                        final File targetRepo,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (repoType == null || srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        LOGGER.log(Level.FINER, "pushAssetToRepo srcFile=\"{0}\" targetFile=\"{1}\"",srcAsset.getAbsolutePath(),
                                                targetRepo.getAbsolutePath());
        LOGGER.log(Level.FINER, "pushAssetToRepo srcFile(dir)=\"{0}\" targetFile=(dir)\"{1}\"",srcAsset.isDirectory(),
                                                targetRepo.isDirectory());

        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        }

        boolean retStat = repo.pushAssetToRepo(srcAsset,targetRepo,
                                            userName,userPwd,outputStr,
                                            isDir)

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
     * @param boolean - isDir     
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static boolean pushAssetToRepo(final String repoType,
                                        final File srcAsset,
                                        final URI targetRepo,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (repoType == null || srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        LOGGER.log(Level.FINER, "pushAssetToRepo srcFile=\"{0}\" targetURI=\"{1}\"",srcAsset.getAbsolutePath(),
                                                targetRepo.toString());

        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        }

        boolean retStat = repo.pushAssetToRepo(srcAsset,targetRepo,
                                            userName,userPwd,outputStr,
                                            isDir)

        return retStat
    }
}

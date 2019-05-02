/**
 * Repository routines for use with the framework
 */
package org.devops.framework.core;

import java.util.logging.Logger
import java.util.logging.Level

import hudson.Launcher
import hudson.FilePath

class Repository implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( Repository.class.getName() )
    private static final long serialVersionUID = 1L;

    /**
     * Utility to pull asset(s) from a repo
     * 
     * @param final String - repoType
     * @param final String - srcAsset
     * @param final String - targetAsset
     * @param final String - userName
     * @param final String - userPwd
     * @param StringBuffer - outputStr
     * @param final Launcher - launcher     
     * @param final FilePath - remoteArea          
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    static final boolean pullAssetFromRepo(final String repoType,
                                        final String srcAsset,
                                        final String targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {

        if (repoType == null || srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        File targetFile = new File(targetAsset)
 
        LOGGER.log(Level.FINE, "pullAssetFromRepo src=\"{0}\" target=\"{1}\"",srcAsset,targetAsset);
        boolean retStat = false

        if (repoType == ConfigPropertiesConstants.FILE) {
            File srcFile = new File(srcAsset)            
            boolean isDir = targetAsset.endsWith(File.separator)

            LOGGER.log(Level.FINE, 
                "pullAssetFromRepo srcFile=\"{0}\" targetFile=\"{1}\" srcFile(dir)=\"{2}\" targetFile(dir)=\"{3}\"",
                srcFile.getAbsolutePath(),targetFile.getAbsolutePath(),
                srcFile.isDirectory(),targetFile.isDirectory());
            LOGGER.log(Level.FINE, 
                "pullAssetFromRepo srcFileName=\"{0}\" targetFileName=\"{1}\"",
                srcFile.getName(),targetFile.getName());

            retStat = pullAssetFromRepo(repoType,srcFile,targetFile,
                                userName,userPwd,outputStr,isDir,
                                launcher,remoteArea)
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY ||
                   repoType == ConfigPropertiesConstants.NEXUS) {
            URI srcRepo = new URI(srcAsset)

            LOGGER.log(Level.FINE, 
                "pullAssetFromRepo srcRepo=\"{0}\" targetFile=\"{1}\" targetFile(dir)=\"{2}\"",
                            srcRepo.toString(),targetFile.getAbsolutePath(),
                            targetFile.isDirectory());

            retStat = pullAssetFromRepo(repoType,srcRepo,targetFile,
                                userName,userPwd,outputStr,false,
                                launcher,remoteArea)
        }        

        return retStat

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
     * @param final Launcher - launcher     
     * @param final FilePath - remoteArea          
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    static final boolean pullAssetFromRepo(final String repoType,
                                        final File srcAsset,
                                        final File targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {
        
        if (repoType == null || srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        LOGGER.log(Level.FINER, "pullAssetFromRepo srcFile=\"{0}\" targetFile=\"{1}\"",srcAsset.getAbsolutePath(),
                                                targetAsset.getAbsolutePath());
        LOGGER.log(Level.FINER, "pullAssetFromRepo srcFile(dir)=\"{0}\" targetFile=(dir)\"{1}\"",srcAsset.isDirectory(),
                                                targetAsset.isDirectory());

        RepoFactory repo = null

        // Create the required repo processing object...
        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.NEXUS) {
            repo = new NexusRepoFactory()
        }

        boolean retStat = repo.pullAssetFromRepo(srcAsset,targetAsset,
                                                userName,userPwd,outputStr,
                                                isDir,launcher,remoteArea)

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
     * @param final Launcher - launcher     
     * @param final FilePath - remoteArea          
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    static final boolean pullAssetFromRepo(final String repoType,
                                        final URI srcAsset,
                                        final File targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {
        
        if (repoType == null || srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        LOGGER.log(Level.FINER, "pullAssetFromRepo srcURI=\"{0}\" targetFile=\"{1}\"",srcAsset.toString(),
                                                targetAsset.toString());

        // Create the required repo processing object...
        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.NEXUS) {
            repo = new NexusRepoFactory()
        }

        boolean retStat = repo.pullAssetFromRepo(srcAsset,targetAsset,
                                                userName,userPwd,outputStr,
                                                isDir,launcher,remoteArea)

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
     * @param final Launcher - launcher     
     * @param final FilePath - remoteArea          
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    static final boolean pushAssetToRepo(final String repoType,
                                        final String srcAsset,
                                        final String targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {
            
        if (repoType == null || srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        LOGGER.log(Level.FINE, "pushAssetToRepo src=\"{0}\" target=\"{1}\"",srcAsset,targetAsset);
        File srcFile = new File(srcAsset)            
        boolean retStat = false

        if (repoType == ConfigPropertiesConstants.FILE) {
            File targetFile = new File(targetAsset)
            boolean isDir = targetAsset.endsWith(File.separator)

            LOGGER.log(Level.FINE, 
                "pushAssetToRepo srcFile=\"{0}\" targetFile=\"{1}\" srcFile(dir)=\"{2}\" targetFile(dir)=\"{3}\"",
                            srcFile.getAbsolutePath(),targetFile.getAbsolutePath(),
                            srcFile.isDirectory(),targetFile.isDirectory());
            LOGGER.log(Level.FINE, 
                "pushAssetToRepo srcFileName=\"{0}\" targetFileName=\"{1}\"",
                srcFile.getName(),targetFile.getName());

            retStat = pushAssetToRepo(repoType,srcFile,targetFile,
                                   userName,userPwd,outputStr,isDir,
                                   launcher,remoteArea)
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY ||
                   repoType == ConfigPropertiesConstants.NEXUS) {
            URI targetRepo = new URI(targetAsset)

            LOGGER.log(Level.FINE, 
                "pushAssetToRepo srcFile=\"{0}\" targetRepo=\"{1}\" srcFile(dir)=\"{2}\"",
                            srcFile.getAbsolutePath(),targetRepo.toString(),
                            srcFile.isDirectory());

            retStat = pushAssetToRepo(repoType,srcFile,targetRepo,
                                   userName,userPwd,outputStr,false,
                                   launcher,remoteArea)
        }        

        return retStat
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
     * @param final Launcher - launcher     
     * @param final FilePath - remoteArea          
     * @param boolean - isDir     
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    static boolean pushAssetToRepo(final String repoType,
                                        final File srcAsset,
                                        final File targetRepo,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {
        
        if (repoType == null || srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        LOGGER.log(Level.FINER, "pushAssetToRepo srcFile=\"{0}\" targetFile=\"{1}\"",srcAsset.getAbsolutePath(),
                                                targetRepo.getAbsolutePath());
        LOGGER.log(Level.FINER, "pushAssetToRepo srcFile(dir)=\"{0}\" targetFile=(dir)\"{1}\"",srcAsset.isDirectory(),
                                                targetRepo.isDirectory());

        // Create the required repo processing object...
        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.NEXUS) {
            repo = new NexusRepoFactory()
        }

        boolean retStat = repo.pushAssetToRepo(srcAsset,targetRepo,
                                            userName,userPwd,outputStr,
                                            isDir,launcher,remoteArea)

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
     * @param final Launcher - launcher     
     * @param final FilePath - remoteArea          
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    static boolean pushAssetToRepo(final String repoType,
                                        final File srcAsset,
                                        final URI targetRepo,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {
        
        if (repoType == null || srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        RepoFactory repo = null

        LOGGER.log(Level.FINER, "pushAssetToRepo srcFile=\"{0}\" targetURI=\"{1}\"",srcAsset.getAbsolutePath(),
                                                targetRepo.toString());

        // Create the required repo processing object...
        if (repoType == ConfigPropertiesConstants.FILE) {
            repo = new FileRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.ARTIFACTORY) {
            repo = new ArtifactoryRepoFactory()
        } else if (repoType == ConfigPropertiesConstants.NEXUS) {
            repo = new NexusRepoFactory()
        }

        boolean retStat = repo.pushAssetToRepo(srcAsset,targetRepo,
                                            userName,userPwd,outputStr,
                                            isDir,
                                            launcher,remoteArea)

        return retStat
    }
}

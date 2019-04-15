/**
 * Repository routines for use with the framework
 */
package org.devops.framework.core;

import java.util.logging.Logger
import java.util.logging.Level

class FileRepoFactory extends RepoFactory {

    private static final Logger LOGGER = Logger.getLogger( FileRepoFactory.class.getName() )

    /**
     * Utility to pull asset(s) from a repo
     * 
     * @param final URI - srcAsset
     * @param final File - targetAsset
     * @param final String - userName
     * @param final String - userPwd
     * @param StringBuffer - outputStr
     * @param boolean - isDir     
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    protected static final boolean pullAssetFromRepo(final URI srcAsset,
                                        final File targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {

        if (srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }
        File srcFile = new File(srcAsset.getPath())
        return pullAssetFromRepo(srcFile,targetAsset,
                                userName,userPwd,outputStr,isDir)
    }

    /**
     * Utility to pull asset(s) from a repo
     * 
     * @param final File - srcAsset
     * @param final File - targetAsset
     * @param final String - userName
     * @param final String - userPwd
     * @param StringBuffer - outputStr
     * @param boolean - isDir     
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    protected static final boolean pullAssetFromRepo(final File srcAsset,
                                        final File targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {

        if (srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        LOGGER.log(Level.FINEST, "pullAssetFromRepo srcFile=\"{0}\" targetFile=\"{1}\"",srcAsset.getAbsolutePath(),
                                                targetAsset.getAbsolutePath());
        LOGGER.log(Level.FINEST, "pullAssetFromRepo srcFile(dir)=\"{0}\" targetFile=(dir)\"{1}\" isDir={2}",srcAsset.isDirectory(),
                                                targetAsset.isDirectory(),isDir);


        if (!srcAsset.exists() || !srcAsset.canRead()) {
            throw new FileNotFoundException("Error: Source file cannot be read")
        }
        if (targetAsset.exists() && !targetAsset.canWrite()) {
            throw new FileNotFoundException("Error: Target exists and is not writeable")
        }

        // Determine what type of copy is needed...
        // Note: For some reason the isFile() is not working for the first statement
        //       so I'm using !isDirectory() instead...
        if (!srcAsset.isDirectory() && (!targetAsset.isDirectory() && !isDir)) {
            Utilities.copyFile(srcAsset,targetAsset)        
        } else if (srcAsset.isDirectory() && (targetAsset.isDirectory() || isDir)) {
            Utilities.copyDirectories(srcAsset,targetAsset,isDir)
        } else if (srcAsset.isDirectory() && (!targetAsset.isDirectory() && !isDir)) {
            String errorMsg = "Error: Invalid parameters specified. Target '"+targetAsset.getAbsolutePath()+"' is not a directory"
            throw new IllegalArgumentException(errorMsg)
        } else if (!srcAsset.isDirectory() && (targetAsset.isDirectory() || isDir)) {
            Utilities.copyFile(srcAsset,targetAsset)        
        } else {
            return false
        }        

        return true
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
    protected static final boolean pushAssetToRepo(final File srcAsset,
                                        final URI targetRepo,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {

        if (srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        File targetFile = new File(targetRepo.getPath())
        return pushAssetToRepo(srcAsset,targetFile,
                                userName,userPwd,outputStr,isDir)
    }

    /**
     * Utility to push asset(s) to a repo
     * 
     * @param final File   - srcAsset
     * @param final File - targetRepo
     * @param final String - userName
     * @param final String - userPwd
     * @param StringBuffer - outputStr
     * @param boolean - isDir     
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    protected static final boolean pushAssetToRepo(final File srcAsset,
                                        final File targetRepo,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {
        
        if (srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        LOGGER.log(Level.FINEST, "pushAssetToRepo srcFile=\"{0}\" targetFile=\"{1}\"",srcAsset.getAbsolutePath(),
                                                targetRepo.getAbsolutePath());
        LOGGER.log(Level.FINEST, "pushAssetToRepo srcFile(dir)=\"{0}\" targetFile=(dir)\"{1}\"",srcAsset.isDirectory(),
                                                targetRepo.isDirectory());

        if (!srcAsset.exists() || !srcAsset.canRead()) {
            throw new FileNotFoundException("Error: Source file cannot be read")
        } else if (targetRepo.isDirectory() || isDir) {
            if (!targetRepo.exists()) {
                throw new FileNotFoundException("Error: Target repo does not exist")
            } else if (targetRepo.exists() && !targetRepo.canWrite()) {
                throw new FileNotFoundException("Error: Target exists and is not writeable")
            }         
        } else if (!targetRepo.isDirectory() && !isDir) {
            if (!targetRepo.getParentFile().exists() || !targetRepo.getParentFile().canWrite()) {
                throw new FileNotFoundException("Error: Target repo does not exist or is not writeable") 
            } else if (targetRepo.exists() && !targetRepo.canWrite()) {
                throw new FileNotFoundException("Error: Target exists and is not writeable")
            }
        }

        if (targetRepo.exists() && !targetRepo.canWrite()) {
            throw new FileNotFoundException("Error: Target exists and is not writeable")
        }

        // Determine what type of copy is needed...
        // Note: For some reason the isFile() is not working for the first statement
        //       so I'm using !isDirectory() instead...
        if (!srcAsset.isDirectory() && (!targetRepo.isDirectory() && !isDir)) {
            Utilities.copyFile(srcAsset,targetRepo)        
        } else if (srcAsset.isDirectory() && (targetRepo.isDirectory() || isDir)) {
            Utilities.copyDirectories(srcAsset,targetRepo,isDir)
        } else if (srcAsset.isDirectory() && (targetRepo.isFile() && !isDir)) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        } else if (srcAsset.isFile() && (targetRepo.isDirectory() || isDir)) {
            Utilities.copyFile(srcAsset,targetRepo)
        } else {
            return false
        }        

        return true
    }

}

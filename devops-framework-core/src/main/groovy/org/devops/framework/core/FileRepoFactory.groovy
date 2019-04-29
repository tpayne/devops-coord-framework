/**
 * Repository routines for use with the framework
 */
package org.devops.framework.core;

import java.util.logging.Logger
import java.util.logging.Level

import hudson.Launcher
import hudson.FilePath

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
     * @param final Launcher - launcher     
     * @param final FilePath - remoteArea          
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    protected static final boolean pullAssetFromRepo(final URI srcAsset,
                                        final File targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {

        if (srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }
        File srcFile = new File(srcAsset.getPath())
        return pullAssetFromRepo(srcFile,targetAsset,
                                userName,userPwd,outputStr,isDir,
                                launcher,remoteArea)
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
     * @param final Launcher - launcher     
     * @param final FilePath - remoteArea          
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    protected static final boolean pullAssetFromRepo(final File srcAsset,
                                        final File targetAsset,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)                                       
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {

        if (srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        LOGGER.log(Level.FINEST, "pullAssetFromRepo srcFile=\"{0}\" targetFile=\"{1}\"",srcAsset.getAbsolutePath(),
                                                targetAsset.getAbsolutePath());
        LOGGER.log(Level.FINEST, "pullAssetFromRepo srcFile(dir)=\"{0}\" targetFile=(dir)\"{1}\" isDir={2}",srcAsset.isDirectory(),
                                                targetAsset.isDirectory(),isDir);

        if (remoteArea == null) {
            if (!srcAsset.exists() || !srcAsset.canRead()) {
                throw new FileNotFoundException("Error: Source file cannot be read")
            }
            if (targetAsset.exists() && !targetAsset.canWrite()) {
                throw new FileNotFoundException("Error: Target exists and is not writeable")
            }
        } else {
            FilePath src = new FilePath(remoteArea.getChannel(),srcAsset.getAbsolutePath())
            FilePath trg = new FilePath(remoteArea.getChannel(),targetAsset.getAbsolutePath())

            if (!src.exists()) {
                throw new FileNotFoundException("Error: Source file does not exist")
            }
        }     

        if (remoteArea == null) {
            // Determine what type of copy is needed...
            // Note: For some reason the isFile() is not working for the first statement
            //       so I'm using !isDirectory() instead...
            if (!srcAsset.isDirectory() && (!targetAsset.isDirectory() && !isDir)) {
                LOGGER.log(Level.FINEST, "pullAssetFromRepo local file copy")
                Utilities.copyFile(srcAsset,targetAsset)        
            } else if (srcAsset.isDirectory() && (targetAsset.isDirectory() || isDir)) {
                LOGGER.log(Level.FINEST, "pullAssetFromRepo local dir2dir copy")
                Utilities.copyDirectories(srcAsset,targetAsset,isDir)
            } else if (srcAsset.isDirectory() && (!targetAsset.isDirectory() && !isDir)) {
                String errorMsg = "Error: Invalid parameters specified. Target '"+targetAsset.getAbsolutePath()+"' is not a directory"
                throw new IllegalArgumentException(errorMsg)
            } else if (!srcAsset.isDirectory() && (targetAsset.isDirectory() || isDir)) {
                LOGGER.log(Level.FINEST, "pullAssetFromRepo local file2dir copy")
                Utilities.copyFile(srcAsset,targetAsset)        
            } else {
                return false
            }        
        } else {
            FilePath src = new FilePath(remoteArea.getChannel(),srcAsset.getAbsolutePath())
            FilePath trg = new FilePath(remoteArea.getChannel(),targetAsset.getAbsolutePath())

            if (!src.isDirectory() && (!trg.isDirectory() && !isDir)) {
                LOGGER.log(Level.FINEST, "pullAssetFromRepo remote file copy")
                try {
                    src.copyTo(trg)
                } catch(IOException ex) {
                    LOGGER.log(Level.FINEST, "pullAssetFromRepo remote file2dir copy ex {0} {1}",
                                ex.getMessage(),
                                Utilities.getStackTraceAsString(ex))
                    throw new IOException(ex.getMessage())
                }        
            } else if (src.isDirectory() && (trg.isDirectory() || isDir)) {
                LOGGER.log(Level.FINEST, "pullAssetFromRepo remote dir2dir copy")
                try {
                    src.copyRecursiveTo(trg)
                } catch(IOException ex) {
                    LOGGER.log(Level.FINEST, "pullAssetFromRepo remote dir2dir copy ex {0} {1}",
                                ex.getMessage(),
                                Utilities.getStackTraceAsString(ex))
                    throw new IOException(ex.getMessage())
                }        
            } else if (src.isDirectory() && (!trg.isDirectory() && !isDir)) {
                String errorMsg = "Error: Invalid parameters specified. Target '"+targetAsset.getAbsolutePath()+"' is not a directory"
                throw new IllegalArgumentException(errorMsg)
            } else if (!src.isDirectory() && (trg.isDirectory() || isDir)) {
                String errorMsg = "Error: Invalid parameters specified. Target '"+targetAsset.getAbsolutePath()+"' is not a file"
                throw new IllegalArgumentException(errorMsg)
            } else {
                return false
            }                                
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
     * @param final Launcher - launcher     
     * @param final FilePath - remoteArea          
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    protected static final boolean pushAssetToRepo(final File srcAsset,
                                        final URI targetRepo,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {

        if (srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        File targetFile = new File(targetRepo.getPath())
        return pushAssetToRepo(srcAsset,targetFile,
                                userName,userPwd,outputStr,isDir,
                                launcher,remoteArea)
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
     * @param final Launcher - launcher     
     * @param final FilePath - remoteArea          
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception
     */
    protected static final boolean pushAssetToRepo(final File srcAsset,
                                        final File targetRepo,
                                        final String userName=null,
                                        final String userPwd=null,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {
        
        if (srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        LOGGER.log(Level.FINEST, "pushAssetToRepo srcFile=\"{0}\" targetFile=\"{1}\"",srcAsset.getAbsolutePath(),
                                                targetRepo.getAbsolutePath());
        LOGGER.log(Level.FINEST, "pushAssetToRepo srcFile(dir)=\"{0}\" targetFile=(dir)\"{1}\"",srcAsset.isDirectory(),
                                                targetRepo.isDirectory());

        if (remoteArea == null) {
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
        } else {
            FilePath src = new FilePath(remoteArea.getChannel(),srcAsset.getAbsolutePath())
            FilePath trg = new FilePath(remoteArea.getChannel(),targetRepo.getAbsolutePath())

            if (!src.exists()) {
                throw new FileNotFoundException("Error: Source file does not exist")
            } else if (trg.isDirectory() || isDir) {
                if (!trg.exists()) {
                    throw new FileNotFoundException("Error: Target repo does not exist")
                }        
            } else if (!trg.isDirectory() && !isDir) {
                if (!trg.getParent().exists()) {
                    throw new FileNotFoundException("Error: Target repo does not exist") 
                }
            }
        }

        if (remoteArea == null) {
            // Determine what type of copy is needed...
            // Note: For some reason the isFile() is not working for the first statement
            //       so I'm using !isDirectory() instead...
            if (!srcAsset.isDirectory() && (!targetRepo.isDirectory() && !isDir)) {
                LOGGER.log(Level.FINEST, "pushAssetToRepo local file copy")
                Utilities.copyFile(srcAsset,targetRepo)        
            } else if (srcAsset.isDirectory() && (targetRepo.isDirectory() || isDir)) {
                LOGGER.log(Level.FINEST, "pushAssetToRepo local dir2dir copy")
                Utilities.copyDirectories(srcAsset,targetRepo,isDir)
            } else if (srcAsset.isDirectory() && (targetRepo.isFile() && !isDir)) {
                throw new IllegalArgumentException("Error: Invalid parameters specified")
            } else if (srcAsset.isFile() && (targetRepo.isDirectory() || isDir)) {
                LOGGER.log(Level.FINEST, "pushAssetToRepo local file2dir copy")
                Utilities.copyFile(srcAsset,targetRepo)
            } else {
                return false
            }        
        } else {
            FilePath src = new FilePath(remoteArea.getChannel(),srcAsset.getAbsolutePath())
            FilePath trg = new FilePath(remoteArea.getChannel(),targetRepo.getAbsolutePath())

            if (!src.isDirectory() && (!src.isDirectory() && !isDir)) {
                LOGGER.log(Level.FINEST, "pushAssetToRepo remote file copy")
                try {
                    src.copyTo(trg)
                } catch(IOException ex) {
                    LOGGER.log(Level.FINEST, "pushAssetToRepo remote file copy ex {0} {1}",
                                ex.getMessage(),
                                Utilities.getStackTraceAsString(ex))
                    throw new IOException(ex.getMessage())
                }        
            } else if (src.isDirectory() && (trg.isDirectory() || isDir)) {
                LOGGER.log(Level.FINEST, "pushAssetToRepo remote dir2dir copy")
                try {
                    src.copyRecursiveTo(trg)
                } catch(IOException ex) {
                    LOGGER.log(Level.FINEST, "pushAssetToRepo remote dir2dir copy ex {0} {1}",
                                ex.getMessage(),
                                Utilities.getStackTraceAsString(ex))
                    throw new IOException(ex.getMessage())
                }        
            } else if (src.isDirectory() && (!trg.isDirectory() && !isDir)) {
                throw new IllegalArgumentException("Error: Invalid parameters specified")
            } else if (!src.isDirectory() && (trg.isDirectory() || isDir)) {
                LOGGER.log(Level.FINEST, "pushAssetToRepo remote file2dir copy")
                String errorMsg = "Error: Invalid parameters specified. Target '"+targetRepo.getAbsolutePath()+"' is not a file"
                throw new IllegalArgumentException(errorMsg)
            } else {
                return false
            }        
        }
        return true
    }

}

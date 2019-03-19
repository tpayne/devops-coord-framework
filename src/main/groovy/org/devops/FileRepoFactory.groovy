/**
 * Repository routines for use with the framework
 */
package org.devops;

class FileRepoFactory extends RepoFactory {

    /**
     * Utility to pull asset(s) from a repo
     * 
     * @param final File - srcAsset
     * @param final File - targetAsset
     * @return boolean 
     * @throws IllegalArgumentException, FileNotFoundException, Exception
     */
    static final boolean pullAssetFromRepo(final File srcAsset,
                                        final File targetAsset)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (srcAsset == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        if (!srcAsset.exists() || !srcAsset.canRead()) {
            throw new FileNotFoundException("Error: Source file cannot be read")
        }
        if (targetAsset.exists() && !targetAsset.canWrite()) {
            throw new FileNotFoundException("Error: Target exists and is not writeable")
        }

        // Determine what type of copy is needed...
        if (srcAsset.isFile() && targetAsset.isFile()) {
            Utilities.copyFile(srcAsset,targetAsset)        
        } else if (srcAsset.isDirectory() && targetAsset.isDirectory()) {
            Utilities.copyDirectories(srcAsset,targetAsset)
        } else if (srcAsset.isDirectory() && targetAsset.isFile()) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        } else if (srcAsset.isFile() && targetAsset.isDirectory()) {
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
     * @param final File - targetRepo
     * @return boolean 
     * @throws IllegalArgumentException, Exception
     */
    static final boolean pushAssetToRepo(final File srcAsset,
                                        final File targetRepo)
        throws IllegalArgumentException, FileNotFoundException, Exception {
        
        if (srcAsset == null || targetRepo == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        if (!srcAsset.exists() || !srcAsset.canRead()) {
            throw new FileNotFoundException("Error: Source file cannot be read")
        } else if (!targetRepo.exists()) {
            throw new FileNotFoundException("Error: Target repo does not exist")            
        }

        if (targetRepo.exists() && !targetRepo.canWrite()) {
            throw new FileNotFoundException("Error: Target exists and is not writeable")
        }

        // Determine what type of copy is needed...
        if (srcAsset.isFile() && targetRepo.isFile()) {
            Utilities.copyFile(srcAsset,targetRepo)        
        } else if (srcAsset.isDirectory() && targetRepo.isDirectory()) {
            Utilities.copyDirectories(srcAsset,targetRepo)
        } else if (srcAsset.isDirectory() && targetRepo.isFile()) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        } else if (srcAsset.isFile() && targetRepo.isDirectory()) {
            Utilities.copyFile(srcAsset,targetRepo)
        } else {
            return false
        }        

        return true
    }

}

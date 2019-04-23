/**
 * SCM routines for use with the framework
 */
package org.devops.framework.core;

import java.util.logging.Logger
import java.util.logging.Level

import hudson.Launcher
import hudson.FilePath

class SCM implements Serializable {

   private static final Logger LOGGER = Logger.getLogger( SCM.class.getName() )

    /**
     * Utility routine to clone code into a workarea
     * 
     * @param final String - scmType
     * @param final String - scmURI
     * @param StringBuffer - outputStr
     * @param final boolean - verbose     
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI,
                                StringBuffer outputStr=null,
                                final boolean verbose=false)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // SCM clone using no user or password...
        //
        return scmClone(scmType,scmURI,null,null,"",outputStr,verbose)
    }

    /**
     * Utility routine to clone code into a workarea
     * 
     * @param final String - scmType
     * @param final String - scmURI
     * @param final String - scmUser
     * @param final String - scmPwd
     * @param StringBuffer - outputStr
     * @param final boolean - verbose     
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI,
                                final String scmUser,
                                final String scmPwd,
                                StringBuffer outputStr=null,
                                final boolean verbose=false)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // SCM clone using user and passwd...
        //
        return scmClone(scmType,scmURI,scmUser,scmPwd,"",outputStr,verbose)
    }

    /**
     * Utility routine to clone code into a workarea
     * 
     * @param final String - scmType
     * @param final String - scmURI
     * @param final File - targetWorkArea
     * @param StringBuffer - outputStr
     * @param final boolean - verbose     
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI,
                                final File targetWorkArea,
                                StringBuffer outputStr=null,
                                final boolean verbose=false)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // SCM clone using workarea...
        //
        return scmClone(scmType,scmURI,null,null,targetWorkArea,outputStr,verbose)
    }

    /**
     * Utility routine to clone code into a workarea
     * 
     * @param final String - scmType
     * @param final String - scmURI
     * @param final String - scmUser
     * @param final String - scmPwd
     * @param final String - targetWorkArea
     * @param StringBuffer - outputStr
     * @param final boolean - verbose     
     * @param final Launcher - launcher     
     * @param final FilePath - remoteArea          
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI,
                                final String scmUser,
                                final String scmPwd,
                                final String targetWorkArea,
                                StringBuffer outputStr=null,
                                final boolean verbose=false,
                                final Launcher launcher=null,
                                final FilePath remoteArea=null)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // SCM clone using workarea...
        //
        File targetWorkAreaFile = null
        if (targetWorkArea != null && !targetWorkArea.isEmpty()) {
            LOGGER.log(Level.FINE, "scmClone wd=\"{0}\"",targetWorkArea);
            targetWorkAreaFile = new File(targetWorkArea)
        } else {
            targetWorkAreaFile = new File("")
        }
        return scmClone(scmType,scmURI,scmUser,scmPwd,targetWorkAreaFile,
                        outputStr,verbose,launcher,remoteArea)
    }

    /**
     * Utility routine to clone code into a workarea
     * 
     * @param final String - scmType
     * @param final String - scmURI
     * @param final String - scmUser
     * @param final String - scmPwd
     * @param final File - targetWorkArea
     * @param StringBuffer - outputStr
     * @param final boolean - verbose     
     * @param final Launcher - launcher  
     * @param final FilePath - remoteArea          
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI,
                                final String scmUser,
                                final String scmPwd,
                                final File targetWorkArea,
                                StringBuffer outputStr=null,
                                final boolean verbose=false,
                                final Launcher launcher=null,
                                final FilePath remoteArea=null)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // Test that the target directory specified exists & is readable...
        // This parameter could be nullable in the future, but for now make
        // it mandatory to ensure target...
        //
        if (remoteArea == null) {
            if (targetWorkArea == null || targetWorkArea.getName().isEmpty()) {
            }
            else if (targetWorkArea.exists() && targetWorkArea.canWrite() &&
                     targetWorkArea.isDirectory()) {
                LOGGER.log(Level.FINER, "scmClone wd=\"{0}\"",targetWorkArea.getAbsolutePath());
            } else {
                throw new FileNotFoundException("Error: The target workarea '"+
                                                targetWorkArea.getAbsolutePath()+
                                                "' either does not exist or is not writable")
            }
        } else {
            if (targetWorkArea == null || targetWorkArea.getName().isEmpty()) {
            } else {
                FilePath area = new FilePath(remoteArea.getChannel(),targetWorkArea.getAbsolutePath())
                if (area.exists() && area.isDirectory()) {
                    LOGGER.log(Level.FINER, "scmClone wd=\"{0}\"",area.getRemote());
                } else {
                    throw new FileNotFoundException("Error: The target workarea '"+
                                                targetWorkArea.getAbsolutePath()+
                                                "' either does not exist or is not writable")
                }
            }
        }            
        
        LOGGER.log(Level.FINER, "scmClone scmURI=\"{0}\"",scmURI);

        if (scmType == null || scmURI == null) {
            throw new IllegalArgumentException("Error: Invalud parameters specified")
        }
        // Test that the specified SCM engine exists...
        String scmExeName = null
        if (scmType == ConfigPropertiesConstants.SCMGIT) {
            scmExeName = "git"
        } else if (scmType == ConfigPropertiesConstants.SCMSVN) {
            scmExeName = "svn"
        } else {
            throw new IllegalArgumentException("Error: The SCM type specified is not supported")
        }

        File scmFile = null
        if (remoteArea == null) {
            scmFile = Utilities.getExecutable(scmExeName)
        } else {
            scmFile = Utilities.getExecutable(scmExeName,remoteArea)
        }

        // Construct the required command...
        String cmdStr = scmFile.getAbsolutePath()+" "
        String pathURI = scmURI
        
        if (scmType == ConfigPropertiesConstants.SCMGIT) {
            // In the future, we could use credential helpers, but not at this time...
            String repoType
            if (pathURI.contains(":")) {
                repoType = pathURI.substring(0,pathURI.lastIndexOf(':')+1).trim()
                pathURI = pathURI.substring(repoType.length(),pathURI.length()).trim()
            }
            cmdStr += " clone "+repoType
            if (scmUser && scmPwd) {
                cmdStr += "//"+scmUser+":"+scmPwd+"@"
                pathURI = pathURI.substring(2,pathURI.length())
            } 
            cmdStr += pathURI
            if (targetWorkArea != null && !targetWorkArea.getName().isEmpty()) {
                cmdStr += " "+targetWorkArea.getAbsolutePath()
            }
            if (!verbose) {
                cmdStr += " --quiet"
            } else {
                cmdStr += " --verbose"
            }
        }
        else if (scmType == ConfigPropertiesConstants.SCMSVN) {
            cmdStr += " co "
            cmdStr += scmURI
            if (scmUser && scmPwd) {
                cmdStr += " --username "+scmUser+" --password "+scmPwd
                pathURI = pathURI.substring(2,pathURI.length())
            } 
            if (targetWorkArea != null && !targetWorkArea.getName().isEmpty()) {
                cmdStr += " "+targetWorkArea.getAbsolutePath()
            }
            if (!verbose) {
                cmdStr += " --quiet"
            }
        }

        StringBuffer returnStr = new StringBuffer()

        int retStat = Utilities.runCmd(cmdStr,returnStr,null,launcher)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()

        LOGGER.log(Level.FINER, "scmClone output=\"{0}\"",returnOutput);
        LOGGER.log(Level.FINER, "scmClone host=\"{0}\"",Utilities.getHostName());
        LOGGER.log(Level.FINER, "scmClone osuser=\"{0}\"",Utilities.getOSUser());
        if (remoteArea == null) {
            if (targetWorkArea != null && !targetWorkArea.getName().isEmpty()) {
                LOGGER.log(Level.FINER, "scmClone file count=\"{0}\"",Utilities.countFiles(targetWorkArea));        
            }
        }
        returnStr = null    
        if (outputStr!=null) {
            outputStr.append(returnOutput)
        }

        return(retStat==0)
    }

}

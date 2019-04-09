/**
 * SCM routines for use with the framework
 */
package org.devops.framework.core;

class SCM implements Serializable {

    /**
     * Utility routine to clone code into a workarea
     * 
     * @param final String - scmType
     * @param final String - scmURI
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI,
                                StringBuffer outputStr=null)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // SCM clone using no user or password...
        //
        return scmClone(scmType,scmURI,null,null,null,outputStr)
    }

    /**
     * Utility routine to clone code into a workarea
     * 
     * @param final String - scmType
     * @param final String - scmURI
     * @param final String - scmUser
     * @param final String - scmPwd
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI,
                                final String scmUser,
                                final String scmPwd,
                                StringBuffer outputStr=null)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // SCM clone using user and passwd...
        //
        return scmClone(scmType,scmURI,scmUser,scmPwd,null,outputStr)
    }

    /**
     * Utility routine to clone code into a workarea
     * 
     * @param final String - scmType
     * @param final String - scmURI
     * @param final File - targetWorkArea
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI,
                                final File targetWorkArea,
                                StringBuffer outputStr=null)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // SCM clone using workarea...
        //
        return scmClone(scmType,scmURI,null,null,targetWorkArea,outputStr)
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
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI,
                                final String scmUser,
                                final String scmPwd,
                                final File targetWorkArea,
                                StringBuffer outputStr=null)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // Test that the target directory specified exists & is readable...
        // This parameter could be nullable in the future, but for now make
        // it mandatory to ensure target...
        //
        if (targetWorkArea == null) {
        }
        else if (targetWorkArea.exists() && targetWorkArea.canWrite() &&
                 targetWorkArea.isDirectory()) {
        } else {
            throw new FileNotFoundException("Error: The target workarea '"+
                                            targetWorkArea.getAbsolutePath()+
                                            "' either does not exist or is not writable")
        }

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

        File scmFile = Utilities.getExecutable(scmExeName)

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
            if (targetWorkArea) {
                cmdStr += " "+targetWorkArea.getAbsolutePath()
            }
            cmdStr += " --quiet"
        }
        else if (scmType == ConfigPropertiesConstants.SCMSVN) {
            cmdStr += " co "
            cmdStr += scmURI
            if (scmUser && scmPwd) {
                cmdStr += " --username "+scmUser+" --password "+scmPwd
                pathURI = pathURI.substring(2,pathURI.length())
            } 
            if (targetWorkArea) {
                cmdStr += " "+targetWorkArea.getAbsolutePath()
            }
            cmdStr += " --quiet"
        }

        StringBuffer returnStr = new StringBuffer()

        int retStat = Utilities.runCmd(cmdStr,returnStr)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null    
        if (outputStr!=null) {
            outputStr.append(returnOutput)
        }

        return(retStat==0)
    }

}

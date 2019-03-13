package org.devops;

/**
 * SCM routines for use with the framework
 */
class SCM implements Serializable {

    /**
     * Utility routine to clone code into a workarea
     * 
     * @param final String - scmType
     * @param final String - scmURI
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // SCM clone using no user or password...
        //
        return scmClone(scmType,scmURI,null,null,null)
    }

    /**
     * Utility routine to clone code into a workarea
     * 
     * @param final String - scmType
     * @param final String - scmURI
     * @param final File - targetWorkArea
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI,
                                final File targetWorkArea)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // SCM clone using no user or password...
        //
        return scmClone(scmType,scmURI,null,null,targetWorkArea)
    }

    /**
     * Utility routine to clone code into a workarea
     * 
     * @param final String - scmType
     * @param final String - scmURI
     * @param final String - scmUser
     * @param final String - scmPwd
     * @param final File - targetWorkArea
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean scmClone(final String scmType,
                                final String scmURI,
                                final String scmUser,
                                final String scmPwd,
                                final File targetWorkArea)
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
        if (scmType == ConfigPropertiesConstants.SCMGIT) {
            // In the future, we could use credential helpers, but not at this time...
            String pathURI = scmURI
            String repoType
            if (pathURI.contains(":")) {
                repoType = pathURI.substring(0,pathURI.lastIndexOf(':')+1).trim()
                pathURI = pathURI.substring(repoType.length(),pathURI.length()).trim()
            }
            cmdStr += " clone "+repoType
            if (scmUser && scmPwd) {
                cmdStr += scmUser+"@"+scmPwd
            } 
            cmdStr += pathURI
            if (targetWorkArea) {
                cmdStr += " "+targetWorkArea.getAbsolutePath()
            }
        }
        else if (scmType == ConfigPropertiesConstants.SCMSVN) {
            cmdStr += " co "
            cmdStr += scmURI
            if (targetWorkArea) {
                cmdStr += " "+targetWorkArea.getAbsolutePath()
            }        
        }

        StringBuffer returnStr = new StringBuffer()

        int retStat = Utilities.runCmd(cmdStr,returnStr)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null    

        return(retStat==0)
    }

}

package org.devops;

/**
 * Container routines for use with the framework
 */
class Container implements Serializable {
    // run
    // push
    // build
    // clean

    /**
     * Utility routine to run a container
     * 
     * @param final String - containerType
     * @param final String - containerName
     * @param final String - command
     * @param StringBuffer - outputStr
     * @param final Map    - vararg list
     * @return boolean 
     * @throws IllegalArgumentException, Exception
     */
    static final boolean runContainer(final String containerType,
                                      final String containerName,
                                      final String commandStr=null,
                                      StringBuffer outputStr=null,
                                      final Map    args = [:])
        throws IllegalArgumentException, Exception {
        
        if (containerType == null || containerName == null) {
            throw new IllegalArgumentException("Error: Invalud parameters specified")
        }
        // Test that the specified container engine exists...
        String containerExeName = null
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            containerExeName = "docker"
        } else {
            throw new IllegalArgumentException("Error: The container type specified is not supported")
        }

        File containerFile = Utilities.getExecutable(containerExeName)

        // Construct the required command...
        String cmdStr = containerFile.getAbsolutePath()+" "
        
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            cmdStr += " run "
            if (args) {
                args.each{
                    cmdStr += " "
                    cmdStr += it.key
                    cmdStr += " "
                    if (it.value) {
                        cmdStr += it.value
                    }
                }
            }
            cmdStr += " "+containerName
            if (commandStr) {
                cmdStr += " "+commandStr
            } 
        }

        StringBuffer returnStr = new StringBuffer()

        int retStat = Utilities.runCmd(cmdStr,returnStr)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        if (outputStr!=null) {
            outputStr.append(returnOutput)
        }
        returnStr = null    

        return(retStat==0)
    }
 
}

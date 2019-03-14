package org.devops;

/**
 * Container routines for use with the framework
 */
class Container implements Serializable {
    // push
    // build
    // clean

    /**
     * Utility to delete a container
     * 
     * @param final String - containerType
     * @param final String - containerName
     * @param final boolean - force
     * @param StringBuffer - outputStr
     * @param final Map    - vararg list
     * @return boolean 
     * @throws IllegalArgumentException, Exception
     */
    static final boolean deleteContainerImage(final String containerType,
                                      final String containerName,
                                      final boolean force=false,
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
            cmdStr += " rmi "
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
            if (force) {
                cmdStr += " -f "
            }
            cmdStr += " "+containerName 
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

    /**
     * Utility routine to build a container
     * 
     * @param final String - containerType
     * @param final String - containerName
     * @param final File   - buildDirectory
     * @param final File   - build file
     * @param StringBuffer - outputStr
     * @param final Map    - vararg list
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean buildContainer(final String containerType,
                                      final String containerName,
                                      final File buildDirectory,
                                      final File containerFile=null,
                                      StringBuffer outputStr=null,
                                      final Map    args = [:])
        throws FileNotFoundException, IllegalArgumentException, Exception {
        
        if (containerType == null || containerName == null || buildDirectory == null) {
            throw new IllegalArgumentException("Error: Invalud parameters specified")
        }

        // Test build directory exists...
        if (!buildDirectory.exists() || !buildDirectory.canWrite()) {
            throw new FileNotFoundException("Error: Build directory specified is invalid")          
        }

        // Test containerFile is valid...
        if (containerFile != null) {
            if (!containerFile.exists() || !containerFile.exists()) {
              throw new FileNotFoundException("Error: Container file specified is invalid")          
            }
        }

        // Test that the specified container engine exists...
        String containerExeName = null
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            containerExeName = "docker"
        } else {
            throw new IllegalArgumentException("Error: The container type specified is not supported")
        }

        File containerExeFile = Utilities.getExecutable(containerExeName)

        // Construct the required command...
        String cmdStr = containerExeFile.getAbsolutePath()+" "
        
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            cmdStr += " build -q "
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
            cmdStr += " "+buildDirectory.getAbsolutePath()+" "
            if (containerFile != null) {
                cmdStr += " "
                cmdStr += " -f " + containerFile.getAbsolutePath()
            }
            cmdStr += " -t "+containerName
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

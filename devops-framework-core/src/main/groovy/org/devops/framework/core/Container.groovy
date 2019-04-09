/**
 * Container routines for use with the framework
 */
package org.devops.framework.core;

class Container implements Serializable {

    /**
     * Utility routine to create a container registry
     * Note: This routine is not intended for use, it is simply
     *       present to support functional/unit-testing requirements
     *
     * @param final String - containerType
     * @param final String - registryName
     * @param final String - imageName
     * @param final int    - portNo
     * @param StringBuffer - outputStr
     * @param final Map    - vararg list
     * @return boolean 
     * @throws IllegalArgumentException, Exception
     */
    static final boolean createContainerRegistry(final String containerType,
                                      final String registryName,
                                      final String imageName,
                                      final int    portNo=5000,
                                      final String commandStr=null,
                                      StringBuffer outputStr=null,
                                      final Map    args = [:])
        throws IllegalArgumentException, Exception {
        
        if (containerType == null || registryName == null || imageName == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }
        // Test that the specified container engine exists...
        String containerExeName = null
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            containerExeName = "docker"
        } else {
            throw new IllegalArgumentException("Error: The container type specified is not supported")
        }

        File containerFile = Utilities.getExecutable(containerExeName)
        if (containerFile == null) {
            throw new Exception("Error: Container engine "+containerExeName+" has not been located")
        }

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
            cmdStr += " -d -p "+portNo+":"+portNo
            cmdStr += " --restart=always --name "
            cmdStr += " "+registryName+" "+imageName
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
     * Utility to push a container
     * 
     * @param final String - containerType
     * @param final String - imageName
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, Exception
     */
    static final boolean pushContainer(final String containerType,
                                      final String imageName,
                                      StringBuffer outputStr=null)
        throws IllegalArgumentException, Exception {
        
        if (containerType == null || imageName == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }
        // Test that the specified container engine exists...
        String containerExeName = null
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            containerExeName = "docker"
        } else {
            throw new IllegalArgumentException("Error: The container type specified is not supported")
        }

        File containerFile = Utilities.getExecutable(containerExeName)
        if (containerFile == null) {
            throw new Exception("Error: Container engine "+containerExeName+" has not been located")
        }

        // Construct the required command...
        String cmdStr = containerFile.getAbsolutePath()+" "
        
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            cmdStr += " push  "
            cmdStr += imageName
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
     * Utility to tag a container
     * 
     * @param final String - containerType
     * @param final String - srcImageName
     * @param final String - trgImageName
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, Exception
     */
    static final boolean tagContainer(final String containerType,
                                      final String srcImageName,
                                      final String trgImageName,
                                      StringBuffer outputStr=null)
        throws IllegalArgumentException, Exception {
        
        if (containerType == null || srcImageName == null || trgImageName == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }
        // Test that the specified container engine exists...
        String containerExeName = null
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            containerExeName = "docker"
        } else {
            throw new IllegalArgumentException("Error: The container type specified is not supported")
        }

        File containerFile = Utilities.getExecutable(containerExeName)
        if (containerFile == null) {
            throw new Exception("Error: Container engine "+containerExeName+" has not been located")
        }

        // Construct the required command...
        String cmdStr = containerFile.getAbsolutePath()+" "
        
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            cmdStr += " tag  "
            cmdStr += " "+srcImageName
            cmdStr += " "+trgImageName 
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
     * Utility to remove a registry
     * 
     * @param final String - containerType
     * @param final String - registryName
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, Exception
     */
    static final boolean deleteContainerRegistry(final String containerType,
                                      final String registryName,
                                      StringBuffer outputStr=null)
        throws IllegalArgumentException, Exception {
        
        if (containerType == null || registryName == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }
        // Test that the specified container engine exists...
        String containerExeName = null
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            containerExeName = "docker"
        } else {
            throw new IllegalArgumentException("Error: The container type specified is not supported")
        }

        File containerFile = Utilities.getExecutable(containerExeName)
        if (containerFile == null) {
            throw new Exception("Error: Container engine "+containerExeName+" has not been located")
        }

        // Construct the required command...
        String cmdStr = containerFile.getAbsolutePath()+" "
        
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            cmdStr += " container stop "+registryName
        }

        StringBuffer returnStr = new StringBuffer()

        int retStat = Utilities.runCmd(cmdStr,returnStr)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        if (outputStr!=null) {
            outputStr.append(returnOutput)
        }

        if (retStat!=0) {
            returnStr = null
            return false
        }

        returnOutput = null
        cmdStr = null
        cmdStr = containerFile.getAbsolutePath()+" "

        if (returnStr.length() > 0) {
            returnStr.delete(0, returnStr.length())
        }

        if (containerType == ConfigPropertiesConstants.DOCKER) {
            cmdStr += " container rm -v "+registryName
        }

        retStat = Utilities.runCmd(cmdStr,returnStr)
        returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        if (outputStr!=null) {
            outputStr.append("\n"+returnOutput)
        }        

        returnStr = null    

        return(retStat==0)
    }

    /**
     * Utility to pull a container
     * 
     * @param final String - containerType
     * @param final String - imageName
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws IllegalArgumentException, Exception
     */
    static final boolean pullContainerImage(final String containerType,
                                            final String imageName,
                                            StringBuffer outputStr=null)
        throws IllegalArgumentException, Exception {
        
        if (containerType == null || imageName == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }
        // Test that the specified container engine exists...
        String containerExeName = null
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            containerExeName = "docker"
        } else {
            throw new IllegalArgumentException("Error: The container type specified is not supported")
        }

        File containerFile = Utilities.getExecutable(containerExeName)
        if (containerFile == null) {
            throw new Exception("Error: Container engine "+containerExeName+" has not been located")
        }

        // Construct the required command...
        String cmdStr = containerFile.getAbsolutePath()+" "
        
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            cmdStr += " pull  "
            cmdStr += " "+imageName
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
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }
        // Test that the specified container engine exists...
        String containerExeName = null
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            containerExeName = "docker"
        } else {
            throw new IllegalArgumentException("Error: The container type specified is not supported")
        }

        File containerFile = Utilities.getExecutable(containerExeName)
        if (containerFile == null) {
            throw new Exception("Error: Container engine "+containerExeName+" has not been located")
        }

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
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }
        // Test that the specified container engine exists...
        String containerExeName = null
        if (containerType == ConfigPropertiesConstants.DOCKER) {
            containerExeName = "docker"
        } else {
            throw new IllegalArgumentException("Error: The container type specified is not supported")
        }

        File containerFile = Utilities.getExecutable(containerExeName)
        if (containerFile == null) {
            throw new Exception("Error: Container engine "+containerExeName+" has not been located")
        }

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
     * @param final String - buildDirectoryStr
     * @param final String - build file
     * @param StringBuffer - outputStr
     * @param final Map    - vararg list
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean buildContainer(final String containerType,
                                      final String containerName,
                                      final String buildDirectoryStr,
                                      final String containerFileStr=null,
                                      StringBuffer outputStr=null,
                                      final Map    args = [:])
        throws FileNotFoundException, IllegalArgumentException, Exception {

          File buildDirectory = new File(buildDirectoryStr);
          File containerFile = new File(containerFileStr);
          boolean retStat = false;

          try {
            retStat = buildContainer(containerType,
                                      containerName,
                                      buildDirectory,
                                      containerFile,
                                      outputStr,
                                      args);
          } catch(Exception ex) {
            buildDirectory = null;
            containerFile = null;
            throw ex; 
          }

          buildDirectory = null;
          containerFile = null;
        
          return retStat;
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
            throw new IllegalArgumentException("Error: Invalid parameters specified")
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
        if (containerExeFile == null) {
            throw new Exception("Error: Container engine "+containerExeName+" has not been located")
        }

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

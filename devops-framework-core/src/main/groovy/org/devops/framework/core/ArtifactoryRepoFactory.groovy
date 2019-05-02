/**
 * Repository routines for use with the framework
 */
package org.devops.framework.core;

import groovy.json.*

import java.util.logging.Logger
import java.util.logging.Level

import hudson.Launcher
import hudson.FilePath

class ArtifactoryRepoFactory extends RepoFactory {

    private static final Logger LOGGER = Logger.getLogger( ArtifactoryRepoFactory.class.getName() )
    private static final long serialVersionUID = 1L;

    /**
     * Utility to pull asset(s) from a repo
     *
     * @param final URI - srcRepo
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
    static final boolean pullAssetFromRepo(final URI srcRepo,
                                        final File targetAsset,
                                        final String userName,
                                        final String userPwd,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {

        if (srcRepo == null || targetAsset == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        if (srcRepo.toURL().toString().endsWith("/")) {
            throw new IllegalArgumentException("Error: The URI has to refer to a file. Directories are not supported")
        }

        File exeRun = null
        String exeName = "curl"
        // TODO add jfrog support sometime when it works better

        try {
            if (remoteArea == null) {
                exeRun = Utilities.getExecutable(exeName)
            } else {
                exeRun = Utilities.getExecutable(exeName,remoteArea)
            }
        } catch (Exception e) {
            throw new FileNotFoundException("Error: Curl has not been located")
        }

        LOGGER.log(Level.FINE, "pullAssetFromRepo src=\"{0}\" target=\"{1}\"",
                    srcRepo.toURL().toString(),targetAsset.getAbsolutePath());

        def cmdStr = null
        if (userName != null && !userName.isEmpty() &&
            userPwd != null && !userPwd.isEmpty()) {
            cmdStr = "-u${userName}:${userPwd} "
            LOGGER.log(Level.FINE, "pullAssetFromRepo username and passwd specified")
        } else {
            cmdStr = ""
            LOGGER.log(Level.FINE, "pullAssetFromRepo no username and passwd")
        }

        cmdStr += " -o \""+targetAsset.getAbsolutePath()+"\" -O \""+srcRepo.toURL().toString()+"\""

        // Construct the required command...
        def runCmd = exeRun.getAbsolutePath()+" "+cmdStr
        exeRun = null
        StringBuffer returnStr = new StringBuffer()
        int retStat = Utilities.runCmd(runCmd,returnStr,null,launcher,true)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null
        LOGGER.log(Level.FINE, "pullAssetFromRepo output=\"{0}\"",returnOutput);

        if (outputStr!=null) {
            outputStr.append(returnOutput)
        }
        returnStr = null

        if (remoteArea == null) {
            if (retStat>0 || !targetAsset.exists()) {
                return false
            }
        } else {
            FilePath area = new FilePath(remoteArea.getChannel(),targetAsset.getAbsolutePath())
            if (retStat>0 || !area.exists()) {
                area = null
                return false
            }
            area = null
        }

        if (retStat==0) {
            // Now we need to test the file to see if it is error message...
            try {
                FilePath area = null
                def slurper = new JsonSlurper()
                def result = null
                if (remoteArea == null) {
                    result = slurper.parse(targetAsset)
                } else {
                    area = new FilePath(remoteArea.getChannel(),targetAsset.getAbsolutePath())
                    result = slurper.parse(area.read())
                }
                if (result != null) {
                    if (outputStr != null) {
                        outputStr.delete(0, outputStr.length())
                        outputStr.append(result.toString())
                    }
                    if (result.toString().contains("{errors=[{")) {
                        if (remoteArea == null) {
                            targetAsset.delete()
                        } else {
                            area.delete()
                        }
                        if (result.errors.status.toString().contains("[404]")) {
                            throw new FileNotFoundException("Error: Source file specified was not found on server")
                        } else if (result.errors.status.toString().contains("[401]")) {
                            throw new SecurityException("Error: Authorization failed for source")
                        }
                        area = null
                        return false
                    }
                    area = null
                }
            } catch (JsonException e) {
                return true
            }
        }
        return false
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
    static final boolean pushAssetToRepo(final File srcAsset,
                                        final URI targetRepo,
                                        final String userName,
                                        final String userPwd,
                                        StringBuffer outputStr=null,
                                        boolean isDir=false,
                                        final Launcher launcher=null,
                                        final FilePath remoteArea=null)
        throws IllegalArgumentException, FileNotFoundException, SecurityException, Exception {

        if (srcAsset == null || targetRepo == null || userName == null || userPwd == null) {
            throw new IllegalArgumentException("Error: Invalid parameters specified")
        }

        if (userName.isEmpty() ||
            userPwd.isEmpty()) {
            throw new SecurityException("Error: Username and password must be specified")
        }

        if (remoteArea == null) {
            if (!srcAsset.exists() || !srcAsset.canRead() || !srcAsset.isFile()) {
                throw new FileNotFoundException("Error: Source file '"+srcAsset.getAbsolutePath()+"' cannot be read")
            }
        } else {
            FilePath src = new FilePath(remoteArea.getChannel(),srcAsset.getAbsolutePath())
            if (!src.exists() || src.isDirectory()) {
                throw new FileNotFoundException("Error: Source file '"+srcAsset.getAbsolutePath()+
                								"' cannot be read or is not a file")
            }
        }

        File exeRun = null
        String exeName = "curl"
        // TODO add jfrog support sometime when it works better

        try {
            if (remoteArea == null) {
                exeRun = Utilities.getExecutable(exeName)
            } else {
                exeRun = Utilities.getExecutable(exeName,remoteArea)
            }
        } catch (Exception e) {
            throw new FileNotFoundException("Error: Curl has not been located")
        }

        LOGGER.log(Level.FINE, "pushAssetToRepo src=\"{0}\" target=\"{1}\"",
                    srcAsset.getAbsolutePath(),targetRepo.toURL().toString());

        def cmdStr = null
        if (userName != null && !userName.isEmpty() &&
            userPwd != null && !userPwd.isEmpty()) {
            cmdStr = "-u${userName}:${userPwd} "
            LOGGER.log(Level.FINE, "pullAssetFromRepo username and passwd specified")
        } else {
            cmdStr = ""
            LOGGER.log(Level.FINE, "pullAssetFromRepo no username and passwd")
        }

        cmdStr += " -T \""+srcAsset.getAbsolutePath()+"\" \""+targetRepo.toURL().toString()+"\""

        // Construct the required command...
        def runCmd = exeRun.getAbsolutePath()+" "+cmdStr
        exeRun = null
        StringBuffer returnStr = new StringBuffer()

        int retStat = Utilities.runCmd(runCmd,returnStr,null,launcher,true)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null
        LOGGER.log(Level.FINE, "pushAssetToRepo output=\"{0}\"",returnOutput);

        // Parse output for any errors...
        if (retStat==0) {
            if (returnOutput.contains("\"errors\" : [ {")) {
                if (returnOutput.contains("\"status\" : 404") ||
                    returnOutput.contains("\"status\" : 403")) {
                    throw new FileNotFoundException("Error: Target file specified was not found on server")
                } else if (returnOutput.contains("\"status\" : 401")) {
                    throw new SecurityException("Error: Authorization failed for target")
                }
                return false
            } else if (returnOutput.contains("\"createdBy\"") && returnOutput.contains("\"downloadUri\"")) {
            } else {
                retStat=1
            }

        }
        if (outputStr!=null) {
            outputStr.append(returnOutput)
        }
        returnStr = null

        if (retStat>0) {
            return false
        } else if (retStat==0) {
            return true
        }
        return false
    }

}

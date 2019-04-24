/**
 * Pipeline plugin extension for tasks
 */
package org.devops.framework.plugin;

import hudson.FilePath;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;
import hudson.model.Run;
import hudson.Launcher;

import java.io.File;
import java.io.IOException;

import org.devops.framework.core.ConfigPropertiesConstants;
import org.devops.framework.core.Container;
import org.devops.framework.core.Utilities;

/**
 * Container class for DSL remotable logic.
 */
class ContainerCmdTask extends GenericCmdTask {

    // Local variables...
    private String containerName;
    private String targetName;
    private String cmdStr;
    private String buildDir;
    private String containerFile;
    private boolean force;

    private String containerType;
    private String cmd;

    /**
     * Default constructor
     * 
     * @param FilePath - workspace
     * @param TaskListener - listener
     * @param Run - build
     * @param Launcher - launcher  
     * @param final String - repoName
     * @param final String - userName
     * @param final String - userPwd
     * @param final String - targetDir          
     */
    public ContainerCmdTask(String containerType, 
            String cmd,
            FilePath workspace, 
            TaskListener listener, 
            Run build, 
            Launcher launcher,
            final String containerName, 
            final String cmdStr, 
            final boolean force,
            final String targetName, 
            final String buildDir,
            final String containerFile) {
        super(workspace, listener, build, launcher);
        this.containerName = containerName;
        this.cmdStr = cmdStr;
        this.force = force;
        this.targetName = targetName;
        this.buildDir = buildDir;
        this.containerFile = containerFile;
        this.containerType = containerType;
        this.cmd = cmd;
    }

    /**
     * Process the Docker command
     */
    @Override
    public Boolean executeMaster() throws IOException, InterruptedException {
        StringBuffer outputStr = new StringBuffer();
        boolean retStat = false;
        try {
            if (cmd.equals("TAG")) {
                retStat = Container.tagContainer(containerType,
                                                containerName,
                                                targetName,
                                                outputStr);
            } else if (cmd.equals("RUN")) {
                retStat = Container.runContainer(containerType,
                                                containerName, 
                                                cmdStr, 
                                                outputStr);
            } else if (cmd.equals("PULL")) {
                retStat = Container.pullContainerImage(containerType,
                                                containerName,
                                                outputStr);            
            } else if (cmd.equals("DELETE")) {
                retStat = Container.deleteContainerImage(containerType,
                                                containerName,
                                                force,
                                                outputStr);            
            } else if (cmd.equals("BUILD")) {
                retStat = Container.buildContainer(containerType,
                                                containerName,
                                                buildDir,
                                                containerFile,
                                                outputStr);
            } else if (cmd.equals("PUSH")) {
                retStat = Container.pushContainer(containerType,
                                                containerName,
                                                outputStr);
            }                  
            String output = outputStr.toString();
            if (retStat) {
                listener.getLogger().println(output);
            } else {
                listener.error(output);
            }
            return retStat;
        } catch(Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Process the Docker command
     */
    @Override
    public Boolean executeSlave() throws IOException, InterruptedException {
        StringBuffer outputStr = new StringBuffer();
        boolean retStat = false;
        try {
            if (cmd.equals("TAG")) {
                retStat = Container.tagContainer(containerType,
                                                containerName,
                                                targetName,
                                                outputStr,
                                                launcher,
                                                workspace);
            } else if (cmd.equals("RUN")) {
                retStat = Container.runContainer(containerType,
                                                containerName, 
                                                cmdStr, 
                                                outputStr,
                                                null,
                                                launcher,
                                                workspace);
            } else if (cmd.equals("PULL")) {
                retStat = Container.pullContainerImage(containerType,
                                                containerName,
                                                outputStr,
                                                launcher,
                                                workspace);            
            } else if (cmd.equals("DELETE")) {
                retStat = Container.deleteContainerImage(containerType,
                                                containerName,
                                                force,
                                                outputStr,
                                                null,
                                                launcher,
                                                workspace);            
            } else if (cmd.equals("BUILD")) {
                retStat = Container.buildContainer(containerType,
                                                containerName,
                                                buildDir,
                                                containerFile,
                                                outputStr,
                                                null,
                                                launcher,
                                                workspace);
            } else if (cmd.equals("PUSH")) {
                retStat = Container.pushContainer(containerType,
                                                containerName,
                                                outputStr,
                                                launcher,
                                                workspace);
            }            
            String output = outputStr.toString();
            if (retStat) {
                listener.getLogger().println(output);
            } else {
                listener.error(output);
            }
            return retStat;
        } catch(Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}

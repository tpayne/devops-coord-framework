/**
 * Pipeline plugin extension for tasks
 */
package org.devops.framework.plugin;

import hudson.FilePath;
import hudson.model.TaskListener;
import hudson.model.Run;
import hudson.Launcher;

import java.io.IOException;

import org.devops.framework.core.Container;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Container class for DSL remotable logic.
 */
class ContainerCmdTask extends GenericCmdTask {

    private static final Logger LOGGER = Logger.getLogger( ContainerCmdTask.class.getName() );

    // Local variables...
    private String containerName;
    private String targetName;
    private String cmdStr;
    private String buildDir;
    private String containerFile;
    private boolean force;
    private boolean quiet=false;

    private String containerType;
    private String cmd;

    /**
     * Default constructor
     * 
     * @param final String - containerType
     * @param final String - cmd
     * @param FilePath - workspace
     * @param TaskListener - listener
     * @param Run - build
     * @param Launcher - launcher  
     * @param final String - containerName
     * @param final String - cmdStr
     * @param final boolean - force
     * @param final String - targetName
     * @param final String - buildDir
     * @param final String - containerFile          
     * @param final boolean - quiet          
     */
    public ContainerCmdTask(final String containerType, 
            final String cmd,
            FilePath workspace, 
            TaskListener listener, 
            Run build, 
            Launcher launcher,
            final String containerName, 
            final String cmdStr, 
            final boolean force,
            final String targetName, 
            final String buildDir,
            final String containerFile,
            final boolean quiet) {
        super(workspace, listener, build, launcher);
        this.containerName = containerName;
        this.cmdStr = cmdStr;
        this.force = force;
        this.targetName = targetName;
        this.buildDir = buildDir;
        this.containerFile = containerFile;
        this.containerType = containerType;
        this.cmd = cmd;
        this.quiet = quiet;
    }

    /**
     * Process the Docker command
     */
    @Override
    public Boolean executeMaster() throws IOException, InterruptedException {
        StringBuffer outputStr = new StringBuffer();
        boolean retStat = false;

        if (quiet) {
            LOGGER.log(Level.FINE, "Master Silent mode enabled");
        } else {
            LOGGER.log(Level.FINE, "Master Silent mode disabled");
        }

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
                if (quiet) {
                } else {
                    listener.getLogger().println(output);
                }
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

        if (quiet) {
            LOGGER.log(Level.FINE, "Slave Silent mode enabled");
        } else {
            LOGGER.log(Level.FINE, "Slave Silent mode disabled");
        }

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
                if (quiet) {
                } else {
                    listener.getLogger().println(output);
                }
            } else {
                listener.error(output);
            }
            return retStat;
        } catch(Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}

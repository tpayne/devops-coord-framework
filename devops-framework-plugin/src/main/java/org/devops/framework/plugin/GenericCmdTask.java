/**
 * Pipeline plugin extension for tasks
 */
package org.devops.framework.plugin;

import hudson.FilePath;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;
import hudson.model.Run;
import hudson.Launcher;

import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

import java.io.File;
import java.io.IOException;

import org.devops.framework.core.Utilities;
import org.devops.framework.core.Values;

import java.io.Serializable;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Base class for Callables using DSL remotable logic.
 */
abstract class GenericCmdTask implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( GenericCmdTask.class.getName() );

    protected FilePath workspace;
    protected TaskListener listener;
    protected Run build;
    protected Launcher launcher;
    protected boolean isMaster = true;
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     * 
     * @param FilePath - workspace
     * @param TaskListener - listener
     * @param Run - build
     * @param Launcher - launcher     
     */
    GenericCmdTask(FilePath workspace, TaskListener listener, Run build, Launcher launcher) {
        this.workspace = workspace;
        this.listener = listener;
        this.build = build;
        this.launcher = launcher;
    }

    public Boolean invoke() 
        throws IOException, InterruptedException  {
        //
        boolean retStatus = true;
        String hostName = workspace.toComputer().getHostName();

        try {
            if (hostName != null &&
                !hostName.isEmpty()) {
                isMaster = Utilities.getHostName().equalsIgnoreCase(hostName);
            } else if (hostName == null) {
                isMaster = true;
            } else {
                isMaster = false;
            }

            if (isMaster) {
                listener.getLogger().println("Running on master");
                retStatus = executeMaster();
            } else {
                listener.getLogger().println("Running on "+hostName);
                retStatus = executeSlave();
            }
            return retStatus;
        } catch (Exception e) {
            listener.fatalError(Values.exceptionMessage("Unable to run command callout", e, "no message - try again"));
            return false;
        }
    }

    /**
     * Process the task. Template method to override in subclasses.
     */
    abstract Boolean executeMaster() throws IOException, InterruptedException;
    abstract Boolean executeSlave() throws IOException, InterruptedException;
}

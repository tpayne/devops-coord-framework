/**
 * Pipeline plugin extension for pulling files from repos
 */
package org.devops.framework.plugin;

import org.devops.framework.core.ConfigPropertiesConstants;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.Run;
import hudson.model.TaskListener;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

public class DevOpsFrameworkAnsibleRunbookStepExecution extends SynchronousNonBlockingStepExecution<Boolean> {

    @StepContextParameter
    private transient TaskListener listener;

    @StepContextParameter
    private transient Run build;

    @StepContextParameter
    private transient Launcher launcher;

    @StepContextParameter
    private transient FilePath workspace;

    private transient final DevOpsFrameworkAnsibleRunbookStep step;

    /**
     * Default constructor
     * 
     * @param DevOpsFrameworkArtifactoryPullStep - step
     * @param StepContext - context
     */
    DevOpsFrameworkAnsibleRunbookStepExecution(DevOpsFrameworkAnsibleRunbookStep step, StepContext context) {
        super(context);
        this.step = step;
    }

    /**
     * Run function
     * 
     * @return Boolean
     * @throws Exception
     */
    @Override
    protected Boolean run() throws Exception {
        listener = getContext().get(TaskListener.class);
        build = getContext().get(Run.class);
        workspace = getContext().get(FilePath.class);
        launcher = getContext().get(Launcher.class);

        listener.getLogger().println("Running playbook '"+
            step.getRunFile()+"' using '"+
            step.getHostFile()+"'");
  
        RunbookCmdTask runTask = new RunbookCmdTask(ConfigPropertiesConstants.ANSIBLE,
                                            workspace,
                                            listener,
                                            build,
                                            launcher,
                                            step.getHostFile(),
                                            step.getRunFile(),
                                            step.getWorkingDir());
        return(runTask.invoke());
    }

    private static final long serialVersionUID = 1L;
}


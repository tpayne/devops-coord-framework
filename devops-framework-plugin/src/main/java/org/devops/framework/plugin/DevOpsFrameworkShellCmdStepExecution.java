/**
 * Pipeline plugin extension for running commands
 */
package org.devops.framework.plugin;

import org.devops.framework.core.ConfigPropertiesConstants;
import org.devops.framework.core.Repository;
import org.devops.framework.core.ArtifactoryRepoFactory;

import java.io.File;

import hudson.Extension;
import hudson.Util;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;

import com.google.common.collect.ImmutableSet;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;

import javax.inject.Inject;

import jenkins.MasterToSlaveFileCallable;
import jenkins.util.BuildListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Set;

public class DevOpsFrameworkShellCmdStepExecution extends SynchronousNonBlockingStepExecution<Boolean> {

    @StepContextParameter
    private transient TaskListener listener;

    @StepContextParameter
    private transient Run build;

    @StepContextParameter
    private transient Launcher launcher;

    @StepContextParameter
    private transient FilePath workspace;

    private transient final DevOpsFrameworkShellCmdStep step;

    /**
     * Default constructor
     *
     * @param DevOpsFrameworkShellCmdStep - step
     * @param StepContext - context
     */
    DevOpsFrameworkShellCmdStepExecution(DevOpsFrameworkShellCmdStep step, StepContext context) {
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

        listener.getLogger().println("Running shell command");

        ShellCmdTask runTask = new ShellCmdTask("SCRIPT",
                                            workspace,
                                            listener,
                                            build,
                                            launcher,
                                            step.getScript(),
                                            null,
                                            step.getWorkingDir(),
                                            step.getQuiet());
        boolean retStat = runTask.invoke();
        return retStat;
    }

    private static final long serialVersionUID = 1L;
}


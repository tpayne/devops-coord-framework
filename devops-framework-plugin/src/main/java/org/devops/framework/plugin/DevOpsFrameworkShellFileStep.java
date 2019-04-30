/**
 * Pipeline plugin extension for running shell commands
 */
package org.devops.framework.plugin;

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

public class DevOpsFrameworkShellFileStep extends Step {

    private String cmdFile;
    private String workingDir;
	private boolean quiet=false;

    /**
     * Default constructor
     *
     * @param String - cmdFile
     * @param String - workingDir
     * @param boolean - quiet
     */
    @DataBoundConstructor
    public DevOpsFrameworkShellFileStep(String cmdFile, String workingDir,
                                       boolean quiet) {
        this.cmdFile = cmdFile;
        this.workingDir = workingDir;
        this.quiet = quiet;
    }

    public String getCmdFile() {
        return this.cmdFile;
    }

    @DataBoundSetter
    public void setCmdFile(final String cmdFile) {
        this.cmdFile = cmdFile.trim();
    }

    public String getWorkingDir() {
        return this.workingDir;
    }

    @DataBoundSetter
    public void setWorkingDir(final String workingDir) {
        this.workingDir = workingDir.trim();
    }

    public boolean getQuiet() {
        return this.quiet;
    }

    @DataBoundSetter
    public void setQuiet(final boolean quiet) {
        this.quiet = quiet;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new DevOpsFrameworkShellFileStepExecution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return "devOpsFrameworkShellFileStep";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Run a shell command script";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, Run.class, Launcher.class, TaskListener.class);
        }
    }
}


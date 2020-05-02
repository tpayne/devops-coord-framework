/**
 * Pipeline plugin extension for tagging containers
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

public class DevOpsFrameworkTagContainerStep extends Step {

    private String containerName;
    private String targetName;
    private boolean quiet=false;


    /**
     * Default constructor
     * 
     * @param String - containerName
     * @param String - targetName
     * @param boolean - quiet
     */
    @DataBoundConstructor
    public DevOpsFrameworkTagContainerStep(String containerName, String targetName,
                                           boolean quiet) {
        this.containerName = containerName;
        this.targetName = targetName;
        this.quiet = quiet;
    }

    public String getContainerName() {
        return this.containerName;
    }

    @DataBoundSetter 
    public void setContainerName(final String containerName) {
        this.containerName = Util.fixEmptyAndTrim(containerName);
    }

    public String getTargetName() {
        return this.targetName;
    }

    @DataBoundSetter 
    public void setTargetName(final String targetName) {
        this.targetName = Util.fixEmptyAndTrim(targetName);
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
        return new DevOpsFrameworkTagContainerStepExecution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return "devOpsFrameworkTagContainerStep";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Tag Container step";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, Run.class, Launcher.class, TaskListener.class);
        }        
    }
}


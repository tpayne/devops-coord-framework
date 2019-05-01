/**
 * Pipeline plugin extension for removing containers
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

public class DevOpsFrameworkRmContainerStep extends Step {

    private String containerName;
    private boolean force = false;

    /**
     * Default constructor
     * 
     * @param String - imageName
     * @param boolean - force
     */
    @DataBoundConstructor
    public DevOpsFrameworkRmContainerStep(String containerName, boolean force) {
        this.containerName = containerName;
        this.force = force;
    }

    public String getContainerName() {
        return this.containerName;
    }

    public boolean getForce() {
        return this.force;
    }

    @DataBoundSetter 
    public void setContainerName(final String containerName) {
        this.containerName = Util.fixEmptyAndTrim(containerName);
    }

    @DataBoundSetter 
    public void setForce(final boolean force) {
        this.force = force;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new DevOpsFrameworkRmContainerStepExecution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return "devOpsFrameworkRmContainerStep";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Remove Container step";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, Run.class, Launcher.class, TaskListener.class);
        }        
    }
}


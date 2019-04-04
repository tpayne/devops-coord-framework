/**
 * Pipeline plugin extension
 */
package org.devops;

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

public class DevOpsFrameworkBuildContainerStep extends Step {

    private String containerName;
    private File   containerFile;
    private File   buildDirectory;

    @DataBoundConstructor
    public DevOpsFrameworkBuildContainerStep(String containerName,
                                String containerFile,
                                String buildDirectory) {
        this.containerName = containerName;
        this.containerFile = new File(containerFile);
        this.buildDirectory = new File(buildDirectory);
    }

    public String getContainerName() {
        return this.containerName;
    }

    public String getContainerFile() {
        return this.containerFile.getAbsolutePath();
    }

    public String getBuildDirectory() {
        return this.buildDirectory.getAbsolutePath();
    }

    @DataBoundSetter 
    public void setContainerName(final String containerName) {
        this.containerName = Util.fixEmptyAndTrim(containerName);
    }

    @DataBoundSetter 
    public void setContainerFile(final String containerFile) {
        this.containerFile = null;
        this.containerFile = new File(Util.fixEmptyAndTrim(containerFile));
    }

    @DataBoundSetter 
    public void setBuildDirectory(final String buildDirectory) {
        this.buildDirectory = null;
        this.buildDirectory = new File(Util.fixEmptyAndTrim(buildDirectory));
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new DevOpsFrameworkBuildContainerStepExecution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return "devOpsFrameworkBuildContainerStep";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Build Container step";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, Run.class, Launcher.class, TaskListener.class);
        }        
    }
}


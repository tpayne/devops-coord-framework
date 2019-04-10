/**
 * Pipeline plugin extension for pushing files to repos
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

public class DevOpsFrameworkFilePushStep extends Step {

    private String srcFile;
    private String targetFile;

    @DataBoundConstructor
    public DevOpsFrameworkFilePushStep(String srcFile, String targetFile) {
        this.srcFile = srcFile;
        this.targetFile = targetFile;
    }

    public String getSrcFile() {
        return this.srcFile;
    }

    @DataBoundSetter 
    public void setSrcFile(final String srcFile) {
        this.srcFile = srcFile.trim();
    }

    public String getTargetFile() {
        return this.targetFile;
    }

    @DataBoundSetter 
    public void setTargetFile(final String targetFile) {
        this.targetFile = targetFile.trim();
    }


    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new DevOpsFrameworkFilePushStepExecution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return "devOpsFrameworkFilePushStep";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Push file to file-based repo";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, Run.class, Launcher.class, TaskListener.class);
        }        
    }
}


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

public class DevOpsFrameworkRunContainerStep extends Step {

    private String containerName;
    private String cmdStr;

    @DataBoundConstructor
    public DevOpsFrameworkRunContainerStep(String containerName,
                                String cmdStr) {
        this.containerName = containerName;
        this.cmdStr = cmdStr;
    }

    public String getContainerName() {
        return this.containerName;
    }

    public String getCmdStr() {
        return this.cmdStr;
    }

    @DataBoundSetter 
    public void setContainerName(final String containerName) {
        this.containerName = Util.fixEmptyAndTrim(containerName);
    }

    @DataBoundSetter 
    public void setCmdStr(final String cmdStr) {
        this.cmdStr = Util.fixEmptyAndTrim(cmdStr);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new DevOpsFrameworkRunContainerStepExecution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return "devOpsFrameworkRunContainerStep";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Run Container step";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, Run.class, Launcher.class, TaskListener.class);
        }        
    }
}


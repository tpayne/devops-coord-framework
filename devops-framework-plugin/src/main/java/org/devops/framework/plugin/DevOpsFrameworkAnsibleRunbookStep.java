/**
 * Pipeline plugin extension for pulling files from repos
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

public class DevOpsFrameworkAnsibleRunbookStep extends Step {

    private String hostFile;
    private String runFile;
    private String workingDir;
    private String userName;
    private String userPwd;

    /**
     * Default constructor
     * 
     * @param String - srcFile
     * @param String - targetFile
     * @param String - userName
     * @param String - userPwd
     */
    @DataBoundConstructor
    public DevOpsFrameworkAnsibleRunbookStep(String hostFile, String runFile, String workingDir,
                                              String userName, String userPwd) {
        this.hostFile = hostFile;
        this.runFile = runFile;
        this.workingDir = workingDir;
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public String getHostFile() {
        return this.hostFile;
    }

    @DataBoundSetter 
    public void setHostFile(final String hostFile) {
        this.hostFile = hostFile.trim();
    }

    public String getRunFile() {
        return this.runFile;
    }

    @DataBoundSetter 
    public void setRunFile(final String runFile) {
        this.runFile = runFile.trim();
    }

    public String getWorkingDir() {
        return this.workingDir;
    }

    @DataBoundSetter 
    public void setWorkingDir(final String workingDir) {
        this.workingDir = workingDir.trim();
    }

    public String getUserName() {
        return this.userName;
    }

    @DataBoundSetter 
    public void setUserName(final String userName) {
        this.userName = Util.fixEmptyAndTrim(userName);
    }

    public String getUserPwd() {
        return this.userPwd;
    }

    @DataBoundSetter 
    public void setUserPwd(final String userPwd) {
        this.userPwd = Util.fixEmptyAndTrim(userPwd);
    }
    
    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new DevOpsFrameworkAnsibleRunbookStepExecution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return "devOpsFrameworkAnsibleRunbookStep";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Run an Ansible playbook";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, Run.class, Launcher.class, TaskListener.class);
        }        
    }
}


/**
 * Pipeline plugin extension for cloning repos
 */
package org.devops.framework.plugin;

import java.io.File;

import hudson.Extension;
import hudson.Util;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.EnvVars;
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

import java.util.logging.Logger;
import java.util.logging.Level;

public class DevOpsFrameworkScmGitCloneStep extends Step {

    private static final Logger LOGGER = Logger.getLogger( DevOpsFrameworkScmGitCloneStep.class.getName() );

    private String repoName;
    private String userName;
    private String userPwd;
    private String targetDir;

    @DataBoundConstructor
    public DevOpsFrameworkScmGitCloneStep(String repoName, String userName,
                                          String userPwd, String targetDir) {
        this.repoName = repoName;
        this.userName = userName;
        this.userPwd = userPwd;
        this.targetDir = targetDir;        
    }

    public String getRepoName() {
        return this.repoName;
    }

    @DataBoundSetter 
    public void setRepoName(final String repoName) {
        this.repoName = Util.fixEmptyAndTrim(repoName);
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

    public String getTargetDir() {
        return this.targetDir;
    }

    @DataBoundSetter 
    public void setTargetDir(final String targetDir) {
        this.targetDir = Util.fixEmptyAndTrim(targetDir);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new DevOpsFrameworkScmGitCloneStepExecution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return "devOpsFrameworkGitCloneStep";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Git Cloning step";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, Run.class, Launcher.class, TaskListener.class);
        }        
    }
}

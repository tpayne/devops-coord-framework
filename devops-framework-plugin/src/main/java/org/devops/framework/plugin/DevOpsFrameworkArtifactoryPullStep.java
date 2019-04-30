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

public class DevOpsFrameworkArtifactoryPullStep extends Step {

    private String srcFile;
    private String targetFile;
    private String userName;
    private String userPwd;
	private boolean quiet=false;

    /**
     * Default constructor
     *
     * @param String - srcFile
     * @param String - targetFile
     * @param String - userName
     * @param String - userPwd
     * @param boolean - quiet
     */
    @DataBoundConstructor
    public DevOpsFrameworkArtifactoryPullStep(String srcFile, String targetFile,
                                        	  String userName, String userPwd,
                                        	  boolean quiet) {
        this.srcFile = srcFile;
        this.targetFile = targetFile;
        this.userName = userName;
        this.userPwd = userPwd;
        this.quiet = quiet;
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

    public boolean getQuiet() {
        return this.quiet;
    }

    @DataBoundSetter
    public void setQuiet(final boolean quiet) {
        this.quiet = quiet;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new DevOpsFrameworkArtifactoryPullStepExecution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return "devOpsFrameworkArtifactoryPullStep";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Pull file from Artifactory-based repo";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(FilePath.class, Run.class, Launcher.class, TaskListener.class);
        }
    }
}


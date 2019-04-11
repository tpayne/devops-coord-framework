/**
 * Pipeline plugin extension for pulling files from repos
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

public class DevOpsFrameworkArtifactoryPullStepExecution extends SynchronousNonBlockingStepExecution<Boolean> {

    @StepContextParameter
    private transient TaskListener listener;

    @StepContextParameter
    private transient Run build;

    @StepContextParameter
    private transient Launcher launcher;

    private transient final DevOpsFrameworkArtifactoryPullStep step;

    /**
     * Default constructor
     * 
     * @param DevOpsFrameworkArtifactoryPullStep - step
     * @param StepContext - context
     */
    DevOpsFrameworkArtifactoryPullStepExecution(DevOpsFrameworkArtifactoryPullStep step, StepContext context) {
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
        StringBuffer outputStr = new StringBuffer();
        listener.getLogger().println("Pull file '"+step.getSrcFile()+"' to '"+step.getTargetFile()+"'");
        boolean retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.ARTIFACTORY,
                                step.getSrcFile(),
                                step.getTargetFile(),
                                (step.getUserName() == null || step.getUserName().isEmpty() ? null : step.getUserName()),
                                (step.getUserPwd() == null || step.getUserPwd().isEmpty() ? null : step.getUserPwd()),
                                outputStr);
        String output = outputStr.toString();
        if (retStat) {
            // Too much output...
            //listener.getLogger().println(output);
        } else {
            listener.error(output);
        }
        outputStr = null;
        return retStat;
    }

    private static final long serialVersionUID = 1L;
}


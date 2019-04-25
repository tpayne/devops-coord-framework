/**
 * Pipeline plugin extension for tasks
 */
package org.devops.framework.plugin;

import hudson.FilePath;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;
import hudson.model.Run;
import hudson.Launcher;

import java.io.File;
import java.io.IOException;

import org.devops.framework.core.ConfigPropertiesConstants;
import org.devops.framework.core.Provision;
import org.devops.framework.core.Utilities;

/**
 * Runbook class for DSL remotable logic.
 */
class RunbookCmdTask extends GenericCmdTask {

    // Local variables...
    private String runbookType;
    private String hostFile;
    private String runbookFile;
    private String workingDir;

    /**
     * Default constructor
     * 
     * @param final String - runbookType
     * @param FilePath - workspace
     * @param TaskListener - listener
     * @param Run - build
     * @param Launcher - launcher  
     * @param final String - hostFile
     * @param final String - runbookFile
     * @param final String - workingDir
     */
    public RunbookCmdTask(final String runbookType, 
            FilePath workspace, 
            TaskListener listener, 
            Run build, 
            Launcher launcher,
            final String hostFile, 
            final String runbookFile, 
            final String workingDir) {
        super(workspace, listener, build, launcher);
        this.hostFile = hostFile;
        this.runbookFile = runbookFile;
        this.workingDir = workingDir;
        this.runbookType = runbookType;
    }

    /**
     * Process the Runbook command
     */
    @Override
    public Boolean executeMaster() throws IOException, InterruptedException {
        StringBuffer outputStr = new StringBuffer();
        boolean retStat = false;
        try {
            retStat = Provision.runPlaybook(runbookType,
                                hostFile,
                                runbookFile,
                                null,null,
                                (workingDir == null || workingDir.isEmpty() ? null : workingDir),
                                outputStr);
            String output = outputStr.toString();
            if (retStat) {
                listener.getLogger().println(output);
            } else {
                listener.error(output);
            }
            return retStat;
        } catch(Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Process the Runbook command
     */
    @Override
    public Boolean executeSlave() throws IOException, InterruptedException {
        StringBuffer outputStr = new StringBuffer();
        boolean retStat = false;
        try {
            retStat = Provision.runPlaybook(runbookType,
                                hostFile,
                                runbookFile,
                                null,null,
                                (workingDir == null || workingDir.isEmpty() ? null : workingDir),
                                outputStr,
                                launcher,
                                workspace);
            String output = outputStr.toString();
            if (retStat) {
                listener.getLogger().println(output);
            } else {
                listener.error(output);
            }
            return retStat;
        } catch(Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}

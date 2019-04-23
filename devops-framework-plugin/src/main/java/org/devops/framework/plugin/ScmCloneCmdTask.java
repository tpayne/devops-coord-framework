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
import org.devops.framework.core.SCM;
import org.devops.framework.core.Utilities;

/**
 * ScmClone class for DSL remotable logic.
 */
class ScmCloneCmdTask extends GenericCmdTask {

    // Local variables...
    private String repoName;
    private String userName;
    private String userPwd;
    private String targetDir;
    private String cmType;

    /**
     * Default constructor
     * 
     * @param FilePath - workspace
     * @param TaskListener - listener
     * @param Run - build
     * @param Launcher - launcher  
     * @param final String - repoName
     * @param final String - userName
     * @param final String - userPwd
     * @param final String - targetDir          
     */
    public ScmCloneCmdTask(String cmType, FilePath workspace, TaskListener listener, 
            Run build, Launcher launcher,
            final String repoName, final String userName, 
            final String userPwd, final String targetDir) {
        super(workspace, listener, build, launcher);
        this.repoName = repoName;
        this.userName = userName;
        this.userPwd = userPwd;
        this.targetDir = targetDir;
        this.cmType = cmType;
    }

    /**
     * Process the clone
     */
    @Override
    public Boolean executeMaster() throws IOException, InterruptedException {
        StringBuffer outputStr = new StringBuffer();
        try {
            listener.getLogger().println("Master cloning repo "+repoName);
            if (targetDir != null || !targetDir.isEmpty()) {
               listener.getLogger().println("Using target directory "+targetDir); 
            }
            boolean retStat = true;
            retStat = SCM.scmClone(cmType,
                                    repoName,
                                    (userName == null || userName.isEmpty() ? null : userName),
                                    (userPwd == null || userPwd.isEmpty() ? null : userPwd),
                                    (targetDir == null || targetDir.isEmpty() ? "" : targetDir),
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
     * Process the clone
     */
    @Override
    public Boolean executeSlave() throws IOException, InterruptedException {
        StringBuffer outputStr = new StringBuffer();
        try {
            listener.getLogger().println("Slave cloning repo "+repoName);
            if (targetDir != null || !targetDir.isEmpty()) {
               listener.getLogger().println("Using target directory "+targetDir); 
            }
            boolean retStat = true;
            retStat = SCM.scmClone(cmType,
                                    repoName,
                                    (userName == null || userName.isEmpty() ? null : userName),
                                    (userPwd == null || userPwd.isEmpty() ? null : userPwd),
                                    (targetDir == null || targetDir.isEmpty() ? "" : targetDir),
                                    outputStr,
                                    false,
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

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
import org.devops.framework.core.Repository;
import org.devops.framework.core.Utilities;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Repo class for DSL remotable logic.
 */
class RepoCmdTask extends GenericCmdTask {

    private static final Logger LOGGER = Logger.getLogger( RepoCmdTask.class.getName() );

    // Local variables...
    private String srcFile;
    private String targetFile;
    private String userName;
    private String userPwd;

    private String repoType;
    private String cmd;
    private boolean quiet=false;

    /**
     * Default constructor
     *
     * @param final String - repoType
     * @param final String - cmd
     * @param FilePath - workspace
     * @param TaskListener - listener
     * @param Run - build
     * @param Launcher - launcher
     * @param final String - targetFile
     * @param final String - targetFile
     * @param final String - userName
     * @param final String - userPwd
     */
    public RepoCmdTask(final String repoType,
            final String cmd,
            FilePath workspace,
            TaskListener listener,
            Run build,
            Launcher launcher,
            final String srcFile,
            final String targetFile,
            final String userName,
            final String userPwd,
            final boolean quiet) {
        super(workspace, listener, build, launcher);
        this.srcFile = srcFile;
        this.targetFile = targetFile;
        this.userName = userName;
        this.userPwd = userPwd;
        this.repoType = repoType;
        this.cmd = cmd;
        this.quiet = quiet;
    }

    /**
     * Process the Repo command
     */
    @Override
    public Boolean executeMaster() throws IOException, InterruptedException {
        StringBuffer outputStr = new StringBuffer();
        boolean retStat = false;
        if (quiet) {
        	LOGGER.log(Level.FINE, "Silent mode enabled");
        } else {
        	LOGGER.log(Level.FINE, "Silent mode disabled");
		}

        try {
            if (cmd.equals("PULL")) {
                retStat = Repository.pullAssetFromRepo(repoType,
                                srcFile,
                                targetFile,
                                ((userName == null || userName.isEmpty()) ? "" : userName),
                                ((userPwd == null || userPwd.isEmpty()) ? "" : userPwd),
                                outputStr);
            } else if (cmd.equals("PUSH")) {
                retStat = Repository.pushAssetToRepo(repoType,
                                srcFile,
                                targetFile,
                                ((userName == null || userName.isEmpty()) ? "" : userName),
                                ((userPwd == null || userPwd.isEmpty()) ? "" : userPwd),
                                outputStr);
            }
            String output = outputStr.toString();
            if (retStat) {
            	if (quiet) {
            	} else {
                	listener.getLogger().println(output);
                }
            } else {
                listener.error(output);
            }
            return retStat;
        } catch(Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Repo the Repo command
     */
    @Override
    public Boolean executeSlave() throws IOException, InterruptedException {
        StringBuffer outputStr = new StringBuffer();
        boolean retStat = false;
        if (quiet) {
        	LOGGER.log(Level.FINE, "Silent mode enabled");
        } else {
        	LOGGER.log(Level.FINE, "Silent mode disabled");
		}

        try {
            if (cmd.equals("PULL")) {
                retStat = Repository.pullAssetFromRepo(repoType,
                                srcFile,
                                targetFile,
                                ((userName == null || userName.isEmpty()) ? null : userName),
                                ((userPwd == null || userPwd.isEmpty()) ? null : userPwd),
                                outputStr,
                                launcher,
                                workspace);
            } else if (cmd.equals("PUSH")) {
                retStat = Repository.pushAssetToRepo(repoType,
                                srcFile,
                                targetFile,
                                ((userName == null || userName.isEmpty()) ? null : userName),
                                ((userPwd == null || userPwd.isEmpty()) ? null : userPwd),
                                outputStr,
                                launcher,
                                workspace);
            }
            String output = outputStr.toString();
            if (retStat) {
            	if (quiet) {
            	} else {
                	listener.getLogger().println(output);
                }
            } else {
                listener.error(output);
            }
           return retStat;
        } catch(Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}

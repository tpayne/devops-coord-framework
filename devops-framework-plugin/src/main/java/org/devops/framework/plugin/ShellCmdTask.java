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
import java.util.Random;

import org.devops.framework.core.ConfigPropertiesConstants;
import org.devops.framework.core.Repository;
import org.devops.framework.core.Utilities;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Repo class for DSL remotable logic.
 */
class ShellCmdTask extends GenericCmdTask {

    private static final Logger LOGGER = Logger.getLogger( ShellCmdTask.class.getName() );

    // Local variables...
    private String scriptCmd;
    private String scriptFile;
    private String workingDir;

    private String cmd;
    private boolean quiet=false;
    private String cmdF = "#!/bin/sh\nset -x\n\n";

    /**
     * Default constructor
     *
     * @param final String - cmd
     * @param FilePath - workspace
     * @param TaskListener - listener
     * @param Run - build
     * @param Launcher - launcher
     * @param final String - scriptCmd
     * @param final String - scriptFile
     * @param final String - workingDir
     * @param final boolean - quiet
     */
    public ShellCmdTask(final String cmd,
            FilePath workspace,
            TaskListener listener,
            Run build,
            Launcher launcher,
            final String scriptCmd,
            final String scriptFile,
            final String workingDir,
            final boolean quiet) {
        super(workspace, listener, build, launcher);
        this.scriptCmd = scriptCmd;
        this.scriptFile = scriptFile;
        this.cmd = cmd;
        this.workingDir = workingDir;
        this.quiet = quiet;
    }

    /**
     * Process the Repo command
     */
    @Override
    public Boolean executeMaster() throws IOException, InterruptedException {
        StringBuffer outputStr = new StringBuffer();
        boolean retStat = false;
        File wd = null;
        File cmdFile = null;

		Random rand = new Random();
		Long uid = rand.nextLong();

        if (workingDir != null && !workingDir.isEmpty()) {
        	wd = new File(workingDir);
        	if (!wd.exists()) {
        		throw new IOException("Error: Working directory '"
        								+wd.getAbsolutePath()+"' does not exist");
        	}
        }
        if (scriptFile != null && !scriptFile.isEmpty()) {
        	cmdFile = new File(scriptFile);
        	if (!cmdFile.exists()) {
        		throw new IOException("Error: Script file '"
        								+cmdFile.getAbsolutePath()+"' does not exist");
        	}
            cmdFile = null;
        }

        if (quiet) {
        	LOGGER.log(Level.FINE, "Silent mode enabled");
        } else {
        	LOGGER.log(Level.FINE, "Silent mode disabled");
		}

        try {
            if (cmd.equals("SCRIPT")) {
                String cmdLine = cmdF+scriptCmd;
                FilePath script = workspace.createTextTempFile("ShellCmdTask"+uid,
                                                           null,cmdLine);
                script.chmod(0755);

				LOGGER.log(Level.FINE, "Running file {0}",script.getRemote());

                retStat = (Utilities.runCmd(script.getRemote(),
                                outputStr,
                                wd,
                                launcher,
                                false,
                                true)==0);
                script.delete();
            } else if (cmd.equals("FILE")) {
                cmdFile = new File(scriptFile);
                retStat = (Utilities.runCmd(cmdFile,
                                outputStr,
                                wd,
                                launcher)==0);
                cmdFile = null;
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
            wd = null;
            cmdFile = null;

            return retStat;
        } catch(Exception e) {
            LOGGER.log(Level.FINE, "Exception stack {0}", Utilities.getStackTraceAsString(e));
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
        File wd = null;
        File cmdFile = null;

        Random rand = new Random();
        Long uid = rand.nextLong();

        if (workingDir != null && !workingDir.isEmpty()) {
            FilePath area = new FilePath(workspace.getChannel(),workingDir);
            if (!area.exists() || !area.isDirectory()) {
                throw new IOException("Error: Working directory '"
                                        +workingDir+"' does not exist");
            }
            area = null;
            wd = new File(workingDir);
        }
        if (scriptFile != null && !scriptFile.isEmpty()) {
            FilePath area = new FilePath(workspace.getChannel(),scriptFile);
            if (!area.exists()) {
                throw new IOException("Error: Script file '"
                                        +scriptFile+"' does not exist");
            }
            area = null;
            cmdFile = new File(scriptFile);
        }

        if (quiet) {
            LOGGER.log(Level.FINE, "Silent mode enabled");
        } else {
            LOGGER.log(Level.FINE, "Silent mode disabled");
        }

        try {
            if (cmd.equals("SCRIPT")) {
                String cmdLine = cmdF+scriptCmd;
                FilePath script = workspace.createTextTempFile("ShellCmdTask"+uid,
                                                           null,cmdLine);
                script.chmod(0755);

                LOGGER.log(Level.FINE, "Running file {0}",script.getRemote());

                retStat = (Utilities.runCmd(script.getRemote(),
                                outputStr,
                                wd,
                                launcher,
                                false,
                                true)==0);
                script.delete();
            } else if (cmd.equals("FILE")) {
                retStat = (Utilities.runCmd(cmdFile,
                                outputStr,
                                wd,
                                launcher)==0);
                cmdFile = null;
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

            wd = null;
            cmdFile = null;

            return retStat;
        } catch(Exception e) {
            LOGGER.log(Level.FINE, "Exception stack {0}", Utilities.getStackTraceAsString(e));
            throw new IOException(e.getMessage());
        }
    }
}

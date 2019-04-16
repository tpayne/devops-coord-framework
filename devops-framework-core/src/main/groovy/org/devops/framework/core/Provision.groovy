/**
 * Provisioning routines for use with the framework
 */
package org.devops.framework.core;

import java.util.logging.Logger
import java.util.logging.Level

class Provision implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( Provision.class.getName() )

    /**
     * Utility routine to run a provisioning playbook/cookbook
     * 
     * @param final String - toolType
     * @param final String - hostFile
     * @param final String - playbookFile
     * @param final File - targetWorkArea
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean runPlaybook(final String toolType,
                                final String hostFile,
                                final String playbookFile,
                                final File targetWorkArea,
                                StringBuffer outputStr=null)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // Invoke file version...
        //
        if (toolType == null || toolType.isEmpty() ||
            hostFile == null || hostFile.isEmpty() ||
            playbookFile == null || playbookFile.isEmpty()) {
            throw new IllegalArgumentException("Error: Invalid arguments specified")
        }

        File hFile = new File(hostFile)
        File playFile = new File(playbookFile)
        File workArea = null

        if (targetWorkArea != null && !targetWorkArea.getName().isEmpty()) {
            workArea = new File(targetWorkArea.getAbsolutePath())
        }

        return runPlaybook(toolType,hFile,playFile,null,null,workArea,outputStr)
    }

    /**
     * Utility routine to run a provisioning playbook/cookbook
     * 
     * @param final String - toolType
     * @param final String - hostFile
     * @param final String - playbookFile
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean runPlaybook(final String toolType,
                                final String hostFile,
                                final String playbookFile,
                                StringBuffer outputStr)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // Invoke file version...
        //
        if (toolType == null || toolType.isEmpty() ||
            hostFile == null || hostFile.isEmpty() ||
            playbookFile == null || playbookFile.isEmpty()) {
            throw new IllegalArgumentException("Error: Invalid arguments specified")
        }

        File hFile = new File(hostFile)
        File playFile = new File(playbookFile)

        return runPlaybook(toolType,hFile,playFile,null,null,null,outputStr)
    }    


    /**
     * Utility routine to run a provisioning playbook/cookbook
     * 
     * @param final String - toolType
     * @param final String - hostFile
     * @param final String - playbookFile
     * @param final String - userName
     * @param final String - password
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean runPlaybook(final String toolType,
                                final String hostFile,
                                final String playbookFile,
                                final String userName,
                                final String password,
                                StringBuffer outputStr)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // Invoke file version...
        //
        if (toolType == null || toolType.isEmpty() ||
            hostFile == null || hostFile.isEmpty() ||
            playbookFile == null || playbookFile.isEmpty()) {
            throw new IllegalArgumentException("Error: Invalid arguments specified")
        }

        File hFile = new File(hostFile)
        File playFile = new File(playbookFile)

        return runPlaybook(toolType,hFile,playFile,userName,password,null,outputStr)
    }    


    /**
     * Utility routine to run a provisioning playbook/cookbook
     * 
     * @param final String - toolType
     * @param final String - hostFile
     * @param final String - playbookFile
     * @param final String - userName
     * @param final String - password
     * @param final String - targetWorkArea
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean runPlaybook(final String toolType,
                                final String hostFile,
                                final String playbookFile,
                                final String userName=null,
                                final String password=null,
                                final String targetWorkArea=null,
                                StringBuffer outputStr=null)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // Invoke file version...
        //
        if (toolType == null || toolType.isEmpty() ||
            hostFile == null || hostFile.isEmpty() ||
            playbookFile == null || playbookFile.isEmpty()) {
            throw new IllegalArgumentException("Error: Invalid arguments specified")
        }

        File hFile = new File(hostFile)
        File playFile = new File(playbookFile)
        File workArea = null

        if (targetWorkArea != null && !targetWorkArea.isEmpty()) {
            workArea = new File(targetWorkArea)
        }

        return runPlaybook(toolType,hFile,playFile,userName,password,workArea,outputStr)
    }

    /**
     * Utility routine to run a provisioning playbook/cookbook
     * 
     * @param final String - toolType
     * @param final File - hostFile
     * @param final File - playbookFile
     * @param final String - userName
     * @param final String - password
     * @param final File - targetWorkArea
     * @param StringBuffer - outputStr
     * @return boolean 
     * @throws FileNotFoundException, IllegalArgumentException, Exception
     */
    static final boolean runPlaybook(final String toolType,
                                final File hostFile,
                                final File playbookFile,
                                final String userName=null,
                                final String password=null,
                                final File targetWorkArea=null,
                                StringBuffer outputStr=null)
        throws FileNotFoundException, IllegalArgumentException, Exception {
        //
        // Test that the target directory specified exists & is readable...
        // This parameter could be nullable in the future, but for now make
        // it mandatory to ensure target...
        //

        if (targetWorkArea == null || targetWorkArea.getName().isEmpty()) {
        }
        else if (targetWorkArea.exists() && targetWorkArea.canWrite() &&
                 targetWorkArea.isDirectory()) {
        } else {
            throw new FileNotFoundException("Error: The target workarea '"+
                                            targetWorkArea.getAbsolutePath()+
                                            "' either does not exist or is not writable")
        }

        if (toolType == null || toolType.isEmpty() ||
            hostFile == null || hostFile.getName().isEmpty() ||
            playbookFile == null || playbookFile.getName().isEmpty()) {
            throw new IllegalArgumentException("Error: Invalid arguments specified")
        }

        LOGGER.log(Level.FINE, "runPlaybook \"{0}\"",toolType);

        // Test the files...
        if (!hostFile.exists() || !hostFile.canRead()) {
            throw new FileNotFoundException("Error: The host file '"+
                                            hostFile.getAbsolutePath()+
                                            "' either does not exist or is not readable")
        } else if (!playbookFile.exists() || !playbookFile.canRead()) {
            throw new FileNotFoundException("Error: The playbook file '"+
                                            playbookFile.getAbsolutePath()+
                                            "' either does not exist or is not readable")
        }

        // Test that the specified provisioning engine exists...
        String provExeName = null
        if (toolType == ConfigPropertiesConstants.ANSIBLE) {
            provExeName = "ansible-playbook"
        } else if (toolType == ConfigPropertiesConstants.CHEF) {
            provExeName = "chef-client"
        } else if (toolType == ConfigPropertiesConstants.PUPPET) {
            provExeName = "puppet"
        } else {
            throw new IllegalArgumentException("Error: The tool type specified is not supported")
        }

        File provFile = Utilities.getExecutable(provExeName)

        // Construct the required command...
        String cmdStr = provFile.getAbsolutePath()+" "
        
        if (toolType == ConfigPropertiesConstants.ANSIBLE) {
            if (userName != null && !userName.isEmpty()) {
                //cmdStr += " -e \"ansible_connection=ssh ansible_user="+userName+"\""
                LOGGER.log(Level.FINER, "runPlaybook user=\"{0}\"",userName);
                cmdStr += " -e \"ansible_user="+userName+"\""
            }
            if (password != null && !password.isEmpty()) {
                cmdStr += " -e \"ansible_ssh_pass="+password+"\""
            }
            LOGGER.log(Level.FINER, "runPlaybook hostFile=\"{0}\"",hostFile.getAbsolutePath());
            LOGGER.log(Level.FINER, "runPlaybook playbook=\"{0}\"",playbookFile.getAbsolutePath());
            cmdStr += " -i "+hostFile.getAbsolutePath()
            cmdStr += " "+playbookFile.getAbsolutePath()
        } else {
            throw new IllegalArgumentException("Error: The tool type specified is not currently supported")
        }

        StringBuffer returnStr = new StringBuffer()
        int retStat = Utilities.runCmd(cmdStr,returnStr,targetWorkArea)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()

        LOGGER.log(Level.FINEST, "runPlaybook Command output \"{0}\"",returnOutput);

        returnStr = null    
        if (outputStr!=null) {
            outputStr.append(returnOutput)
        }

        return(retStat==0)
    }

}

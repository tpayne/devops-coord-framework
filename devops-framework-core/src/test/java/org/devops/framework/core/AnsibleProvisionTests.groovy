package org.devops.framework.core;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit AnsibleProvisionTests tests for Provision class using Ansible.
 */
public class AnsibleProvisionTests extends GroovyTestCase {

   File propFile = new File("."+"/src/test/resources/unitTest.properties")
   def map = Utilities.mapProperties(propFile)

   /**
    * Utility function for getting tmpDir
    */
   File getTmpDir() {
      if (runUnitTestsOnly()) {
        return new File(System.getProperty("java.io.tmpdir"))
      } else {
        return new File((map.get("tmpDir") != null) ? map.get("tmpDir") : System.getProperty("java.io.tmpdir"))
      }
   }

   /**
    * Utility function for seeing if need to just run unit-tests
    */
   boolean runUnitTestsOnly() {
      if (System.getenv("DEVOPS_FRAMEWORK_UNITTESTS")!=null) {
        return true;
      }
      String unitTests = map.get("unit_tests_only")
      if (unitTests != null && !unitTests.isEmpty()) {
        return(unitTests.contains("true"))
      }
      return false
   }

   /**
    * Unit test for basic Ansible playbook running
    */
   void testAnsibleBasicFile() {
      if (runUnitTestsOnly()) {
        return;
      }
      File hostFile = new File("."+map.get("ansible_hostFile"))
      File playbook = new File("."+map.get("ansible_playbook"))
      
      boolean retStat = Provision.runPlaybook(ConfigPropertiesConstants.ANSIBLE,hostFile,playbook)
      assertTrue(retStat)
   }

   /**
    * Unit test for basic Ansible playbook running
    */
   void testAnsibleBasicStr() {
      if (runUnitTestsOnly()) {
        return;
      }

      String hostFile = new File("."+map.get("ansible_hostFile")).getAbsolutePath()
      String playbook = new File("."+map.get("ansible_playbook")).getAbsolutePath()
      
      boolean retStat = Provision.runPlaybook(ConfigPropertiesConstants.ANSIBLE,hostFile,playbook)
      assertTrue(retStat)
   }

   /**
    * Unit test for basic Ansible playbook running fail
    */
   void testAnsibleBasicStrInvalidFile() {
      if (runUnitTestsOnly()) {
        return;
      }

      String hostFile = "doesnotexist.txt"
      String playbook = hostFile
      
      try {
        boolean retStat = Provision.runPlaybook(ConfigPropertiesConstants.ANSIBLE,hostFile,playbook)
        assert("Error")
      } catch(FileNotFoundException ex) {
      } catch(Exception ex) {
        assert("Unexpected exception "+ex.getMessage())
      }
   }   

   /**
    * Unit test for basic Ansible playbook running fail
    */
   void testAnsibleBasicStrInvalidInput() {
      if (runUnitTestsOnly()) {
        return;
      }

      String hostFile = null
      String playbook = hostFile
      
      try {
        boolean retStat = Provision.runPlaybook(ConfigPropertiesConstants.ANSIBLE,hostFile,playbook)
        assert("Error")
      } catch(IllegalArgumentException ex) {
      } catch(Exception ex) {
        assert("Unexpected exception "+ex.getMessage())
      }
   }   

   /**
    * Unit test for basic Ansible playbook running with work area fail
    */
   void testAnsibleBasicWithDirFail() {
      if (runUnitTestsOnly()) {
        return;
      }

      String hostFile = new File("."+map.get("ansible_hostFile")).getAbsolutePath()
      String playbook = new File("."+map.get("ansible_playbook")).getAbsolutePath()
      File tmpDir = new File("/doesnotexist")

      try {
        boolean retStat = Provision.runPlaybook(ConfigPropertiesConstants.ANSIBLE,hostFile,playbook,tmpDir)
        assert("Error")
      } catch(FileNotFoundException ex) {
      } catch(Exception ex) {
        assert("Unexpected exception "+ex.getMessage())
      }
   }   

   /**
    * Unit test for basic Ansible playbook running with work area
    */
   void testAnsibleBasicWithDir() {
      if (runUnitTestsOnly()) {
        return;
      }

      String hostFile = new File("."+map.get("ansible_hostFile")).getAbsolutePath()
      String playbook = new File("."+map.get("ansible_playbook")).getAbsolutePath()
      File tmpDir = getTmpDir()

      boolean retStat = Provision.runPlaybook(ConfigPropertiesConstants.ANSIBLE,hostFile,playbook,tmpDir)
      assertTrue(retStat)
   } 

   /**
    * Unit test for basic Ansible playbook running with outputStr
    */
   void testAnsibleBasicWithStr() {
      if (runUnitTestsOnly()) {
        return;
      }

      String hostFile = new File("."+map.get("ansible_hostFile")).getAbsolutePath()
      String playbook = new File("."+map.get("ansible_playbook")).getAbsolutePath()
      File tmpDir = getTmpDir()
      StringBuffer returnStr = new StringBuffer()

      boolean retStat = Provision.runPlaybook(ConfigPropertiesConstants.ANSIBLE,hostFile,playbook,returnStr)
      String returnOutput = returnStr.toString()
      returnStr = null
      returnOutput = returnOutput.trim()
      assertTrue(retStat)
      assertTrue(returnOutput.contains("ok: [localhost] => {"))
   }  

   /**
    * Unit test for basic Ansible playbook running with outputStr and user
    */
   void testAnsibleBasicWithUserPwdStr() {
      if (runUnitTestsOnly()) {
        return;
      }

      String hostFile = new File("."+map.get("ansible_hostFile")).getAbsolutePath()
      String playbook = new File("."+map.get("ansible_playbook")).getAbsolutePath()
      String userName = map.get("ansible_User")
      String password = map.get("ansible_UserPwd")
      File tmpDir = getTmpDir()

      boolean retStat = true

      if (userName != null && password != null &&
          !userName.isEmpty() && !password.isEmpty()) {
        StringBuffer returnStr = new StringBuffer()
        retStat = Provision.runPlaybook(ConfigPropertiesConstants.ANSIBLE,hostFile,
                                                playbook,
                                                userName,password,
                                                returnStr)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null
        assertTrue(returnOutput.contains("ok: [localhost] => {") && returnOutput.contains(userName))
      }
      assertTrue(retStat)
   } 

   /**
    * Unit test for basic Ansible playbook running with outputStr and user
    */
   void testAnsibleBasicWithInvalidUserPwdStr() {
      if (runUnitTestsOnly()) {
        return;
      }

      String hostFile = new File("."+map.get("ansible_hostFile")).getAbsolutePath()
      String playbook = new File("."+map.get("ansible_playbook")).getAbsolutePath()
      String userName = map.get("ansible_User")
      String password = map.get("ansible_UserPwd")
      File tmpDir = getTmpDir()

      boolean retStat = false

      if (userName != null && password != null &&
          !userName.isEmpty() && !password.isEmpty()) {
        StringBuffer returnStr = new StringBuffer()
        retStat = Provision.runPlaybook(ConfigPropertiesConstants.ANSIBLE,hostFile,
                                                playbook,
                                                userName+"DDDDD",
                                                password+"DDDDD",
                                                returnStr)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null
      }
      assertFalse(retStat)
   }       
}
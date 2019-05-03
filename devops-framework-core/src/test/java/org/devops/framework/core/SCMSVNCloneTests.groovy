package org.devops.framework.core;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit SCMTests tests for SCM class.
 */
public class SCMSVNCloneTests extends GroovyTestCase {

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
    * Unit test for svn cloning
    */
   void testScmSVNCloneBasic() {
      if (runUnitTestsOnly()) {
        return;
      }
      String scmURI = map.get("git_repoURI")
      File tmpDir = new File(map.get("git_repoDir")+".git")
      
      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI)
      assertTrue(retStat)
      retStat = false
      if (tmpDir.exists()) {
         retStat = true
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      assertTrue(retStat)
   }

   /**
    * Unit test for svn cloning
    */
   void testScmSVNCloneBasicTrue() {
      if (runUnitTestsOnly()) {
        return;
      }
      String scmURI = map.get("git_repoURI")
      File tmpDir = new File(map.get("git_repoDir")+".git")
      
      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI,null,true)
      assertTrue(retStat)
      retStat = false
      if (tmpDir.exists()) {
         retStat = true
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      assertTrue(retStat)
   }

   /**
    * Unit test for svn cloning with user/password
    */
   void testScmSVNCloneBasicWithUser() {
      if (runUnitTestsOnly()) {
        return;
      }
      String scmURI = map.get("git_repoURI")
      File tmpDir = new File(map.get("git_repoDir")+".git")
      
      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // As this is a public URI, it will not matter what users you throw at it...
      String user = map.get("git_repoUser")
      String pwd  = map.get("git_repoUserPwd")

      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI,user,pwd)
      assertTrue(retStat)
      retStat = false
      if (tmpDir.exists()) {
         retStat = true
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      assertTrue(retStat)
   }

   /**
    * Unit test for svn cloning with user/password
    */
   void testScmSVNCloneBasicWithUserTrue() {
      if (runUnitTestsOnly()) {
        return;
      }
      String scmURI = map.get("git_repoURI")
      File tmpDir = new File(map.get("git_repoDir")+".git")
      
      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // As this is a public URI, it will not matter what users you throw at it...
      String user = map.get("git_repoUser")
      String pwd  = map.get("git_repoUserPwd")

      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI,user,pwd,null,true)
      assertTrue(retStat)
      retStat = false
      if (tmpDir.exists()) {
         retStat = true
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      assertTrue(retStat)
   }

   /**
    * Unit test for svn cloning with target directory
    */
   void testScmSVNCloneWithTarget() {
      if (runUnitTestsOnly()) {
        return;
      }
      String scmURI = map.get("git_repoURI")
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+File.separator+map.get("git_repoDir")+".git/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // Test for file not found...
      try {
         boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI,tmpDir)
         assertTrue(false)
      } catch(FileNotFoundException ex) {
      } catch(Exception) {
         throw ex
      }

      assertTrue(tmpDir.mkdirs() && tmpDir.setWritable(true))
      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI,tmpDir)
      assertTrue(retStat)

      if (tmpDir.exists()) {
         retStat = true
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      assertTrue(retStat)
   }

   /**
    * Unit test for svn cloning with target directory
    */
   void testScmSVNCloneWithTargetTrue() {
      if (runUnitTestsOnly()) {
        return;
      }
      String scmURI = map.get("git_repoURI")
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+File.separator+map.get("git_repoDir")+".git/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // Test for file not found...
      try {
         boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI,tmpDir,null,true)
         assertTrue(false)
      } catch(FileNotFoundException ex) {
      } catch(Exception) {
         throw ex
      }

      assertTrue(tmpDir.mkdirs() && tmpDir.setWritable(true))
      StringBuffer output = new StringBuffer()
      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI,tmpDir,output,true)
      assertTrue(retStat)
      output = null

      if (tmpDir.exists()) {
         retStat = true
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      assertTrue(retStat)
   }

   /**
    * Unit test for svn cloning with target directory and user/password
    */
   void testScmSVNCloneWithTargetAndUser() {
      if (runUnitTestsOnly()) {
        return;
      }
      String scmURI = map.get("git_repoURI")
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+File.separator+map.get("git_repoDir")+".git/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // As this is a public URI, it will not matter what users you throw at it...
      String user = map.get("git_repoUser")
      String pwd  = map.get("git_repoUserPwd")

      // Test for file not found...
      try {
         boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI,
                                    user,pwd,tmpDir)
         assertTrue(false)
      } catch(FileNotFoundException ex) {
      } catch(Exception) {
         throw ex
      }

      assertTrue(tmpDir.mkdirs() && tmpDir.setWritable(true))
      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI,
                                    user,pwd,tmpDir)
      assertTrue(retStat)

      if (tmpDir.exists()) {
         retStat = true
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      assertTrue(retStat)
   }

   /**
    * Unit test for svn cloning with target directory and user/password
    */
   void testScmSVNCloneWithTargetAndUserTrue() {
      if (runUnitTestsOnly()) {
        return;
      }
      String scmURI = map.get("git_repoURI")
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+File.separator+map.get("git_repoDir")+".git/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // As this is a public URI, it will not matter what users you throw at it...
      String user = map.get("git_repoUser")
      String pwd  = map.get("git_repoUserPwd")

      // Test for file not found...
      try {
         boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI,
                                    user,pwd,tmpDir,null,true)
         assertTrue(false)
      } catch(FileNotFoundException ex) {
      } catch(Exception) {
         throw ex
      }

      assertTrue(tmpDir.mkdirs() && tmpDir.setWritable(true))
      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMSVN,scmURI,
                                    user,pwd,tmpDir,null,true)
      assertTrue(retStat)
      
      if (tmpDir.exists()) {
         retStat = true
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      assertTrue(retStat)
   }
}
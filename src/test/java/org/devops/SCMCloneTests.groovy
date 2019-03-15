package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit SCMTests tests for SCM class.
 */
public class SCMCloneTests extends GroovyTestCase {

   File propFile = new File("."+"/src/test/resources/unitTest.properties")
   def map = Utilities.mapProperties(propFile)

   // Utility function to get temporary directory...
   File getTmpDir() {
      return new File((map.get("tmpDir") != null) ? map.get("tmpDir") : System.getProperty("java.io.tmpdir"))
   }

   void testScmGitCloneBasic() {
      String scmURI = map.get("git_repoURI")
      File tmpDir = new File(map.get("git_repoDir"))
      
      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMGIT,scmURI)
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

   void testScmGitCloneBasicWithUser() {
      String scmURI = map.get("git_repoURI")
      File tmpDir = new File(map.get("git_repoDir"))
      
      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // As this is a public URI, it will not matter what users you throw at it...
      String user = map.get("git_repoUser")
      String pwd  = map.get("git_repoUserPwd")

      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMGIT,scmURI,user,pwd)
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

   void testScmGitCloneWithTarget() {
      String scmURI = map.get("git_repoURI")
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+map.get("git_repoDir")+"/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // Test for file not found...
      try {
         boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMGIT,scmURI,tmpDir)
         assertTrue(false)
      } catch(FileNotFoundException ex) {
      } catch(Exception) {
         throw ex
      }

      assertTrue(tmpDir.mkdirs() && tmpDir.setWritable(true))
      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMGIT,scmURI,tmpDir)
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

   void testScmGitCloneWithTargetAndUser() {
      String scmURI = map.get("git_repoURI")
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+map.get("git_repoDir")+"/")

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
         boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMGIT,scmURI,
                                    user,pwd,tmpDir)
         assertTrue(false)
      } catch(FileNotFoundException ex) {
      } catch(Exception) {
         throw ex
      }

      assertTrue(tmpDir.mkdirs() && tmpDir.setWritable(true))
      boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMGIT,scmURI,
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

   void testScmSVNCloneBasic() {
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

   void testScmSVNCloneBasicWithUser() {
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

   void testScmSVNCloneWithTarget() {
      String scmURI = map.get("git_repoURI")
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+map.get("git_repoDir")+".git/")

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

   void testScmSVNCloneWithTargetAndUser() {
      String scmURI = map.get("git_repoURI")
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+map.get("git_repoDir")+".git/")

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

}
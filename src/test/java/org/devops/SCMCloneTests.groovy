package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit SCMTests tests for SCM class.
 */
public class SCMCloneTests extends GroovyTestCase {

   void testScmGitCloneBasic() {
      String scmURI = "https://github.com/tpayne/deployment-script-examples.git"
      File tmpDir = new File("deployment-script-examples")
      
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
      String scmURI = "https://github.com/tpayne/deployment-script-examples.git"
      File tmpDir = new File("deployment-script-examples")
      
      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // As this is a public URI, it will not matter what users you throw at it...
      String user = "user1"
      String pwd  = "user1"

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
      String scmURI = "https://github.com/tpayne/deployment-script-examples.git"
      File   tempDir = Utilities.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+"deployment-script-examples"+"/")

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
      String scmURI = "https://github.com/tpayne/deployment-script-examples.git"
      File   tempDir = Utilities.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+"deployment-script-examples"+"/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // As this is a public URI, it will not matter what users you throw at it...
      String user = "user1"
      String pwd  = "user1"

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
      String scmURI = "https://github.com/tpayne/deployment-script-examples.git"
      File tmpDir = new File("deployment-script-examples.git")
      
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
      String scmURI = "https://github.com/tpayne/deployment-script-examples.git"
      File tmpDir = new File("deployment-script-examples.git")
      
      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // As this is a public URI, it will not matter what users you throw at it...
      String user = "user1"
      String pwd  = "user1"

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
      String scmURI = "https://github.com/tpayne/deployment-script-examples.git"
      File   tempDir = Utilities.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+"deployment-script-examples.git"+"/")

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
      String scmURI = "https://github.com/tpayne/deployment-script-examples.git"
      File   tempDir = Utilities.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+"deployment-script-examples.git"+"/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }

      // As this is a public URI, it will not matter what users you throw at it...
      String user = "user1"
      String pwd  = "user1"

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
package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit RepositoryTests tests for Repository class.
 */
public class RepositoryTests extends GroovyTestCase {

   File propFile = new File("."+"/src/test/resources/unitTest.properties")
   def map = Utilities.mapProperties(propFile)

   // Utility function to get temporary directory...
   File getTmpDir() {
      return new File((map.get("tmpDir") != null) ? map.get("tmpDir") : System.getProperty("java.io.tmpdir"))
   }

   void testRepoFilePushBasicFailArgs() {
      try {
         boolean retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.FILE,null,propFile)
      } catch(IllegalArgumentException ex) {
      } catch(Exception ex) {
         assertTrue(false)
      }
   }

   void testRepoFilePushBasicFailNoSrc() {
      File   tempDir = this.getTmpDir()
      Random rand = new Random()
      Long uid = rand.nextLong()      
      File targetDir = new File(tempDir.getCanonicalPath()+"/repoTest"+uid)
      File srcPath = new File("/FROGGY"+uid)

      try {
         boolean retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.FILE,srcPath,targetDir)
      } catch(FileNotFoundException ex) {
      } catch(Exception ex) {
         assertTrue(false)
      }
   }

   void testRepoFilePullBasicFailArgs() {
      try {
         boolean retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.FILE,null,propFile)
      } catch(IllegalArgumentException ex) {
      } catch(Exception ex) {
         assertTrue(false)
      }
   }

   void testRepoFilePullBasicFailNoSrc() {
      File   tempDir = this.getTmpDir()
      Random rand = new Random()
      Long uid = rand.nextLong()      
      File targetDir = new File(tempDir.getCanonicalPath()+"/repoTest"+uid)
      File srcPath = new File("/FROGGY"+uid)

      try {
         boolean retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.FILE,srcPath,targetDir)
      } catch(FileNotFoundException ex) {
      } catch(Exception ex) {
         assertTrue(false)
      }
   }

   void testRepoFilePushBasicFailNoTarget() {
      File   tempDir = this.getTmpDir()
      Random rand = new Random()
      Long uid = rand.nextLong()      
      File targetDir = new File(tempDir.getCanonicalPath()+"/repoTest"+uid)

      if (targetDir.exists()) {
         targetDir.setWritable(true)
         Utilities.deleteDirs(targetDir)
         targetDir.delete()
      } 

      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+map.get("docker_buildFile"))

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read a source file to use "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(buildDirectory.getAbsolutePath()+File.separator+map.get("docker_fileName"))

      ldockerFile.delete()
      ldockerFile << dockerFile.bytes

      try {
         boolean retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.FILE,ldockerFile,targetDir)
      } catch(FileNotFoundException ex) {
         ldockerFile.delete()
      } catch(Exception ex) {
         ldockerFile.delete()
         assertTrue(false)
      }
   }

   void testRepoFilePullBasicFailNoTarget() {
      File   tempDir = this.getTmpDir()
      Random rand = new Random()
      Long uid = rand.nextLong()      
      File targetDir = new File(tempDir.getCanonicalPath()+"/repoTest"+uid)

      if (targetDir.exists()) {
         targetDir.setWritable(true)
         Utilities.deleteDirs(targetDir)
         targetDir.delete()
      } 

      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+map.get("docker_buildFile"))

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read a source file to use "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(buildDirectory.getAbsolutePath()+File.separator+map.get("docker_fileName"))

      ldockerFile.delete()
      ldockerFile << dockerFile.bytes

      try {
         boolean retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.FILE,ldockerFile,targetDir)
      } catch(FileNotFoundException ex) {
         ldockerFile.delete()
      } catch(Exception ex) {
         ldockerFile.delete()
         assertTrue(false)
      }
   }

   void testRepoFilePushBasic() {
      File   tempDir = this.getTmpDir()
      Random rand = new Random()
      Long uid = rand.nextLong()      
      File targetDir = new File(tempDir.getCanonicalPath()+"/repoTest"+uid)
      
      if (targetDir.exists()) {
         targetDir.setWritable(true)
         Utilities.deleteDirs(targetDir)
         targetDir.delete()
      } 

      targetDir.mkdirs()
      targetDir.setWritable(true)

      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+map.get("docker_buildFile"))

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read a source file to use "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(buildDirectory.getAbsolutePath()+File.separator+map.get("docker_fileName"))

      ldockerFile.delete()
      ldockerFile << dockerFile.bytes

      boolean retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.FILE,ldockerFile,targetDir)
      assertTrue(retStat)
      retStat = false
      ldockerFile.delete()
      ldockerFile = null
      ldockerFile = new File(targetDir.getAbsolutePath()+File.separator+map.get("docker_fileName"))
      assertTrue(ldockerFile.exists())
      if (targetDir.exists()) {
         retStat = true
         targetDir.setWritable(true)
         Utilities.deleteDirs(targetDir)
         targetDir.delete()
      }
      assertTrue(!targetDir.exists())
      assertTrue(retStat)
   }

   void testRepoFilePullBasic() {
      File   tempDir = this.getTmpDir()
      Random rand = new Random()
      Long uid = rand.nextLong()      
      File targetDir = new File(tempDir.getCanonicalPath()+"/repoTest"+uid)
      
      if (targetDir.exists()) {
         targetDir.setWritable(true)
         Utilities.deleteDirs(targetDir)
         targetDir.delete()
      } 

      targetDir.mkdirs()
      targetDir.setWritable(true)

      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+map.get("docker_buildFile"))

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read a source file to use "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(buildDirectory.getAbsolutePath()+File.separator+map.get("docker_fileName"))

      ldockerFile.delete()
      ldockerFile << dockerFile.bytes

      boolean retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.FILE,ldockerFile,targetDir)
      assertTrue(retStat)
      retStat = false
      ldockerFile.delete()
      ldockerFile = null
      ldockerFile = new File(targetDir.getAbsolutePath()+File.separator+map.get("docker_fileName"))
      assertTrue(ldockerFile.exists())
      if (targetDir.exists()) {
         retStat = true
         targetDir.setWritable(true)
         Utilities.deleteDirs(targetDir)
         targetDir.delete()
      }
      assertTrue(!targetDir.exists())
      assertTrue(retStat)
   } 

   void testPullDir() {
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcDir = new File("."+"/src/test/")
      File targetDir = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      targetDir.mkdirs()
      boolean retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.FILE,srcDir,targetDir)
      int i = 0
      // Count dirs in target...
      for(File c : targetDir.listFiles()) {
         if (c.isDirectory()) {
            i++
         }   
      }
      Utilities.deleteDirs(targetDir)
      assertTrue(retStat)
      assertTrue(i==3)
   } 

   void testPushDir() {
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcDir = new File("."+"/src/test/")
      File targetDir = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      targetDir.mkdirs()
      boolean retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.FILE,srcDir,targetDir)
      int i = 0
      // Count dirs in target...
      for(File c : targetDir.listFiles()) {
         if (c.isDirectory()) {
            i++
         }   
      }
      Utilities.deleteDirs(targetDir)
      assertTrue(retStat)
      assertTrue(i==3)
   } 

   void testPushDirTxt() {
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcDir = new File("."+"/src/test/")
      File targetDir = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      targetDir.mkdirs()
      boolean retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.FILE,srcDir.getAbsolutePath(),
                                                   targetDir.getAbsolutePath())
      int i = 0
      // Count dirs in target...
      for(File c : targetDir.listFiles()) {
         if (c.isDirectory()) {
            i++
         }   
      }
      Utilities.deleteDirs(targetDir)
      assertTrue(retStat)
      assertTrue(i==3)
   } 

   void testPullDirTxt() {
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcDir = new File("."+"/src/test/")
      File targetDir = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      targetDir.mkdirs()
      boolean retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.FILE,srcDir.getAbsolutePath(),
                                                   targetDir.getAbsolutePath())
      int i = 0
      // Count dirs in target...
      for(File c : targetDir.listFiles()) {
         if (c.isDirectory()) {
            i++
         }   
      }
      Utilities.deleteDirs(targetDir)
      assertTrue(retStat)
      assertTrue(i==3)
   } 
}
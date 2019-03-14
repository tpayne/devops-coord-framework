package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit Container tests for Container class.
 */
public class ContainerTests extends GroovyTestCase {

   void testrunContainerBasic() {
      String centos = "centos"
      boolean retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,centos)
      assertTrue(retStat)
   }

   void testrunContainerCmd() {
      String centos = "centos"
      String cmdStr = "df -H"
      boolean retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,centos,cmdStr)
      assertTrue(retStat)
   }

   void testrunContainerOutputCmd() {
      String centos = "centos"
      String cmdStr = "echo hello"
      StringBuffer outputStr = new StringBuffer()
      boolean retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,centos, cmdStr, outputStr)
      String output = outputStr
      assertTrue(retStat)
      assertEquals(output,"hello")
   }

   void testrunContainerCmdOpts() {
      String centos = "centos"
      String cmdStr = "df -H"
      Random rand = new Random()
      Long uid = rand.nextLong()

      def opts = [
         "--name"             : "testname"+uid,
         "--no-healthcheck"   : null,
         "--read-only"        : null,
         "--rm"               : null
      ]

      boolean retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,centos, cmdStr, null, opts)
      assertTrue(retStat)
   }

   void testrmContainerBasic() {
      String centos = "centos"
      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,centos, true)
      retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,centos)
      assertTrue(retStat)
      retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,centos, true)
      assertTrue(retStat)
   }

   void testrmContainerBasicFail() {
      String centos = "centos"
      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,centos)
      retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,centos)
      assertFalse(retStat)
   }

   void testrmContainerBasicFailOutput() {
      String centos = "centos"
      StringBuffer outputStr = new StringBuffer()
      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,centos)
      retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,centos, true, outputStr)
      assertEquals(outputStr.toString().trim(),"Error response from daemon: No such image: centos:latest")
   }

   void testBuildContainBasic() {
      Random rand = new Random()
      Long uid = rand.nextLong()

      String devimage = "centos-devimage-"+uid
      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+"/src/test/resources/DockerFile.test")

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read the master docker file "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(buildDirectory.getAbsolutePath()+"/Dockerfile")

      ldockerFile.delete()
      ldockerFile << dockerFile.bytes

      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      retStat = Container.buildContainer(ConfigPropertiesConstants.DOCKER,devimage,buildDirectory)
      boolean retStat1 = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      ldockerFile.delete()
      assertTrue(retStat)
   }

   void testBuildContainBuildDir() {
      Random rand = new Random()
      Long uid = rand.nextLong()
      File   tempDir = Utilities.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+"containerTests"+"/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      tmpDir.mkdirs()

      String devimage = "centos-devimage-"+uid
      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+"/src/test/resources/DockerFile.test")

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read the master docker file "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(tmpDir.getAbsolutePath()+"/Dockerfile")

      ldockerFile.delete()
      ldockerFile << dockerFile.bytes

      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      retStat = Container.buildContainer(ConfigPropertiesConstants.DOCKER,devimage,tmpDir)
      boolean retStat1 = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      ldockerFile.delete()

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }      
      assertTrue(retStat)
   }

   void testBuildContainBuildDirFile() {
      Random rand = new Random()
      Long uid = rand.nextLong()
      File   tempDir = Utilities.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+"containerTests"+"/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      tmpDir.mkdirs()

      String devimage = "centos-devimage-"+uid
      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+"/src/test/resources/DockerFile.test")

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read the master docker file "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(tmpDir.getAbsolutePath()+"/Dockerfile")

      ldockerFile.delete()
      ldockerFile << dockerFile.bytes

      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      retStat = Container.buildContainer(ConfigPropertiesConstants.DOCKER,devimage,tmpDir,ldockerFile)
      boolean retStat1 = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      ldockerFile.delete()

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }      
      assertTrue(retStat)
   }

   void testBuildContainOutputStr() {
      Random rand = new Random()
      Long uid = rand.nextLong()
      File   tempDir = Utilities.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+"containerTests"+"/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      tmpDir.mkdirs()

      String devimage = "centos-devimage-"+uid
      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+"/src/test/resources/DockerFile.test")

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read the master docker file "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(tmpDir.getAbsolutePath()+"/Dockerfile")

      ldockerFile.delete()
      ldockerFile << dockerFile.bytes

      StringBuffer outputStr = new StringBuffer()
      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      retStat = Container.buildContainer(ConfigPropertiesConstants.DOCKER,devimage,tmpDir,ldockerFile, outputStr)
      boolean retStat1 = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      ldockerFile.delete()
      String retStr = outputStr.toString()
      outputStr = null

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }      
      assertTrue(retStat)
      assertTrue(retStr.contains("sha256:"))
   }

   void testBuildContainOpts() {
      Random rand = new Random()
      Long uid = rand.nextLong()
      File   tempDir = Utilities.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+"/"+"containerTests"+"/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      tmpDir.mkdirs()

      String devimage = "centos-devimage-"+uid
      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+"/src/test/resources/DockerFile.test")

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read the master docker file "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(tmpDir.getAbsolutePath()+"/Dockerfile")

      ldockerFile.delete()
      ldockerFile << dockerFile.bytes

      StringBuffer outputStr = new StringBuffer()
      def opts = [
         "--force-rm" : null,
         "--pull"     : null,
         "--label"    : "devimage"
      ]
      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      retStat = Container.buildContainer(ConfigPropertiesConstants.DOCKER,devimage,tmpDir,ldockerFile,outputStr,opts)
      boolean retStat1 = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      ldockerFile.delete()
      String retStr = outputStr.toString()
      outputStr = null

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }      
      assertTrue(retStat)
      assertTrue(retStr.contains("sha256:"))
   }
}
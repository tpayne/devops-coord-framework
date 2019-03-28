package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit Container tests for Container class.
 */
public class ContainerTests extends GroovyTestCase {

   File propFile = new File("."+"/src/test/resources/unitTest.properties")
   def map = Utilities.mapProperties(propFile)

   /**
    * Utility function for getting tmpDir
    */
   File getTmpDir() {
        return new File((map.get("tmpDir") != null) ? map.get("tmpDir") : System.getProperty("java.io.tmpdir"))
   }

   /**
    * Unit test for running a container
    */
   void testrunContainerBasic() {
      String imgName = map.get("docker_container")
      boolean retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,imgName)
      assertTrue(retStat)
   }

   /**
    * Unit test for running container
    */
   void testrunContainerCmd() {
      String imgName = map.get("docker_container")
      String cmdStr = "df"
      boolean retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,imgName,cmdStr)
      assertTrue(retStat)
   }

   /**
    * Unit test for running container with output
    */
   void testrunContainerOutputCmd() {
      String imgName = map.get("docker_container")
      String cmdStr = "echo hello"
      StringBuffer outputStr = new StringBuffer()
      boolean retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,imgName, cmdStr, outputStr)
      String output = outputStr
      assertTrue(retStat)
      assertTrue(output.contains("hello"))
   }

   /**
    * Unit test for running container with options
    */
   void testrunContainerCmdOpts() {
      String imgName = map.get("docker_container")
      String cmdStr = "df"
      Random rand = new Random()
      Long uid = rand.nextLong()

      def opts = [
         "--name"             : "testname"+uid,
         "--no-healthcheck"   : null,
         "--read-only"        : null,
         "--rm"               : null
      ]

      boolean retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,imgName, cmdStr, null, opts)
      assertTrue(retStat)
   }

   /**
    * Unit test for deleting a container
    */
   void testrmContainerBasic() {
      String imgName = map.get("docker_container")
      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,imgName, true)
      retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,imgName)
      assertTrue(retStat)
      retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,imgName, true)
      assertTrue(retStat)
   }

   /**
    * Unit test for deleting a container with failure
    */
   void testrmContainerBasicFail() {
      String imgName = map.get("docker_container")
      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,imgName)
      retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,imgName)
      assertFalse(retStat)
   }

   /**
    * Unit test for deleting a container with output
    */
   void testrmContainerBasicFailOutput() {
      String imgName = map.get("docker_container")
      StringBuffer outputStr = new StringBuffer()
      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,imgName, false, outputStr)
      assertTrue(outputStr.toString().contains("conflict: unable to remove repository reference"))
      retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,imgName, true, outputStr)
      assertTrue(outputStr.toString().contains("Untagged: "+imgName+":latest"))
   }

   /**
    * Unit test for building a container
    */
   void testBuildContainBasic() {
      Random rand = new Random()
      Long uid = rand.nextLong()
      String imgName = map.get("docker_container")

      String devimage = imgName+"-devimage-"+uid
      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+map.get("docker_buildFile"))

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read the master docker file "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(buildDirectory.getAbsolutePath()+File.separator+map.get("docker_fileName"))

      ldockerFile.delete()
      ldockerFile << dockerFile.bytes

      boolean retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      retStat = Container.buildContainer(ConfigPropertiesConstants.DOCKER,devimage,buildDirectory)
      boolean retStat1 = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,devimage,true)
      ldockerFile.delete()
      assertTrue(retStat)
   }

   /**
    * Unit test for building a container
    */
   void testBuildContainBuildDir() {
      Random rand = new Random()
      Long uid = rand.nextLong()
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+File.separator+"containerTests"+"/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      tmpDir.mkdirs()
      String imgName = map.get("docker_container")
      String devimage = imgName+"-devimage-"+uid

      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+map.get("docker_buildFile"))

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read the master docker file "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(tmpDir.getAbsolutePath()+File.separator+map.get("docker_fileName"))

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

   /**
    * Unit test for building a container
    */
   void testBuildContainBuildDirFile() {
      Random rand = new Random()
      Long uid = rand.nextLong()
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+File.separator+"containerTests"+"/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      tmpDir.mkdirs()

      String imgName = map.get("docker_container")
      String devimage = imgName+"-devimage-"+uid

      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+map.get("docker_buildFile"))

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read the master docker file "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(tmpDir.getAbsolutePath()+File.separator+map.get("docker_fileName"))

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

   /**
    * Unit test for building a container
    */
   void testBuildContainOutputStr() {
      Random rand = new Random()
      Long uid = rand.nextLong()
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+File.separator+"containerTests"+"/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      tmpDir.mkdirs()

      String imgName = map.get("docker_container")
      String devimage = imgName+"-devimage-"+uid

      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+map.get("docker_buildFile"))

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read the master docker file "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(tmpDir.getAbsolutePath()+File.separator+map.get("docker_fileName"))

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

   /**
    * Unit test for building a container
    */
   void testBuildContainOpts() {
      Random rand = new Random()
      Long uid = rand.nextLong()
      File   tempDir = this.getTmpDir()
      File tmpDir = new File(tempDir.getCanonicalPath()+File.separator+"containerTests"+"/")

      if (tmpDir.exists()) {
         tmpDir.setWritable(true)
         Utilities.deleteDirs(tmpDir)         
         tmpDir.delete()
         assertFalse(tmpDir.exists())
      }
      tmpDir.mkdirs()

      String imgName = map.get("docker_container")
      String devimage = imgName+"-devimage-"+uid

      File buildDirectory = new File(".")
      File dockerFile = new File(buildDirectory.getAbsolutePath()+map.get("docker_buildFile"))

      if (!dockerFile.exists() || !dockerFile.canRead()) {
         assert("Cannot read the master docker file "+dockerFile.getAbsolutePath())
      }

      File ldockerFile = new File(tmpDir.getAbsolutePath()+File.separator+map.get("docker_fileName"))

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

   /**
    * Unit test for tagging/pulling/pushing a container
    */
   void testtagContainerBasic() {
      String imageName = map.get("docker_container")
      String regName = imageName+"-"
      String regImageName = map.get("docker_registryImage")
      Random rand = new Random()
      Long uid = rand.nextLong()
      int portNo = 5000

      regName += uid

      boolean retStat = Container.createContainerRegistry(ConfigPropertiesConstants.DOCKER,regName,regImageName, portNo)
      assertTrue(retStat)
      retStat = Container.pullContainerImage(ConfigPropertiesConstants.DOCKER,imageName)
      assertTrue(retStat)
      String regURI = map.get("docker_registryURI")+portNo+File.separator+regName
      retStat = Container.tagContainer(ConfigPropertiesConstants.DOCKER,imageName,regURI)
      assertTrue(retStat)
      retStat = Container.pushContainer(ConfigPropertiesConstants.DOCKER,regURI)
      assertTrue(retStat)
      retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,regURI,true)
      retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,imageName,true)
      retStat = Container.pullContainerImage(ConfigPropertiesConstants.DOCKER,regURI)
      retStat = Container.deleteContainerRegistry(ConfigPropertiesConstants.DOCKER,regName)
      assertTrue(retStat)
      retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,regURI,true)
      retStat = Container.deleteContainerImage(ConfigPropertiesConstants.DOCKER,imageName,true)
   }   
}
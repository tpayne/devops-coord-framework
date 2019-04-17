package org.devops.framework.core;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

/**
 * Unit Utility tests for Utility class.
 */
public class UtilityTests extends GroovyTestCase {

   File propFile = new File("."+"/src/test/resources/unitTest.properties")
   def map = Utilities.mapProperties(propFile)

   /**
    * Utility function for getting tmpDir
    */
   File getTmpDir() {
      return new File((map.get("tmpDir") != null) ? map.get("tmpDir") : System.getProperty("java.io.tmpdir"))
   }


   /**
    * Unit test for read and write functions
    */
   void testReadWriteFile() {
      Random rand = new Random()
      Long uid = rand.nextLong()
      File propFile = new File("."+"/src/test/resources/unitTest.properties")

      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)
      targetFile.delete()
      assertTrue(targetFile.createNewFile())
      Utilities.writeFile(targetFile,"Hello world")
      String tst = new String(Utilities.readAllBytes(targetFile))
      targetFile.delete()
      assertTrue(tst.contains("Hello world"))
      tst = null
      tst = Utilities.readFile(propFile)
      assertTrue(tst.contains("# SCM properties..."))
   }

   /**
    * Unit test parsing arguments
    */
   void testParseArgs() {
      String cmdStr = "/opt/local/bin/curl  -X POST --data-urlencode \'payload={\"text\":\"The build may have worked. The return status is unsure\"}\' localhost:0:XCVD/"
      List<String> args = Utilities.parseArgs(cmdStr)
      assertEquals(args.size(),1)
      cmdStr = "arg1 arg2 arg3 arg4 \"arg 5\" arg6"
      args = Utilities.parseArgs(cmdStr)
      assertEquals(args.size(),6)
   }

   /**
    * Unit test for running commands
    */
   void testShellCmdSuccess() {
      def cmdStr
      StringBuffer returnStr = new StringBuffer()

      cmdStr = "echo 1"
      int retStat = Utilities.runCmd(cmdStr,returnStr)
      String returnOutput = returnStr.toString()
      returnOutput = returnOutput.trim()
      assertEquals(retStat,0)
      assertEquals(returnOutput,"1")
   }

   /**
    * Unit test for running commands and failure
    */
   void testShellCmdFailure() {
      def cmdStr
      StringBuffer returnStr = new StringBuffer()
      
      cmdStr = "lsdx"
      int retStat = Utilities.runCmd(cmdStr,returnStr)
      String returnOutput = returnStr.toString()
      returnOutput = returnOutput.trim()
      assertTrue(retStat>0)
      assertTrue(returnOutput.contains("lsdx: command not found") ||
                 returnOutput.contains("\"lsdx\": error=2, No such file or directory"))
   }

   /**
    * Unit test for reading property files
    */
   void testPropertyFileRead() {
      File propFile = new File("."+"/src/test/resources/unitTest.properties")
      def map = Utilities.mapProperties(propFile)
      assertTrue(map.containsKey("slack_webhookURI"))
   }

   /**
    * Unit test for file copy IO
    */
   void testCopyFile() {
      File propFile = new File("."+"/src/test/resources/unitTest.properties")
      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+propFile.getName())

      Utilities.copyFile(propFile,targetFile)
      boolean retStat = targetFile.exists()
      targetFile.delete()
      assertTrue(retStat)
   }  

   /**
    * Unit test for directory copying
    */
   void testCopyFileDir() {
      File propFile = new File("."+"/src/test/resources/unitTest.properties")
      File targetFile = new File(getTmpDir().getAbsolutePath())

      Utilities.copyFile(propFile,targetFile)
      String propFileName = propFile.getName()
      propFile = null
      propFile = new File(targetFile.getAbsolutePath()+File.separator+propFileName)
      boolean retStat = propFile.exists()
      propFile.delete()
      assertTrue(retStat)
   }  

   /**
    * Unit test for directory copying
    */
   void testCopyDirDir() {
      File propFile = new File(getTmpDir().getAbsolutePath())
      File targetFile = new File(getTmpDir().getAbsolutePath())

      boolean retStat = false

      try {
         Utilities.copyFile(propFile,targetFile)   
      } catch(IOException e) {
         retStat=true
      } catch(Exception e) {
      }    
      assertTrue(retStat)
   }  

   /**
    * Unit test for directory copying
    */
   void testCopyDir() {
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcDir = new File("."+"/src/test/")
      File targetDir = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      targetDir.mkdirs()
      Utilities.copyDirectories(srcDir,targetDir)  
      int i = 0
      // Count dirs in target...
      for(File c : targetDir.listFiles()) {
         if (c.isDirectory()) {
            i++
         }   
      }
      Utilities.deleteDirs(targetDir)
      assertTrue(i==3)
   } 

   /**
    * Unit test for directory/file copying
    */
   void testCopyDirFile() {
      File propFile = new File("."+"/src/test/resources/unitTest.properties")
      File targetFile = new File(getTmpDir().getAbsolutePath())

      boolean retStat = false

      try {
         Utilities.copyDirectories(propFile,targetFile)   
      } catch(IOException e) {
         retStat=true
      } catch(Exception e) {
      }    
      assertTrue(retStat)
   }         

   /**
    * Unit test for MD5 chksumming
    */
   void testHashFile() {
      File propFile = new File("."+"/src/test/resources/DockerFile.test")

      boolean retStat = true
      try {
         String hashCode = Utilities.calcFileMD5(propFile)  
         assertEquals(hashCode,"458fd5e7b77ce70e98b3e33c9ac57721")
      } catch(FileNotFoundException e) {
         assert("File not found")
      } catch(Exception e) {
         retStat = false
      }    
      assertTrue(retStat)
   }    

   /**
    * Unit test for getting file extentions
    */
  void testgetFileExt() {
    File propFile = new File("."+"/src/test/resources/unitTest.properties")
    String fileExt = Utilities.getFileExt(propFile)
    assertEquals("properties",fileExt)
  }  

   /**
    * Unit test for getting hostname
    */
  void testgetHostName() {
    String hostName = Utilities.getHostName()
    assertTrue(hostName!=null && !hostName.isEmpty())
  }  

   /**
    * Unit test for getting os user
    */
  void testgetUserName() {
    String user = Utilities.getOSUser()
    assertTrue(user!=null && !user.isEmpty())   
  }  

   /**
    * Unit test for counting files
    */
  void testCountFiles() {
    long count = Utilities.countFiles(getTmpDir())
    assertTrue(count>0)   
  }   
}

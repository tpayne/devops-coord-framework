package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit Utility tests for Utility class.
 */
public class UtilityTests extends GroovyTestCase {

   File propFile = new File("."+"/src/test/resources/unitTest.properties")
   def map = Utilities.mapProperties(propFile)

   // Utility function to get temporary directory...
   File getTmpDir() {
      return new File((map.get("tmpDir") != null) ? map.get("tmpDir") : System.getProperty("java.io.tmpdir"))
   }

   void testReadFile() {

   }

   void testWriteFile() {
      Random rand = new Random()
      Long uid = rand.nextLong()

      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)
      targetFile.delete()
      assertTrue(targetFile.createNewFile())
      Utilities.writeFile(targetFile,"Hello world")
      String tst = new String(Utilities.readAllBytes(targetFile))
      targetFile.delete()
      assertTrue(tst.contains("Hello world"))
   }

   void testParseArgs() {
      String cmdStr = "/opt/local/bin/curl  -X POST --data-urlencode \'payload={\"text\":\"The build may have worked. The return status is unsure\"}\' localhost:0:XCVD/"
      List<String> args = Utilities.parseArgs(cmdStr)
      assertEquals(args.size(),1)
      cmdStr = "arg1 arg2 arg3 arg4 \"arg 5\" arg6"
      args = Utilities.parseArgs(cmdStr)
      assertEquals(args.size(),6)
   }

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

   void testPropertyFileRead() {
      File propFile = new File("."+"/src/test/resources/unitTest.properties")
      def map = Utilities.mapProperties(propFile)
      assertTrue(map.containsKey("slack_webhookURI"))
   }

   void testCopyFile() {
      File propFile = new File("."+"/src/test/resources/unitTest.properties")
      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+propFile.getName())

      Utilities.copyFile(propFile,targetFile)
      boolean retStat = targetFile.exists()
      targetFile.delete()
      assertTrue(retStat)
   }  

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

   void testHashFile() {
      File propFile = new File("."+"/src/test/resources/unitTest.properties")

      boolean retStat = true
      try {
         String hashCode = Utilities.calcFileMD5(propFile)  
         assertEquals(hashCode,"ad282eef15d1436e7e549b4b4603455b")
      } catch(FileNotFoundException e) {
         assert("File not found")
      } catch(Exception e) {
         retStat = false
      }    
      assertTrue(retStat)
   }    

   void testgetFileExt() {
      File propFile = new File("."+"/src/test/resources/unitTest.properties")
      String fileExt = Utilities.getFileExt(propFile)
      assertEquals("properties",fileExt)
   }   
}

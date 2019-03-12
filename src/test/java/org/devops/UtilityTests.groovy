package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit Utility tests for Utility class.
 */
public class UtilityTests extends GroovyTestCase {

      void testReadFile() {

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
         assertTrue(returnOutput.contains("lsdx: command not found"))
      }
}

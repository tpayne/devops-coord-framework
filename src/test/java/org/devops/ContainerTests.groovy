package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit Container tests for Container class.
 */
public class ContainerTests extends GroovyTestCase {

   void testContainerBasic() {
      String centos = "centos"
      boolean retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,centos)
      assertTrue(retStat)
   }

   void testContainerCmd() {
      String centos = "centos"
      String cmdStr = "df -H"
      boolean retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,centos,cmdStr)
      assertTrue(retStat)
   }

   void testContainerOutputCmd() {
      String centos = "centos"
      String cmdStr = "echo hello"
      StringBuffer outputStr = new StringBuffer()
      boolean retStat = Container.runContainer(ConfigPropertiesConstants.DOCKER,centos, cmdStr, outputStr)
      String output = outputStr
      assertTrue(retStat)
      assertEquals(output,"hello")
   }

   void testContainerCmdOpts() {
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
}
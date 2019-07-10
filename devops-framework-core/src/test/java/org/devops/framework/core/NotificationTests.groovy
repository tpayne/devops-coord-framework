package org.devops.framework.core;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit Notifications tests for Notifications class.
 */
public class NotificationsTests extends GroovyTestCase {

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
    * Unit test for using Slack messaging
    */
   void testSlackNotificationOk() {
      if (!runUnitTestsOnly()) {
        String slackURI = map.get("slack_webhookURI")
        if (slackURI != null && !slackURI.isEmpty()) {
          String text = map.get("slack_channelMsg")
          boolean retStat = Notifications.messageSlackChannel(slackURI,text)
          assertTrue(retStat)
        } 
      }
   }

   /**
    * Unit test for using Slack messaging
    */
   void testSlackNotificationFail() {
      if (!runUnitTestsOnly()) {
        String slackURI = "localhost:0:XCVD/"
        String text = map.get("slack_channelMsg")
        if (text != null && !text.isEmpty()) {
          boolean retStat = Notifications.messageSlackChannel(slackURI,text)
          assertFalse(retStat)
        }
      }
   }

   /**
    * Unit test for using email
    */
   void testEmailComplex() {
      String mailServer = map.get("smtp_mailServer")
      String fromEmailAddress = map.get("smtp_mailFrom")
      String toEmailAddress = map.get("smtp_mailTo")
      String subjectTxt = map.get("smtp_mailSubject")
      String emailText = map.get("smtp_mailText")
      boolean enableTLS = true

      String serverUserName = map.get("smtp_mailUser")
      String serverUserPwd = map.get("smtp_mailUserPwd")

      if (mailServer != null && !mailServer.isEmpty() &&
          serverUserName != null && !serverUserName.isEmpty() &&
          serverUserPwd != null && !serverUserPwd.isEmpty()) {
         try {
            boolean retStat = Notifications.sendEmail(mailServer, serverUserName,
                                                      serverUserPwd,enableTLS,
                                                      fromEmailAddress,toEmailAddress,
                                                      subjectTxt,emailText)
            
            assertTrue(retStat)
         } catch(Exception ex) {
            assertTrue(false)
         }
      }
   }

   /**
    * Unit test for using email
    */
   void testEmail() {
      String mailServer = map.get("smtp_mailServer")
      String fromEmailAddress = map.get("smtp_mailFrom")
      String toEmailAddress = map.get("smtp_mailTo")
      String subjectTxt = map.get("smtp_mailSubject")
      String emailText = map.get("smtp_mailText")

      if (mailServer != null && !mailServer.isEmpty() &&
          fromEmailAddress != null && !fromEmailAddress.isEmpty() &&
          toEmailAddress != null && !toEmailAddress.isEmpty()) {
         try {
            boolean retStat = Notifications.sendEmail(mailServer,
                                                      fromEmailAddress,toEmailAddress,
                                                      subjectTxt,emailText)
            assertTrue(retStat)
         } catch(Exception ex) {
            assertTrue(false)
         }
      }
   }

   /**
    * Unit test for using email
    */
   void testEmailFile() {
      String mailServer = map.get("smtp_mailServer")
      String fromEmailAddress = map.get("smtp_mailFrom")
      String toEmailAddress = map.get("smtp_mailTo")
      String subjectTxt = map.get("smtp_mailSubject")
      String emailText = map.get("smtp_mailText")

      if (mailServer != null && !mailServer.isEmpty() &&
          fromEmailAddress != null && !fromEmailAddress.isEmpty() &&
          toEmailAddress != null && !toEmailAddress.isEmpty()) {
         try {
            boolean retStat = Notifications.sendEmail(mailServer,
                                                      fromEmailAddress,toEmailAddress,
                                                      subjectTxt,propFile)
            assertTrue(retStat)
         } catch(Exception ex) {
            assertTrue(false)
         }
      }
   }  

   /**
    * Unit test for using email
    */
   void testEmailHTMLFile() {
      String mailServer = map.get("smtp_mailServer")
      String fromEmailAddress = map.get("smtp_mailFrom")
      String toEmailAddress = map.get("smtp_mailTo")
      String subjectTxt = map.get("smtp_mailSubject")
      String emailText = map.get("smtp_mailText")

      if (mailServer != null && !mailServer.isEmpty() &&
          fromEmailAddress != null && !fromEmailAddress.isEmpty() &&
          toEmailAddress != null && !toEmailAddress.isEmpty()) {
         try {
            boolean retStat = Notifications.sendEmail(mailServer,
                                                      fromEmailAddress,toEmailAddress,
                                                      subjectTxt,propFile,true)
            assertTrue(retStat)
         } catch(Exception ex) {
            assertTrue(false)
         }
      }
   }      
}

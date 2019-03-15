package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit Notifications tests for Notifications class.
 */
public class NotificationsTests extends GroovyTestCase {

   File propFile = new File("."+"/src/test/resources/unitTest.properties")
   def map = Utilities.mapProperties(propFile)

   void testSlackNotificationOk() {
      String slackURI = map.get("slack_webhookURI")
      String text = map.get("slack_channelMsg")
      boolean retStat = Notifications.messageSlackChannel(slackURI,text)
      assertTrue(retStat)
   }

   void testSlackNotificationFail() {
      String slackURI = "localhost:0:XCVD/"
      String text = map.get("slack_channelMsg")
      boolean retStat = Notifications.messageSlackChannel(slackURI,text)
      assertFalse(retStat)
   }

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
}

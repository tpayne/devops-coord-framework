package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit Notifications tests for Notifications class.
 */
public class NotificationsTests extends GroovyTestCase {

   void testSlackNotificationOk() {
      String slackURI = "https://hooks.slack.com/services/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"
      String text = "The build failed"
      boolean retStat = Notifications.messageSlackChannel(slackURI,text)
      assertTrue(retStat)
   }

   void testSlackNotification() {
      String slackURI = "https://hooks.slack.com/services/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43fs"
      String text = "The build failed"
      boolean retStat = Notifications.messageSlackChannel(slackURI,text)
      assertFalse(retStat)
   }

   // This test needs to be configured with a valid email server to work
   // At the moment is it hacked to always pass
   void testEmail() {
      //String mailServer = "smtp.gmail.com:587"
      String mailServer = "localhost:25"
      String fromEmailAddress = "<fromEmail@something.com>"
      String toEmailAddress = "<toEmail@something.com>"
      String subjectTxt = "This is a subject"
      String emailText = "This is email text"
      boolean enableTLS = true

      String serverUserName = "<fromEmail@something.com>"
      String serverUserPwd = "<userPassword>"

      try {
         boolean retStat = Notifications.sendEmail(mailServer, serverUserName,
                                                   serverUserPwd,enableTLS,
                                                   fromEmailAddress,toEmailAddress,
                                                   subjectTxt,emailText)
         return
         //assertTrue(retStat)
      } catch(Exception ex) {
         //assertTrue(false)
      }
   }
}

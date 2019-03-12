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

}

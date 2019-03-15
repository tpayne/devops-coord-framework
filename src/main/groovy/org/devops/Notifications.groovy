package org.devops;

import groovy.json.JsonOutput
import java.io.File
import java.util.*
import javax.mail.*
import javax.mail.internet.*
import javax.activation.*
import javax.mail.internet.MimeMessage
import javax.mail.PasswordAuthentication

/**
 * Notification routines for use with the framework
 */
class Notifications implements Serializable {

    // Local authenticator class...
    static private class SMTPAuth extends Authenticator {
        private PasswordAuthentication authentication

        public SMTPAuth(String userId, String pwd) {
             authentication = new PasswordAuthentication(userId, pwd)
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
             return authentication;
        }
    }

    /**
     * Simple Email messaging routine. 
     *
     * @param  String - mailServer
     * @param  String - fromEmailAddress
     * @param  String - toEmailAddress
     * @param  String - subject
     * @param  String - emailText
     * @return boolean
     */
    static boolean sendEmail(String mailServer,
                        String fromEmailAddress,
                        String toEmailAddress,
                        String subjectTxt,
                        String emailText) 
        throws MessagingException, Exception {
            return(sendEmail(mailServer,null,null,false,
                    fromEmailAddress,
                    toEmailAddress,
                    subjectTxt,
                    emailText))
    }

    /**
     * Simple Email messaging routine. This may or may not work depending on
     * how the email server is configured and what authentication model it uses.
     * If you need to modify it then it is likely it will be the SMTPAuth routine
     * that needs changing to cope with different security patterns.
     *
     * @param  String - mailServer
     * @param  String - serverUserName,
     * @param  String - serverUserPwd,
     * @parms  boolean - enableTLS
     * @param  String - fromEmailAddress
     * @param  String - toEmailAddress
     * @param  String - subject
     * @param  String - emailText
     * @return boolean
     */
    static boolean sendEmail(String mailServer,
                        String serverUserName,
                        String serverUserPwd,
                        boolean enableTLS,
                        String fromEmailAddress,
                        String toEmailAddress,
                        String subjectTxt,
                        String emailText) 
        throws MessagingException, Exception {

        String portN = "25"
        String lmailServer = mailServer;
        if (lmailServer.contains(":")) {
            portN = lmailServer.substring(lmailServer.lastIndexOf(':')+1).trim()
            lmailServer = lmailServer.substring(0,lmailServer.lastIndexOf(':')).trim()
        }
 
        // Get system properties
        Properties properties = System.getProperties()

        // Setup mail server
        properties.setProperty("mail.smtp.host", lmailServer)
        properties.setProperty("mail.smtp.port", portN)
        properties.setProperty("mail.host",      lmailServer)

        if (enableTLS) {
            properties.setProperty("mail.smtp.auth", "true")
            properties.setProperty("mail.smtp.starttls.enable", "true")
        }

        Session session = null
        Authenticator auth = null

        if (serverUserName != null && serverUserPwd != null) {
            // Get an authenticator object & session manager...
            auth = new SMTPAuth(serverUserName, serverUserPwd)
            session = Session.getInstance(properties, auth)
        } else {
            session = Session.getInstance(properties, null)
        }

        try {
            // Create a default MimeMessage object.
            new MimeMessage(session).with { message ->
                
                // Add From, Subject and Content
                from = new InternetAddress( fromEmailAddress )
                subject = subjectTxt
                setContent emailText, 'text/html'

                // Add recipients
                addRecipient( Message.RecipientType.TO, new InternetAddress( toEmailAddress ) )

                // Send the message
                Transport.send( message )
            }
        }
        catch(MessagingException ex) {
            throw ex
        } catch(Exception ex) {
            throw ex
        }
        return true
    }

    /**
     * Simple Slack channel messaging routine
     * See https://api.slack.com/incoming-webhooks for how to setup 
     * channel incoming webhooks
     * @param final String - slackURI
     * @param final String - ltext
     * @throws FileNotFoundException
     * @return boolean
     */
    static boolean messageSlackChannel(String slackURI,
                                       String ltext) 
    throws FileNotFoundException {
        
        // Use a JSON payload
        def payload = JsonOutput.toJson([text : ltext ])
        def curlStr = " -X POST --data-urlencode \'payload=${payload}\' ${slackURI}"
        // Use a direct payload
        //def curlStr = " -X POST -H \'Content-type: application/json\' --data \'{\"text\":"+
        //                "\""+ltext+"\"}\' " + slackURI

        File curlExe = Utilities.getExecutable("curl")
        if (curlExe == null) {
            throw new FileNotFoundException("Error: Curl has not been located")
        }

        // Construct the required command...
        String cmdStr = curlExe.getAbsolutePath()+" "+curlStr
        
        StringBuffer returnStr = new StringBuffer()

        int retStat = Utilities.runCmd(cmdStr,returnStr)
        String returnOutput = returnStr.toString()
        returnOutput = returnOutput.trim()
        returnStr = null
        
        if (retStat>0 || returnOutput.contains("invalid_payload") ||
            returnOutput.isEmpty() || !returnOutput.contains("ok")) {
            return false
        } else if (retStat==0 && returnOutput.contains("ok")) {
            return true
        } 
        return false
    }
 }

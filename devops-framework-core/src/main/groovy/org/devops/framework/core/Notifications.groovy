/**
 * Notification routines for use with the framework
 */
package org.devops.framework.core;

import groovy.json.JsonOutput
import java.io.File
import java.util.*
import javax.mail.*
import javax.mail.internet.*
import javax.activation.*
import javax.mail.internet.MimeMessage
import javax.mail.PasswordAuthentication

class Notifications implements Serializable {

    /**
     * Local authenicator class for email 
     */
     static private class SMTPAuth extends Authenticator {
        private PasswordAuthentication authentication

        /** 
        * Constructor method for authenticator
        * @param final String - userId
        * @param final String - pwd
        */
        public SMTPAuth(final String userId, final String pwd) {
             authentication = new PasswordAuthentication(userId, pwd)
        }

        /** 
        * Accessor method for authenticator
        * @return  PasswordAuthentication - authentication
        */
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
             return authentication;
        }
    }

    /**
     * Simple Email messaging routine. 
     *
     * @param  final String - mailServer
     * @param  final String - fromEmailAddress
     * @param  final String - toEmailAddress
     * @param  final String - subject
     * @param  final String - emailText
     * @return boolean
     * @throws MessagingException, Exception
     */
    static boolean sendEmail(final String mailServer,
                        final String fromEmailAddress,
                        final String toEmailAddress,
                        final String subjectTxt,
                        final String emailText) 
        throws MessagingException, Exception {
            return(sendEmail(mailServer,null,null,false,
                    fromEmailAddress,
                    toEmailAddress,
                    subjectTxt,
                    emailText,
                    null))
    }

    /**
     * Simple Email messaging routine. 
     *
     * @param  final String - mailServer
     * @param  final String - fromEmailAddress
     * @param  final String - toEmailAddress
     * @param  final String - subject
     * @param  final File - emailText
     * @param  final boolean - bHtml
     * @return boolean
     * @throws MessagingException, Exception, FileNotFoundException
     */
    static boolean sendEmail(final String mailServer,
                        final String fromEmailAddress,
                        final String toEmailAddress,
                        final String subjectTxt,
                        final File   emailText,
                        final boolean bHtml=false) 
        throws MessagingException, Exception, FileNotFoundException {
            return(sendEmail(mailServer,null,null,false,
                    fromEmailAddress,
                    toEmailAddress,
                    subjectTxt,
                    null, 
                    emailText,
                    bHtml))
    }

    /**
     * Simple Email messaging routine. This may or may not work depending on
     * how the email server is configured and what authentication model it uses.
     * If you need to modify it then it is likely it will be the SMTPAuth routine
     * that needs changing to cope with different security patterns.
     *
     * @param  final String - mailServer
     * @param  final String - serverUserName,
     * @param  final String - serverUserPwd,
     * @parms  final boolean - enableTLS
     * @param  final String - fromEmailAddress
     * @param  final String - toEmailAddress
     * @param  final String - subject
     * @param  final String - emailText
     * @param  final File   - emailFile
     * @param  final boolean - bHtml
     * @return boolean
     * @throws MessagingException, Exception, FileNotFoundException
     */
    static boolean sendEmail(final String mailServer,
                        final String serverUserName,
                        final String serverUserPwd,
                        final boolean enableTLS,
                        final String fromEmailAddress,
                        final String toEmailAddress,
                        final String subjectTxt,
                        final String emailText,
                        final File   emailFile,
                        final boolean bHtml=false) 
        throws MessagingException, Exception, FileNotFoundException {

        String portN = "25"
        String lmailServer = mailServer;
 
        if (emailFile != null) {
            if (!emailFile.exists() || !emailFile.canRead()) {
                throw new FileNotFoundException("Error: Email text file does not exist")
            }
        }

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

                String emailTxt = null 
                if (emailFile != null) {
                    emailTxt = Utilities.readFile(emailFile)
                } else {
                    emailTxt = new String(emailText)
                }             

                if (bHtml) {
                    setContent emailTxt, 'text/html'
                } else {
                    setText emailTxt
                }

                // Add recipients
                addRecipient( Message.RecipientType.TO, new InternetAddress( toEmailAddress ) )

                // Send the message
                Transport.send( message )

                // Free memory
                emailTxt = null
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
    static boolean messageSlackChannel(final String slackURI,
                                       final String ltext) 
    throws FileNotFoundException {
        
        // Use a JSON payload
        def payload = JsonOutput.toJson([text : ltext ])
        def curlStr = " -X POST --data-urlencode \'payload=${payload}\' ${slackURI}"
        // Use a direct payload
        //def curlStr = " -X POST -H \'Content-type: application/json\' --data \'{\"text\":"+
        //                "\""+ltext+"\"}\' " + slackURI

        File curlExe = null
        try {
            curlExe = Utilities.getExecutable("curl")
        } catch(FileNotFoundException e) {
            throw new FileNotFoundException("Error: Curl has not been located")            
        }
        
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

package org.devops;

import groovy.json.JsonOutput

/**
 * Notification routines for use with the framework
 */
class Notifications implements Serializable {
    /**
     * Simple Slack channel messaging routine
     * See https://api.slack.com/incoming-webhooks for how to setup 
     * channel incoming webhooks
     * @return boolean
     */
    static boolean messageSlackChannel(String slackURI,
                                       String text) {
        
        // Use a JSON payload
        def payload = JsonOutput.toJson([text : text ])
        def cmdStr = "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURI}"

        // Use a direct payload
        //def cmdStr = "curl -X POST -H \'Content-type: application/json\' --data \'{\"text\":"+
        //                "\""+text+"\"}\' " + slackURI
        
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

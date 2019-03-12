package org.devops;

import groovy.json.JsonOutput

/**
 * Notification routines for use with the framework
 */
class Notifications implements Serializable {
    /**
     * Slack channel messaging routine
     *
     * @return bool
     */
    static def messageSlackChannel(String slackURI,
                                   String channel,
                                   String userName,
                                   String text) {
        def payload = JsonOutput.toJson([text      : text,
                                         channel   : channel,
                                         username  : userName,
                                         icon_emoji: ":jenkins:"])

        def returnStr
        def cmdStr = "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURI}"
        int retStat = Utilities.runCmd()
        if (retStat>0) {
            println returnStr
        } 
    }
 }

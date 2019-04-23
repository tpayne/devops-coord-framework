/**
 * Pipeline plugin extension for tasks
 */
package org.devops.framework.plugin;

import jenkins.MasterToSlaveFileCallable;

/**
 * Base class for all Callable tasks.
 * <p>
 * Currently we are using the SECURITY-144-compat library, which may not be a great idea
 * <p>
 * See also https://wiki.jenkins-ci.org/display/JENKINS/Slave+To+Master+Access+Control.
 */
abstract class BaseCallable extends MasterToSlaveFileCallable<Boolean> {
    /* This abstract class exists temporarily to make it easier to change the base class of all Callable tasks in this plugin. */
}

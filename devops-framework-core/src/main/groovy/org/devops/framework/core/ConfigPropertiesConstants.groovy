/**
 * Constant definitions for use with the framework
 */
package org.devops.framework.core;

import java.util.Locale

public class ConfigPropertiesConstants implements Serializable {

	// Generic properties...
    public static final Locale ROOT_LOCALE = Locale.US
    public static final String XML = "XML"
    public static final String JSON = "JSON"
    
	// SCM constants...
	public static final String SCMGIT = "SCMGIT"
	public static final String SCMSVN = "SCMSVN"

	// Container constants...
	public static final String DOCKER = "DOCKER"

	// Repository constants...
	public static final String FILE = "FILEREPO"
	public static final String ARTIFACTORY = "ARTIFACTORYREPO"
	public static final String NEXUS = "NEXUSREPO"

	// Provisioning constants...
	public static final String ANSIBLE = "ANSIBLE"
	//
	// Puppet and Chef require root access which is too dangerous for this framework.
	// As of now, they will not be supported, unless a great need is required.
	//
	public static final String CHEF = "CHEF"
	public static final String PUPPET = "PUPPET"

    private static final long serialVersionUID = 1L;

}


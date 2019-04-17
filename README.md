Devops Framework
================

````
Framework Status: Ready for use
````

Contents
========

* [Overview](#overview)
* [Framework Objects](#framework-objects)
* [Service Objects](#service-objects)
* [So, why have a DevOps framework?](#so-why-have-a-devops-framework)
	* [The Problem Statement](#the-problem-statement)
	* [The Implementation](#the-implementation)
* [Framework Process Flows](#framework-process-flows)
	* [The Overall Process Flow](#the-overall-process-flow)
	* [The CI Process Flow](#the-ci-process-flow)
	* [The CD Process Flow](#the-cd-process-flow)
* [Jenkins and Compiler Support](#jenkins-and-compiler-support)
* [Framework Classes](#framework-classes)
	* [Build Class](#build-class)
	* [Deploy Class](#deploy-class)
	* [Test Class](#test-class)
	* [Integration Class](#integration-class)
	* [ReleaseCandidate Class](#releasecandidate-class)
	* [CIFramework Class](#ciframework-class)
	* [CDFramework Class](#cdframework-class)
* [How to Install](#how-to-install)
* [Service Classes](#service-classes)
	* [Utilities](#utilities)
	* [SCM](#scm)
	* [Notifications](#notifications)
	* [Container](#container)
	* [Repository](#repository)
	* [ComponentManifest](#componentmanifest)
	* [Provision](#provision)
* [How to Use](#how-to-use)
* [Framework Documentation](#framework-documentation)
* [Class Usage Examples and Running Unit-tests](#class-usage-examples-and-running-unit-tests)
	* [Running the unit-tests](#running-the-unit-tests)
* [Pipeline Examples](#pipeline-examples)
	* [Screenshots](#screenshots)
* [DSL Plugin](#dsl-plugin)
* [Liability Warning](#liability-warning)
* [Licensing](#licensing)
* [Known Issues](#known-issues)

Overview
========
This repository delivers a framework that was created to help manage the DevOps process for releases that involve a 
large number of components and/or that need a standard process for managing components and how they are delivered.

The framework provides a set of objects intended to manage each major phase of a CI/CD pipeline into which custom logic
can be placed that will then be execute at the correct place, so removing the need for linking jobs together or building
custom flow logic into your pipeline.

For example, a `Build` object is present that allows you to register callbacks for operations like: -
- Cleaning work areas
- Getting code
- Running a build process
- Running a unit-test process
- Running a static code analysis process etc.

All you need to do is register callbacks to do your own specific build logic or unit-test process etc. and the framework
will manage all the running of that process and the coordination with other steps.

The framework also provides a small set of "most commonly used functions" service library to help "kick-start" your CI/CD
process if needed. These functions include operations like: -
- Slack notification
- Email
- Container services
- SCM services
- various file based services

These can be used (if needed) from Jenkins or command line.

Framework Plugins for Jenkins
-----------------------------
The framework provides two different plugins for Jenkins. The first of these is a shared library which is used to provide
the CI/CD framework (and service) objects discussed below. The second is a set of DSL extensions which - if installed 
as a Jenkins plugin - provides a set of DSL extensions which can be used in a Jenkins pipeline syntax. These are also 
discussed below.

Framework Objects
=================
The framework is split into two main pipelines: -
- The CI process for individual products/components 
- The CD process for integrated releases

The CI process is used to manage the build, deploy and test processes for each individual component and the CD process 
is used to manage the integration and release phases.

The main interface objects for controlling these phases are shown below: -

>| Class | Description | 
>| ----- | ----------- |
>| `CIFramework` | Which is used to control your CI process (Build, Deploy and Test process) |
>| `CDFramework` | Which is used to control your CD process (Integration and Release candidate verification process)|

The `CIFramework` interface is split into three main phases - each of which are controlled by a specific object: -

>| Class | Description | 
>| ----- | ----------- |
>| `Build` | Which is used to control your build process |
>| `Deploy` | Which is used to control your deployment process |
>| `Test` | Which is used to control your test process |

Likewise, the `CDFramework` interface is split into two main phases - each of which are controlled by a specific object
as well: -

>| Class | Description | 
>| ----- | ----------- |
>| `Integration` | Which is used to control your integration process |
>| `ReleaseCandidate` | Which is used to control your Release candidate process |

These interface objects allow you to register the callbacks that you want to use for each main process - like build - 
and then the framework will take care of running these callbacks in the correct flow and managing the coordination between
the steps.

Service Objects
===============
The framework also provides a number of "service" objects aimed at providing commonly required DevOps capabilities like 
cloning code, running Docker images, sending emails etc. These services are provided to help make a DevOps implementation
easier to do by providing working services and utilities that are commonly required.

>| Class | Description | 
>| ----- | ----------- |
>| `Notifications` | For email and [Slack](https://slack.com) IRC messaging | 
>| `SCM` | For GIT and SVN SCM function(s) - currently only supporting cloning |
>| `Container` | For various container management commands - currently only supporting [Docker](https://www.docker.com) |
>| `Repository` | For pushing and pulling files from repos - currently only file, [Artifactory](https://jfrog.com/artifactory/) and [Nexus OSS v2](https://www.sonatype.com/nexus-repository-oss) is supported |
>| `ComponentManifest` | For maintaining your manifest of integrated components. This is the list of component names, versions, status and locations that you register with the manifest. These can then be accessed later on for usage in other testing or release processes |
>| `Provision` | For running provisioning scripts |

So, why have a DevOps framework
===============================
As mentioned above, the framework is primarily provided as a way of allowing you to control - in a standard way - 
how products are on-boarded into CI/CD and how that process is then managed. It does this in two ways. Firstly,
by providing a standard framework into which product-teams can implement their CI processes, so ensuring everyone
is using the same guidelines. Secondly, by having the framework manage the process flow of the overall steps and
the coordination between them.

The Problem Statement
---------------------
As an organization with many products or different components in place, transferring these all over to a CI/CD process
may be a bit daunting - especially if they each use different types of technologies or build processes. Without a 
framework in place, those products may each end up implementing their own CI/CD process or technology, making the 
management and integration of those products a big challenge.

Integration of products and the management of that process is also a big problematic area. A release that is made up
of many products means that you need to track which versions of products have been released, which versions are
in test and how they can work with each other. If, for example, your release if made up of 20 different products
managing the status and dependencies of those products could be a large task. This is something the framework can
do for you by managing the list of components and how they are promoted through the release pipeline.

Also, by extending the base framework, you can implement specific customisations that you might want to apply
to every product - like security scans - that cannot be "forgotten" or missed when teams implement their own CD
processes.

The Implementation
------------------
The framework has been implemented using Groovy/Java based callbacks. This means that you can easily integrate it into
Jenkins pipelines as a shared library or use it standalone. The callback mechanism means that virtually any CLI or 
API based build technology can be run using it, so it is totally flexible regarding the CI/CD processes it can wrap. 

Examples of both standalone and Jenkins pipeline implementations are provided.

Framework Process Flows
=======================
The process flows provided by the framework are described below.

The Overall Process Flow
------------------------
The overall process flow is that used to implement the CI/CD pipeline. The picture below shows how this works.

>![Overall Process flow](https://github.com/tpayne/devops-framework/blob/master/devops-framework-pipeline/src/main/resources/OverallFrameworkFlow.jpg)

Individual products use a standard CI build, deploy and test process to verify their changes are working as expected. 
These are then promoted to the component manifest for further testing in an integrated flow.

In the context of a release end to end flow, the framework works as shown in the picture below. The CI process is used
for feature development and the CD process is used for the release verification. Quality gates are implemented using 
the test processes to determine if components should be added to the component manifest and to determine if the release
candidate can be promoted to production.

>![E2E Process flow](https://github.com/tpayne/devops-framework/blob/master/devops-framework-pipeline/src/main/resources/DevOpsE2EOverview.jpg)


The CI Process Flow
-------------------
The CI process flow controls the component or product-level build, deploy and test process. This pipeline works as
follows.

>![CI Process flow](https://github.com/tpayne/devops-framework/blob/master/devops-framework-pipeline/src/main/resources/CIFrameworkProcessFlow.jpg)

When a product has finished with the testing process, then it will be promoted to the component manifest to make it a
candidate for integration testing.

The CD Process Flow
-------------------
When an update is detected to the component manifest, this kicks off the CD flow which then runs the integration and other
release verification processes to ensure that the release stack is ready for deployment to production. This pipeline flow 
is shown below.

>![CD Process flow](https://github.com/tpayne/devops-framework/blob/master/devops-framework-pipeline/src/main/resources/CDFrameworkProcessFlow.jpg)

Jenkins and Compiler Support
============================
The framework will only run with the following: -
- JDK 8+ (javac 1.8.0_201+)
- Jenkis LTS 2.164.1+

You can downgrade the versions by modifying the `pom.xml` file, but you will also need to downgrade any dependent plugins.

The framework has only been compiled and tested using the documented versions above (and the latest versions of Jenkins 2.168).

Framework Classes
=================
The following are the main framework classes and the methods that they have.

Build Class
-----------
The Build class is provided to control your build process and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `prepareWorkArea()`| A callout provided to help prepare your workarea for a build |
>| `getCode()`| A callout provided to help you pull your code |
>| `preBuild()`| A callout provided to help prepare for a build |
>| `runBuild()`| A callout provided to help run a build process |
>| `postBuild()`| A callout provided to help run a post build process |
>| `runUnitTests()`| A callout provided to help run any unit tests |
>| `evaluateUnitTests()`| A callout provided to help evaluate any unit tests results |
>| `runStaticCodeTests()`| A callout provided to help run any static code analysis process |
>| `evaluateStaticCodeTests()`| A callout provided to help evaluate any analysis results |
>| `bakeImage()`| A callout provided to help bake an image |
>| `uploadAssets()`| A callout provided to help upload any assets created during the build |
>| `logResults()`| A callout provided to help log any results | 
>| `promote()`| A callout provided to help promote a build to the next phase | 
>| `runPipeline()`| Run the pipeline |

All callbacks are run in the above order, no matter how your register them.

Deploy Class
------------
The Deploy class is provided to control your deploy process and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `prepareForDeploy()` | A callout provided to help prepare your environment for a deploy |
>| `getAssets()` | A callout provided to help you pull your deploy assets |
>| `preDeploy()` | A callout provided to help prepare for a deploy |
>| `runDeploy()` | A callout provided to help run a deploy process |
>| `postDeploy()` | A callout provided to help run a post deploy process |
>| `runSmokeTests()` | A callout provided to help run any smoke tests |
>| `evaluateSmokeTests()` | A callout provided to help evaluate any smoke tests results |
>| `logResults()` | A callout provided to help log any results |
>| `promote()` | A callout provided to help promote a deploy to the next phase | 
>| `runPipeline()` | Run the pipeline |

All callbacks are run in the above order, no matter how your register them.

Test Class
----------
The Test class is provided to control your test process and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `prepareForTest()` | A callout provided to help prepare your environment for testing |
>| `getAssets()` | A callout provided to help you pull your test assets |
>| `preTest()` | A callout provided to help prepare for a test |
>| `runTest()` | A callout provided to help run a test process |
>| `postTest()` | A callout provided to help run a post test process |
>| `evaluateTests()` | A callout provided to help evaluate any tests results |
>| `logResults()` | A callout provided to help log any results | 
>| `promote()` | A callout provided to help promote a test to the next phase | 
>| `runPipeline()` | Run the pipeline |

All callbacks are run in the above order, no matter how your register them.

Integration Class
-----------------
The Integration class is provided to control your integration process and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `getComponentList()` | A callout provided to help get the component list for processing |
>| `prepareForDeploy()` | A callout provided to help prepare an environment for use |
>| `getDeployAssets()` | A callout provided to help pull your deployment assets |
>| `preDeploy()` | A callout provided to help prepare for the deploy |
>| `runDeploy()` | A callout provided to help run the deploy |
>| `postDeploy()` | A callout provided to help perform any post deployment actions |
>| `runSmokeTests()` | A callout provided to help run smoke tests |
>| `evaluateSmokeTests()` | A callout provided to help evaluate the smoke test results |
>| `logDeployResults()` | A callout provided to help log the deployment results |
>| `prepareForTest()` | A callout provided to help prepare for testing |
>| `getTestAssets()` | A callout provided to help pull test assets |
>| `preTest()` | A callout provided to help prepare for the testing |
>| `runTests()` | A callout provided to help run the testing |
>| `postTest()` | A callout provided to help perform any post testing activities |
>| `evaluateTestResults()` | A callout provided to help evaluate test results |
>| `logTestResults()` | A callout provided to help log test results |
>| `promote()` | A callout provided to help promote a test to the next phase | 
>| `runPipeline()` | Run the pipeline |

All callbacks are run in the above order, no matter how your register them.

ReleaseCandidate Class
----------------------
The ReleaseCandidate class is provided to control your release process and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `getComponentList()` | A callout provided to help get the component list for processing |
>| `prepareForDeploy()` | A callout provided to help prepare an environment for use |
>| `getDeployAssets()` | A callout provided to help pull your deployment assets |
>| `preDeploy()` | A callout provided to help prepare for the deploy |
>| `runDeploy()` | A callout provided to help run the deploy |
>| `postDeploy()` | A callout provided to help perform any post deployment actions |
>| `runSmokeTests()` | A callout provided to help run smoke tests |
>| `evaluateSmokeTests()` | A callout provided to help evaluate the smoke test results |
>| `logDeployResults()` | A callout provided to help log the deployment results |
>| `prepareForTest()` | A callout provided to help prepare for testing |
>| `getTestAssets()` | A callout provided to help pull test assets |
>| `preTest()` | A callout provided to help prepare for the testing |
>| `runTests()` | A callout provided to help run the testing |
>| `postTest()` | A callout provided to help perform any post testing activities |
>| `evaluateTestResults()` | A callout provided to help evaluate test results |
>| `logTestResults()` | A callout provided to help log test results |
>| `rollback()` | A callout provided to help perform a rollback if required |
>| `finish()` | A callout provided to help perform any final actions if needed |
>| `runPipeline()` | Run the pipeline |

All callbacks are run in the above order, no matter how your register them.

In addition, every callback takes the following parameters: -

>| Parameter | Description | 
>| --------- | ----------- |
>| `body:{}` | Used to specify the Groovy code to run the process |
>| `finalHandler:{}` | Used to specify any Groovy code which will be invoked after the process has run |
>| `exceptionHandler:{}` | Used to specify any Groovy code which will be invoked if any exception occurs |

CIFramework Class
-----------------
The CIFramework class is provided to control your CI process and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `setBuild()` | A callout provided to set your build object into the CI framework for processing |
>| `getBuild()` | A callout provided to get your build object from the CI framework |
>| `setDeploy()` | A callout provided to set your deploy object into the CI framework for processing |
>| `getDeploy()` | A callout provided to get your deploy object from the CI framework |
>| `setTest()` | A callout provided to set your test object into the CI framework for processing |
>| `getTest()` | A callout provided to get your test object from the CI framework |
>| `launchCI()` | A callout provided to run the CI process |

CDFramework Class
-----------------
The CDFramework class is provided to control your CD process and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `setIntegration()` | A callout provided to set your integration object into the CD framework for processing |
>| `getIntegration()` | A callout provided to get your integration object from the CD framework |
>| `setReleaseCandidate()` | A callout provided to set your release candidate object into the CD framework for processing |
>| `getReleaseCandidate()` | A callout provided to get your release candidate object from the CD framework |
>| `launchCD()` | A callout provided to run the CD process |

How to Install
==============
To install this [Jenkins share library](https://jenkins.io/doc/book/pipeline/shared-libraries), do the following...

	1) git clone https://github.com/tpayne/devops-framework.git
	2) cd devops-framework
	3) mvn package
	4) cd devops-framework-pipeline/target/
	5) Unzip devops-framework-pipeline-dsl-pack.zip into a working directory
	6) Use the instructions in the Jenkins Wiki (https://jenkins.io/doc/book/pipeline/shared-libraries/#global-shared-libraries) to install the shared library into your Jenkins system

You will need to configure the unit-tests as discussed below and install/configure `Docker` and `Ansible` as they are used
during the test process. If you want to just build the packages without doing any unit-tests, then you can do this via...

	mvn clean package -Dmaven.test.skip=true
	
This will build the packages only.

Service Classes
===============
The following are the main service classes and the methods that they have.

Utilities
---------
This class provides various useful utilities that are used and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `isUnix()` | Used to detect UNIX based OS |
>| `isWindows()` | Used to detect Windows based OS |
>| `mapProperties()` | Used to map a properties file intp a Map |
>| `getDefaultProperties()` | Used to read any default properties that might have been setup for the framework to use |
>| `readAllBytes()` | Used to read a file into memory as an array of bytes. Mostly for binary files |
>| `readFile()` | Used to read a text file into memory as a string |
>| `writeFile()` | Used to write strings or bytes to a file |
>| `getExecutable()` | Used to locate an executable file in the path and return a File object to it |
>| `runCmd()` | Used to run a shell command and trap any output if wanted |
>| `getTmpDir()` | Used to return a File object to the temporary directory setup on the machine |
>| `deleteDirs()` | Used to emulate rm -fr  |
>| `copyFile()` | Used to copy files  |
>| `copyDirectories()` | Used to copy directories  |
>| `getFileExt()` | Used to get a file extension  |
>| `calcFileMD5()` | Used to get a calculate a file's MD5 checksum  |

SCM
---
This class provides all the SCM related support and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `scmClone()` | Used for cloning code from supported SCM repos like Git or SVN |

Notifications
-------------
This class provides notification related functionality and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `sendMail()` | Used for sending email text or HTML/text notifications |
>| `messageSlackChannel()` | Used for sending notifications to Slack |
	
Container
---------
This class provides container related functionality and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `createContainerRegistry()` | Used to create a container register for supported platforms like Docker |
>| `pushContainer()` | Used to push an image to a container register |
>| `tagContainer()` | Used to tag a container for future use |
>| `deleteContainerRegistry()` | Used to delete a container register |
>| `pullContainerImage()` | Used to pull a container image from somewhere |
>| `deleteContainerImage()` | Used to delete a container image |
>| `runContainer()` | Used to run a container |
>| `buildContainer()` | Used to build or bake a container from a given build file |

Repository
----------
This class provides repository related functionality and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `pullAssetFromRepo()` | Used to pull an asset from a repo |
>| `pushAssetToRepo()` | Used to push an asset to a repo |
	

ComponentManifest
-----------------
This class provides repository related functionality for maintaining your component manifest list (used in releases) and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `isValid()` | Used to check the manifest is valid |
>| `getRepo()` | Used to get the repo file details |
>| `setRepo()` | Used to set the repo file details |
>| `getCommitComment()` | Used to get the last commit comment |
>| `getCommitter()` | Used to get the last committer |
>| `getCommitDate()` | Used to get the last commit date |
>| `getManifestVersion()` | Used to get the manifest version |
>| `setManifestVersion()` | Used to set the manifest version |
>| `getManifestStatus()` | Used to get the manifest status |
>| `setManifestStatus()` | Used to set the manifest status |
>| `getComponentList()` | Used to get the list of components registered in the manifest |
>| `getComponent()` | Used to get the details for an individual component |
>| `addComponent()` | Used to add a component to the manifest |
>| `updateComponent()` | Used to update the details for an individual component registered in the manifest |
>| `removeComponent()` | Used to remove a component from the manifest |
>| `convertManifestToJSON()` | Used to convert the component manifest to a JSON string |
>| `commit()` | Used to commit the manifest details to the repo file |

The manifest file (by default) is a JSON file stored in the repository file. This manifest file looks like this: -

	{
	  "commitComment": "Committed version 30 by alexgray at 13:20:12 27/03/2019",
	  "compList": {
	    "gitplugin": {
	      "componentVersion": "1553692812066",
	      "componentName": "gitplugin",
	      "componentLocation": "/Volumes/WorkDisk/tmp/Repos/github.hpi.1553692812066",
	      "componentStatus": "Integration Test"
	    },
	    "dimensionsscm": {
	      "componentVersion": "1553692305487",
	      "componentName": "dimensionsscm",
	      "componentLocation": "/Volumes/WorkDisk/tmp/Repos/dimensionsscm.hpi.1553692305487",
	      "componentStatus": "Integration Test"
	    },
	    "JavaAppCICD": {
	      "componentVersion": "1553609424273",
	      "componentName": "JavaAppCICD",
	      "componentLocation": "/Volumes/WorkDisk/tmp/Repos/jpetstore.war.1553609424273",
	      "componentStatus": "Integration Test"
	    }
	  },
	  "committer": "alexgray",
	  "status": "Integration Test",
	  "version": "30",
	  "commitUTCDate": 1553692812367
	}

There are a number of parts to it as follows: -

>| Key | Description | 
>| --- | ----------- |
>| `commitComment` | Used to hold the last commit comment |
>| `compList` | Used to hold the registered components |
>| `committer` | Used to register who did the last commit |
>| `status` | Used to hold the current status |
>| `version` | Used to hold the current version |
>| `commitUTCDate` | Used to hold the last commit date/time in UTC format |

The `compList` is formatted as: -

>| Key | Description | 
>| --- | ----------- |
>| `<componentKey>` | Used to hold the component key, e.g. JavaAppCICD |
>| `componentVersion` | Used to hold the registered component version |
>| `componentName` | Used to hold the registered component name |
>| `componentLocation` | Used to hold the registered component location, i.e. where it is stored |
>| `componentStatus` | Used to hold the registered component status |

The values of these keys are free text and are set by the appropriate `set...()` methods. What formats
you use are up to you.

Further keys can be added like MD5 checksum by modifying the `ComponentManifest.groovy` file as needed.

Provision
----------
This class provides provisioning related functionality and has the following methods: -

>| Method | Description | 
>| ------ | ----------- |
>| `runPlaybook()` | Used to run a play/runbook (Ansible only) |

How to Use
==========
The shared library works by providing controller classes that you can use in a pipeline job. As such, you need 
to create a pipeline job and register the callbacks that you want to use.

For example, a sample pipeline might look like

	// Sample pipeline...
	@Library('devops-framework')
	import org.devops.*
	
	// Include the core services as I want to use them as well...
	@GrabResolver(name='devops-core', root='file:///Volumes/WorkDisk/GROOVY/devops-framework/devops-framework-core/target/lib/')
	@Grab('org.devops.framework.core:devops-framework-core:0.0.1-SNAPSHOT')
	
	// Import the framework core classes...
	import org.devops.framework.core.*

	def config = [
	    property1: 'value1',
	    property2: 'value2',
	    property3: 'value3'
	]

	def bld = new org.devops.Build(this, config)

	node {    
	        File fetchDir = new File("/Volumes/WorkDisk/tmp/BuildJobs/")
		String scmURI = "https://github.com/sourcerepo/JavaAppCICD.git"
		String slackURI = "https://hooks.slack.com/services/SDJSHETEJDKRJFHFIDLODJFF"

		// Register my build process...    
		bld.runBuild(body:{
		    // This is a Maven which will compile and run all the unit tests as
		    // part of the process, so we do not need all the other build callbacks...
		    println ">Run build...<"
		    sh(script: "cd ${fetchDir.getAbsolutePath()}; chmod +rx ./mvnw; ./mvnw -q package")
		}, finalHandler:{ println ">Build Job Done<" })

		// Register my get code callback...
		bld.getCode(body:{
		    println ">Get code - cloning from GIT...<"
		    StringBuffer outputStr = new StringBuffer()
		    boolean retStat = SCM.scmClone(ConfigPropertiesConstants.SCMGIT,
						    scmURI,fetchDir,outputStr)
		    if (retStat) {
			println "Code clone worked"
			Notifications.messageSlackChannel(slackURI,"${label}: Clone worked")
		    } else {
			println "Code clone failed"
			println outputStr.toString()
			Notifications.messageSlackChannel(slackURI,"${label}: Code clone failed - "+outputStr.toString())
		    }  
		    outputStr = null
		}) 

		// Register my bake callback...
		bld.bakeImage(body:{ println ">Run my bake<" })

		// Register a prepareWorkArea callback...
		bld.prepareWorkArea(body:{
		    println ">Prepare Work Area - clean my files up...<"
		    if (fetchDir.exists()) {
			Utilities.deleteDirs(fetchDir)
		    }
		    fetchDir.mkdirs()
		})

		// Run my pipeline...
		bld.runPipeline()
	}

This will run the `prepareWorkArea()`, `getCode()`, `runBuild()` and `bakeImage()` callbacks in this
order.
	
Framework Documentation
=======================
The framework code comes with some limited JavaDoc present which can be used to generate API documentation
by using the command

	% mvn gplus:generateStubs gplus:groovydoc

This will generate JavaDoc style comments in the `target/` directory

Class Usage Examples and Running Unit-tests
===========================================
Virtually all the class functions documented above have example [unit/functional tests](https://github.com/tpayne/devops-framework/tree/master/src/test/java/org/devops) which are run in the Maven test phase.

You can use these as examples to show you how to use the classes. 

Running the unit-tests
----------------------
The values used in the unit-tests for things like slack channels and GitHub repos are currently hardcoded to working values.
However, these will need to be changed if you wish to run the tests. These values are located in a properties file 
[`unitTest.properties`](https://github.com/tpayne/devops-framework/blob/master/src/test/resources/unitTest.properties). 

These will need to be modified to values more specific for you otherwise some of the tests may fail.

(Note - The tests have very little documentation imbedded in them currently. This will be added slowly).

Pipeline Examples
=================
Examples of pipelines created using the framework can be found in the [`examples`](https://github.com/tpayne/devops-framework/tree/master/examples) directory.

>| Example | Description | 
>| ------ | ----------- |
>| [`buildJenkinsPluginPipeline.txt`](https://github.com/tpayne/devops-framework/blob/master/examples/buildJenkinsPluginPipeline.txt) | Example build pipeline that fetches code, builds it, commits it to a repo, then updates a component manifest with the new version |
>| [`buildJenkinsPluginPipelineWithSlackNotif.txt`](https://github.com/tpayne/devops-framework/blob/master/examples/buildJenkinsPluginPipelineWithSlackNotif.txt) | As `buildJenkinsPluginPipeline.txt`, but also includes Slack channel notifications and shows how the component manifest can track many different components |
>| [`CIJenkinsPluginPipeline.txt`](https://github.com/tpayne/devops-framework/blob/master/examples/CIJenkinsPluginPipeline.txt) | Example build pipeline that fetches code, builds it, commits it to a repo, creates a baked Docker image, then updates a component manifest with the new version. It is implemented using the CIFramework object |
>| [`IntegrationTestPlugin.txt`](https://github.com/tpayne/devops-framework/blob/master/examples/IntegrationTestPlugin.txt) | Example integration pipeline that fetches binaries from the component manifest, bakes a Docker image using them, then tests the container. It is implemented using the Integration object |

Screenshots
-----------
Some screenshots of the various jobs are shown below

**Build test case**

The following is an example run using the Build object.

>![Build Test case](https://github.com/tpayne/devops-framework/blob/master/devops-framework-pipeline/examples/BuildTestCase.jpg)

**CI Framework test case**

The following is an example run using the CIFramework object.

>![CIFramework Test case](https://github.com/tpayne/devops-framework/blob/master/devops-framework-pipeline/examples/CIFrameworkTestCase.jpg)

**Integration test case**

The following is an example run using the Integration object.

>![Integration Test case](https://github.com/tpayne/devops-framework/blob/master/devops-framework-pipeline/examples/IntegrationTestCase.jpg)

DSL Plugin
==========
As well as the framework objects above, the framework also provides a HPI DSL plugin which when loaded into Jenkins (as a
normal plugin) adds a number of service steps which can be used in your Jenkins pipeline to get your CI/CD process
off the ground. These service steps are as follows.

>| Step DSL | Description | 
>| -------- | ----------- | 
>| `devOpsFrameworkBuildContainerStep` | This step provides a way of building container images (Docker) | 
>| `devOpsFrameworkPullContainerStep` | This step provides a way of pulling container images (Docker) | 
>| `devOpsFrameworkRunContainerStep` | This step provides a way of running container images (Docker) | 
>| `devOpsFrameworkRmContainerStep` | This step provides a way of removing container images (Docker) | 
>| `devOpsFrameworkTagContainerStep` | This step provides a way of tagging container images (Docker) | 
>| `devOpsFrameworkSvnCloneStep` | This step provides a way of cloning a branch from a SVN repo | 
>| `devOpsFrameworkGitCloneStep` | This step provides a way of cloning a branch from a GIT repo | 
>| `devOpsFrameworkFilePushStep` | This step provides a way of pushing an asset to a file-based repo | 
>| `devOpsFrameworkFilePullStep` | This step provides a way of pulling an asset to a file-based repo | 
>| `devOpsFrameworkNexusPushStep` | This step provides a way of pushing an asset to a Nexus-based repo | 
>| `devOpsFrameworkNexusPullStep` | This step provides a way of pulling an asset to a Nexus-based repo | 
>| `devOpsFrameworkArtifactoryPushStep` | This step provides a way of pushing an asset to a Artifactory-based repo | 
>| `devOpsFrameworkArtifactoryPullStep` | This step provides a way of pulling an asset to a Artifactory-based repo | 
>| `devOpsFrameworkAnsibleRunbookStep` | This step provides a way of running an Ansible playbook |

Plugin Syntax
-------------
The above steps have the following syntax: -

###### Building a container image

_Name:_ `devOpsFrameworkBuildContainerStep`

_Purpose:_ This step is for building a container image from a makefile

_Example:_

	devOpsFrameworkBuildContainerStep buildDirectory: '/tmp', 
		containerFile: '/tmp/Dockerfile', containerName: 'tomcat'

_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `buildDirectory` | `'<someDirectory>'` | Mandatory parameter to specify which directory to run the build in |
| `containerFile` | `'<fileName>'` | Optional parameter to specify which build file to use. The default is Dockerfile |
| `containerName` | `'<containerName>'` | Mandatory parameter to specify the container name to build |

###### Pulling a container image

_Name:_ `devOpsFrameworkPullContainerStep`

_Purpose:_ This step is for pulling a container image from a repo

_Example:_

	devOpsFrameworkPullContainerStep containerName: 'tomcat'	

_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `containerName` | `'<containerName>'` | Mandatory parameter to specify the container name to pull |

###### Running a container image

_Name:_ `devOpsFrameworkRunContainerStep`

_Purpose:_ This step is for running a container image with a command

_Example:_

	devOpsFrameworkRunContainerStep cmdStr: 'df -H', containerName: 'tomcat'	

_Parameters:_ 

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `containerName` | `'<containerName>'` | Mandatory parameter to specify the container name to run |
| `cmdStr` | `'<commandString>'` | Mandatory parameter to specify the command string to run |


###### Removing a container image

_Name:_ `devOpsFrameworkRmContainerStep`

_Purpose:_ This step is for deleting a container image from the local repo

_Example:_

	devOpsFrameworkRmContainerStep containerName: 'tomcat', force: true

_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `containerName` | `'<containerName>'` | Mandatory parameter to specify the container name to delete |
| `force` | `'<true|false>'` | Optional parameter to force the container to be removed |

###### Tagging a container image

_Name:_ `devOpsFrameworkTagContainerStep`

_Purpose:_ This step is for tagging a container image 

_Example:_

	devOpsFrameworkTagContainerStep containerName: 'tomcat', targetName: 'taggedTomcat'	

_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `containerName` | `'<containerName>'` | Mandatory parameter to specify the container name to tag |
| `targetName` | `'<targetName>'` | Mandatory parameter to specify the target tag |
	
###### Clone SVN Repo

_Name:_ `devOpsFrameworkSvnCloneStep`

_Purpose:_ This step is for cloning the code from a SVN repository 

_Example:_

	devOpsFrameworkSvnCloneStep repoName: 'http://svnrepo.net/mycode/', targetDir: '/tmp/', 
		userName: 'user1', userPwd: 'user1Pwd'

_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `repoName` | `'<repoName>'` | Mandatory parameter to specify the repo to clone|
| `targetDir` | `'<someDirectory>'` | Optional parameter to specify the target directory to use |
| `userName` | `'<userName>'` | Optional parameter to specify a valid SCM username |
| `userPwd` | `'<password>'` | Optional parameter to specify a valid SCM user password |

###### Clone GIT Repo

_Name:_ `devOpsFrameworkGitCloneStep`

_Purpose:_ This step is for cloning the code from a GIT repository 

_Example:_

	devOpsFrameworkGitCloneStep repoName: 'https://github.com/myuser/myrepo.git', 
		targetDir: '/tmp/', userName: 'user1', userPwd: 'user1Pwd'

_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `repoName` | `'<repoName>'` | Mandatory parameter to specify the repo to clone |
| `targetDir` | `'<someDirectory>'` | Optional parameter to specify the target directory to use |
| `userName` | `'<userName>'` | Optional parameter to specify a valid SCM username |
| `userPwd` | `'<password>'` | Optional parameter to specify a valid SCM user password |

###### Pull File Repo

_Name:_ `devOpsFrameworkFilePullStep`

_Purpose:_ This step is for pulling files from a file-based repo 

_Example:_

	devOpsFrameworkFilePullStep srcFile: '/Volumes/FileRepo/ASD/Releases/latest/asd.hpi', 
		targetFile: '/Volumes/WorkArea/Compon_ASD/target/'
		
_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `srcFile` | `'<fileName>'` | Mandatory parameter to specify a file or directory to pull |
| `targetFile` | `'<fileName>'` | Mandatory parameter to specify the target |

###### Push File Repo

_Name:_ `devOpsFrameworkFilePushStep`

_Purpose:_ This step is for pushing files to a file-based repo 

_Example:_

	devOpsFrameworkFilePushStep srcFile: '/Volumes/WorkArea/Compon_ASD/target/asd.hpi', 
		targetFile: '/Volumes/FileRepo/ASD/Releases/latest/'
		
_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `srcFile` | `'<fileName>'` | Mandatory parameter to specify a file or directory to push |
| `targetFile` | `'<fileName>'` | Mandatory parameter to specify the target |

###### Pull Nexus Repo

_Name:_ `devOpsFrameworkNexusPullStep`

_Purpose:_ This step is for pulling files from a Nexus-based repo 

_Example:_

    devOpsFrameworkNexusPullStep srcFile: 'http://localhost:8081/nexus/sites/generic-local/comp/Asd.war', 
        targetFile: '/Volumes/WorkDisk/deploy/ASD.war', 
        userName: 'admin', userPwd: 'admin123'
	
_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `srcFile` | `'<fileName>'` | Mandatory parameter to specify a file to pull from Nexus |
| `targetFile` | `'<fileName>'` | Mandatory parameter to specify the target |
| `userName` | `'<userName>'` | Optional parameter to specify a valid repo username |
| `userPwd` | `'<password>'` | Optional parameter to specify a valid repo user password |

###### Push Nexus Repo

_Name:_ `devOpsFrameworkNexusPushStep`

_Purpose:_ This step is for pushing files to a Nexus-based repo 

_Example:_

    devOpsFrameworkNexusPushStep srcFile: '/Volumes/WorkArea/Compon_ASD/target/asd.war',
        targetFile: 'http://localhost:8081/nexus/sites/generic-local/comp/Asd.war',
        userName: 'admin', userPwd: 'admin123'
		
_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `srcFile` | `'<fileName>'` | Mandatory parameter to specify the file to push to Nexus |
| `targetFile` | `'<fileName>'` | Mandatory parameter to specify the target |
| `userName` | `'<userName>'` | Optional parameter to specify a valid repo username |
| `userPwd` | `'<password>'` | Optional parameter to specify a valid repo user password |

###### Pull Artifactory Repo

_Name:_ `devOpsFrameworkArtifactoryPullStep`

_Purpose:_ This step is for pulling files from a Artifactory-based repo 

_Example:_

    devOpsFrameworkArtifactoryPullStep srcFile: 'http://localhost:8081/artifactory/generic-local/comp/Asd.war', 
        targetFile: '/Volumes/WorkDisk/deploy/ASD.war', 
        userName: 'admin', userPwd: 'admin123'
	
_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `srcFile` | `'<fileName>'` | Mandatory parameter to specify a file to pull from Artifactory |
| `targetFile` | `'<fileName>'` | Mandatory parameter to specify the target |
| `userName` | `'<userName>'` | Optional parameter to specify a valid repo username |
| `userPwd` | `'<password>'` | Optional parameter to specify a valid repo user password |

###### Push Artifactory Repo

_Name:_ `devOpsFrameworkArtifactoryPushStep`

_Purpose:_ This step is for pushing files to a Artifactory-based repo 

_Example:_

    devOpsFrameworkArtifactoryPushStep srcFile: '/Volumes/WorkArea/Compon_ASD/target/asd.war',
        targetFile: 'http://localhost:8081/artifactory/generic-local/comp/Asd.war',
        userName: 'admin', userPwd: 'admin123'
		
_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `srcFile` | `'<fileName>'` | Mandatory parameter to specify the file to push to Artifactory |
| `targetFile` | `'<fileName>'` | Mandatory parameter to specify the target |
| `userName` | `'<userName>'` | Optional parameter to specify a valid repo username |
| `userPwd` | `'<password>'` | Optional parameter to specify a valid repo user password |

###### Run an Ansible Playbook

_Name:_ `devOpsFrameworkAnsibleRunbookStep`

_Purpose:_ This step is for running an Ansible playbook

_Example:_

    devOpsFrameworkAnsibleRunbookStep hostFile: '/Volumes/WorkDisk/resources/ansible_hosts',
        workingDir: '/Volumes/WorkDisk/tmp/',
        runFile: '/Volumes/WorkDisk/resources/ansible_playbook.yml'
		
_Parameters:_

| Parameter | Value | Description |
| --------- | ----- | ----------- |
| `hostFile` | `'<fileName>'` | Mandatory parameter to specify the hosts file to use |
| `runFile` | `'<fileName>'` | Mandatory parameter to specify the playbook to use |
| `workingDir` | `'<userName>'` | Optional parameter to specify a valid repo username |
	
Liability Warning
=================
The contents of this repository (documents and examples) are provided “as-is” with no warrantee implied 
or otherwise about the accuracy or functionality of the examples.

You use them at your own risk. If anything results to your machine or environment or anything else as a 
result of ignoring this warning, then the fault is yours only and has nothing to do with me.

Licensing
=========
This software is licensed using the terms and provisions of the [MIT license](https://en.wikipedia.org/wiki/MIT_License).

Known Issues
============
The following are known issues: -
- The framework has not been fully ported or tested on Windows. Do not be surprised if some work needs to be done to make it work on that OS. Where possible classes have been written to support both UNIX and Windows, but they have not been tested on Windows, so some issues may occur. The framework was developed and tested on Unix.
- The Jfrog-cli is not currently supported in the Artifactory classes. This will be added later on.
- Currently, the framework can run both with in and without of Jenkins (if so required), but this duality is not guaranteed to be maintained in the future.
- Container singletons have been provided to show how they can be created, but these have not been verified in usage
- Due to a "feature" with Jenkins, any process which takes 5+ mins to run will be killed by Jenkins. This is core Jenkins pipeline and cannot be overriden without writing a custom threading plugin. The Jenkins developers seem to regard long running jobs as errors. To overcome this you will need to use the DevOps-framework DSL plugin for Jenkins. DSL functions have been added that provide the same functionality, but remove the time issue.

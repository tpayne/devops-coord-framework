Devops framework example
========================

````
Framework Status: Beta (still in test, but ready for playing with)
````

This repository holds an example supporting devops-framework for use with Jenkins shared libraries.

The framework provides a set of callouts which can be used to control your [DevOps](https://en.wikipedia.org/wiki/DevOps) process depending
on the callouts that you use.

There are 5 main interface classes provided. These are...

>| Class | Description | 
>| ----- | ----------- |
>| `Build` | Which is used to control your build process |
>| `Deploy` | Which is used to control your deployment process |
>| `Test` | Which is used to control your test process |
>| `Integration` | Which is used to control your integration process |
>| `ReleaseCandidate` | Which is used to control your Release candidate process |
	
There are also a number of service classes which wrap tools and make them available for use within
framework. These are...

>| Class | Description | 
>| ----- | ----------- |
>| `Notifications` | For email and [Slack](https://slack.com) IRC messaging | 
>| `SCM` | For GIT and SVN SCM function(s) - currently only supporting cloning |
>| `Container` | For various container management commands - currently only supporting [Docker](https://www.docker.com) |
>| `Repository` | For pushing and pulling files from repos - currently only file & [Artifactory](https://jfrog.com/artifactory/) is supported |
>| `ComponentManifest` | For maintaining your manifest of integrated components |

Why have a framework?
=====================
The framework is provided as a way of allowing you to control how products are on-boarded into CI/CD.

You may have many products that need to be converted over to using CI/CD and without a framework in place
those products may end up using lots of different standards or pipelines for implementing their CI/CD process.
This framework allows you to have one standard for implementation, whilst at the same time allowing complete
flexiblity about the processes that are run.

Also, by extending the base framework, you can implement specific customisations that you might want to apply
to every product - like security scans - that cannot be overriden by manipulating the build pipeline.

You can also add services and library routines that will then be available to everyone - building on the functionality
already available.

This callback format has already been used successfully in a number of companies to overcome various different issues.

Jenkins & Compiler Support
==========================
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
The Build class is provided to control your build process and has the following methods...

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
The Deploy class is provided to control your deploy process and has the following methods...

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
The Test class is provided to control your test process and has the following methods...

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
The Integration class is provided to control your integration process and has the following methods...

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
The ReleaseCandidate class is provided to control your release process and has the following methods...

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

How to Install
==============
To install this Jenkins share library, do the following...

	1) git clone https://github.com/tpayne/devops-framework.git
	2) cd devops-framework
	3) mvn package
	4) cd target/
	5) Unzip devops-framework-0.0.1-SNAPSHOT-artifact.zip into a working directory
	6) Use the instructions in the Jenkins Wiki (https://jenkins.io/doc/book/pipeline/shared-libraries/#global-shared-libraries) to install the shared library into your Jenkins system

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
>| `readAllBytes()` | Used to read a file into memory as an array of bytes |
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
>| `sendMail()` | Used for sending email notifications |
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
	
How to Use
==========
The shared library works by providing controller classes that you can use
in a pipeline job. As such, you need to create a pipeline job and register the
callbacks that you want to use.

For example, a sample pipeline might look like

	// Sample pipeline...
	@Library('devops-framework')

	def config = [
	    property1: 'value1',
	    property2: 'value2',
	    property3: 'value3'
	]

	def bld = new org.devops.Build(this, config)

	node {    
		// Register my build process...    
		bld.runBuild(body:{ 
			println ">Start build...<"
			sh label: '', script: '''cd /tmp
			echo hello2
			pwd
			sh /Users/alexgray/build.sh
			'''        
		}, finalHandler:{ println ">Build Job Done<" })

		// Register my get code callback...
		bld.getCode(body:{ println ">Get my code<" })

		// Register my bake callback...
		bld.bakeImage(body:{ println ">Run my bake<" })

		// Register a pre-build callback...
		bld.preBuild(body:{
			println ">Pre-build - clean my files up...<"
			sh label: 'Pre-build sh:', script: '''cd /tmp
			if [ -f main ]; then
				ls main
				rm -f main
			fi
			'''        
		})
		// Run my pipeline...
		bld.runPipeline()
	}

This will run the `getCode()`, `preBuild()`, `runBuild()` and `bakeImage()` callbacks in this
order.

Each callback takes the following...

>| Method | Description | 
>| ------ | ----------- |
>| `body:{}` | Used to specify the Groovy code to run the process |
>| `finalHandler:{}` | Used to specify any Groovy code which will be invoked after the process has run |
>| `exceptionHandler:{}` | Used to specify any Groovy code which will be invoked if any exception occurs |
	
Framework Documentation
=======================
The framework code comes with some limited JavaDoc present which can be used to generate API documentation
by using the command

	% mvn gplus:generateStubs gplus:groovydoc

This will generate JavaDoc style comments in the `target/` directory

Class Usage Examples
====================
Virtually all the class functions documented above have example unit/functional tests which are run in the Maven test phase.

You can use these as examples to show you how to use the classes.

(Note - The tests have very little documentation imbedded in them currently. This will be added slowly).

Pipeline Examples
=================
Examples of pipelines created using the framework can be found in the [`examples`](https://github.com/tpayne/devops-framework/tree/master/examples) directory.

>| Example | Description | 
>| ------ | ----------- |
>| `buildJenkinsPluginPipeline.txt` | Example build pipeline that fetches code, builds it, commits it to a repo, then updates a component manifest with the new version |
>| `buildJenkinsPluginPipelineWithSlackNotif.txt` | As `buildJenkinsPluginPipeline.txt`, but also includes Slack channel notifications and shows how the component manifest can track many different components |

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

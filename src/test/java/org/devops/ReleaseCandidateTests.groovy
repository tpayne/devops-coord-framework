package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit ReleaseCandidate for ReleaseCandidate class.
 */
public class ReleaseCandidateTests extends GroovyTestCase {

	/**
	 * Unit tests for registering callbacks
	 */
   	void testFunctions() {
      	ReleaseCandidate bld = new ReleaseCandidate(null,null)

      	// Register callbacks - any order...
		bld.evaluateSmokeTests(body:{println ">Evaluare smoke-tests<"})
		bld.evaluateTestResults(body:{println ">Evaluare tests<"})
		bld.getDeployAssets(body:{println ">Get deploy assets<"}) 
		bld.getTestAssets(body:{println ">Get test assets<"}) 
		bld.logDeployResults(body:{println ">Log deploy results<"})
		bld.logTestResults(body:{println ">Log test results<"})
		bld.postDeploy(body:{println ">Post Deploy<"})
		bld.postTest(body:{println ">Post test<"})
		bld.preDeploy(body:{println ">Pre Deploy<"})
		bld.preTest(body:{println ">Pre test<"})
		bld.runDeploy(body:{println ">Run Deploy<"})
		bld.runTests(body:{println ">Run test<"})
		bld.runSmokeTests(body:{println ">Run smoke tests<"})
      	bld.getComponentList(body:{println ">Get components<"})
      	bld.prepareForDeploy(body:{println ">Prepare for deploy<"})
      	bld.prepareForTest(body:{println ">Prepare for Test<"})
      	bld.rollback(body:{println ">Rollback<"})
      	bld.finish(body:{println ">Finish<"})

		bld.runPipeline()
   	}

	/**
	 * Unit tests for registering callbacks and exceptions
	 */
  	void testFunctionsWithException() {
      	ReleaseCandidate bld = new ReleaseCandidate(null,null)

      	// Register callbacks - any order...
		bld.evaluateSmokeTests(body:{println ">Evaluare smoke-tests<"},
				  exceptionHandler:{println ">Do exception<"})
		bld.evaluateTestResults(body:{println ">Evaluare tests<"},
				  exceptionHandler:{println ">Do exception<"})
		bld.getDeployAssets(body:{println ">Get deploy assets<"},
				  exceptionHandler:{println ">Do exception<"}) 
		bld.getTestAssets(body:{println ">Get test assets<"},
				  exceptionHandler:{println ">Do exception<"}) 
		bld.logDeployResults(body:{println ">Log deploy results<"},
				  exceptionHandler:{println ">Do exception<"})
		bld.logTestResults(body:{println ">Log test results<"},
				  exceptionHandler:{println ">Do exception<"})
		bld.postDeploy(body:{println ">Post Deploy<"},
				  exceptionHandler:{println ">Do exception<"})
		bld.postTest(body:{println ">Post test<"},
				  exceptionHandler:{println ">Do exception<"})
		bld.preDeploy(body:{println ">Pre Deploy<"},
				  exceptionHandler:{println ">Do exception<"})
		bld.preTest(body:{println ">Pre test<"},
				  exceptionHandler:{println ">Do exception<"})
		bld.runDeploy(body:{println ">Run Deploy<"},
				  exceptionHandler:{println ">Do exception<"})
		bld.runTests(body:{println ">Run test<"},
				  exceptionHandler:{println ">Do exception<"})
		bld.runSmokeTests(body:{println ">Run smoke tests<"},
				  exceptionHandler:{println ">Do exception<"})
      	bld.getComponentList(body:{println ">Get components<"},
				  exceptionHandler:{println ">Do exception<"})
      	bld.prepareForDeploy(body:{println ">Prepare for deploy<"},
				  exceptionHandler:{println ">Do exception<"})
      	bld.prepareForTest(body:{println ">Prepare for Test<"},
				  exceptionHandler:{println ">Do exception<"})
      	bld.rollback(body:{println ">Rollback<"},
				  exceptionHandler:{println ">Do exception<"})
      	bld.finish(body:{println ">Finish<"},
				  exceptionHandler:{println ">Do exception<"})

		bld.runPipeline()
   	}

	/**
	 * Unit tests for registering callbacks and exceptions and finals
	 */
  	void testFunctionsWithFinal() {
      	ReleaseCandidate bld = new ReleaseCandidate(null,null)

      	// Register callbacks - any order...
		bld.evaluateSmokeTests(body:{println ">Evaluare smoke-tests<"},
					  finalHandler:{println ">Do final<"})
		bld.evaluateTestResults(body:{println ">Evaluare tests<"},
					  finalHandler:{println ">Do final<"})
		bld.getDeployAssets(body:{println ">Get deploy assets<"},
					  finalHandler:{println ">Do final<"})
		bld.getTestAssets(body:{println ">Get test assets<"},
					  finalHandler:{println ">Do final<"})
		bld.logDeployResults(body:{println ">Log deploy results<"},
					  finalHandler:{println ">Do final<"})
		bld.logTestResults(body:{println ">Log test results<"},
					  finalHandler:{println ">Do final<"})
		bld.postDeploy(body:{println ">Post Deploy<"},
					  finalHandler:{println ">Do final<"})
		bld.postTest(body:{println ">Post test<"},
					  finalHandler:{println ">Do final<"})
		bld.preDeploy(body:{println ">Pre Deploy<"},
					  finalHandler:{println ">Do final<"})
		bld.preTest(body:{println ">Pre test<"},
					  finalHandler:{println ">Do final<"})
		bld.runDeploy(body:{println ">Run Deploy<"},
					  finalHandler:{println ">Do final<"})
		bld.runTests(body:{println ">Run test<"},
					  finalHandler:{println ">Do final<"})
		bld.runSmokeTests(body:{println ">Run smoke tests<"},
					  finalHandler:{println ">Do final<"})
      	bld.getComponentList(body:{println ">Get components<"},
					  finalHandler:{println ">Do final<"})
      	bld.prepareForDeploy(body:{println ">Prepare for deploy<"},
					  finalHandler:{println ">Do final<"})
      	bld.prepareForTest(body:{println ">Prepare for Test<"},
					  finalHandler:{println ">Do final<"})
      	bld.rollback(body:{println ">Rollback<"},
					  finalHandler:{println ">Do final<"})
      	bld.finish(body:{println ">Finish<"},
					  finalHandler:{println ">Do final<"})

		bld.runPipeline()
   	}   	
}

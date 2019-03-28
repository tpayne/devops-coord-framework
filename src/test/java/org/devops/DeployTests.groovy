package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit test for Deploy class.
 */
public class DeployTests extends GroovyTestCase {

	/**
	 * Unit tests for registering callbacks
	 */
   	void testFunctions() {
      	Deploy bld = new Deploy(null,null)

      	// Register callbacks - any order...
		bld.evaluateSmokeTests(body:{println ">Evaluare smoke-tests<"})
		bld.getAssets(body:{println ">Get assets<"}) 
		bld.logResults(body:{println ">Log results<"})
		bld.postDeploy(body:{println ">Post Deploy<"})
		bld.preDeploy(body:{println ">Pre Deploy<"})
		bld.promote(body:{println ">Promote<"})      
		bld.runDeploy(body:{println ">Run Deploy<"})
		bld.runSmokeTests(body:{println ">Run smoke tests<"})
      	bld.prepareForDeploy(body:{println ">Prepare for deploy<"})

		bld.runPipeline()
   	}

	/**
	 * Unit tests for registering callbacks and exceptions
	 */  	
	 void testFunctionsWithException() {
      	Deploy bld = new Deploy(null,null)

      	// Register callbacks - any order...
		bld.evaluateSmokeTests(body:{println ">Evaluare smoke-tests<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.getAssets(body:{println ">Get assets<"}, 
					  exceptionHandler:{println ">Do exception<"})
		bld.logResults(body:{println ">Log results<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.postDeploy(body:{println ">Post Deploy<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.preDeploy(body:{println ">Pre Deploy<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.promote(body:{println ">Promote<"},      
					  exceptionHandler:{println ">Do exception<"})
		bld.runDeploy(body:{println ">Run Deploy<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.runSmokeTests(body:{println ">Run smoke tests<"},
					  exceptionHandler:{println ">Do exception<"})
      	bld.prepareForDeploy(body:{println ">Prepare for deploy<"},
					  exceptionHandler:{println ">Do exception<"})

		bld.runPipeline()
   	}

	/**
	 * Unit tests for registering callbacks and exceptions and finals
	 */
  	void testFunctionsWithFinal() {
      	Deploy bld = new Deploy(null,null)

      	// Register callbacks - any order...
		bld.evaluateSmokeTests(body:{println ">Evaluare smoke-tests<"},
					  finalHandler:{println ">Do final<"})
		bld.getAssets(body:{println ">Get assets<"},
					  finalHandler:{println ">Do final<"})
		bld.logResults(body:{println ">Log results<"},
					  finalHandler:{println ">Do final<"})
		bld.postDeploy(body:{println ">Post Deploy<"},
 					  finalHandler:{println ">Do final<"})
		bld.preDeploy(body:{println ">Pre Deploy<"},
					  finalHandler:{println ">Do final<"})
		bld.promote(body:{println ">Promote<"}, 
					  finalHandler:{println ">Do final<"})
		bld.runDeploy(body:{println ">Run Deploy<"},
					  finalHandler:{println ">Do final<"})
		bld.runSmokeTests(body:{println ">Run smoke tests<"},
					  finalHandler:{println ">Do final<"})
      	bld.prepareForDeploy(body:{println ">Prepare for deploy<"},
					  finalHandler:{println ">Do final<"})

		bld.runPipeline()
   	}   	
}

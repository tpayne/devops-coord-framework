package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit test for Build class.
 */
public class BuildTests extends GroovyTestCase {

	/**
	 * Unit tests for registering callbacks
	 */
   	void testFunctions() {
      	Build bld = new Build(null,null)

      	// Register callbacks - any order...
		bld.bakeImage(body:{println ">Bake image<"})
		bld.evaluateStaticCodeTests(body:{println ">Evaluate code analysis<"})
		bld.evaluateUnitTests(body:{println ">Evaluare unit-tests<"})
		bld.getCode(body:{println ">Get code<"}) 
		bld.logResults(body:{println ">Log results<"})
		bld.postBuild(body:{println ">Post build<"})
		bld.preBuild(body:{println ">Pre build<"})
		bld.promote(body:{println ">Promote<"})      
		bld.runBuild(body:{println ">Run build<"})
		bld.runStaticCodeTests(body:{println ">Run code analysis<"})
		bld.runUnitTests(body:{println ">Run unit tests<"})
		bld.uploadAssets(body:{println ">Upload assets<"})
      	bld.prepareWorkArea(body:{println ">Prepare workarea<"})

		bld.runPipeline()
   	}

	/**
	 * Unit tests for registering callbacks and exceptions
	 */
  	void testFunctionsWithException() {
      	Build bld = new Build(null,null)

      	// Register callbacks - any order...
		bld.bakeImage(body:{println ">Bake image<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.evaluateStaticCodeTests(body:{println ">Evaluate code analysis<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.evaluateUnitTests(body:{println ">Evaluare unit-tests<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.getCode(body:{println ">Get code<"},
					  exceptionHandler:{println ">Do exception<"}) 
		bld.logResults(body:{println ">Log results<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.postBuild(body:{println ">Post build<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.preBuild(body:{println ">Pre build<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.promote(body:{println ">Promote<"},
					  exceptionHandler:{println ">Do exception<"})     
		bld.runBuild(body:{println ">Run build<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.runStaticCodeTests(body:{println ">Run code analysis<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.runUnitTests(body:{println ">Run unit tests<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.uploadAssets(body:{println ">Upload assets<"},
					  exceptionHandler:{println ">Do exception<"})
      	bld.prepareWorkArea(body:{println ">Prepare workarea<"},
					  exceptionHandler:{println ">Do exception<"})

		bld.runPipeline()
   	}

	/**
	 * Unit tests for registering callbacks and exceptions and finals
	 */
  	void testFunctionsWithFinal() {
      	Build bld = new Build(null,null)

      	// Register callbacks - any order...
		bld.bakeImage(body:{println ">Bake image<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.evaluateStaticCodeTests(body:{println ">Evaluate code analysis<"},
					  finalHandler:{println ">Do final<"})
		bld.evaluateUnitTests(body:{println ">Evaluare unit-tests<"},
					  finalHandler:{println ">Do final<"})
		bld.getCode(body:{println ">Get code<"},
					  finalHandler:{println ">Do final<"})
		bld.logResults(body:{println ">Log results<"},
					  finalHandler:{println ">Do final<"})
		bld.postBuild(body:{println ">Post build<"},
					  finalHandler:{println ">Do final<"})
		bld.preBuild(body:{println ">Pre build<"},
					  finalHandler:{println ">Do final<"})
		bld.promote(body:{println ">Promote<"},
					  finalHandler:{println ">Do final<"})
		bld.runBuild(body:{println ">Run build<"},
					  finalHandler:{println ">Do final<"})
		bld.runStaticCodeTests(body:{println ">Run code analysis<"},
					  finalHandler:{println ">Do final<"})
		bld.runUnitTests(body:{println ">Run unit tests<"},
					  finalHandler:{println ">Do final<"})
		bld.uploadAssets(body:{println ">Upload assets<"},
					  finalHandler:{println ">Do final<"})
      	bld.prepareWorkArea(body:{println ">Prepare workarea<"},
					  finalHandler:{println ">Do final<"})

		bld.runPipeline()
   	}   	
}

package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit test for Test class.
 */
public class TestTests extends GroovyTestCase {

   	void testFunctions() {
      	Test bld = new Test(null,null)

      	// Register callbacks - any order...
		bld.evaluateTests(body:{println ">Evaluare tests<"})
		bld.getAssets(body:{println ">Get assets<"}) 
		bld.logResults(body:{println ">Log results<"})
		bld.postTest(body:{println ">Post Test<"})
		bld.preTest(body:{println ">Pre Test<"})
		bld.promote(body:{println ">Promote<"})      
		bld.runTest(body:{println ">Run Test<"})
      	bld.prepareForTest(body:{println ">Prepare for Test<"})

		bld.runPipeline()
   	}

  	void testFunctionsWithException() {
      	Test bld = new Test(null,null)

      	// Register callbacks - any order...
		bld.evaluateTests(body:{println ">Evaluare tests<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.getAssets(body:{println ">Get assets<"}, 
					  exceptionHandler:{println ">Do exception<"})
		bld.logResults(body:{println ">Log results<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.postTest(body:{println ">Post Test<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.preTest(body:{println ">Pre Test<"},
					  exceptionHandler:{println ">Do exception<"})
		bld.promote(body:{println ">Promote<"},      
					  exceptionHandler:{println ">Do exception<"})
		bld.runTest(body:{println ">Run Test<"},
					  exceptionHandler:{println ">Do exception<"})
      	bld.prepareForTest(body:{println ">Prepare for Test<"},
					  exceptionHandler:{println ">Do exception<"})

		bld.runPipeline()
   	}

  	void testFunctionsWithFinal() {
      	Test bld = new Test(null,null)

      	// Register callbacks - any order...
		bld.evaluateTests(body:{println ">Evaluare tests<"},
					  finalHandler:{println ">Do final<"})
		bld.getAssets(body:{println ">Get assets<"},
					  finalHandler:{println ">Do final<"})
		bld.logResults(body:{println ">Log results<"},
					  finalHandler:{println ">Do final<"})
		bld.postTest(body:{println ">Post Test<"},
 					  finalHandler:{println ">Do final<"})
		bld.preTest(body:{println ">Pre Test<"},
					  finalHandler:{println ">Do final<"})
		bld.promote(body:{println ">Promote<"}, 
					  finalHandler:{println ">Do final<"})
		bld.runTest(body:{println ">Run Test<"},
					  finalHandler:{println ">Do final<"})
      	bld.prepareForTest(body:{println ">Prepare for Test<"},
					  finalHandler:{println ">Do final<"})

		bld.runPipeline()
   	}   	
}

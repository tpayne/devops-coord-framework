package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit test for Build class.
 */
public class BuildTests extends GroovyTestCase {

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
}

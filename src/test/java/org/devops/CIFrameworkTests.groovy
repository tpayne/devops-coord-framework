package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit test for CIFramework class.
 */
public class CIFrameworkTests extends GroovyTestCase {

   	void testFunctions() {
      	Build bld = new Build()
      	Deploy dpl = new Deploy()
      	Test tst = new Test()

      	CIFramework ci = new CIFramework(bld,dpl,tst)
      	assertTrue((ci.getBuild()!=null))
      	assertTrue((ci.getDeploy()!=null))
      	assertTrue((ci.getTest()!=null))

      	ci = null

      	ci = new CIFramework()

      	assertTrue((ci.getBuild()==null))
      	assertTrue((ci.getDeploy()==null))
      	assertTrue((ci.getTest()==null))
   	}

   	void testSetGetFunctions() {
      	Build bld = new Build()
      	Deploy dpl = new Deploy()
      	Test tst = new Test()

      	CIFramework ci = new CIFramework()
      	ci.setBuild(bld)
      	ci.setDeploy(dpl)
      	ci.setTest(tst)
      	assertTrue((ci.getBuild()!=null))
      	assertTrue((ci.getDeploy()!=null))
      	assertTrue((ci.getTest()!=null))
   	}  

   	void testlauchFunctions() {
      	CIFramework ci = new CIFramework()

      	Build bld = new Build()

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
      	ci.setBuild(bld)

      	ci.launchCI()

       	Deploy dpl = new Deploy()

      	// Register callbacks - any order...
		dpl.evaluateSmokeTests(body:{println ">Evaluare smoke-tests<"})
		dpl.getAssets(body:{println ">Get assets<"}) 
		dpl.logResults(body:{println ">Log results<"})
		dpl.postDeploy(body:{println ">Post Deploy<"})
		dpl.preDeploy(body:{println ">Pre Deploy<"})
		dpl.promote(body:{println ">Promote<"})      
		dpl.runDeploy(body:{println ">Run Deploy<"})
		dpl.runSmokeTests(body:{println ">Run smoke tests<"})
      	dpl.prepareForDeploy(body:{println ">Prepare for deploy<"})
      	ci.setDeploy(dpl)

      	ci.launchCI()

      	Test tst = new Test()

      	// Register callbacks - any order...
		tst.evaluateTests(body:{println ">Evaluare tests<"})
		tst.getAssets(body:{println ">Get assets<"}) 
		tst.logResults(body:{println ">Log results<"})
		tst.postTest(body:{println ">Post Test<"})
		tst.preTest(body:{println ">Pre Test<"})
		tst.promote(body:{println ">Promote<"})      
		tst.runTest(body:{println ">Run Test<"})
      	tst.prepareForTest(body:{println ">Prepare for Test<"})     	
      	ci.setTest(tst)

      	ci.launchCI()

   	}     	 	
}

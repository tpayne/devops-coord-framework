package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit test for CDFramework class.
 */
public class CDFrameworkTests extends GroovyTestCase {

   	void testFunctions() {
      	Integration intC = new Integration()
      	ReleaseCandidate rc = new ReleaseCandidate()

      	CDFramework ci = new CDFramework(intC,rc)
      	assertTrue((ci.getIntegration()!=null))
      	assertTrue((ci.getReleaseCandidate()!=null))
 
      	ci = null

      	ci = new CDFramework()

      	assertTrue((ci.getIntegration()==null))
      	assertTrue((ci.getReleaseCandidate()==null))
   	}

   	void testSetGetFunctions() {
      	Integration intC = new Integration()
      	ReleaseCandidate rc = new ReleaseCandidate()

      	CDFramework ci = new CDFramework()
      	ci.setIntegration(intC)
      	ci.setReleaseCandidate(rc)
      	assertTrue((ci.getIntegration()!=null))
      	assertTrue((ci.getReleaseCandidate()!=null))
   	}  

   	void testlauchFunctions() {
      	CDFramework ci = new CDFramework()

      	Integration bld = new Integration()

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
		bld.promote(body:{println ">Promote<"})      
		bld.runDeploy(body:{println ">Run Deploy<"})
		bld.runTests(body:{println ">Run test<"})
		bld.runSmokeTests(body:{println ">Run smoke tests<"})
      	bld.getComponentList(body:{println ">Get components<"})
      	bld.prepareForDeploy(body:{println ">Prepare for deploy<"})
      	bld.prepareForTest(body:{println ">Prepare for Test<"})
      	ci.setIntegration(bld)

      	ci.launchCD()

      	ReleaseCandidate rc = new ReleaseCandidate()

      	// Register callbacks - any order...
		rc.evaluateSmokeTests(body:{println ">Evaluare smoke-tests<"})
		rc.evaluateTestResults(body:{println ">Evaluare tests<"})
		rc.getDeployAssets(body:{println ">Get deploy assets<"}) 
		rc.getTestAssets(body:{println ">Get test assets<"}) 
		rc.logDeployResults(body:{println ">Log deploy results<"})
		rc.logTestResults(body:{println ">Log test results<"})
		rc.postDeploy(body:{println ">Post Deploy<"})
		rc.postTest(body:{println ">Post test<"})
		rc.preDeploy(body:{println ">Pre Deploy<"})
		rc.preTest(body:{println ">Pre test<"})
		rc.runDeploy(body:{println ">Run Deploy<"})
		rc.runTests(body:{println ">Run test<"})
		rc.runSmokeTests(body:{println ">Run smoke tests<"})
      	rc.getComponentList(body:{println ">Get components<"})
      	rc.prepareForDeploy(body:{println ">Prepare for deploy<"})
      	rc.prepareForTest(body:{println ">Prepare for Test<"})
      	rc.rollback(body:{println ">Rollback<"})
      	rc.finish(body:{println ">Finish<"})
      	ci.setReleaseCandidate(rc)

      	ci.launchCD()
   	}     	 	
}

package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit CallbackFactory tests for CallbackFactory class.
 */
public class CallbackFactoryTests extends GroovyTestCase {

   /**
    * Unit test get callback functions
    */
   void testGetCallback() {
      CallbackFactory cb = new CallbackFactory()
      Map bld = cb.getCallbacks(new Build(null,null),null)
      Map dpl = cb.getCallbacks(new Deploy(null,null),null)
      Map tst = cb.getCallbacks(new Test(null,null),null)
      Map itg = cb.getCallbacks(new Integration(null,null),null)
      Map rc = cb.getCallbacks(new ReleaseCandidate(null,null),null)

      assertEquals(bld,CallbackConstants.BuildCallbackMap)
      assertEquals(dpl,CallbackConstants.DeployCallbackMap)
      assertEquals(tst,CallbackConstants.TestCallbackMap)
      assertEquals(itg,CallbackConstants.IntegrationCallbackMap)
      assertEquals(rc,CallbackConstants.ReleaseCandCallbackMap)
   }
}

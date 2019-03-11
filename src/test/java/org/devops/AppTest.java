package org.devops;

import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static TestSuite suiteJava()
    {
    	TestSuite allJavaTests = new TestSuite();
    	//allJavaTests.addTestSuite(AppTest.class);
        return allJavaTests;
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	TestRunner.run(AppTest.suiteJava());
        assertTrue( true );
    }
}


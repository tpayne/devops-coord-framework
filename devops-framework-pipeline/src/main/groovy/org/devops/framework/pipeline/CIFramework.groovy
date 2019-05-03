/**
 * CI framework class for running component build, deploy and test processes as one
 */
package org.devops;

import java.util.logging.Logger
import java.util.logging.Level

class CIFramework implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( CIFramework.class.getName() )
    private static final long serialVersionUID = 1L;

    // Process objects
    private Build buildProcess = null
    private Deploy deployProcess = null
    private Test testProcess = null

    /**
     * Default constructor
     */
    public CIFramework() {
    }

    /**
     * Object constructor
     * 
     * @param final Build - build process
     * @param final Deploy - deploy process
     * @param final Test - test process     
     */
    public CIFramework(final Build buildProcess,
                             final Deploy deployProcess,
                             final Test testProcess) {
        this.buildProcess = buildProcess
        this.deployProcess = deployProcess
        this.testProcess = testProcess        
    }

    /**
     * Get the Build process
     * @return final Build
     */    
    final Build getBuild() {
        return this.buildProcess
    }

    /**
     * Set the Build process
     * @param final Build - buildProcess
     */
    void setBuild(final Build buildProcess) {
        this.buildProcess = buildProcess
    }

    /**
     * Get the Deploy process
     * @return final Deploy
     */    
    final Deploy getDeploy() {
        return this.deployProcess
    }

    /**
     * Set the Deploy process
     * @param final Deploy - deployProcess
     */
    void setDeploy(final Deploy deployProcess) {
        this.deployProcess = deployProcess
    }

    /**
     * Get the Test process
     * @return final Test
     */    
    final Test getTest() {
        return this.testProcess
    }

    /**
     * Set the Test process
     * @param final Test - testProcess
     */
    void setTest(final Test testProcess) {
        this.testProcess = testProcess
    }    

    /**
     * Launch the CI process
     */
    void launchCI() {
        LOGGER.log( Level.FINE, "CI Process build")
        if (this.buildProcess != null) {
            buildProcess.runPipeline()
        }
        LOGGER.log( Level.FINE, "CI Process deploy")
        if (this.deployProcess != null) {
            deployProcess.runPipeline()
        }
        LOGGER.log( Level.FINE, "CI Process test")
        if (this.testProcess != null) {
            testProcess.runPipeline()
        }        
    } 
}

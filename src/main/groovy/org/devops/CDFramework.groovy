/**
 * CD framework class for running integration and release candidate processes as one
 */
package org.devops;

class CDFramework implements Serializable {

    // Process objects
    private Integration intProcess = null
    private ReleaseCandidate rcProcess = null

    /**
     * Default constructor
     */
    public CDFramework() {
    }

    /**
     * Object constructor
     * 
     * @param final Integration - build process
     * @param final ReleaseCandidate - deploy process
     * @param final Test - test process     
     */
    public CDFramework(final Integration intProcess,
                       final ReleaseCandidate rcProcess) {
        this.intProcess = intProcess
        this.rcProcess = rcProcess
    }

    /**
     * Get the Integration process
     * @return final Integration
     */    
    final Integration getIntegration() {
        return this.intProcess
    }

    /**
     * Set the Integration process
     * @param final Integration - intProcess
     */
    void setIntegration(final Integration intProcess) {
        this.intProcess = intProcess
    }

    /**
     * Get the ReleaseCandidate process
     * @return final ReleaseCandidate
     */    
    final ReleaseCandidate getReleaseCandidate() {
        return this.rcProcess
    }

    /**
     * Set the ReleaseCandidate process
     * @param final ReleaseCandidate - rcProcess
     */
    void setReleaseCandidate(final ReleaseCandidate rcProcess) {
        this.rcProcess = rcProcess
    }

    /**
     * Launch the CD process
     */
    void launchCD() {
        if (this.intProcess != null) {
            intProcess.runPipeline()
        }
        if (this.rcProcess != null) {
            rcProcess.runPipeline()
        }
    } 
}

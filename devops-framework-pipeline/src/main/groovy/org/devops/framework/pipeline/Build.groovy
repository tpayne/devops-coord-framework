 /**
 * Build coordinator
 */
package org.devops;

import java.util.logging.Logger
import java.util.logging.Level

class Build extends BaseCoordinator implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( Build.class.getName() )

    /**
     * Default Constructor
     */
    Build() {
    }
    
    /**
     * Constructor
     * @param step
     * @param Map - config     
     */
    Build(steps, Map config) {
        if (steps) {
            super.steps = steps
        }
        if (config) {
            super.config = config
        }
    }

    /**
     * Method to return number of callbacks
     * @return int
     */
    int getCallbackNo() {
        return super.getCallbackNo()
    }

    /**
     * Method to convert callouts to JSON
     * @return String
     */
    final String convertCalloutsToJSON() {
        return super.convertCalloutsToJSON()
    }

    /**
     * Method to convert callouts to txt
     * @return String
     */
    final String convertCalloutsToTxT() {
        return super.convertCalloutsToTxT()
    }

    /**
     * Method to run the pipeline
     */
    public void runPipeline() {
        super.runPipeline()
    }

    /**
     * Callback method
     * @param Map - args
     */
    def prepareWorkArea(Map args = [:]) {
        super._replacePackage(CallbackConstants.prepareWorkArea, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def getCode(Map args = [:])  {
        super._replacePackage(CallbackConstants.getCode, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def preBuild(Map args = [:])  {
        super._replacePackage(CallbackConstants.preBuild, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def runBuild(Map args = [:]) {
        super._replacePackage(CallbackConstants.runBuild, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def postBuild(Map args = [:]) {
        super._replacePackage(CallbackConstants.postBuild, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def runUnitTests(Map args = [:]) {
        super._replacePackage(CallbackConstants.runUnitTests, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def evaluateUnitTests(Map args = [:]) {
        super._replacePackage(CallbackConstants.evaluateUnitTests, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def runStaticCodeTests(Map args = [:]) {
        super._replacePackage(CallbackConstants.runStaticCodeTests, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def evaluateStaticCodeTests(Map args = [:]) {
        super._replacePackage(CallbackConstants.evaluateStaticCodeTests, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def bakeImage(Map args = [:]) {
        super._replacePackage(CallbackConstants.bakeImage, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def uploadAssets(Map args = [:]) {
        super._replacePackage(CallbackConstants.uploadAssets, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def logResults(Map args = [:]) {
        super._replacePackage(CallbackConstants.logResults, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def promote(Map args = [:]) {
        super._replacePackage(CallbackConstants.promote, args)
    }
}


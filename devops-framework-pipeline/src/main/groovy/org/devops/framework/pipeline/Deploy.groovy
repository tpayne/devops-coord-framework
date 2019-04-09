/**
 * Deploy coordinator
 */
package org.devops;

import java.util.logging.Logger
import java.util.logging.Level

class Deploy extends BaseCoordinator implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( Deploy.class.getName() )

    /**
     * Default Constructor
     */
    Deploy() {
    }

    /**
     * Constructor
     * @param step
     * @param Map - config     
     */
    Deploy(steps, Map config) {
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
    def prepareForDeploy(Map args = [:]) {
        super._replacePackage(CallbackConstants.prepareForDeploy, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def getAssets(Map args = [:])  {
        super._replacePackage(CallbackConstants.getAssets, args)
     }

    /**
     * Callback method
     * @param Map - args
     */
    def preDeploy(Map args = [:])  {
        super._replacePackage(CallbackConstants.preDeploy, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def runDeploy(Map args = [:]) {
        super._replacePackage(CallbackConstants.runDeploy, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def postDeploy(Map args = [:]) {
        super._replacePackage(CallbackConstants.postDeploy, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def runSmokeTests(Map args = [:]) {
        super._replacePackage(CallbackConstants.runSmokeTests, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def evaluateSmokeTests(Map args = [:]) {
        super._replacePackage(CallbackConstants.evaluateSmokeTests, args)
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

/**
 * Integration coordinator
 */
package org.devops;

import java.util.logging.Logger
import java.util.logging.Level

class Integration extends BaseCoordinator implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( Integration.class.getName() )

    /**
     * Default Constructor
     */
    Integration() {
    }
    
    /**
     * Constructor
     * @param step
     * @param Map - config     
     */    
     Integration(steps, Map config) {
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
    def getComponentList(Map args = [:]) {
        super._replacePackage(CallbackConstants.getComponentList, args)
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
    def getDeployAssets(Map args = [:]) {
        super._replacePackage(CallbackConstants.getDeployAssets, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def preDeploy(Map args = [:]) {
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
    def logDeployResults(Map args = [:]) {
        super._replacePackage(CallbackConstants.logDeployResults, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def prepareForTest(Map args = [:]) {
        super._replacePackage(CallbackConstants.prepareForTest, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def getTestAssets(Map args = [:]) {
        super._replacePackage(CallbackConstants.getTestAssets, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def preTest(Map args = [:]) {
        super._replacePackage(CallbackConstants.preTest, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def runTests(Map args = [:]) {
        super._replacePackage(CallbackConstants.runTests, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def postTest(Map args = [:]) {
        super._replacePackage(CallbackConstants.postTest, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def evaluateTestResults(Map args = [:]) {
        super._replacePackage(CallbackConstants.evaluateTestResults, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def logTestResults(Map args = [:]) {
        super._replacePackage(CallbackConstants.logTestResults, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def promote(Map args = [:]) {
        super._replacePackage(CallbackConstants.promote, args)
    } 
}

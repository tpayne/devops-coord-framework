/**
 * Integration coordinator
 */
package org.devops;

class Integration implements Serializable {

    def steps = null
    def config = null
    private CallbackFactory cbFactory = new CallbackFactory()

    private Map stepMap = null

    /**
     * Method to run the pipeline
     */    
    def runPipeline() {
        cbFactory.executeStack(this.steps,this.stepMap)
    }

    /**
     * Method to replace a package in the map with a custom callout
     * @param String - key
     * @param Map    - args
     */     
     private def _replacePackage(String key, Map args) {
        if (this.stepMap == null) {
            this.stepMap = cbFactory.getCallbacks(this,this.config)
        }
        cbFactory.replacePackage(stepMap,key,args)       
    }

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
            this.steps = steps
        }
        if (config) {
            this.config = config
        }    
    }


    /**
     * Callback method
     * @param Map - args
     */
    def getComponentList(Map args = [:]) {
        _replacePackage(CallbackConstants.getComponentList, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def prepareForDeploy(Map args = [:]) {
        _replacePackage(CallbackConstants.prepareForDeploy, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def getDeployAssets(Map args = [:]) {
        _replacePackage(CallbackConstants.getDeployAssets, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def preDeploy(Map args = [:]) {
        _replacePackage(CallbackConstants.preDeploy, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def runDeploy(Map args = [:]) {
        _replacePackage(CallbackConstants.runDeploy, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def postDeploy(Map args = [:]) {
        _replacePackage(CallbackConstants.postDeploy, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def runSmokeTests(Map args = [:]) {
        _replacePackage(CallbackConstants.runSmokeTests, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def evaluateSmokeTests(Map args = [:]) {
        _replacePackage(CallbackConstants.evaluateSmokeTests, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def logDeployResults(Map args = [:]) {
        _replacePackage(CallbackConstants.logDeployResults, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def prepareForTest(Map args = [:]) {
        _replacePackage(CallbackConstants.prepareForTest, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def getTestAssets(Map args = [:]) {
        _replacePackage(CallbackConstants.getTestAssets, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def preTest(Map args = [:]) {
        _replacePackage(CallbackConstants.preTest, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def runTests(Map args = [:]) {
        _replacePackage(CallbackConstants.runTests, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def postTest(Map args = [:]) {
        _replacePackage(CallbackConstants.postTest, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def evaluateTestResults(Map args = [:]) {
        _replacePackage(CallbackConstants.evaluateTestResults, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def logTestResults(Map args = [:]) {
        _replacePackage(CallbackConstants.logTestResults, args)
    } 

    /**
     * Callback method
     * @param Map - args
     */
    def promote(Map args = [:]) {
        _replacePackage(CallbackConstants.promote, args)
    } 
}

/**
 * Test coordinator
 */
 package org.devops;

class Test implements Serializable {

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
    Test() {
    }

    /**
     * Constructor method
     * @param steps
     * @param Map - config
     */
    Test(steps, Map config) {
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
    def prepareForTest(Map args = [:]) {
        _replacePackage(CallbackConstants.prepareForTest, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def getAssets(Map args = [:])  {
        _replacePackage(CallbackConstants.getAssets, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def preTest(Map args = [:])  {
        _replacePackage(CallbackConstants.preTest, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def runTest(Map args = [:]) {
        _replacePackage(CallbackConstants.runTest, args)
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
    def evaluateTests(Map args = [:]) {
        _replacePackage(CallbackConstants.evaluateTests, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def logResults(Map args = [:]) {
        _replacePackage(CallbackConstants.logResults, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def promote(Map args = [:]) {
        _replacePackage(CallbackConstants.promote, args)
    }
}

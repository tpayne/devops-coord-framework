/**
 * Build coordinator
 */
 package org.devops;

class Build implements Serializable {

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
    Build() {
    }
    
    /**
     * Constructor
     * @param step
     * @param Map - config     
     */
    Build(steps, Map config) {
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
    def prepareWorkArea(Map args = [:]) {
        _replacePackage(CallbackConstants.prepareWorkArea, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def getCode(Map args = [:])  {
        _replacePackage(CallbackConstants.getCode, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def preBuild(Map args = [:])  {
        _replacePackage(CallbackConstants.preBuild, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def runBuild(Map args = [:]) {
        _replacePackage(CallbackConstants.runBuild, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
     def postBuild(Map args = [:]) {
        _replacePackage(CallbackConstants.postBuild, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def runUnitTests(Map args = [:]) {
        _replacePackage(CallbackConstants.runUnitTests, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def evaluateUnitTests(Map args = [:]) {
        _replacePackage(CallbackConstants.evaluateUnitTests, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def runStaticCodeTests(Map args = [:]) {
        _replacePackage(CallbackConstants.runStaticCodeTests, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def evaluateStaticCodeTests(Map args = [:]) {
        _replacePackage(CallbackConstants.evaluateStaticCodeTests, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def bakeImage(Map args = [:]) {
        _replacePackage(CallbackConstants.bakeImage, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def uploadAssets(Map args = [:]) {
        _replacePackage(CallbackConstants.uploadAssets, args)
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

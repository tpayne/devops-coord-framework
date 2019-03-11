package org.devops;

/**
 * Build coordinator
 */
 class Build implements Serializable {

    def steps = null
    def config = null
    private CallbackFactory cbFactory = new CallbackFactory()

    private Map stepMap = null

    // Run the pipeline
    def runPipeline() {
        cbFactory.executeStack(this.steps,this.stepMap)
    }

    // Replace callbacks in the function stack with custom callbacks...
    private def _replacePackage(String key, Map args) {
        if (this.stepMap == null) {
            this.stepMap = cbFactory.getCallbacks(this,this.config)
        }
        cbFactory.replacePackage(stepMap,key,args)       
    }

    Build(steps, Map config) {
        if (steps) {
            this.steps = steps
        }
        if (config) {
            this.config = config
        }
    }

    // Callback functions...
    def prepareWorkArea(Map args = [:]) {
        _replacePackage(CallbackConstants.prepareWorkArea, args)
    }

    def getCode(Map args = [:])  {
        _replacePackage(CallbackConstants.getCode, args)
    }

    def preBuild(Map args = [:])  {
        _replacePackage(CallbackConstants.preBuild, args)
    }

    def runBuild(Map args = [:]) {
        _replacePackage(CallbackConstants.runBuild, args)
    }

    def postBuild(Map args = [:]) {
        _replacePackage(CallbackConstants.postBuild, args)
    }

    def runUnitTests(Map args = [:]) {
        _replacePackage(CallbackConstants.runUnitTests, args)
    }

    def evaluateUnitTests(Map args = [:]) {
        _replacePackage(CallbackConstants.evaluateUnitTests, args)
    }

    def runStaticCodeTests(Map args = [:]) {
        _replacePackage(CallbackConstants.runStaticCodeTests, args)
    }

    def evaluateStaticCodeTests(Map args = [:]) {
        _replacePackage(CallbackConstants.evaluateStaticCodeTests, args)
    }

    def bakeImage(Map args = [:]) {
        _replacePackage(CallbackConstants.bakeImage, args)
    }

    def uploadAssets(Map args = [:]) {
        _replacePackage(CallbackConstants.uploadAssets, args)
    }

    def logResults(Map args = [:]) {
        _replacePackage(CallbackConstants.logResults, args)
    }

    def promote(Map args = [:]) {
        _replacePackage(CallbackConstants.promote, args)
    }
}

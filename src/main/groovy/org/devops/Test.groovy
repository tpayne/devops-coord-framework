package org.devops;

/**
 * Test coordinator
 */
 class Test implements Serializable {

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

    Test(steps, Map config) {
        if (steps) {
            this.steps = steps
        }
        if (config) {
            this.config = config
        }    
    }

    // Callback functions...
    def prepareForTest(Map args = [:]) {
        _replacePackage(CallbackConstants.prepareForTest, args)
    }

    def getAssets(Map args = [:])  {
        _replacePackage(CallbackConstants.getAssets, args)
    }

    def preTest(Map args = [:])  {
        _replacePackage(CallbackConstants.preTest, args)
    }

    def runTest(Map args = [:]) {
        _replacePackage(CallbackConstants.runTest, args)
    }

    def postTest(Map args = [:]) {
        _replacePackage(CallbackConstants.postTest, args)
    }

    def evaluateTests(Map args = [:]) {
        _replacePackage(CallbackConstants.evaluateTests, args)
    }

    def logResults(Map args = [:]) {
        _replacePackage(CallbackConstants.logResults, args)
    }

    def promote(Map args = [:]) {
        _replacePackage(CallbackConstants.promote, args)
    }
}

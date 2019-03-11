package org.devops;

class Deploy implements Serializable {

    def steps = null
    def config = null
    private CallbackFactory cbFactory = new CallbackFactory()

    private Map stepMap = null

    def runPipeline() {
        cbFactory.executeStack(this.steps,this.stepMap)
    }

    private def _replacePackage(String key, Map args) {
        if (this.stepMap == null) {
            this.stepMap = cbFactory.getCallbacks(this,this.config)
        }
        cbFactory.replacePackage(stepMap,key,args)       
    }

    Deploy(steps, Map config) {
        if (steps) {
            this.steps = steps
        }
        if (config) {
            this.config = config
        }    
    }

    def prepareForDeploy(Map args = [:]) {
        _replacePackage(CallbackConstants.prepareForDeploy, args)
    }

    def getAssets(Map args = [:])  {
        _replacePackage(CallbackConstants.getAssets, args)
     }

    def preDeploy(Map args = [:])  {
        _replacePackage(CallbackConstants.preDeploy, args)
    }

    def runDeploy(Map args = [:]) {
        _replacePackage(CallbackConstants.runDeploy, args)
    }

    def postDeploy(Map args = [:]) {
        _replacePackage(CallbackConstants.postDeploy, args)
    }

    def runSmokeTests(Map args = [:]) {
        _replacePackage(CallbackConstants.runSmokeTests, args)
    }

    def evaluateSmokeTests(Map args = [:]) {
        _replacePackage(CallbackConstants.evaluateSmokeTests, args)
    }

    def logResults(Map args = [:]) {
        _replacePackage(CallbackConstants.logResults, args)
    }

    def promote(Map args = [:]) {
        _replacePackage(CallbackConstants.promote, args)
    }
}

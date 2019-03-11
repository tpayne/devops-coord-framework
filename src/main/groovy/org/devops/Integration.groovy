package org.devops;

class Integration implements Serializable {

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

    Integration(steps, Map config) {
        if (steps) {
            this.steps = steps
        }
        if (config) {
            this.config = config
        }    
    }

    def getComponentList(Map args = [:]) {
        _replacePackage(CallbackConstants.getComponentList, args)
    } 

    def prepareForDeploy(Map args = [:]) {
        _replacePackage(CallbackConstants.prepareForDeploy, args)
    } 

    def getDeployAssets(Map args = [:]) {
        _replacePackage(CallbackConstants.getDeployAssets, args)
    } 

    def preDeploy(Map args = [:]) {
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

    def evaluateSmoteTests(Map args = [:]) {
        _replacePackage(CallbackConstants.evaluateSmoteTests, args)
    } 

    def logDeployResults(Map args = [:]) {
        _replacePackage(CallbackConstants.logDeployResults, args)
    } 

    def prepareForTest(Map args = [:]) {
        _replacePackage(CallbackConstants.prepareForTest, args)
    } 

    def getTestAssets(Map args = [:]) {
        _replacePackage(CallbackConstants.getTestAssets, args)
    } 

    def preTest(Map args = [:]) {
        _replacePackage(CallbackConstants.preTest, args)
    } 

    def runTests(Map args = [:]) {
        _replacePackage(CallbackConstants.runTests, args)
    } 

    def postTest(Map args = [:]) {
        _replacePackage(CallbackConstants.postTest, args)
    } 

    def evaluateTestResults(Map args = [:]) {
        _replacePackage(CallbackConstants.evaluateTestResults, args)
    } 

    def logTestResults(Map args = [:]) {
        _replacePackage(CallbackConstants.logTestResults, args)
    } 

    def promote(Map args = [:]) {
        _replacePackage(CallbackConstants.promote, args)
    } 
}

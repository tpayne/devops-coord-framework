/**
 * Test coordinator
 */
 package org.devops;

class Test extends BaseCoordinator implements Serializable {

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
     * Method to run the pipeline
     */
    public void runPipeline() {
        super.runPipeline()
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
    def getAssets(Map args = [:])  {
        super._replacePackage(CallbackConstants.getAssets, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def preTest(Map args = [:])  {
        super._replacePackage(CallbackConstants.preTest, args)
    }

    /**
     * Callback method
     * @param Map - args
     */
    def runTest(Map args = [:]) {
        super._replacePackage(CallbackConstants.runTest, args)
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
    def evaluateTests(Map args = [:]) {
        super._replacePackage(CallbackConstants.evaluateTests, args)
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

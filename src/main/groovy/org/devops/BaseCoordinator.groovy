/**
 * Build coordinator
 */
 package org.devops;

class BaseCoordinator implements Serializable {

    protected steps = null
    protected config = null
    protected CallbackFactory cbFactory = new CallbackFactory()

    protected Map stepMap = null

    /**
     * Method to return number of callbacks
     * @return int
     */
    protected int getCallbackNo() {
        if (stepMap != null) {
            return stepMap.size()
        }
    }

    /**
     * Method to convert callouts to JSON
     * @return String
     */
    protected String convertCalloutsToJSON() {
        if (stepMap != null) {
            return Utilities.convertMapToJSON(stepMap)
        }
    }

    /**
     * Method to replace a package in the map with a custom callout
     * @param String - key
     * @param Map    - args
     */  
    protected void _replacePackage(String key, Map args) {
        if (this.stepMap == null) {
            this.stepMap = cbFactory.getCallbacks(this,this.config)
        }
        cbFactory.replacePackage(stepMap,key,args)       
    }

    /**
     * Method to run the pipeline
     */
    protected void runPipeline() {
        cbFactory.executeStack(this.steps,this.stepMap)
    }

    /**
     * Default Constructor
     */
    BaseCoordinator() {
    }
    
    /**
     * Constructor
     * @param step
     * @param Map - config     
     */
    BaseCoordinator(steps, Map config) {
        if (steps) {
            this.steps = steps
        }
        if (config) {
            this.config = config
        }
    }
}

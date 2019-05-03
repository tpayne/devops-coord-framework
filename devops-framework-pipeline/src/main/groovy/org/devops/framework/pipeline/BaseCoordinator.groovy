/**
 * Base coordinator
 */
package org.devops;

import org.devops.framework.core.Utilities

import java.util.logging.Logger
import java.util.logging.Level

class BaseCoordinator implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( BaseCoordinator.class.getName() )
    private static final long serialVersionUID = 1L;

    protected steps = null
    protected config = null
    protected CallbackFactory cbFactory = new CallbackFactory()
    protected Map stepMap = null

    /**
     * Method to return number of callbacks
     * @return int
     */
    protected int getCallbackNo() {
        if (this.stepMap != null) {
            return this.stepMap.size()
        }
        return -1
    }

    /**
     * Method to convert callouts to JSON
     * @return String
     */
    protected String convertCalloutsToJSON() {
        if (this.stepMap != null) {
            String json = Utilities.convertMapToJSON(this.stepMap)
            return json
        }
        return null
    }

    /**
     * Method to convert callouts to string
     * @return String
     */
    protected String convertCalloutsToTxt() {
        if (this.stepMap != null) {
            String str = this.stepMap.toString()
            return str
        }
        return null
    }

    /**
     * Method to replace a package in the map with a custom callout
     * @param String - key
     * @param Map    - args
     */  
    protected void _replacePackage(String key, Map args) {
        if (this.stepMap == null) {
            this.stepMap = new LinkedHashMap(cbFactory.getCallbacks(this,this.config))
        }
        cbFactory.replacePackage(stepMap,key,args)       
    }

    /**
     * Method to run the pipeline
     */
    protected void runPipeline() {
        LOGGER.log(Level.FINE, "Processing pipeline...");
        cbFactory.runStack(this.steps,this.stepMap)
        LOGGER.log(Level.FINE, "Processed pipeline");
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

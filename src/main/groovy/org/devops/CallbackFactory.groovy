/**
 * Callback factory for use with the framework
 */
 package org.devops;

class CallbackFactory implements Serializable {

    /**
     * Gets callback stack to use
     *
     * @param final def - Callback class
     * @param final Map - Configs passed in
     * @return Map - The callback stack
     */
    static Map getCallbacks(final def cbClass, final Map config) {
        Map stackMap = null

        if (cbClass instanceof Build) {
            stackMap = CallbackConstants.BuildCallbackMap
        } else if (cbClass instanceof Deploy) {
            stackMap = CallbackConstants.DeployCallbackMap
        } else if (cbClass instanceof Test) {
            stackMap = CallbackConstants.TestCallbackMap
        } else if (cbClass instanceof Integration) {
            stackMap = CallbackConstants.IntegrationCallbackMap
        } else if (cbClass instanceof ReleaseCandidate) {
            stackMap = CallbackConstants.ReleaseCandCallbackMap
        } 
        return stackMap
    }

    /**
     * Replace packages if wanted
     *
     * @param Map - The callback stack
     * @param String - Key to replace
     * @param Map - Callback to use instead of default
     */    
    static def replacePackage(Map stepMap, String key, Map args = [:]) {
        if (stepMap && key && args) {
            if (stepMap.containsKey(key)) {
                Map replaceA = stepMap.get(key)
                replaceA = args
                stepMap.put(key,replaceA)
            }
        }
    }

    /**
     * Execute the callback stack
     *
     * @param final def - Steps passed down from Jenkins
     * @param final Map - Callback stack to unwind and run
     */        
    static def executeStack(final def steps, final Map stepsToRun) {
        stepsToRun.each{
            if (it.value != null) {
                runStep(it.value,it.key,steps,stepsToRun)
            }
        }
    }

    /**
     * Run a step from callback stack
     *
     * @param final Map - Step to run
     * @param final String - Callback name
     * @param final def - Jenkins step 
     * @param final Map - Callback stack of functions if needed
     */     
    static private runStep(final Map toRun, final String key, final def steps, final Map stepsToRun) {
        if (toRun != null) {
            if (steps) {
                steps.stage(key) {
                    // Do nothing for now...
                }
            }
            Map args = toRun
            // Run the steps...
            try {
                // Check if I have any steps to run...
                if (args.body != null) {
                    args.body()
                }
            } catch(Exception ex) {
                // Special handling for RC...
                if (stepsToRun.getClass() == CallbackConstants.ReleaseCandCallbackMap) {
                    Map rollback = getCallbackFor(CallbackConstants.rollback, stepsToRun)
                    if (rollback != null && 
                        !toRun.getKey().equalsIgnoreCase(CallbackConstants.rollback)) {
                        runStep(rollback,steps)
                    }
                }
                // Run the execution handler
                if (args.exceptionHandler != null) {
                    args.exceptionHandler()
                }
                throw ex
            } finally {
                // Run the final handler
                if (args.finalHandler != null) {
                    args.finalHandler()
                }
            }                
        }        
    }

    /**
     * Return callback for given key
     *
     * @param final String - Callback key
     * @param final Map - Callback list
     * @return Map - The callback found
     */
    static private Map getCallbackFor(final String key, final Map stepsToRun) {
        return (Map)stepsToRun.getKey(key)
    }      
 }

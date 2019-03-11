package org.devops;

/**
 * Callback factory for use with the framework
 */
 class CallbackFactory implements Serializable {

    /**
     * Gets callback stack to use
     *
     * @param def - Callback class
     * @param Map - Configs passed in
     * @return Map - The callback stack
     */
    static Map getCallbacks(def cbClass, Map config) {
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
     * @param def - Steps passed down from Jenkins
     * @param Map - Callback stack to unwind and run
     */        
    static def executeStack(def steps, Map stepsToRun) {
        stepsToRun.each{
            if (it.value != null) {
                runStep(it.value,it.key,steps,stepsToRun)
            }
        }
    }

    /**
     * Run a step from callback stack
     *
     * @param Map - Step to run
     * @param String - Callback name
     * @param def - Jenkins step 
     * @param Map - Callback stack of functions if needed
     */     
    static private runStep(Map toRun, String key, def steps, Map stepsToRun) {
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

    // Return a callback for a given key...
    static private Map getCallbackFor(String key, Map stepsToRun) {
        return (Map)stepsToRun.getKey(key)
    }      
 }

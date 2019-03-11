package org.devops;

class CallbackFactory implements Serializable {

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
    
    static def replacePackage(Map stepMap, String key, Map args = [:]) {
        if (stepMap && key && args) {
            if (stepMap.containsKey(key)) {
                Map replaceA = stepMap.get(key)
                replaceA = args
                stepMap.put(key,replaceA)
            }
        }
    }

    static def executeStack(def steps, Map stepsToRun) {
        stepsToRun.each{
            if (it.value != null) {
                runStep(it.value,steps,stepsToRun)
            }
        }
    }

    static private runStep(Map toRun, def steps, Map stepsToRun) {
        if (toRun != null) {
            if (steps) {
                steps.stage(toRun.getKey()) {
                    // Do nothing for now...
                }
            }
            Map args = toRun
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
                if (args.exceptionHandler != null) {
                    args.exceptionHandler()
                }
                throw ex
            } finally {
                if (args.finalHandler != null) {
                    args.finalHandler()
                }
            }                
        }        
    }

    static private Map getCallbackFor(String key, Map stepsToRun) {
        return (Map)stepsToRun.getKey(key)
    }      
 }

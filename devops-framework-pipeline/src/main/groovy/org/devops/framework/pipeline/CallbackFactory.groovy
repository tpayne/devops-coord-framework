/**
 * Callback factory for use with the framework
 */
package org.devops;

import java.util.logging.Logger
import java.util.logging.Level
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

@SuppressFBWarnings(value="SE_NO_SERIALVERSIONID")
class CallbackFactory implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( CallbackFactory.class.getName() )
    private static final long serialVersionUID = 1L;

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
    static void runStack(final def steps, final Map stepsToRun) {
        if (stepsToRun != null) {
            LOGGER.log(Level.FINE, "Processing Steps {0}", stepsToRun.size() );
            executeStack(steps,stepsToRun)
            LOGGER.log(Level.FINE, "Steps processed");            
        } else {
            LOGGER.log(Level.FINE, "Steps are null");                        
        }
    }

    /**
     * Execute the callback stack
     *
     * @param final def - Steps passed down from Jenkins
     * @param final Map - Callback stack to unwind and run
     */        
    static private void executeStack(final def steps, final Map stepsToRun) {
        if (stepsToRun != null) {
            int i = 0
            int toRun = stepsToRun.size()
            Iterator it = stepsToRun.entrySet().iterator();
            while (it.hasNext()) {
                final Map.Entry pair = (Map.Entry)it.next();
                LOGGER.log( Level.FINER, "- Checking [{0}/{1}] {2}...", i, toRun, pair.getKey())
                if (pair.getValue() != null) {
                    LOGGER.log( Level.FINER, "- Processing [{0}]: {1}", pair.getKey(), pair.getValue())
                    runStep(pair.getValue(),pair.getKey(),steps,stepsToRun)
                } else {
                    LOGGER.log( Level.FINER, "- Skipping [{0}]: {1}", pair.getKey())
                }
                i++
            }
        } else {
            LOGGER.log( Level.FINER, "Step to run is null")
        }
        LOGGER.log( Level.FINER, "Steps run")
    }

    /**
     * Run a step from callback stack
     *
     * @param final Map - Step to run
     * @param final String - Callback name
     * @param final def - Jenkins step 
     * @param final Map - Callback stack of functions if needed
     */     
    static private void runStep(final Map toRun, final String key, final def steps, final Map stepsToRun) {
        if (toRun != null) {
            Map args = toRun
            try {
                LOGGER.log( Level.FINEST, "- Running {0}", args.toString())
                // Check if I have any steps to run...
                if (steps != null) {
                    steps.stage(key) {
                        // Run the steps...
                        if (args.body != null) {
                            args.body()
                        }
                    }
                } else {
                    // Run the steps...
                    if (args.body != null) {
                        args.body()
                    }
                }
            } catch(Exception ex) {
                LOGGER.log( Level.SEVERE, "- Processing exception {0}", ex.getMessage())
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
                LOGGER.log( Level.FINEST, "- Processing final")
                // Run the final handler
                if (args.finalHandler != null) {
                    args.finalHandler()
                }
                LOGGER.log( Level.FINEST, "- Processing final done")
            }                
        } else {
            LOGGER.log( Level.FINEST, "- Process is null")
        }
        LOGGER.log( Level.FINEST, "- Running done")      
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

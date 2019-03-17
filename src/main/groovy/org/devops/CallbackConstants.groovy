/**
 * Constant definitions for use with the framework
 */
 package org.devops;

class CallbackConstants {
    
    // Generic constants...
    static final String logResults = "logResults"
    static final String promote = "promote" 
    static final String getAssets = "getAssets"
    static final String getComponentList = "getComponentList"
    static final String getDeployAssets = "getDeployAssets"
    static final String logDeployResults = "logDeployResults"
    static final String getTestAssets = "getTestAssets"
    static final String evaluateTestResults = "evaluateTestResults"
    static final String logTestResults = "logTestResults"

    // Build constants...
    static final String prepareWorkArea = "prepareWorkArea"
    static final String getCode = "getCode"
    static final String preBuild = "preBuild"
    static final String runBuild = "runBuild"
    static final String postBuild = "postBuild"
    static final String runUnitTests = "runUnitTests"
    static final String evaluateUnitTests = "evaluateUnitTests"
    static final String runStaticCodeTests = "runStaticCodeTests"
    static final String evaluateStaticCodeTests = "evaluateStaticCodeTests"
    static final String bakeImage = "bakeImage"
    static final String uploadAssets = "uploadAssets"

    // Build callback stack...
    static final def BuildCallbackMap = [
        prepareWorkArea: null,
        getCode: null,
        preBuild: null,
        runBuild: null,
        postBuild: null,
        runUnitTests: null,
        evaluateUnitTests: null,
        runStaticCodeTests: null,
        evaluateStaticCodeTests: null,
        bakeImage: null,
        uploadAssets: null,
        logResults: null,
        promote: null
    ]

    // Deploy constants...
    static final String prepareForDeploy = "prepareForDeploy"
    static final String preDeploy = "preDeploy"
    static final String runDeploy = "runDeploy"
    static final String postDeploy = "postDeploy"
    static final String runSmokeTests = "runSmokeTests"
    static final String evaluateSmokeTests = "evaluateSmokeTests"

    // Deploy callback stack...
    static final def DeployCallbackMap = [
        prepareForDeploy: null,
        getAssets: null,
        preDeploy: null,
        runDeploy: null,
        postDeploy: null,
        runSmokeTests: null,
        evaluateSmokeTests: null,
        logResults: null,
        promote: null
    ]

    // Test constants...
    private static final String prepareForTest = "prepareForTest"
    private static final String preTest = "preTest"
    private static final String runTest = "runTest"
    private static final String postTest = "postTest"
    private static final String evaluateTests = "evaluateTests"
 
    // Test callback stack...
    static final def TestCallbackMap = [
        prepareForTest: null,
        getAssets: null,
        preTest: null,
        runTest: null,
        postTest: null,
        evaluateTests: null,
        logResults: null,
        promote: null
    ]

    // Integration constants...
    private static final String runTests = "runTests"
    
    // Integration callback stack...
    static final def IntegrationCallbackMap = [
        getComponentList: null,
        prepareForDeploy: null,
        getDeployAssets: null,
        preDeploy: null,
        runDeploy: null,
        postDeploy: null,
        runSmokeTests: null,
        evaluateSmokeTests: null,
        logDeployResults: null,
        prepareForTest: null,
        getTestAssets: null,
        preTest: null,
        runTests: null,
        postTest: null,
        evaluateTestResults: null,
        logTestResults: null,
        promote: null       
    ]

    // Release candidate constants...
    static final String rollback = "rollback"
    static final String finish = "finish"

    // Release callback stack...
    static final def ReleaseCandCallbackMap = [
        getComponentList: null,
        prepareForDeploy: null,
        getDeployAssets: null,
        preDeploy: null,
        runDeploy: null,
        postDeploy: null,
        runSmokeTests: null,
        evaluateSmokeTests: null,
        logDeployResults: null,
        prepareForTest: null,
        getTestAssets: null,
        preTest: null,
        runTests: null,
        postTest: null,
        evaluateTestResults: null,
        logTestResults: null,
        rollback: null,
        finish: null       
    ]
}

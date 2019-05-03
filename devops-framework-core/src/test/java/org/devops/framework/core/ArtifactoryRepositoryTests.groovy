package org.devops.framework.core;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit ArtifactoryRepositoryTests tests for Repository class.
 */
public class ArtifactoryRepositoryTests extends GroovyTestCase {

   File propFile = new File("."+"/src/test/resources/unitTest.properties")
   def map = Utilities.mapProperties(propFile)

   /**
    * Utility function for getting tmpDir
    */
   File getTmpDir() {
      if (runUnitTestsOnly()) {
        return new File(System.getProperty("java.io.tmpdir"))
      } else {
        return new File((map.get("tmpDir") != null) ? map.get("tmpDir") : System.getProperty("java.io.tmpdir"))
      }
   }

   /**
    * Utility function for seeing if need to just run unit-tests
    */
   boolean runUnitTestsOnly() {
      if (System.getenv("DEVOPS_FRAMEWORK_UNITTESTS")!=null) {
        return true;
      }
      String unitTests = map.get("unit_tests_only")
      if (unitTests != null && !unitTests.isEmpty()) {
        return(unitTests.contains("true"))
      }
      return false
   }

   /**
    * Push file to Artifactory repo 
    */
   void testPushFileToArtifactory() {
      if (runUnitTestsOnly()) {
        return;
      }
      File srcFile = new File(propFile.getAbsolutePath())
      URI targetRepo = new URI(map.get("artifactory_repoURI")+"/unitTest/")

      String userName = map.get("artifactory_repoUser")
      String userPwd = map.get("artifactory_repoUserPwd")
      boolean retStat = true

      if (targetRepo != null && !targetRepo.toString().isEmpty() &&
          userName != null && !userName.isEmpty()) {
         retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.ARTIFACTORY,srcFile,
                                                   targetRepo,userName,userPwd)
      }
      assertTrue(retStat)
   } 

   /**
    * Pull file from Artifactory repo 
    */
   void testPullFileFromArtifactory() {
      if (runUnitTestsOnly()) {
        return;
      }
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcFile = new File(propFile.getAbsolutePath())
      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      URI srcRepo = new URI(map.get("artifactory_repoURI")+"/unitTest/unitTest.properties")

      String userName = map.get("artifactory_repoUser")
      String userPwd = map.get("artifactory_repoUserPwd")
      StringBuffer outputStr = new StringBuffer()

      boolean retStat = true

      if (srcRepo != null && !srcRepo.toString().isEmpty() &&
          userName != null && !userName.isEmpty()) {
         retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.ARTIFACTORY,srcFile,
                                                   srcRepo,userName,userPwd)
         assertTrue(retStat)
         targetFile.delete()
         
         retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.ARTIFACTORY,srcRepo,
                                                   targetFile,userName,userPwd)
         if (retStat && targetFile.exists()) {
         } else {
            retStat = false
         }
         targetFile.delete()
      }
      assertTrue(retStat)
   } 

   /**
    * Push file to Artifactory repo 
    */
   void testPushFileToArtifactoryStr() {
      if (runUnitTestsOnly()) {
        return;
      }
      File srcFile = new File(propFile.getAbsolutePath())
      URI targetRepo = new URI(map.get("artifactory_repoURI")+"/unitTest/")

      String userName = map.get("artifactory_repoUser")
      String userPwd = map.get("artifactory_repoUserPwd")
      boolean retStat = true

      if (targetRepo != null && !targetRepo.toString().isEmpty() &&
          userName != null && !userName.isEmpty()) {
         StringBuffer outputStr = new StringBuffer()
         retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.ARTIFACTORY,srcFile,
                                                   targetRepo,userName,userPwd,outputStr)
         outputStr = null
      }
      assertTrue(retStat)
   } 

   /**
    * Pull file from Artifactory repo 
    */
   void testPullFileFromArtifactoryStr() {
      if (runUnitTestsOnly()) {
        return;
      }
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcFile = new File(propFile.getAbsolutePath())
      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      URI srcRepo = new URI(map.get("artifactory_repoURI")+"/unitTest/unitTest.properties")

      String userName = map.get("artifactory_repoUser")
      String userPwd = map.get("artifactory_repoUserPwd")

      boolean retStat = true

      if (srcRepo != null && !srcRepo.toString().isEmpty() &&
          userName != null && !userName.isEmpty()) {
         retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.ARTIFACTORY,srcFile,
                                                   srcRepo,userName,userPwd)
         assertTrue(retStat)
         targetFile.delete()
         
         StringBuffer outputStr = new StringBuffer()
         retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.ARTIFACTORY,srcRepo,
                                                   targetFile,userName,userPwd,outputStr)
         outputStr = null
         if (retStat && targetFile.exists()) {
         } else {
            retStat = false
         }
         targetFile.delete()
      }
      assertTrue(retStat)
   } 

   /**
    * Pull directory from Artifactory repo failure test
    */
   void testPullFileFromArtifactoryStrFailDir() {
      if (runUnitTestsOnly()) {
        return;
      }
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcFile = new File(propFile.getAbsolutePath())
      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      URI srcRepo = new URI(map.get("artifactory_repoURI")+"/unitTest/")

      String userName = map.get("artifactory_repoUser")
      String userPwd = map.get("artifactory_repoUserPwd")

      boolean retStat = true

      if (srcRepo != null && !srcRepo.toString().isEmpty() &&
          userName != null && !userName.isEmpty()) {
         retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.ARTIFACTORY,srcFile,
                                                   srcRepo,userName,userPwd)
         assertTrue(retStat)
         targetFile.delete()
         retStat = false

         StringBuffer outputStr = new StringBuffer()
         try {
            retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.ARTIFACTORY,srcRepo,
                                                   targetFile,userName,userPwd,outputStr)
         } catch(IllegalArgumentException ex) {
            retStat = true
         } catch(Exception ex) {
         }
         outputStr = null
         targetFile.delete()
      }
      assertTrue(retStat)
   }

   /**
    * Push file to Artifactory repo failure test
    */
   void testPushFileToArtifactoryStrFail() {
      if (runUnitTestsOnly()) {
        return;
      }
      File srcFile = new File(propFile.getAbsolutePath())
      URI targetRepo = new URI("http://localhost:25/someStupidURIThatDoesNotExist")

      String userName = map.get("artifactory_repoUser")
      String userPwd = map.get("artifactory_repoUserPwd")
      boolean retStat = false

      if (targetRepo != null && !targetRepo.toString().isEmpty() &&
          userName != null && !userName.isEmpty()) {
         StringBuffer outputStr = new StringBuffer()
         retStat = true
         retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.ARTIFACTORY,srcFile,
                                                   targetRepo,userName,userPwd,outputStr)
         outputStr = null
      }
      assertFalse(retStat)
   } 

   /**
    * Pull file from Artifactory repo failure test
    */
   void testPullFileFromArtifactoryStrFail() {
      if (runUnitTestsOnly()) {
        return;
      }
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcFile = new File(propFile.getAbsolutePath())
      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      URI srcRepo = new URI("http://localhost:25343423/someStupidURIThatDoesNotExist")

      String userName = map.get("artifactory_repoUser")
      String userPwd = map.get("artifactory_repoUserPwd")

      boolean retStat = false

      if (srcRepo != null && !srcRepo.toString().isEmpty() &&
          userName != null && !userName.isEmpty()) {
         StringBuffer outputStr = new StringBuffer()
         retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.ARTIFACTORY,srcFile,
                                                   srcRepo,userName,userPwd,outputStr)
         assertFalse(retStat)
         targetFile.delete()
         outputStr.delete(0, outputStr.length())
         
         retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.ARTIFACTORY,srcRepo,
                                                   targetFile,userName,userPwd,outputStr)
         outputStr = null
         targetFile.delete()
      }
      assertFalse(retStat)
   } 

   /**
    * Pull file from Artifactory repo failure test
    */
   void testPullFileFromArtifactoryStrFailURL() {
      if (runUnitTestsOnly()) {
        return;
      }
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcFile = new File(propFile.getAbsolutePath())
      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      URI srcRepo = new URI("http://localhost:99999231/someStupidURIThatDoesNotExist")

      String userName = map.get("artifactory_repoUser")
      String userPwd = map.get("artifactory_repoUserPwd")

      boolean retStat = false

      if (srcRepo != null && !srcRepo.toString().isEmpty() &&
          userName != null && !userName.isEmpty()) {
         StringBuffer outputStr = new StringBuffer()
         retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.ARTIFACTORY,srcFile,
                                                   srcRepo,userName,userPwd,outputStr)
         assertFalse(retStat)
         targetFile.delete()
         outputStr.delete(0, outputStr.length())
         
         retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.ARTIFACTORY,srcRepo,
                                                   targetFile,userName,userPwd,outputStr)
         outputStr = null
         targetFile.delete()
      }
      assertFalse(retStat)
   }  

   /**
    * Pull non-existing file from Artifactory repo 
    */
   void testPullInvalidFileFromArtifactoryStr() {
      if (runUnitTestsOnly()) {
        return;
      }
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcFile = new File(propFile.getAbsolutePath())
      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      URI srcRepo = new URI(map.get("artifactory_repoURI")+"/unitTest/unitTest.properties")
      URI invalidSrcRepo = new URI(map.get("artifactory_repoURI")+"/unitTest/xnitTest2.properties")      

      String userName = map.get("artifactory_repoUser")
      String userPwd = map.get("artifactory_repoUserPwd")

      boolean retStat = false

      if (srcRepo != null && !srcRepo.toString().isEmpty() &&
          userName != null && !userName.isEmpty()) {
         retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.ARTIFACTORY,srcFile,
                                                   srcRepo,userName,userPwd)
         assertTrue(retStat)
         targetFile.delete()
         retStat = true
         StringBuffer outputStr = new StringBuffer()

         try {
            retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.ARTIFACTORY,invalidSrcRepo,
                                                   targetFile,userName,userPwd,outputStr)
         } catch(FileNotFoundException ex) {
            retStat = false   
         } catch(Exception ex) {
            retStat = true
         }
         outputStr = null
         targetFile.delete()
      }
      assertFalse(retStat)
   }   

   /**
    * Authent error pull
    */
   void testPullAuthErrFromArtifactoryStr() {
      if (runUnitTestsOnly()) {
        return;
      }
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcFile = new File(propFile.getAbsolutePath())
      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      URI srcRepo = new URI(map.get("artifactory_repoURI")+"/unitTest/unitTest.properties")

      String userName = map.get("artifactory_repoUser")
      String userPwd = map.get("artifactory_repoUserPwd")

      boolean retStat = false

      if (srcRepo != null && !srcRepo.toString().isEmpty() &&
          userName != null && !userName.isEmpty()) {
         retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.ARTIFACTORY,srcFile,
                                                   srcRepo,userName,userPwd)
         assertTrue(retStat)
         targetFile.delete()
         retStat = true
         StringBuffer outputStr = new StringBuffer()

         try {
            retStat = Repository.pullAssetFromRepo(ConfigPropertiesConstants.ARTIFACTORY,srcRepo,
                                                   targetFile,userName+"gifger",userPwd,outputStr)
         } catch(SecurityException ex) {
            retStat = false   
         } catch(Exception ex) {
            retStat = true
         }
         outputStr = null
         targetFile.delete()
      }
      assertFalse(retStat)
   }   

   /**
    * Authent error push
    */
   void testPushAuthErrFromArtifactoryStr() {
      if (runUnitTestsOnly()) {
        return;
      }
      Random rand = new Random()
      Long uid = rand.nextLong()

      File srcFile = new File(propFile.getAbsolutePath())
      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)

      URI srcRepo = new URI(map.get("artifactory_repoURI")+"/unitTest/unitTest.properties")

      String userName = map.get("artifactory_repoUser")
      String userPwd = map.get("artifactory_repoUserPwd")

      boolean retStat = false

      if (srcRepo != null && !srcRepo.toString().isEmpty() &&
          userName != null && !userName.isEmpty()) {
         retStat = true
         StringBuffer outputStr = new StringBuffer()

         try {
            retStat = Repository.pushAssetToRepo(ConfigPropertiesConstants.ARTIFACTORY,srcFile,
                                                      srcRepo,userName+"fdfsd",userPwd)
         } catch(SecurityException ex) {
            retStat = false   
         } catch(Exception ex) {
            retStat = true
         }
         outputStr = null
         targetFile.delete()
      }
      assertFalse(retStat)
   }   

}
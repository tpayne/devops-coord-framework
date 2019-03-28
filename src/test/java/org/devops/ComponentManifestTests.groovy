package org.devops;

import groovy.util.GroovyTestCase;
import junit.framework.*;
import junit.textui.TestRunner;

/**
 * Unit ComponentManifest tests for Utility class.
 */
public class ComponentManifestTests extends GroovyTestCase {

   File propFile = new File("."+"/src/test/resources/unitTest.properties")
   def map = Utilities.mapProperties(propFile)

   /**
    * Utility function to get tmpDir used for testing
    */
   File getTmpDir() {
      return new File((map.get("tmpDir") != null) ? map.get("tmpDir") : System.getProperty("java.io.tmpdir"))
   }

   /**
    * Unit test the adding component
    */
   void testaddComponent() {
      ComponentManifest compList = new ComponentManifest()
      compList.addComponent('comp1','1.0','approved','file:///tmp/file.zip')
      compList.addComponent('comp2','1.1','approved','file:///tmp/file1.zip')
      compList.addComponent('comp3','1.2','approved','file:///tmp/file2.zip')
      compList.addComponent('comp4','1.3','approved','file:///tmp/file3.zip')
      compList.addComponent('comp5','1.4','approved','file:///tmp/file4.zip')
      compList.addComponent('comp6','1.5','approved','file:///tmp/file5.zip')
      compList.addComponent('comp7','1.6','approved','file:///tmp/file6.zip')
      compList.addComponent('comp8','1.7','approved','file:///tmp/file7.zip') 
      //compList.getComponentList().each { k, v -> println "${k}:${v.componentName}-${v.componentVersion}-${v.componentLocation}"}  
      assertTrue(compList.getComponentList().size()==8)  
   }

   /**
    * Unit test the getting component
    */
   void testgetComponent() {
      ComponentManifest compList = new ComponentManifest()
      compList.addComponent('comp1','1.0','approved','file:///tmp/file.zip')
      compList.addComponent('comp2','1.1','approved','file:///tmp/file1.zip')
      compList.addComponent('comp3','1.2','approved','file:///tmp/file2.zip')
      compList.addComponent('comp4','1.3','approved','file:///tmp/file3.zip')
      compList.addComponent('comp5','1.4','approved','file:///tmp/file4.zip')
      compList.addComponent('comp6','1.5','approved','file:///tmp/file5.zip')
      compList.addComponent('comp7','1.6','approved','file:///tmp/file6.zip')
      compList.addComponent('comp8','1.7','approved','file:///tmp/file7.zip') 
      assertTrue(compList.getComponentList().size()==8)  
      assertEquals(compList.getComponent('comp4').componentVersion,'1.3')
      assertEquals(compList.getComponent('comp5').componentVersion,'1.4')
   }

   /**
    * Unit test getters and setters
    */
   void testGetAndSet() {
      ComponentManifest compList = new ComponentManifest()
      compList.setManifestVersion('1.0')
      compList.setManifestStatus('failed')
      assertEquals(compList.getManifestVersion(),'1.0')
      assertEquals(compList.getManifestStatus(),'failed')
      compList.addComponent('comp1','1.0','approved','file:///tmp/file.zip')
      compList.addComponent('comp2','1.1','approved','file:///tmp/file1.zip')
      assertEquals(compList.getComponent('comp1').componentLocation,'file:///tmp/file.zip')
      assertEquals(compList.getComponent('comp2').componentLocation,'file:///tmp/file1.zip')      
   }   

   /**
    * Unit test JSON coverter
    */
   void testConvert2JSON() {
      ComponentManifest compList = new ComponentManifest()
      compList.setManifestVersion('1.0')
      compList.setManifestStatus('failed')
      compList.addComponent('comp1','1.0','approved','file:///tmp/file.zip')
      compList.addComponent('comp2','1.1','approved','file:///tmp/file1.zip')
      compList.addComponent('comp3','1.2','approved','file:///tmp/file2.zip')
      compList.addComponent('comp4','1.3','approved','file:///tmp/file3.zip')
      compList.addComponent('comp5','1.4','approved','file:///tmp/file4.zip')
      compList.addComponent('comp6','1.5','approved','file:///tmp/file5.zip')
      compList.addComponent('comp7','1.6','approved','file:///tmp/file6.zip')
      compList.addComponent('comp8','1.7','approved','file:///tmp/file7.zip') 
      String manifestJSON = compList.convertManifestToJSON()
      ComponentManifest compList1 = new ComponentManifest(manifestJSON)
      assertEquals(compList1.getManifestVersion(),compList.getManifestVersion())
      assertEquals(compList1.getManifestStatus(),compList.getManifestStatus())
      assertTrue(compList1.getComponentList().size()==compList.getComponentList().size()) 
      assertEquals(compList1.getComponent('comp1').componentLocation,compList.getComponent('comp1').componentLocation)
      assertEquals(compList1.getComponent('comp2').componentLocation,compList.getComponent('comp2').componentLocation)
      //compList1.getComponentList().each { k, v -> println "${k}:${v.componentName}-${v.componentVersion}-${v.componentLocation}"} 
   } 

   /**
    * Unit test committing
    */
   void testCommit() {
      ComponentManifest compList = new ComponentManifest()
      compList.addComponent('comp1','1.0','approved','file:///tmp/file.zip')
      compList.addComponent('comp2','1.1','approved','file:///tmp/file1.zip')
      compList.addComponent('comp3','1.2','approved','file:///tmp/file2.zip')
      compList.addComponent('comp4','1.3','approved','file:///tmp/file3.zip')
      compList.addComponent('comp5','1.4','approved','file:///tmp/file4.zip')
      compList.addComponent('comp6','1.5','approved','file:///tmp/file5.zip')
      compList.addComponent('comp7','1.6','approved','file:///tmp/file6.zip')
      compList.addComponent('comp8','1.7','approved','file:///tmp/file7.zip') 
      compList.setManifestVersion('1.0')
      compList.setManifestStatus('failed')

      Random rand = new Random()
      Long uid = rand.nextLong()

      File targetFile = new File(getTmpDir().getAbsolutePath()+File.separator+"utilTest-"+uid)
      compList.setRepo(targetFile)
      targetFile.delete()
      assertTrue(compList.commit())

      ComponentManifest compList1 = new ComponentManifest(targetFile)

      assertEquals(compList1.getManifestVersion(),compList.getManifestVersion())
      assertEquals(compList1.getManifestStatus(),compList.getManifestStatus())
      assertTrue(compList1.getComponentList().size()==compList.getComponentList().size()) 
      assertEquals(compList1.getComponent('comp1').componentLocation,compList.getComponent('comp1').componentLocation)
      assertEquals(compList1.getComponent('comp2').componentLocation,compList.getComponent('comp2').componentLocation)
      //compList1.getComponentList().each { k, v -> println "${k}:${v.componentName}-${v.componentVersion}-${v.componentLocation}"} 
      compList1.addComponent('comp11','1.10','approved','file:///tmp/1file.zip')
      compList1.addComponent('comp21','1.11','approved','file:///tmp/1file1.zip')
      compList1.addComponent('comp31','1.12','approved','file:///tmp/1file2.zip')
      compList1.addComponent('comp41','1.13','approved','file:///tmp/1file3.zip')
      compList1.addComponent('comp51','1.14','approved','file:///tmp/1file4.zip')
      compList1.addComponent('comp61','1.15','approved','file:///tmp/1file5.zip')
      compList1.addComponent('comp71','1.16','approved','file:///tmp/1file6.zip')
      compList1.addComponent('comp81','1.17','approved','file:///tmp/1file7.zip') 
      int size = compList1.getComponentList().size()
      size--
      compList1.removeComponent('comp51')
      assertTrue(compList1.getComponentList().size()==size)
      compList1.updateComponent('comp81','1.17','rejected','file:///tmp/1file7.zip') 
      compList1.updateComponent('comp71','1.16','rejected','file:///tmp/1file6.zip')
      assertEquals(compList1.getComponent('comp81').componentStatus,'rejected')
      assertEquals(compList1.getComponent('comp71').componentStatus,'rejected')
      assertTrue(compList1.commit())

      ComponentManifest compList2 = new ComponentManifest(targetFile)
      assertTrue(compList1.getComponentList().size()==compList2.getComponentList().size()) 
      assertEquals(compList1.getManifestVersion(),compList2.getManifestVersion())
      assertEquals(compList1.getManifestStatus(),compList2.getManifestStatus())   
      assertEquals(compList1.getCommitter(),compList2.getCommitter())
      assertEquals(compList1.getComponent('comp1').componentLocation,compList2.getComponent('comp1').componentLocation)
      assertEquals(compList1.getComponent('comp2').componentLocation,compList2.getComponent('comp2').componentLocation)
      assertEquals(compList1.getComponent('comp81').componentStatus,compList2.getComponent('comp81').componentStatus)
      assertEquals(compList1.getComponent('comp81').componentStatus,compList2.getComponent('comp81').componentStatus)
      targetFile.delete()
   }     
}

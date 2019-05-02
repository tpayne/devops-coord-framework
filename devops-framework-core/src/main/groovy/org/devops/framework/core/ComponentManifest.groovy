/**
 * Component manifest routines for use with the framework
 */
package org.devops.framework.core;

import groovy.json.*
import java.text.SimpleDateFormat

class ComponentManifest implements Serializable {

    private static final long serialVersionUID = 1L;

    private class ManifestFile implements Serializable {
        def version
        def status
        def commitUTCDate
        def committer
        def commitComment
        Map<String,CompList> compList
    }

    private ManifestFile manifest = null
    private File repoFile = null

    /**
     * Default constructor
     */
    public ComponentManifest() {
        this.manifest = new ManifestFile()
        this.manifest.compList = new LinkedHashMap<String,CompList>(50)
    }

    /**
     * String constructor
     * 
     * @param final String - JSON string
     */
    public ComponentManifest(final String jSON) {
        try {
            def slurper = new JsonSlurper()
            this.manifest = slurper.parseText(jSON)
        } catch(Exception ex) {
            this.manifest = new ManifestFile()
            this.manifest.compList = new LinkedHashMap<String,CompList>()
        }
    }

    /**
     * File constructor
     * 
     * @param final File - JSON file
     */
    public ComponentManifest(final File jSON) {
        try {
            def slurper = new JsonSlurper()
            this.manifest = slurper.parse(jSON)
            this.repoFile = jSON
        } catch(Exception ex) {
            this.manifest = new ManifestFile()
            this.manifest.compList = new LinkedHashMap<String,CompList>()
            this.repoFile = jSON
        }
    }

    /**
     * Check the object is valid
     * @return boolean
     */
    final boolean isValid() {
        return (this.manifest.compList.size()!=0)
    }

    /**
     * Get the repo file
     * @return final File
     */    
    final File getRepo() {
        return this.repoFile
    }

    /**
     * Set the repo file
     * @param final File - repoFile
     */
    void setRepo(final File repoFile) {
        this.repoFile = repoFile
    }

    /**
     * Get the last commit comment
     * @return final String
     */     
    final String getCommitComment() {
        return manifest.commitComment
    }

    /**
     * Get the last committer
     * @return final String
     */    
     final String getCommitter() {
        return manifest.committer
    }

    /**
     * Get the last date in UTC
     * @return final String
     */    
     final String getCommitDate() {
        return manifest.commitUTCDate
    }

    /**
     * Get the manifest version
     * @return final String
     */     
    final String getManifestVersion() {
        return manifest.version
    }

    /**
     * Set the manifest version
     * @param final String - version
     */    
     void setManifestVersion(final String version) {
        manifest.version = version
    }

    /**
     * Get the manifest status
     * @return final String
     */ 
    final String getManifestStatus() {
        return manifest.status
    }

    /**
     * Set the manifest status
     * @param final String - status
     */  
    void setManifestStatus(final String status) {
        manifest.status = status
    }    

    /**
     * Get the component list
     * @return final Map
     */ 
    final Map getComponentList() {
        return manifest.compList
    }

    /**
     * Get the component data
     * @return final CompList
     */ 
    final CompList getComponent(final String compName) {
        return manifest.compList.get(compName)
    }

    /**
     * Add a component to the manifest
     * @param final String - component name
     * @param final String - component version
     * @param final String - component status
     * @param final String - component location
     */    
     void addComponent(final String compName,
                      final String version,
                      final String status,
                      final String location) {
        CompList tmp = new CompList(componentName:compName, componentVersion:version,
                                    componentStatus:status, componentLocation:location)
        manifest.compList.put(compName,tmp)
    } 

    /**
     * Update a component in the manifest
     * @param final String - component name
     * @param final String - component version
     * @param final String - component status
     * @param final String - component location
     */  
     void updateComponent(final String compName,
                         final String version,
                         final String status,
                         final String location) {
        addComponent(compName,version,status,location)
    }

    /**
     * Remove a component from the manifest
     * @param final String - component name
     */  
    void removeComponent(final String compName) {
        manifest.compList.remove(compName)
    }


    /**
     * Get the component manifest data in JSON format
     * @return final String
     */ 
    final String convertManifestToJSON() {
        def builder = new JsonBuilder(manifest)
        return builder.toString()
    }

    /**
     * Commit the manifest data to the manifest repo
     * @param final String - commit comment
     * @return boolean
     */ 
    final boolean commit(final String commitComment = null) {
        boolean retStat = true
        if (this.repoFile == null) {
            return false
        }

        if (commitComment != null) {
            this.manifest.commitComment = commitComment
        } else {
            this.manifest.commitComment = "Committed version "+manifest.version+" by "+System.getProperty("user.name")+" at "+
                        new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(Calendar.getInstance().getTime())
        }
        this.manifest.committer = System.getProperty("user.name")
        this.manifest.commitUTCDate = System.currentTimeMillis()

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(this.repoFile));
            writer.write(convertManifestToJSON());
            retStat = true
        } catch (Exception e) {
            retStat = false
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
                retStat = false
            }
        }
        return retStat
    }
}

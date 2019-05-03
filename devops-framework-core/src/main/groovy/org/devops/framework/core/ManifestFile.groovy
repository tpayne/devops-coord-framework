/**
 * Component list manifest class for use with the framework
 */
package org.devops.framework.core;

class ManifestFile implements Serializable {
    def version
    def status
    def commitUTCDate
    def committer
    def commitComment
    Map<String,CompList> compList

    private static final long serialVersionUID = 1L;

}

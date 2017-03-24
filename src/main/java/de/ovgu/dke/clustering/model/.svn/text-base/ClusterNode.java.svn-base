package de.ovgu.dke.clustering.model;

import java.util.Collection;

/**
 * 
 * @author mhermkes
 *
 */
public interface ClusterNode extends Cluster 
{
	
	public Collection<Integer> getDataInSubTree();
	
	/**
	 * Checks wether the given document belongs to the cluster or sub-clusters.
	 * The document is specified by the document id.
	 * @param dataId
	 * @return <tt>true</tt> if the given document id belongs to the cluster or 
	 * 		   sub-clusters, otherwise <tt>false</tt>.
	 */
	public boolean belongsToSubTree(int dataId);
	
	/**
	 * Returns the number of assigned data to this cluster and sub-clusters.
	 * @return
	 */
	public int numberOfDataInSubTree();
	
	/**
	 * 
	 * @return
	 */
	public int numberOfSubClusters();
	
	/**
	 * 
	 * @return
	 */
	public boolean hasSubClusters();
	
	/**
	 *  
	 * @return
	 */
	public Collection<? extends ClusterNode> getSubClusters();
}

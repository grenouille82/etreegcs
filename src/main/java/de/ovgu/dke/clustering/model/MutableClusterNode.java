package de.ovgu.dke.clustering.model;


/**
 * 
 * @author mhermkes
 *
 */
public interface MutableClusterNode<T extends ClusterNode> extends ClusterNode, MutableCluster
{	
	/**
	 * 
	 * @param cluster
	 */
	public void addSubCluster(T cluster);
	
	/**
	 * 
	 * @param idx
	 * @param cluster
	 */
	public void insertSubCluster(int idx, T cluster);
	
	/**
	 * 
	 * @param cluster
	 */
	public void removeSubCluster(ClusterNode cluster);
	
	/**
	 * Removes the sub cluster at the given index
	 * @param idx
	 * @return
	 */
	public T removeSubCluster(int idx);
	
	public void removeAllSubClusters();
}

package de.ovgu.dke.clustering.model;

import java.util.Collection;

/**
 * 
 * @author mhermkes
 *
 */
public interface Cluster
{
	/**
	 * Returns the id of the cluster.
	 * 
	 * @return
	 */
	public int getId();
	
	/**
	 * Returns all data points assigned to the cluster.
	 * 
	 * @return
	 */
	public Collection<Integer> getData();
	
	/**
	 * Checks whether the given data point belongs to the cluster.
	 * 
	 * @param dataId
	 * @return
	 */
	public boolean belongsToCluster(int dataId);
	

	/**
	 * Returns the number of data points..
	 * 
	 * @return the number of data points.
	 */
	public int size(); 
}

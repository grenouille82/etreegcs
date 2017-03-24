package de.ovgu.dke.clustering.model;



/**
 * 
 * @author mhermkes
 *
 */
public interface MutableCluster extends Cluster
{
	/**
	 * Deletes a data point from the cluster. The id of the document is removed and the 
	 * centroid is not updated. Note, that the centroid must be updated manually. The data
	 * point is represented by document id as <tt>int</tt> value.
	 * 
	 * @param dataId the represented data point as <tt>int</tt> value.
	 */
	public void deleteData(int dataId);
	
	/**
	 * Adds a data point to the cluster.
	 * 
	 * @param docId 
	 */
	public void assignData(int docId);
	
	/**
	 * Deletes all data points from the cluster.
	 *
	 */
	public void clearData();
}
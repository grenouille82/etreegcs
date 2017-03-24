package de.ovgu.dke.clustering.model;


import de.ovgu.dke.util.ObjectSet;

public interface MutablePrototypeCluster extends PrototypeCluster, MutableCluster
{	
	/**
	 * Sets the cluster centroid vector. The id of the document is stored and the 
	 * centroid is not updated. The centroid must be updated manually.
	 * 
	 * @param vector is set to be the centroid of the cluster.
	 */
	public void setCentroid(double[] data);
	
	/**
	 * Sets the cluster center as mean vector of all data points.
	 * 
	 * @param dataset
	 */
	public void computeCentroid(ObjectSet dataset);

}

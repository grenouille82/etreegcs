package de.ovgu.dke.clustering.model;

/**
 * 
 * @author mhermkes
 *
 */
public interface PrototypeCluster extends Cluster
{
	/**
	 * Returns the centroid of the cluster. 
	 * 
	 * @return
	 */
	public double[] getCentroid();
}

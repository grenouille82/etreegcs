package de.ovgu.dke.clustering.model;

import java.util.Collection;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;


/**
 * 
 * @author mhermkes
 *
 * @param <T>
 * TODO: - extends the Iterable Interface
 */
public interface ClusterModel<T extends Cluster> //extends Iterable<T>
{
	public int numberOfClusters();
	
	public T getCluster(int id);
	
	public T getClusterByData(int dataId);;
	
	public ObjectSet getDataset();
	
	public DistanceMeasure getDistanceMetric();
	
	public Collection<? extends T> getClusters();
}
		
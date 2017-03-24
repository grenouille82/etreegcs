package de.ovgu.dke.clustering.util;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public interface InterClusterDistance<T extends Cluster>
{
	public void define(ObjectSet dataset);
	
	public void define(DistanceMeasure metric);	
	
	public double distance(T a, T b);
	
	public double distance(T a, T b, ProximityMatrix matrix);
}

/*
 * Created on 21.07.2005
 */
package de.ovgu.dke.clustering.model;

import java.util.Arrays;
import java.util.Set;

import de.ovgu.dke.util.ObjectSet;


/**
 * This class describes a single cluster. A cluster has a center, data points
 * assigned to it, a label and a term vector.
 * 
 * @author kbade, mhermkes
 */
public class SimplePrototypeCluster extends SimpleCluster 
implements MutablePrototypeCluster 
{	
	/** Vector that is the centroid of the cluster. */
	protected double[] centroid;
	
	/**
	 * 
	 * @param vector
	 */
	public SimplePrototypeCluster(double[] vector) 
	{
		super();
		if(vector == null)
			throw new NullPointerException();
		centroid = vector;
	}
	
	/**
	 * 
	 * @param data
	 */
	public SimplePrototypeCluster(Set<Integer> data) 
	{
		super(data);
	}
	
	/**
	 * 
	 * @param data
	 * @param dataset
	 */
	public SimplePrototypeCluster(Set<Integer> data, ObjectSet dataset) 
	{
		super(data);
		if(dataset == null)
			throw new NullPointerException();
		if(data.size() > dataset.size())
			throw new IllegalArgumentException("Unspecified docIds");
		
		computeCentroid(dataset);
	}
	
	/**
	 * 
	 * @param dataId
	 * @param vector
	 */
	public SimplePrototypeCluster(int dataId, double[] vector)
	{
		super();
		if(vector == null)
			throw new NullPointerException();
		centroid = vector;
		data.add(dataId);
	}

	/**
	 * 
	 * @param anotherCluster
	 */
	public SimplePrototypeCluster(Cluster anotherCluster)
	{
		super(anotherCluster);
		if(anotherCluster instanceof PrototypeCluster) {
			double[] anotherCentroid = ((PrototypeCluster) anotherCluster).getCentroid();
			centroid = Arrays.copyOf(anotherCentroid, anotherCentroid.length);
		}
	}
	
	/**
	 * 
	 * @param anotherCluster
	 */
	public SimplePrototypeCluster(PrototypeCluster anotherCluster)
	{
		super(anotherCluster);
		double[] anotherCentroid = anotherCluster.getCentroid();
		centroid = Arrays.copyOf(anotherCentroid, anotherCentroid.length);
		
	}
	

	/**
	 * Allows readable access to the cluster centroid vector.
	 * 
	 * @return The document vector of the cluster centroid.
	 */
	public double[] getCentroid() 
	{
		return centroid;
	}

	/**
	 * Sets the cluster center vector.
	 * 
	 * @param vector
	 *            Vector, which describes the center of the cluster.
	 */
	public void setCentroid(double[] vector) 
	{
		if(vector == null)
			throw new NullPointerException();
		centroid = vector;
	}


	
	/**
	 * Sets the cluster center as mean vector of all data points.
	 * 
	 * @param results
	 *            The result list of all documents used for clustering.
	 */
	public void computeCentroid(ObjectSet dataset) 
	{
		int n = data.size();
		if (n == 0)
			return;
		if (n == 1) {
			double[] vector = dataset.get(data.iterator().next()).getRepresentation();
			centroid = Arrays.copyOf(vector, vector.length);
		} else {
			int dim		= dataset.getObjectSize();
			double v[]	= null;
			centroid 	= new double[dim];
			
			
			for(Integer dataId : data)
			{
				for(int i=0; i<dim; i++)
				{
					v = dataset.get(dataId).getRepresentation();
					centroid[i] += v[i]; 
				}
			}
			for(int i=0; i<dim; i++)
				centroid[i] /= n;
		}
	}
	

}

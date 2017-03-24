package de.ovgu.dke.clustering.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class SimpleCluster implements MutableCluster
{
	private final static AtomicInteger ID_GENERATOR = new AtomicInteger();
	
	/** An unique identifier of the cluster */
	private int id;
	
	/**
	 * List of assigned data points. Each data point is represented by the id in
	 * the result element set.
	 */
	protected Set<Integer> data;
	
	/** Creates an empty Cluster*/
	public SimpleCluster() 
	{
		id	 = ID_GENERATOR.getAndIncrement();
		data = new HashSet<Integer>();
		
	}
	
	/**
	 * 
	 * @param data
	 */
	public SimpleCluster(Set<Integer> data)
	{
		if(data == null)
			throw new NullPointerException();
		id = ID_GENERATOR.getAndIncrement();
		this.data = data;
	}
	
	/**
	 * 
	 * @param anotherCluster
	 */
	public SimpleCluster(Cluster anotherCluster)
	{
		if(anotherCluster == null)
			throw new NullPointerException();
		id = anotherCluster.getId();
		if(anotherCluster instanceof ClusterNode) 
			data = new HashSet<Integer>(((ClusterNode) anotherCluster).getDataInSubTree());
		else
			data = new HashSet<Integer>(anotherCluster.getData());
	}
	

	/**
	 * Returns the unique identifier of the cluster.
	 * 
	 * @return the identifier as an <tt>Integer</tt> object.
	 */
	public final int getId() 
	{
		return id;
	}
	
	/**
	 * Returns the number of data elements.
	 * 
	 * @return The number of data elements.
	 */
	public int size() 
	{
		return data.size();
	}
	
	/**
	 * 
	 * @return
	 */
	public Collection<Integer> getData()
	{
		return Collections.unmodifiableCollection(data);
	}


	
	/**
	 * 
	 * @param docID
	 */
	public void assignData(int docID) 
	{
		data.add(docID);
	}
	
	/**
	 * 
	 * @param docID
	 */
	public void deleteData(int docID) 
	{
		data.remove(docID);
	}
	

	/** Deletes all data points from the cluster. */
	public void clearData() 
	{
		data.clear();
	}
	
	/**
	 * 
	 * @param docID
	 * @return
	 */
	public boolean belongsToCluster(int dataId) 
	{
		return data.contains(dataId);
	}	
	
	/**
	 * 
	 * @return
	 */
	public final int hashCode()
	{
		return id;
	}
	
	/**
	 * 
	 * @param anObject
	 * @return
	 */
	public final boolean equals(Object anObject)
	{
		if(anObject instanceof Cluster) 
			return id == ((Cluster) anObject).getId();
		return false;		
	}
	
	public String toString()
	{
		return "Cluster_"+id;
	}
}

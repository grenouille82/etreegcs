package de.ovgu.dke.clustering.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.ovgu.dke.util.ObjectSet;

/**
 * 
 * @author mhermkes
 *
 */
public class SimpleFlatClusterModel extends AbstractClusterModel<Cluster>
implements FlatClusterModel
{
	protected List<MutableCluster> clusters;
	
	
	/**
	 * 
	 * @param dataset
	 */
	public SimpleFlatClusterModel(ObjectSet dataset)
	{
		super(dataset);
		clusters = new ArrayList<MutableCluster>();
	}
	
	/**
	 * 
	 * @param dataset
	 * @param clusters
	 */
	public SimpleFlatClusterModel(ObjectSet dataset, Collection<? extends MutableCluster> clusters)
	{
		super(dataset);
		if(clusters == null)
			throw new NullPointerException();
		
		this.clusters = new ArrayList<MutableCluster>(clusters);
		//TODO: check for duplicates
	}
	
	
	/**
	 * 
	 * @param cluster
	 * @return
	 */
	public boolean addCluster(MutableCluster cluster)
	{
		validateClusterData(cluster);
		return fastAddCluster(cluster);
	}
	
	/**
	 * 
	 * @param cluster
	 * @return
	 */
	public boolean fastAddCluster(MutableCluster cluster)
	{
		if(cluster == null)
			throw new NullPointerException();
		
		boolean retVal = false;
		if(!clusters.contains(cluster)) {
			clusters.add(cluster);
			retVal = true;
		}
		return retVal;
	}
	
	public MutableCluster getClusterAt(int idx) 
	{
		return clusters.get(idx);
	}

	public MutableCluster getCluster(int id) 
	{
		MutableCluster retVal = null;
		for(MutableCluster cluster : clusters)
		{
			if(id == cluster.getId()) {
				retVal = cluster;
				break;
			}
		}
		return retVal;
	}
	
	/**
	 * 
	 * @param idx
	 * @return
	 */
	public MutableCluster removeClusterAt(int idx)
	{
		return clusters.remove(idx);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public MutableCluster removeCluster(int id)
	{
		MutableCluster retVal = null;
		Iterator<MutableCluster> it = clusters.iterator();
		while(it.hasNext()) {
			MutableCluster cluster = it.next();
			if(cluster.getId() == id) {
				it.remove();
				retVal = cluster;
				break;
			}
		}
		return retVal;
	}
	

	public int numberOfClusters() 
	{
		return clusters.size();
	}

	public Iterator<MutableCluster> iterator() 
	{
		return clusters.iterator();
	}

	/**
	 * 
	 */
	public MutableCluster getClusterByData(int docId) 
	{
		MutableCluster retVal = null;
		for(MutableCluster cluster : clusters)
		{
			if(cluster.belongsToCluster(docId)) {
				retVal = cluster;
				break;
			}	
		}
		return retVal;
	}
	
	public Collection<MutableCluster> getClusters()
	{
		return clusters;
	}
	
	/**
	 * 
	 * @param cluster
	 */
	protected final void validateClusterData(Cluster cluster)
	{
		Collection<Integer> data = cluster.getData();
		int ub = dataset.size()-1;
		for(int docId : data)
		{
			if(docId < 0 || docId > ub)
				throw new IllegalArgumentException("dataset does not contain docId: " + docId);
		}
	}
}




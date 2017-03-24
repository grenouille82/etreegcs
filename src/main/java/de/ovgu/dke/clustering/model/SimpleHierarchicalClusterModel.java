package de.ovgu.dke.clustering.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import de.ovgu.dke.tree.LinkedTree;
import de.ovgu.dke.util.ObjectSet;


/**
 * 
 * @author mhermkes
 *
 */
public class SimpleHierarchicalClusterModel extends AbstractClusterModel<ClusterNode> 
implements HierarchicalClusterModel
{
	protected MutableClusterNode root;
	
	protected HashMap<Integer, MutableClusterNode> idClusterMap;
	
	protected LinkedTree<Integer> structure;
	
	/**
	 * 
	 * @param dataset
	 */
	public SimpleHierarchicalClusterModel(ObjectSet dataset) 
	{
		super(dataset);
		structure = new LinkedTree<Integer>();
		idClusterMap = new HashMap<Integer, MutableClusterNode>();
	}
	
	/**
	 * 
	 * @param dataset
	 * @param cluster
	 */
	public SimpleHierarchicalClusterModel(ObjectSet dataset,
										  MutableClusterNode cluster)
	{
		super(dataset);
		if(cluster == null)
			throw new NullPointerException();
		
		root = cluster;
		idClusterMap = new HashMap<Integer, MutableClusterNode>();
		initialize(root);
	}
	
	
	public ClusterNode getRootCluster() 
	{
		return root;
	}
	
	/**
	 * 
	 *
	 */
	public void setRootCluster(MutableClusterNode cluster)
	{
		if(cluster == null) {
			structure.clear();
			idClusterMap.clear();
		} else {
			initialize(cluster);
		}
		root = cluster;
	}

	public MutableClusterNode getCluster(int id) 
	{
		return idClusterMap.get(id);
	}

	public int numberOfClusters() 
	{
		return structure.size();
	}
	
	public MutableClusterNode getClusterByData(int dataId) 
	{
		MutableClusterNode retVal = null;
		for(MutableClusterNode cluster : idClusterMap.values())
		{
			if(cluster.belongsToCluster(dataId)) 
				retVal = cluster;
		}
		return retVal;
	}
	
	public LinkedTree<Integer> getClusterStructure()
	{
		return structure;
	}
	
	public void update()
	{
		initialize(root);
	}
	
	public Collection<MutableClusterNode> getClusters()
	{
		return Collections.unmodifiableCollection(idClusterMap.values());
	}
	
	public Collection<MutableClusterNode> getLeafClusters()
	{
		Collection<MutableClusterNode> retVal = new LinkedList<MutableClusterNode>();
		for(MutableClusterNode cluster : idClusterMap.values())
		{
			if(!cluster.hasSubClusters())
				retVal.add(cluster);
		}
		return retVal;
	}
	

	/**
	 * 
	 * @param cluster
	 * @return
	 */
	protected void initialize(MutableClusterNode cluster)
	{
		structure = new LinkedTree<Integer>(cluster.getId());
		idClusterMap = new HashMap<Integer, MutableClusterNode>();
		
		
		LinkedList<ClusterNode> list = new LinkedList<ClusterNode>();
		list.add(cluster);
		ClusterNode parent = null;
		while(!list.isEmpty())
		{
			parent = list.removeFirst();
			idClusterMap.put(parent.getId(), (MutableClusterNode) parent);
			Collection<? extends ClusterNode> children = parent.getSubClusters();
			for(ClusterNode cl : children)
			{
				if(!(cl instanceof MutableClusterNode))
					throw new IllegalArgumentException("cluster node is not mutable.clusterId: " + cluster.getId());
				structure.appendElement(parent.getId(),cl.getId());
				list.addLast(cl);
			}
		}
	}


}

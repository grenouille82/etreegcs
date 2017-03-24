package de.ovgu.dke.clustering.algorithm.hierarchical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.ovgu.dke.clustering.model.ClusterNode;
import de.ovgu.dke.clustering.model.SimpleClusterNode;

//NOTE: totalsize only propagated on level up  by changing the hierarchy
// workaround: propgate up to the root node this changes or permit to change
// the cluster structure
public class HAClusterNode extends SimpleClusterNode 
{
	protected HAClusterNode parent;
	
	protected double weight;
	
	protected int totalSize;
	
	public HAClusterNode()
	{
		super();
	}
	
	public HAClusterNode(double weight)
	{
		this();
		this.weight = weight;
	}
	
	/**
	 * 
	 * @param weight
	 * @param data
	 */
	public HAClusterNode(double weight, Set<Integer> data)
	{
		super(data);
		this.weight = weight;
	}
	
	
	public HAClusterNode getParent()
	{
		return parent;
	}
	
	public void setParent(HAClusterNode node)
	{
		if(parent != null) 
			parent.subClusters.remove(node);
		parent = node;
		if(node != null) {
			node.subClusters.add(this);
			node.totalSize += totalSize;
		}
	}
	
	public boolean hasParent()
	{
		return parent != null;
	}
	
	public double getWeight()
	{
		return weight;
	}
	
	public void setWeight(double weight)
	{
		this.weight = weight;
	}

	public void assignData(int dataId)
	{
		super.assignData(dataId);
		totalSize++;
	}
	
	public void clearData()
	{
		totalSize -= data.size();
		super.clearData();
	}
	
	/**
	 * 
	 * @return
	 */
	public Collection<? extends HAClusterNode> getSubClusters() 
	{
		List<HAClusterNode> retVal = new ArrayList<HAClusterNode>(subClusters.size());
		for(SimpleClusterNode subCluster : subClusters)
			retVal.add((HAClusterNode) subCluster);
		return retVal;
	}

	public void addSubCluster(SimpleClusterNode cluster) 
	{
		if(cluster == null)
			throw new NullPointerException();
		if(!(cluster instanceof HAClusterNode))
			throw new IllegalArgumentException("cluster must be of the type HAClusterNode");
		
		subClusters.add((HAClusterNode) cluster);
	}
	
	public void addSubCluster(HAClusterNode cluster)
	{
		if(cluster == null)
			throw new NullPointerException();
		
		if(cluster.parent != null)
			cluster.parent.totalSize -= cluster.totalSize;
		cluster.parent = this;
		subClusters.add(cluster);
		totalSize += cluster.totalSize;
	}

	public void insertSubCluster(int idx, SimpleClusterNode cluster) 
	{
		if(cluster == null)
			throw new NullPointerException();
		if(!(cluster instanceof HAClusterNode))
			throw new IllegalArgumentException("cluster must be of the type HAClusterNode");
		
		insertSubCluster(idx, (HAClusterNode) cluster);
	}
	
	public void insertSubCluster(int idx, HAClusterNode cluster)
	{
		if(cluster == null)
			throw new NullPointerException();
	
		if(cluster.parent != null)
			cluster.parent.totalSize -= cluster.totalSize;
		cluster.parent = this;
		subClusters.add(idx, cluster);
		totalSize += cluster.totalSize;
	}

	public void removeSubCluster(ClusterNode cluster) 
	{
	   if(subClusters.remove(cluster))
		   ((HAClusterNode) cluster).parent = null;
	}
	
	public void removeAllSubClusters()
	{
		for(SimpleClusterNode subCluster : subClusters)
			((HAClusterNode) subCluster).parent = null;
		super.removeAllSubClusters();
		totalSize = data.size();
	}

	public HAClusterNode removeSubCluster(int idx) 
	{
		HAClusterNode remCluster = (HAClusterNode) subClusters.remove(idx);
		remCluster.parent = null;
		totalSize -= remCluster.totalSize;
		return remCluster;
	}

	public int numberOfDataInSubTree()
	{
		return totalSize;
	}
}

package de.ovgu.dke.clustering.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SimpleClusterNode extends SimpleCluster
implements MutableClusterNode<SimpleClusterNode>
{	
	protected List<SimpleClusterNode> subClusters;
	
	//protected HashSet<Integer> dataInSubClusters;
	
	/** Creates an empty Cluster*/
	public SimpleClusterNode() 
	{
		super();
		initialize();
	}
	
	/**
	 * 
	 * @param data
	 */
	public SimpleClusterNode(Set<Integer> data)
	{
		super(data);
		initialize();
	}
	
	public SimpleClusterNode(Cluster anotherCluster)
	{
		super(anotherCluster);
		initialize();
		if(anotherCluster instanceof ClusterNode) {
			for(ClusterNode child : ((ClusterNode)anotherCluster).getSubClusters())
				subClusters.add(new SimpleClusterNode(child));
		}
	}
	
	public SimpleClusterNode(ClusterNode anotherCluster)
	{
		super(anotherCluster);
		initialize();
		for(ClusterNode child : anotherCluster.getSubClusters())
			subClusters.add(new SimpleClusterNode(child));
	}
	
	/**
	 * 
	 * @param dataId
	 * @return
	 */
	public boolean belongsToSubTree(int dataId) 
	{
		if(belongsToCluster(dataId))
			return true;
		
		boolean retVal = false;
		for(ClusterNode child : subClusters) {
			if(child.belongsToCluster(dataId)) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<Integer> getDataInSubTree() 
	{
		HashSet<Integer> retVal = new HashSet<Integer>();
		retVal.addAll(data);
		for(ClusterNode child : subClusters)
			retVal.addAll(child.getDataInSubTree());
		return retVal;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<? extends SimpleClusterNode> getSubClusters() 
	{
		return Collections.unmodifiableCollection(subClusters);
	}

	/**
	 * 
	 * @return
	 */
	public int numberOfDataInSubTree() 
	{	
		int retVal = size();
		for(ClusterNode child : subClusters)
			retVal += child.numberOfDataInSubTree();
		return retVal;
	}

	/**
	 * 
	 * @return
	 */
	public int numberOfSubClusters() 
	{
		return subClusters.size();
	}

	public void addSubCluster(SimpleClusterNode cluster) 
	{
		if(cluster == null)
			throw new NullPointerException();
		subClusters.add(cluster);
	}

	public void insertSubCluster(int idx, SimpleClusterNode cluster) 
	{
		if(cluster == null)
			throw new NullPointerException();
		subClusters.add(idx, cluster);
	}

	public void removeSubCluster(ClusterNode cluster) 
	{
		subClusters.remove(cluster);
	}

	public SimpleClusterNode removeSubCluster(int idx) 
	{
		return subClusters.remove(idx);
	}
    
	public boolean hasSubClusters()
	{
		return !subClusters.isEmpty();
	}
	
	public void removeAllSubClusters()
	{
		subClusters = new LinkedList<SimpleClusterNode>();
	}
	
    /**
     * 
     *
     */
    protected void initialize()
    {
    	subClusters = new LinkedList<SimpleClusterNode>();
    }
}


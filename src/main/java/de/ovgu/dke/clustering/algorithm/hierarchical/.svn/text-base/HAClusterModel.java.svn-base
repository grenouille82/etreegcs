package de.ovgu.dke.clustering.algorithm.hierarchical;


import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.clustering.model.MutableClusterNode;
import de.ovgu.dke.clustering.model.SimpleHierarchicalClusterModel;
import de.ovgu.dke.util.ObjectSet;

public class HAClusterModel extends SimpleHierarchicalClusterModel
implements HierarchicalClusterModel
{

	public HAClusterModel(ObjectSet dataset) 
	{
		super(dataset);
	}

	/**
	 * 
	 * @param dataset
	 * @param cluster
	 */
	public HAClusterModel(ObjectSet dataset, HAClusterNode cluster)
	{
		super(dataset, cluster);
	}
	
	
	public HAClusterNode getRootCluster() 
	{
		return (HAClusterNode) root;
	}
	
	/**
	 * 
	 *
	 */
	public void setRootCluster(MutableClusterNode cluster)
	{
		if(!(cluster instanceof HAClusterNode))
			throw new IllegalArgumentException("cluster must be of the type HAClusterNode");
		setRootCluster((HAClusterNode)cluster);
	}
	
	public void setRootCluster(HAClusterNode cluster)
	{
		cluster.setParent(null);
		super.setRootCluster(cluster);
	}

	public HAClusterNode getCluster(int id) 
	{
		
		return (HAClusterNode) super.getCluster(id);
	}
	
	public HAClusterNode getClusterByData(int dataId) 
	{
		return (HAClusterNode) super.getClusterByData(dataId);
	}
	
	public void setDataset(ObjectSet dataset)
	{
		if(dataset == null)
			throw new NullPointerException();
		this.dataset = dataset;
	}
}

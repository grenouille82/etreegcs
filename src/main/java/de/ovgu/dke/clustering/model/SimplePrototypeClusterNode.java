package de.ovgu.dke.clustering.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.ovgu.dke.util.ObjectSet;

/**
 * 
 * @author mhermkes
 *
 */
public class SimplePrototypeClusterNode extends SimplePrototypeCluster
implements MutablePrototypeClusterNode<SimplePrototypeClusterNode>
{
	protected List<SimplePrototypeClusterNode> subClusters;
	
	protected HashSet<Integer> dataInSubTree;
	
	public SimplePrototypeClusterNode(double[] vector)
	{
		super(vector);
		initialize();
	}
	
	public SimplePrototypeClusterNode(int dataId, double[] vector) 
	{
		super(dataId, vector);
		initialize();
	}
	
	/**
	 * 
	 * @param dataIds
	 * @param dataset
	 */
	public SimplePrototypeClusterNode(Set<Integer> data, ObjectSet dataset) 
	{
		super(data, dataset);
		initialize();
	}

	
	public SimplePrototypeClusterNode(Cluster anotherCluster)
	{
		super(anotherCluster);
		initialize();
		if(anotherCluster instanceof ClusterNode) {
			dataInSubTree.addAll(((ClusterNode)anotherCluster).getDataInSubTree());
			for(ClusterNode child : ((ClusterNode)anotherCluster).getSubClusters())
				subClusters.add(new SimplePrototypeClusterNode(child));
		}
		
	}
	
	public SimplePrototypeClusterNode(PrototypeClusterNode anotherCluster)
	{
		super(anotherCluster);
		initialize();
		dataInSubTree.addAll(anotherCluster.getDataInSubTree());
		for(ClusterNode child : anotherCluster.getSubClusters())
			subClusters.add(new SimplePrototypeClusterNode(child));
	}
	
	/**
	 * 
	 * @param docId
	 * @return
	 */
	public boolean belongsToSubTree(int docId) 
	{
		return dataInSubTree.contains(docId) || belongsToCluster(docId);
	}

	/**
	 * 
	 * @return
	 */
	public Collection<Integer> getDataInSubTree() 
	{
		HashSet<Integer> retVal = new HashSet<Integer>(numberOfDataInSubTree());
		retVal.addAll(dataInSubTree);
		retVal.addAll(data);
		return retVal;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<? extends SimplePrototypeClusterNode> getSubClusters() 
	{
		return Collections.unmodifiableCollection(subClusters);
	}

	/**
	 * 
	 * @return
	 */
	public int numberOfDataInSubTree() 
	{
		return data.size()+dataInSubTree.size();
	}

	/**
	 * 
	 * @return
	 */
	public int numberOfSubClusters() 
	{
		return subClusters.size();
	}

	public void addSubCluster(SimplePrototypeClusterNode cluster) 
	{
		if(cluster == null)
			throw new NullPointerException();
		
		subClusters.add(cluster);
		dataInSubTree.addAll(cluster.getDataInSubTree());
	}

	public void insertSubCluster(int idx, SimplePrototypeClusterNode cluster) 
	{
		if(cluster == null)
			throw new NullPointerException();
		
		subClusters.add(idx, cluster);
		dataInSubTree.addAll(cluster.getDataInSubTree());
	}

	public void removeSubCluster(ClusterNode cluster) 
	{
		subClusters.remove(cluster);
		dataInSubTree.removeAll(cluster.getDataInSubTree());
	}
	
	public void removeAllSubClusters()
	{
		subClusters	  = new LinkedList<SimplePrototypeClusterNode>();
		dataInSubTree = new HashSet<Integer>();
	}
	
	public boolean hasSubClusters()
	{
		return !subClusters.isEmpty();
	}

	public SimplePrototypeClusterNode removeSubCluster(int idx) 
	{
		SimplePrototypeClusterNode cluster = subClusters.remove(idx);
		if(cluster != null)
			dataInSubTree.removeAll(cluster.getDataInSubTree());
		return cluster;
	}
	
	public void computeCentroid(ObjectSet dataset)
	{
		super.computeCentroid(dataset);
		int dim	 = dataset.getObjectSize();
		centroid = new double[dim];
		
		int n = subClusters.size();
		double[] weights = new double[n];
		double[][] childCentroids = new double[n][];
		double totalNData = 0d;
		int i=0;
		for(MutablePrototypeClusterNode child : subClusters)
		{
			child.computeCentroid(dataset);
			childCentroids[i] = child.getCentroid();
			weights[i] = child.numberOfDataInSubTree();
			totalNData += weights[i++];
		}
		
		if(size() > 0) {
			n++;
			totalNData += size();
			double weight = (double) size()/ totalNData;
			for(i=0; i<dim; i++)
				centroid[i] *= weight;
		}
		
		for(i=0; i<weights.length; i++)
			weights[i] /= totalNData;
		
		for(i=0; i<dim; i++)
		{
			for(int j=0; j<weights.length; j++)
				centroid[i] += weights[j]*childCentroids[j][i];
			centroid[i] /= n;
		}
	}
    
    
    /**
     * 
     *
     */
    private final void initialize()
    {
    	subClusters = new LinkedList<SimplePrototypeClusterNode>();
    	dataInSubTree = new HashSet<Integer>();
    }
}

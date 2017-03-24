package de.ovgu.dke.clustering.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import de.ovgu.dke.clustering.algorithm.flat.KMeansModel;
import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.ClusterModel;
import de.ovgu.dke.clustering.model.ClusterNode;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.util.ObjectSet;

/**
 * 
 * 
 * @author Marcel Hermkes
 *
 */
public class ClusterConfusionMatrix
{
	public static ClusterConfusionMatrix createClusterConfusionMatrix(FlatClusterModel model)
	{
		if(model==null)
			throw new NullPointerException();
		
		ObjectSet dataset = model.getDataset();
		if(!dataset.dataClassified())
			throw new IllegalArgumentException("dataset contains no supervision");
		
		Collection<String> classLabels 	= collectClassLabels(dataset);
		Collection<Cluster> clusters 	= new LinkedList<Cluster>(model.getClusters());
		ClusterConfusionMatrix retVal 	= new ClusterConfusionMatrix(clusters, classLabels);
		for(Cluster c : clusters) 
		{
			String label = null;
			Collection<Integer> data = c.getData();
			for(int i : data)
			{
				label = dataset.get(i).getClassLabel();
				if(label != null)
					retVal.incrementCase(c, label);
			}
		}
		//System.out.println(retVal.toString());
		return retVal;
	}
	
	public static ClusterConfusionMatrix createClusterConfusionMatrix(HierarchicalClusterModel model, boolean cumulated)
	{
		if(model==null)
			throw new NullPointerException();
		
		ObjectSet dataset = model.getDataset();
		if(!dataset.dataClassified())
			throw new IllegalArgumentException("dataset contains no supervision");
		
		Collection<String> classLabels 		= collectClassLabels(dataset);
		Collection<ClusterNode> clusters 	= new LinkedList<ClusterNode>(model.getClusters());
		ClusterConfusionMatrix retVal 		= new ClusterConfusionMatrix(clusters, classLabels);
		System.out.println(clusters.size());
		for(ClusterNode c : clusters) 
		{
			String label = null;
			//TODO: c.getDataInSubTree() can be very time consuming depending on the ClusterNode
			//      implementation. To cumulate the values implement a method that do a postorder
			//      traversal, so that the values of a node can be computed efficiently by their
			//      immediate child nodes
			Collection<Integer> data = (cumulated) ? c.getDataInSubTree() : c.getData();
			for(int i : data)
			{
				label = dataset.get(i).getClassLabel();
				if(label != null)
					retVal.incrementCase(c, label);
			}
		}
		//this is a bad hack
		if(cumulated)
			retVal.setClassLabelCountsToMax();
		
		return retVal;
	}
	
	private final static Collection<String> collectClassLabels(ObjectSet dataset)
	{
		HashSet<String> retVal = new HashSet<String>();
		int n = dataset.size();
		String label = null;
		for(int i=0; i<n; i++) 
		{
			label = dataset.get(i).getClassLabel();
			if(label != null)
				retVal.add(label);
		}
		return retVal;
	}
	
	/**
	 * 
	 */
	private HashMap<String, Integer> classLabelIndices;
	
	private HashMap<Cluster, Integer> clusterIndices;
	
	private Cluster clusters[];
	
	private String classLabels[];
	
	private int totalClusterCases[];
	
	private int totalClassLabelCases[];
	                              
	                           
	/**
	 * 
	 */
	private int matrix[][];
	
	/**
	 * 
	 */
	private int totalCases;
	
	/**
	 *
	 */
	public ClusterConfusionMatrix(Collection<? extends Cluster> clusters, Collection<String> classLabels)
	{
		if(clusters == null)
			throw new NullPointerException();
		if(classLabels == null)
			throw new NullPointerException();
		
		int xDim = clusters.size();
		int yDim = classLabels.size();
		
		matrix = new int[xDim][yDim];
		totalClusterCases	 = new int[xDim];
		totalClassLabelCases = new int[yDim];
		
		this.clusters	 = new Cluster[xDim];
		this.classLabels = new String[yDim];
		
		clusterIndices 		= new HashMap<Cluster, Integer>(xDim);
		classLabelIndices	= new HashMap<String, Integer>(yDim);
		
		int i=0;
		for(Cluster c : clusters)
		{
			this.clusters[i] = c;
			clusterIndices.put(c, i++);
		}
		i=0;
		for(String label : classLabels)
		{
			this.classLabels[i] = label;
			classLabelIndices.put(label, i++);
		}
	}
	

	void setClassLabelCountsToMax()
	{
		Arrays.fill(totalClassLabelCases, 0);
		totalCases = 0;
		for(int i=0, n=matrix.length; i<n; i++)
		{
			for(int j=0, m=matrix[i].length; j<m; j++)
				totalClassLabelCases[j] = Math.max(matrix[i][j], totalClassLabelCases[j]);
		}
		for(int i=0,n=totalClassLabelCases.length; i<n; i++)
			totalCases += totalClassLabelCases[i];
	}
	
	/**
	 */
	public int count(Cluster cluster, String classLabel)
	{
		int clusterIndex = containsCheck(cluster);
		int labelIndex	 = containsCheck(classLabel);
		return count(clusterIndex, labelIndex);
	}
	
	/**
	 * 
	 */
	public int count(int clusterIndex, int labelIndex)
	{
		clusterRangeCheck(clusterIndex);
		labelRangeCheck(labelIndex);
		
		return matrix[clusterIndex][labelIndex];
	}

	public int totalClusterCount(Cluster cluster)
	{
		int clusterIndex = containsCheck(cluster);
		return totalClusterCount(clusterIndex);
	}
	
	public int totalClusterCount(int index)
	{
		clusterRangeCheck(index);
		return totalClusterCases[index];
	}
	
	public int totalClassLabelCount(String label)
	{
		int labelIndex = containsCheck(label);
		return totalClassLabelCount(labelIndex);
	}
	
	public int totalClassLabelCount(int index)
	{
		labelRangeCheck(index);
		return totalClassLabelCases[index];
	}

	
	/**
	 * 
	 */
	public int totalCount() 
	{
		return totalCases;
	}

	
	/**
	 * 
	 */
	public void incrementCase(Cluster cluster, String classLabel)
	{
		int clusterIndex = containsCheck(cluster);
		int labelIndex	 = containsCheck(classLabel);
		incrementCase(clusterIndex, labelIndex);
	}
	
	/**
	 *
	 */
	public void incrementCase(int clusterIndex, int labelIndex)
	{
		clusterRangeCheck(clusterIndex);
		labelRangeCheck(labelIndex);
		
		matrix[clusterIndex][labelIndex]++;
		totalClusterCases[clusterIndex]++;
		totalClassLabelCases[labelIndex]++;
		totalCases++;
	}
		
	/**
	 * 
	 */
	public void addCases(Cluster cluster, String classLabel, int nCases)
	{
		int clusterIndex = containsCheck(cluster);
		int labelIndex	 = containsCheck(classLabel);
		addCases(clusterIndex, labelIndex, nCases);
	}
	
	/**
	 * 
	 */
	public void addCases(int clusterIndex, int labelIndex, int nCases)
	{
		validateCases(nCases);
		clusterRangeCheck(clusterIndex);
		labelRangeCheck(labelIndex);
		
		matrix[clusterIndex][labelIndex] += nCases;
		totalClusterCases[clusterIndex]	 += nCases;
		totalClassLabelCases[labelIndex] += nCases;
		totalCases += nCases;
	}
	
	

	/**
	 * 
	 */
	public Cluster getClusterByIndex(int index)
	{
		clusterRangeCheck(index);
		return clusters[index];
	}
	
	/**
	 * 
	 */
	public String getClassLabelByIndex(int index)
	{
		labelRangeCheck(index);
		return classLabels[index];
	}

	/**
	 *
	 */
	public Collection<Cluster> getClusters() 
	{
		return Arrays.asList(clusters);
	}
	
	
	/**
	 *
	 */
	public Collection<String> getClassLabels() 
	{
		return Arrays.asList(classLabels);
	}

	/**
	 * 
	 * @param category
	 * @return
	 */
	public boolean containsCluster(Cluster cluster)
	{
		return clusterIndices.containsKey(cluster);
	}
	
	/**
	 * 
	 * @param category
	 * @return
	 */
	public boolean containsClassLabel(String label)
	{
		return classLabelIndices.containsKey(label);
	}
	
	
	public int indexOfCluster(Cluster cluster)
	{
		Integer index = clusterIndices.get(cluster);
		return (index == null) ? -1 : index;
	}
	
	public int indexOfClassLabel(String label)
	{
		Integer index = classLabelIndices.get(label);
		return (index == null) ? -1 : index;
	}
	
	/**
	 * 
	 */
	public int numberOfClusters() 
	{
		return clusters.length;
	}
	
	public int numberOfClassLabels()
	{
		return classLabels.length;
	}
	
	/**
	 * 
	 * @return
	 */
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i<classLabels.length; i++)
		{
			builder.append('\t');
			builder.append(classLabels[i]);
		}
		builder.append("\tTotal");
				
		for(int i=0; i<clusters.length; i++)
		{
			builder.append('\n');
			builder.append(clusters[i].getId());
			builder.append(':');
			for(int j=0; j<classLabels.length; j++)
			{
				builder.append('\t');
				builder.append(matrix[i][j]);
			}
			builder.append('\t');
			builder.append(totalClusterCases[i]);
		}
		
		builder.append("\nTotal:");
		for(int i=0; i<classLabels.length; i++)
		{
			builder.append('\t');
			builder.append(totalClassLabelCases[i]);
		}
		builder.append('\t');
		builder.append(totalCases);
		return builder.toString();
	}
	
	/**
	 * 
	 */
	public void clearCases() 
	{
	}

	
	/**
	 * 
	 * @param element
	 * @return
	 * 
	 * @throws NoSuchElementException
	 */
	private final int containsCheck(Cluster cluster)
	{	
		Integer retVal = clusterIndices.get(cluster);
		if(retVal == null) {
			throw new NoSuchElementException("Cluster: " + cluster.getId());
		}
		return retVal;
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 * 
	 * @throws NoSuchElementException
	 */
	private final int containsCheck(String label)
	{	
		Integer retVal = classLabelIndices.get(label);
		if(retVal == null) {
			throw new NoSuchElementException("ClassLabel: " + label);
		}
		return retVal;
	}
	
	/**
	 * 
	 * @param nCases
	 */
	private final void validateCases(int nCases)
	{
		if(nCases < 0) {
			throw new IllegalArgumentException(
			"Number of cases must be positive. Cases=" + nCases);
		}
	}
	
	
	/**
	 * 
	 * @param index
	 */
	private final void clusterRangeCheck(int index)
	{
		if(index < 0 || index >= clusters.length)
			throw new IndexOutOfBoundsException(
			"Index: " + index + ", Size: " + clusters.length);
	}
	
	/**
	 * 
	 * @param index
	 */
	private final void labelRangeCheck(int index)
	{
		if(index < 0 || index >= classLabels.length)
			throw new IndexOutOfBoundsException(
			"Index: " + index + ", Size: " + classLabels.length);
	}
}

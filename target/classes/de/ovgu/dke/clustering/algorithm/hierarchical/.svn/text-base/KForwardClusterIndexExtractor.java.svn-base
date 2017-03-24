package de.ovgu.dke.clustering.algorithm.hierarchical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import de.ovgu.dke.clustering.algorithm.competitive.ArrayIndexedMST;
import de.ovgu.dke.clustering.model.SimpleClusterNode;
import de.ovgu.dke.clustering.util.ProximityMatrix;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Settings;

public class KForwardClusterIndexExtractor implements ClusterHierarchyExtractor
{
	private int minClusterSize = 10;
	
	private int k = 15;
	
	private double stopThreshold;
	private double leafThreshold = 0.2;
	
	public HAClusterModel extract(HAClusterModel clusterModel) 
	{
		System.out.println(clusterModel.numberOfClusters());
		ObjectSet dataset 		= clusterModel.getDataset();
		DistanceMeasure metric 	= clusterModel.getDistanceMetric();
		HAClusterNode root 		= clusterModel.getRootCluster();
		Collection<? extends HAClusterNode> childs = root.getSubClusters();
		for(HAClusterNode child : childs)
			System.out.println("size:" + child.numberOfDataInSubTree());
		ExtHAClusterNode extRoot = extractExtNode(root, dataset, metric);
		printClusterStats(extRoot);
		pruneHierarchy(extRoot, metric);
		System.out.println("pruned");
		printClusterStats(extRoot);
		HAClusterModel retVal = new HAClusterModel(dataset, extRoot);
		retVal.setDistanceMetric(metric);
		return retVal;
	}

	private final void pruneHierarchy(ExtHAClusterNode root, DistanceMeasure metric)
	{
		Comparator<HAClusterNode> comparator = new WeightClusterNodeComparator();
		LinkedList<ExtHAClusterNode> queue = new LinkedList<ExtHAClusterNode>();
		LinkedList<ExtHAClusterNode> sortedCandidates = new LinkedList<ExtHAClusterNode>();
		queue.add(root);
		while(!queue.isEmpty()) {
			ExtHAClusterNode currNode = queue.removeFirst();
			System.out.println("curr " + currNode.numberOfSubClusters());
			System.out.println(currNode.getSubClusters());
			sortedCandidates.addAll(currNode.getSubClusters());
			Collections.sort(sortedCandidates, comparator);
			System.out.println("cand " + sortedCandidates.size());
			currNode.removeAllSubClusters();
			
			if(sortedCandidates.size() != 2)
				throw new IllegalArgumentException("");
			
			ExtHAClusterNode[] optChildClusters = sortedCandidates.toArray(new ExtHAClusterNode[0]);
			double optDBIndex = computeDaviesBouldinIndex(sortedCandidates, metric);
			//double optDBIndex = computeCSIndex(sortedCandidates, metric);
			
			int forwardSteps = 0;
			int steps = 0;
			HashSet<Integer> noiseData = new HashSet<Integer>();
			while(!sortedCandidates.isEmpty() && forwardSteps <= k)
			{
				steps++;
				ExtHAClusterNode candidate = sortedCandidates.pollFirst();
				//for(ExtHAClusterNode t : sortedCandidates)
					//System.out.print(t.weight + "\t");
				//System.out.println();
				//System.out.println(candidate.getWeight());
				//all candidates are leaf nodes
				if(!candidate.hasSubClusters() && candidate.getWeight() == 0d)
					break;
				if(candidate.hasSubClusters()) {
					noiseData.addAll(candidate.getData());
					sortedCandidates.addAll(candidate.getSubClusters());
					Collections.sort(sortedCandidates, comparator);
					double dbIndex = computeDaviesBouldinIndex(sortedCandidates, metric);
					//double dbIndex = computeCSIndex(sortedCandidates, metric);
					if(dbIndex >= optDBIndex) {
						optChildClusters = sortedCandidates.toArray(new ExtHAClusterNode[0]);
						optDBIndex = dbIndex;
						forwardSteps = 0;
						//propagate the data up in the hierarchy
						if(noiseData.size() != 0) {
							for(Integer dataId : noiseData)
								currNode.assignData(dataId);
							noiseData.clear();
						}
					} else 
						forwardSteps++;
				} else {
					candidate.setWeight(0d);
					sortedCandidates.addLast(candidate);
				}
			}
			System.out.println("bdi =" + optDBIndex);
			System.out.println("size =" + optChildClusters.length);
			System.out.println("steps =" + steps);
			
			if(!currNode.hasParent() || optDBIndex <currNode.dbIndex*1.09) {
				for(ExtHAClusterNode candidate : optChildClusters)
				{
					candidate.dbIndex = optDBIndex;
					candidate.setParent(currNode);
					if(candidate.hasSubClusters())
						queue.add(candidate);
				}
			} else {
				currNode.totalSize = currNode.totalData.size();
				currNode.fastAddData(currNode.totalData);
			}
			
			sortedCandidates.clear();
		}
	}
	
	private final ExtHAClusterNode extractExtNode(HAClusterNode node, ObjectSet dataset, DistanceMeasure metric)
	{
		if(!node.hasSubClusters()) {
			Collection<Integer> data = node.getData();
			double centroid[]	= computeCentroid(data, dataset);
			double avgDist		= computeAvgDistances(data, centroid, dataset, metric);
			ExtHAClusterNode extNode = new ExtHAClusterNode(centroid, avgDist, data);
			extNode.setWeight(node.getWeight());
			return extNode;
		}
		
		ExtHAClusterNode extNode = null;
		Collection<? extends HAClusterNode> children = node.getSubClusters();
		double maxWeight = Double.NEGATIVE_INFINITY;
		for(HAClusterNode child : children)
		{
			ExtHAClusterNode extChild = extractExtNode(child, dataset, metric);
			if(extNode == null) {
				int dim = extChild.centroid.length;
				extNode = new ExtHAClusterNode(dim);
			}
		
			extNode.addSubTreeData(extChild.getDataInSubTree());
			for(int i=0, n=extNode.centroid.length; i<n; i++)
				extNode.centroid[i] += extChild.centroid[i]*extChild.numberOfDataInSubTree();
			if(extChild.numberOfDataInSubTree() >= minClusterSize) 
				extNode.addSubCluster(extChild);
		    else  
				extNode.addWithinData(extChild.getDataInSubTree());
			
			double weight = extChild.getWeight();
			if(weight>maxWeight)
				maxWeight = weight;
			
		}
		
		if(extNode.numberOfSubClusters()==1) {
			HAClusterNode remNode = extNode.removeSubCluster(0);
			for(HAClusterNode child : remNode.getSubClusters())
				extNode.addSubCluster(child);
			extNode.setWeight(maxWeight);
			extNode.addWithinData(remNode.getData());
		} else 
			extNode.setWeight(node.getWeight());
		
		//set the node to a leaf
		if((extNode.totalSize-extNode.size())*leafThreshold <= extNode.size()) {
			extNode.removeAllSubClusters();
			extNode.fastAddData(extNode.totalData);
			extNode.totalSize = extNode.totalData.size();
		}
		
		for(int i=0, n=extNode.centroid.length; i<n; i++)
			extNode.centroid[i] /= extNode.numberOfDataInSubTree();
		extNode.avgDistances = computeAvgDistances(extNode.totalData, extNode.centroid, dataset, metric);
		return extNode;
	}
	
	private final double[] computeCentroid(Collection<Integer> dataIds, ObjectSet dataset)
	{
		int dim = dataset.getObjectSize();
		double[] retVal = new double[dim];
		for(Integer id : dataIds)
		{
			double v[] = dataset.get(id).getRepresentation();
			for(int i=0; i<dim; i++)
				retVal[i] += v[i];
		}
		for(int i=0, n=dataIds.size(); i<dim; i++)
			retVal[i] /=n;
		return retVal;
	}
	
	private final void printClusterStats(ExtHAClusterNode node)
	{
		System.out.println(node.numberOfDataInSubTree() + "\t" + node.hasSubClusters() + "\t" + node.numberOfSubClusters()+"\t" + node.getWeight()+"\t" + node.totalData.size()+ "\t" + node.size());
		if(node.hasSubClusters()) {
			for(ExtHAClusterNode child : node.getSubClusters())
				printClusterStats(child);
		}
	}
	
	private final double computeAvgDistances(Collection<Integer> dataIds, double[] centroid, 
											 ObjectSet dataset, DistanceMeasure metric)
	{
		double retVal = 0d;
		for(Integer id : dataIds)
		{
			double[] v = dataset.get(id).getRepresentation();
			retVal += metric.getDistance(centroid, v);
		}
		return retVal/dataIds.size();
	}
	
	private double computeDaviesBouldinIndex(Collection<ExtHAClusterNode> clusters, DistanceMeasure metric)
	{
		int n = clusters.size();
		double bdi = 0d;
		for(ExtHAClusterNode a : clusters)
		{
			double maxVal = Double.NEGATIVE_INFINITY;
			for(ExtHAClusterNode b : clusters)
			{
				if(!a.equals(b)) {
					//System.out.println(a.avgDistances +"\t"+ b.avgDistances + "\t"+metric.getDistance(a.centroid, b.centroid));
					double val = (a.avgDistances+b.avgDistances)/metric.getDistance(a.centroid, b.centroid);
					if(val > maxVal)
						maxVal = val;
				}
			}
			bdi += maxVal;
		}
		return bdi/n;
	}
	
	private double computeCSIndex(Collection<ExtHAClusterNode> clusters, DistanceMeasure metric)
	{
		double cs = 0d;
		ArrayIndexedMST<ExtHAClusterNode> mst = computeMinimumSpanningTree(clusters, metric);
		cs = mst.minDistance()/mst.maxDistance();
		return cs;
	}
	
	private static class ExtHAClusterNode extends HAClusterNode
	{
		double[] centroid;
		double avgDistances;
		
		HashSet<Integer> totalData;
		
		double dbIndex;
		
		ExtHAClusterNode(int centroidDimension)
		{
			super();
			centroid  = new double[centroidDimension];
			totalData = new HashSet<Integer>();
		}
		
		ExtHAClusterNode(double centroid[], double avgDistances, Collection<Integer> withinData)
		{
			super();
			this.centroid = centroid;
			this.avgDistances = avgDistances;
			this.data.addAll(withinData);
			totalData = new HashSet<Integer>(withinData);
			this.totalSize = totalData.size();
		}
		 
		void addSubTreeData(Collection<Integer> data)
		{
			totalData.addAll(data);
		}
		
		void addWithinData(Collection<Integer> data)
		{
			totalData.addAll(data);
			for(Integer id : data)
				super.assignData(id);
		}
		
		void fastAddData(Collection<Integer> data)
		{
			this.data.addAll(data);
		}
		
		public HashSet<Integer> getDataInSubTree()
		{
			return totalData;
		}
		
		public Collection<? extends ExtHAClusterNode> getSubClusters() 
		{
			List<ExtHAClusterNode> retVal = new ArrayList<ExtHAClusterNode>(subClusters.size());
			for(SimpleClusterNode subCluster : subClusters)
				retVal.add((ExtHAClusterNode) subCluster);
			return retVal;
		}
	}
	
	private static class WeightClusterNodeComparator implements Comparator<HAClusterNode>
	{

		public int compare(HAClusterNode a, HAClusterNode b)
		{
			double wa = a.getWeight();
			double wb = b.getWeight();
			if(wa > wb) {
				return -1;
			} else if(wb > wa) {
				return 1;
			} else 
				return 0;
		}
		
	}
	
	private final ArrayIndexedMST<ExtHAClusterNode> computeMinimumSpanningTree(Collection<ExtHAClusterNode> nodes, DistanceMeasure metric)
	{
	
		
		ExtHAClusterNode[] elements = nodes.toArray(new ExtHAClusterNode[0]);
		int n = elements.length;
	
		double[] minDistances = new double[n];
		Arrays.fill(minDistances, Double.POSITIVE_INFINITY);
		int[] fringe = new int[n];
		ProximityMatrix proxMatrix = computeProximityMatrix(elements, metric);
		ArrayIndexedMST<ExtHAClusterNode> mst = new ArrayIndexedMST<ExtHAClusterNode>(elements);
		for(int v=0, min=-1; min!=0; v=min)
		{
			min = 0;
			for(int w=1; w<n; w++)
			{
				if(mst.parentElementIndex(w) == -1) {
					double dist = proxMatrix.getValue(v, w);
					if(dist<minDistances[w]) {
						minDistances[w] = dist;
						fringe[w] = v;
					}
					if(minDistances[w]<minDistances[min])
						min = w;
				}
			}
			if(min != 0)
				mst.setParentLink(min, fringe[min], minDistances[min]);
		}
		
		return mst;
	}
	
	private ProximityMatrix computeProximityMatrix(ExtHAClusterNode[] elements, DistanceMeasure metric)
	{
		if(elements == null)
			throw new NullPointerException();
		
		int n = elements.length;
		ProximityMatrix matrix = new ProximityMatrix(n);
		for(int i=0; i<n; i++) 
		{
			for(int j=i+1; j<n; j++)
			{
				double dist = metric.getDistance(elements[i].centroid, elements[j].centroid);
				matrix.setValue(i, j, dist);
			}
		}
		return matrix;
	}

	public void applyDefaultSettings() {
		// TODO Auto-generated method stub
		
	}

	public void applySettings(Settings settings) {
		// TODO Auto-generated method stub
		
	}

	public Settings getDefaultSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	public Settings getSettings() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

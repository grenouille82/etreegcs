package de.ovgu.dke.clustering.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import de.ovgu.dke.clustering.algorithm.competitive.ArrayIndexedMST;
import de.ovgu.dke.clustering.model.ClusterNode;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.clustering.model.PrototypeCluster;
import de.ovgu.dke.clustering.model.SimpleFlatClusterModel;
import de.ovgu.dke.clustering.model.SimplePrototypeCluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public final class ClusteringUtility
{
	private ClusteringUtility() {}
	
	public static FlatClusterModel flattenHierarchyOnLeafs(HierarchicalClusterModel model)
	{
		if(model == null)
			throw new NullPointerException();
		
		ObjectSet dataset 		= model.getDataset();
		DistanceMeasure metric	= model.getDistanceMetric();
		
		SimpleFlatClusterModel retVal = new SimpleFlatClusterModel(dataset);
		retVal.setDistanceMetric(metric);
		
		LinkedList<ClusterNode> queue = new LinkedList<ClusterNode>();
		queue.add(model.getRootCluster());
		while(!queue.isEmpty())
		{
			ClusterNode candidate = queue.removeFirst();
			if(!candidate.hasSubClusters()) {
				HashSet<Integer> cpData = new HashSet<Integer>(candidate.getData());
				SimplePrototypeCluster cluster = new SimplePrototypeCluster(cpData, dataset);
				retVal.fastAddCluster(cluster);
			} else
				queue.addAll(candidate.getSubClusters());
		}
		
		return retVal;
	}
	
	public static <T extends PrototypeCluster> ArrayIndexedMST<T> computeMinimumSpanningTree(T[] elements, DistanceMeasure metric)
	{
		if(elements == null)
			throw new NullPointerException();
		
		int n = elements.length;
	
		double[] minDistances = new double[n];
		Arrays.fill(minDistances, Double.POSITIVE_INFINITY);
		int[] fringe = new int[n];
		ProximityMatrix proxMatrix = computeProximityMatrix(elements, metric);
		ArrayIndexedMST<T> mst = new ArrayIndexedMST<T>(elements);
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
	
	public static <T extends PrototypeCluster> ProximityMatrix computeProximityMatrix(T[] elements, DistanceMeasure metric)
	{
		if(elements == null)
			throw new NullPointerException();
		
		int n = elements.length;
		ProximityMatrix matrix = new ProximityMatrix(n);
		for(int i=0; i<n; i++) 
		{
			for(int j=i+1; j<n; j++)
			{
				double dist = metric.getDistance(elements[i].getCentroid(), elements[j].getCentroid());
				matrix.setValue(i, j, dist);
			}
		}
		return matrix;
	}
	
	public static double[] computeCentroid(Collection<Integer> data, ObjectSet dataset)
	{
		int dim = dataset.getObjectSize();
		double centroid[] = new double[dim];
		for(Integer dataId : data)
		{
			double[] v = dataset.get(dataId).getRepresentation();
			for(int i=0; i<dim; i++)
				centroid[i] += v[i];
		}
		
		int n = data.size();
		for(int i=0; i<dim; i++)
			centroid[i] /= n;
		return centroid;
	}
}

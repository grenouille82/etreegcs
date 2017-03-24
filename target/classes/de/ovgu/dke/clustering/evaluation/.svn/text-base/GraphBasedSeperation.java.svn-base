package de.ovgu.dke.clustering.evaluation;

import java.util.Collection;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.util.ProximityMatrix;

public class GraphBasedSeperation extends AbstractEvaluationMeasure<FlatClusterModel>
implements UnsupervisedEvaluationMeasure<FlatClusterModel>
{
	public double compute(FlatClusterModel model)
	{
		if(model == null)
			throw new NullPointerException();
		ProximityMatrix proxMatrix = 
			ProximityMatrix.createProximityMatrix(model.getDataset(), model.getDistanceMetric());
		return compute(model, proxMatrix);
	}
	
	public double compute(FlatClusterModel model, ProximityMatrix proximityMatrix)
	{
		if(model == null)
			throw new NullPointerException();
		if(proximityMatrix == null)
			throw new NullPointerException();
		
		overallValue = 0d;
		clusterValues.clear();
		
		int n = model.numberOfClusters();
		for(int i=0; i<n; i++)
		{
			Cluster c 	 = model.getCluster(i);
			double value = computeSeperation(i, model, proximityMatrix);
			clusterValues.put(c.getId(), value);
			overallValue += value/c.size();
		}
		
		return overallValue;
	}
	public String getName()
	{
		return "GraphBasedSeperation";
	}
	
	private final double computeSeperation(int clusterId, FlatClusterModel model, ProximityMatrix proximityMatrix)
	{
		double seperation = 0d;
		
		Collection<Integer> dataX = model.getClusterAt(clusterId).getData();
		int n = model.numberOfClusters();
		for(int i=0; i<n && clusterId!=i; i++)
		{
			Cluster c = model.getClusterAt(i);
			Collection<Integer> dataY = c.getData();

			for(int x : dataX)
			{
				for(int y : dataY) 
					seperation += proximityMatrix.getValue(x,y);
			}
		}
		return seperation;
	}

}


package de.ovgu.dke.clustering.evaluation;

import java.util.Collection;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.util.ProximityMatrix;

public final class GraphBasedCohesion extends AbstractEvaluationMeasure<FlatClusterModel>
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
			double value = computeCohesion(c, proximityMatrix);
			clusterValues.put(c.getId(), value);
			overallValue += value/c.size();
		}
		
		return overallValue;
	}
	public String getName()
	{
		return "GraphBasedCohesian";
	}
	
	private final double computeCohesion(Cluster c, ProximityMatrix proxMatrix)
	{
		double cohesion = 0d;
		Collection<Integer> data = c.getData();
		for(int i : data)
		{
			for(int j : data) 
				cohesion += proxMatrix.getValue(i,j);
		}
		return cohesion;
	}

}

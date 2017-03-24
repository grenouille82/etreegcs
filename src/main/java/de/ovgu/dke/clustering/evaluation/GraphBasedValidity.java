package de.ovgu.dke.clustering.evaluation;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.util.ProximityMatrix;

public final class GraphBasedValidity extends AbstractEvaluationMeasure<FlatClusterModel>
implements UnsupervisedEvaluationMeasure<FlatClusterModel>
{
	private final GraphBasedCohesion cohesion 	  = new GraphBasedCohesion();
	private final GraphBasedSeperation seperation = new GraphBasedSeperation();
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
		cohesion.compute(model, proximityMatrix);
		seperation.compute(model, proximityMatrix);
		
		overallValue = 0d;
		clusterValues.clear();
		
		int n = model.numberOfClusters();
		for(int i=0; i<n; i++)
		{
			Cluster c 	 = model.getCluster(i);
			double value = computeValidity(c.getId());
			clusterValues.put(c.getId(), value);
			overallValue += value;
		}
		
		return overallValue;
	}
	public String getName()
	{
		return "GraphBasedValidity";
	}
	
	private final double computeValidity(int clusterId)
	{
		double validity = seperation.getValue(clusterId)/cohesion.getValue(clusterId);
		return validity;
	}

}

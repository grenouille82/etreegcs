package de.ovgu.dke.clustering.evaluation;

import java.util.Collection;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.util.ProximityMatrix;

public class SilhouetteCoefficient extends AbstractEvaluationMeasure<FlatClusterModel>
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
		
		int totalCnt = 0;
		int n = model.numberOfClusters();
		
		for(int i=0; i<n; i++)
		{
			Cluster ci = model.getClusterAt(i);
			Collection<Integer> datai = ci.getData();
			double cCoefficient = 0d;
			for(int pi : datai)
			{
				//avg proximity to all objects in the cluster
				double a = 0d;
				//max proximity to an object in a different cluster
				double b = Double.POSITIVE_INFINITY;
				for(int j=0; j<n; j++)
				{
					Cluster cj = model.getClusterAt(j);
					Collection<Integer> dataj = cj.getData();
					if(i==j) {
						//compute the average distance
						for(int pj : dataj)
							a += proximityMatrix.getValue(pi, pj);
						a /= (double) ci.size()-1;						
					} else {
						//search for the farthest distance value
						double avgDist = 0d;
						for(int pj : dataj)
							avgDist += proximityMatrix.getValue(pi, pj);
						avgDist /= (double) cj.size();
						if(avgDist < b)
							b = avgDist;
					}
				}
				cCoefficient += (b-a)/Math.max(a, b);
				//System.out.println(a + "\t" + b + "\t" + ((b-a)/Math.max(a, b)));
				totalCnt++;
			}
			overallValue += cCoefficient;
			clusterValues.put(ci.getId(), cCoefficient/ci.size());
		}
		overallValue /= (double) totalCnt;
		return overallValue;
	}
	
	public String getName() 
	{
		return "SilhoutteCoefficient";
	}
}

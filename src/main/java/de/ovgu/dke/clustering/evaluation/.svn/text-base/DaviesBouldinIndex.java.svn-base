package de.ovgu.dke.clustering.evaluation;

import java.util.Arrays;

import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.PrototypeCluster;
import de.ovgu.dke.clustering.util.CentroidDistance;
import de.ovgu.dke.clustering.util.CentroidLinkage;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public final class DaviesBouldinIndex extends AbstractEvaluationMeasure<FlatClusterModel>
implements UnsupervisedEvaluationMeasure<FlatClusterModel>
{
	public double compute(FlatClusterModel model) 
	{
		if(model == null)
			throw new NullPointerException();


		clusterValues.clear();
		overallValue = 0d;
		
		DistanceMeasure metric 	= model.getDistanceMetric();
		ObjectSet dataset		= model.getDataset();
		
		int n = model.numberOfClusters();
		
		CentroidDistance cd = new CentroidDistance(dataset, metric);
		CentroidLinkage cl	= new CentroidLinkage(dataset, metric);
		
		double[] intraDistances		= new double[n];
		Arrays.fill(intraDistances, Double.NaN);
		double[][] interDistances	= new double[n-1][];
		for(int i=0; i<n-1; i++)
		{
			interDistances[i] = new double[n-(i+1)];
			Arrays.fill(interDistances[i], Double.NaN);
		}
		
		PrototypeCluster[] clusters = new PrototypeCluster[n];
		for(int i=0; i<n; i++)
		{
			if(!(model.getClusterAt(i) instanceof PrototypeCluster))
				throw new IllegalArgumentException("evaluation measure only " +
						"							applicable to prototype clusters");
			
			clusters[i] = (PrototypeCluster) model.getClusterAt(i);
		}
		
		PrototypeCluster a, b;
		double maxDBIndex, dbIndex;
		double interDist;
		for(int i=0; i<n; i++)
		{
			maxDBIndex = Double.NEGATIVE_INFINITY;
			a = clusters[i];
			if(Double.isNaN(intraDistances[i]))
				intraDistances[i] = cd.distance(a);
			for(int j=0; j<n; j++)
			{
				if(i!=j) {
					b = clusters[j];
					if(Double.isNaN(intraDistances[j]))
						intraDistances[j] = cd.distance(b);
		
					interDist = (i<j) ? interDistances[i][j-i-1] : interDistances[j][i-j-1];
					if(Double.isNaN(interDist)) {
						interDist = cl.distance(a, b);
						if(i<j)
							interDistances[i][j-i-1] = interDist;
						else 
							interDistances[j][i-j-1] = interDist;
					}
					 
					dbIndex = (intraDistances[i]+intraDistances[j])/interDist;
					if(dbIndex > maxDBIndex)
						maxDBIndex = dbIndex;
				}
				
			}
		
			clusterValues.put(a.getId(), maxDBIndex);
			overallValue += maxDBIndex/n;
		}
		
		return overallValue;
	}

	public String getName() 
	{
		return "DaviesBouldinIndex";
	}
	
	
}

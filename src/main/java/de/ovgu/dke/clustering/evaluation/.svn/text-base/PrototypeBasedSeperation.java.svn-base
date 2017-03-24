package de.ovgu.dke.clustering.evaluation;


import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.PrototypeCluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public class PrototypeBasedSeperation extends AbstractEvaluationMeasure<FlatClusterModel>
implements UnsupervisedEvaluationMeasure<FlatClusterModel>
{	
	public double compute(FlatClusterModel model) 
	{
		if(model == null)
			throw new NullPointerException();
		
		overallValue = 0d;
		clusterValues.clear();
		
		DistanceMeasure metric 	= model.getDistanceMetric();
		ObjectSet dataset 		= model.getDataset();
		
		int n 		 = model.numberOfClusters();
		int dim 	 = dataset.getObjectSize();
		int totalCnt = 0;
		double[][] prototypes = new double[n][];
		double[] weights 	  = new double[n];
		
		//extracting prototypes
		for(int i=0; i<n; i++)
		{
			if(!(model.getClusterAt(i) instanceof PrototypeCluster))
				throw new IllegalArgumentException("evaluation measure only " +
						"							applicable to prototype clusters");
			
			PrototypeCluster c 	= (PrototypeCluster) model.getClusterAt(i);
			prototypes[i]		= c.getCentroid();
			weights[i] 			= c.size();
			totalCnt 			+= c.size();
			
		}
		//compute indiviual cluster weights
		for(int i=0; i<n; i++)
			weights[i] /= (double) totalCnt;
		
		//compute overall centroid
		double[] overallCentroid = computeCentroid(prototypes, weights, dim);
		
		double d = 0d;
		//compute seperation
		for(int i=0; i<n; i++)
		{
			Cluster c = model.getClusterAt(i);
			d = metric.getDistance(overallCentroid, prototypes[i]);
			clusterValues.put(c.getId(), d);
			overallValue += c.size()*d;
		}
		
		return overallValue;
	}

	public String getName() 
	{
		return "PrototypeBasedCohesion";
	}

	private final double[] computeCentroid(double[][] vectors, double[] weight, final int dim)
	{
		double[] retVal = new double[dim];
		
		final int n = vectors.length;
		for(int i=0; i<n; i++)
		{
			for(int j=0; j<dim; j++)
				retVal[j] += vectors[i][j]*weight[j];
		}
		
		for(int i=0; i<dim; i++)
			retVal[i] /= n;
		
		return retVal;
	}
}

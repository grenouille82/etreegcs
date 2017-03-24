package de.ovgu.dke.clustering.algorithm.flat;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import de.ovgu.dke.clustering.algorithm.hierarchical.HAClusterModel;
import de.ovgu.dke.clustering.algorithm.hierarchical.HAClusterer;
import de.ovgu.dke.clustering.evaluation.BCubedFMeasure;
import de.ovgu.dke.clustering.evaluation.DaviesBouldinIndex;
import de.ovgu.dke.clustering.evaluation.Entropy;
import de.ovgu.dke.clustering.evaluation.Purity;
import de.ovgu.dke.clustering.evaluation.SilhouetteCoefficient;
import de.ovgu.dke.clustering.evaluation.StandardFMeasure;
import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.MutablePrototypeCluster;
import de.ovgu.dke.clustering.util.PrototypeInitializer;
import de.ovgu.dke.clustering.util.PrototypeInitialzerType;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.MathUtil;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

/**
 * 
 * @author mhermkes
 * TODO: - metric can also be a similarity metric
 * 		 - optimization of the target function must be generic
 */
public class KMeansClusterer implements FlatClusterer<KMeansModel>
{
	private static int DEFAULT_K = 11;
	
	private static int DEFAULT_MAX_RUNS = 10;
	
	private static int DEFAULT_MAX_OPT_STEPS = Integer.MAX_VALUE;
	
	private static PrototypeInitialzerType DEFAULT_PROTOTYPE_INITIALIZER_TYPE = PrototypeInitialzerType.RandomExample; 
	
	private static DistanceMeasure DEFAULT_METRIC = DistanceMeasure.COSINE;
	
	private int k;
	
	private int maxRuns;
	
	private int maxOptSteps;
	
	private PrototypeInitialzerType pIType;
	
	private DistanceMeasure metric;
	
	public KMeansClusterer()
	{
		applyDefaultSettings();
	}
	
	public KMeansModel computeClusterModel(ObjectSet dataset) 
	{
		if(k > dataset.size())
			throw new IllegalArgumentException("number of desired clusters is larger than dataset");
		
		PrototypeInitializer pInitializer = PrototypeInitialzerType.createPrototypeInitializer(pIType, dataset);
		
		int n = dataset.size();
		double minError = Double.POSITIVE_INFINITY;
		KMeansModel bestModel = null;
		for(int iteration=0; iteration<maxRuns; iteration++)
		{
			double[][] kCentroids = pInitializer.getNVectors(k);
			KMeansModel trainedModel = new KMeansModel(dataset, kCentroids, metric);
			System.out.println("trainedSize " + trainedModel.numberOfClusters()); 
			for(int step=0; step<maxOptSteps; step++)
			{
				System.out.println(step);
				//assigning data to the closest cluster
				MutablePrototypeCluster cluster = null;
				double data[] = null;
				for(int i=0; i<n; i++) 
				{
					data = dataset.get(i).getRepresentation();
					cluster = findClosestCluster(data, trainedModel);
					cluster.assignData(i);
				}
				
				//recomputing the centroids of the clusters
				boolean stable = true;
				double[] newCentroid, oldCentroid;
				for(int i=0; i<k; i++)
				{
					//FIXME: problems, if the computeCentroid method of clusters create no new reference.array.
					//in this situation oldCentroid and newCentroid are the same.
					oldCentroid = trainedModel.getCentroid(i);
					trainedModel.updateCentroid(i);
					newCentroid = trainedModel.getCentroid(i);
					if(metric.getDistance(oldCentroid, newCentroid) > 0.00001d) 
						stable = false;
				}
				if(stable)
					break;
				trainedModel.clearData();
			}
			
			double error = trainedModel.getError();
			if(error<minError) {
				bestModel = trainedModel;
				minError  = error;
			}
		}
		bestModel.setDistanceMetric(metric);
		return bestModel;
	}

	public void applySettings(Settings settings) 
	{
		if(settings != null) {
			Parameter p = settings.getParameter("k");
			if(p!=null)
				setNumberOfClusters(Integer.valueOf(p.getValue()));
			
			p = settings.getParameter("maxRuns");
			if(p!=null)
				setNumberOfRuns(Integer.valueOf(p.getValue()));
		
		}
	}
	
	public void applyDefaultSettings() 
	{
		k = DEFAULT_K;
		metric = DEFAULT_METRIC;
		pIType = DEFAULT_PROTOTYPE_INITIALIZER_TYPE;
		maxOptSteps = DEFAULT_MAX_OPT_STEPS;
		maxRuns 	= DEFAULT_MAX_RUNS;
	}

	public Settings getDefaultSettings() 
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("k", Integer.toString(DEFAULT_K)));
		settings.setParameter(new Parameter("maxOptSteps", Integer.toString(DEFAULT_MAX_OPT_STEPS)));
		settings.setParameter(new Parameter("maxRuns", Integer.toString(DEFAULT_MAX_RUNS)));
		settings.setParameter(new Parameter("pIType", DEFAULT_PROTOTYPE_INITIALIZER_TYPE.name()));
		return settings;
	}

	public Settings getSettings() 
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("k", Integer.toString(k)));
		settings.setParameter(new Parameter("maxOptSteps", Integer.toString(maxOptSteps)));
		settings.setParameter(new Parameter("maxRuns", Integer.toString(maxRuns)));
		settings.setParameter(new Parameter("pIType", pIType.name()));
		return settings;
	}
	
	public int getNumberOfClusters()
	{
		return k;
	}
	
	public void setNumberOfClusters(int k)
	{
		if(k<1)
			throw new IllegalArgumentException("number of clusters must be greater than 0");
		this.k = k;
	}
	
	public DistanceMeasure getDistanceMeasure()
	{
		return metric;
	}
	
	public void setMetric(DistanceMeasure metric)
	{
		if(metric == null)
			throw new NullPointerException();
		this.metric = metric;
	}
	
	public int getOptimizationSteps()
	{
		return maxOptSteps;
	}
	
	public void setOptimizationSteps(int n)
	{
		if(n < 1)
			throw new IllegalArgumentException("optimization steps must be greater than 0");
		maxOptSteps = n;
	}
	
	public int getNumberOfRuns()
	{
		return maxRuns;
	}
	
	public void setNumberOfRuns(int n)
	{
		if(n<1)
			throw new IllegalArgumentException("number of runs must be greater than 0");
		maxRuns = n;
	}

	public PrototypeInitialzerType getPrototypeInitializerType()
	{
		return pIType;
	}
	
	public void setPrototypeInitializerType(PrototypeInitialzerType type)
	{
		if(type == null)
			throw new NullPointerException();
		pIType = type;
	}
	
	private final MutablePrototypeCluster findClosestCluster(double[] vector, KMeansModel clusterModel)
	{
		if(vector == null)
			throw new NullPointerException();
		double minDistance = Double.POSITIVE_INFINITY;
		MutablePrototypeCluster retVal = null;
		
		double distance = 0d;
		for(int i=0; i<clusterModel.numberOfClusters(); i++)
		{
			distance = clusterModel.distance(i, vector);
			if(distance < minDistance) {
				minDistance = distance;
				retVal = clusterModel.getClusterAt(i);
			}
		}
	
		return retVal;
	}
	
	public static void main(String args[])
	{
		try {
			System.out.println(Math.min(Double.POSITIVE_INFINITY, 3.));
			System.out.println(Math.max(Double.POSITIVE_INFINITY, 3.));
			System.out.println(Math.min(Double.NEGATIVE_INFINITY, 3.));
			System.out.println(Math.max(Double.NEGATIVE_INFINITY, 3.));
			ObjectSet dataset = new CSVObjectSet("/home/grenouille/irg_workspace/datasets/bs_small_stem.csv", "class");
			System.out.println(dataset.size());
			//FlatClusterModel model = new FakeSupervisedClusterer().computeClusterModel(dataset);
			//FlatClusterModel model = new KMeansClusterer().computeClusterModel(dataset);
			HAClusterModel hModel = new HAClusterer().computeClusterModel(dataset);
			System.out.println(hModel.numberOfClusters());
			//printClassDistribution(model);
			/*
			System.out.println(model.getDataset().size());
			System.out.println("f-measure: "+new BCubedFMeasure().compute(model));
			System.out.println("bdi: " +new DaviesBouldinIndex().compute(model));
			System.out.println("sc: " +new SilhouetteCoefficient().compute(model));
			System.out.println("purity: " +new Purity().compute(model));
			System.out.println("entropy: " +new Entropy().compute(model));
			*/
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printClassDistribution(KMeansModel model)
	{
		ObjectSet dataset = model.getDataset();
		HashMap<Cluster, HashMap<String,Integer>> assignments = new HashMap<Cluster, HashMap<String,Integer>>();
		
		for(int i=0, n=model.numberOfClusters(); i<n; i++)
		{
			Cluster cluster = model.getClusterAt(i);
			assignments.put(cluster, new HashMap<String, Integer>());
		}
		
		for(int i=0, n=model.numberOfClusters(); i<n; i++)
		{
			Cluster cluster = model.getClusterAt(i);
			HashMap<String, Integer> distr = assignments.get(cluster);
			Collection<Integer> data = cluster.getData();
			for(Integer docId : data)
			{
				String label = dataset.get(docId).getClassLabel();
				Integer freq = distr.get(label);
				if(freq == null)
					distr.put(label, 1);
				else
					distr.put(label, freq+1);
			}
		}
		
		
		
		System.out.println("\ndistribution " );
		for(Entry<Cluster, HashMap<String, Integer>> nodeEntry : assignments.entrySet())
		{
			System.out.print(nodeEntry.getKey().getId()+": ");
			for(Entry<String, Integer> freqEntry : nodeEntry.getValue().entrySet())
				System.out.print(freqEntry.getKey()+"="+freqEntry.getValue()+"\t");
			System.out.println();
		}
	}
	
}
	
	


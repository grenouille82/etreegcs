package de.ovgu.dke.clustering.msctest;

import java.io.FileWriter;
import java.util.Collection;

import de.ovgu.dke.clustering.algorithm.flat.FakeSupervisedClusterer;
import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.util.ClusterAvgDistanceMatrix;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public final class ClassDistanceComputation 
{
	private ClassDistanceComputation() {}
	
	public static void main(String[] args)
	{
		if (args.length < 2) {
			System.out.println("Wrong usage: DataStatistic <Distance Metric> <Dataset InFile> [<OutFile>]");
			System.exit(0);
		}
		
		String metricName = args[0];
		String inFile	= args[1];
		String outFile	= null;
		if(args.length == 3)
			outFile = args[2];
		
		DistanceMeasure metric = lookupMetric(metricName);
		FakeSupervisedClusterer clusterer = new FakeSupervisedClusterer();
		
		try {
		System.out.println("Loading Dataset");
		ObjectSet dataset = new CSVObjectSet(inFile,"class");
		System.out.println("loaded...");
		System.out.println("Create Clustering");
		FlatClusterModel model = clusterer.computeClusterModel(dataset);
		System.out.println("created...");
		System.out.println("Compute Matrix");
		ClusterAvgDistanceMatrix matrix = new ClusterAvgDistanceMatrix(model, metric);
		System.out.println("computed...");
		if(outFile != null) {
			FileWriter writer = new FileWriter(outFile);
			writer.write(matrix.toString());
			writer.write('\n');
			writer.write(createClusterLabelString(model));
			writer.write('\n');
			writer.write("metric="+metricName);
			writer.flush();
			writer.close();
		} 
		
		System.out.println(matrix);
		System.out.println(createClusterLabelString(model));
		System.out.println("metric="+metricName);
		
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static String createClusterLabelString(FlatClusterModel model)
	{
		StringBuilder builder = new StringBuilder();
		ObjectSet dataset = model.getDataset();
		for(int i=0, n=model.numberOfClusters(); i<n; i++)
		{
			Cluster cluster = model.getClusterAt(i);
			Collection<Integer> data = cluster.getData();
			builder.append(cluster.toString());
			builder.append("=");
			int id = data.iterator().next();
			builder.append(dataset.get(id).getClassLabel());
			if(i<(n-1))
				builder.append(", ");
		}
		return builder.toString();
	}
	
	public static DistanceMeasure lookupMetric(String metricName)
	{
		DistanceMeasure.MetricType metricType = DistanceMeasure.MetricType.valueOf(metricName);
		return DistanceMeasure.createMetric(metricType);
	}
}

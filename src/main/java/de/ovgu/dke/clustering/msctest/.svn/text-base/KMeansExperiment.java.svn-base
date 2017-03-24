package de.ovgu.dke.clustering.msctest;

import java.io.IOException;

import de.ovgu.dke.clustering.algorithm.flat.KMeansClusterer;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class KMeansExperiment 
{
	private static String evalPath = "/home/grenouille/irg_workspace/so_evaluation/clustering/";
	public static void main(String args[])
	{
		try {
			FlatClusterEvaluator evaluator = new FlatClusterEvaluator();
			ObjectSet dataset = new CSVObjectSet("/home/grenouille/irg_workspace/datasets/reuters_small2_topic100_stem.csv", "class");
			KMeansClusterer clusterer = new KMeansClusterer();
			
			clusterer.applySettings(getReuter2Settings());
			clusterer.setMetric(DistanceMeasure.COSINE);
			
			evaluator.applyClusterer(clusterer);
			evaluator.applyDataset(dataset);
			evaluator.evaluate();
			evaluator.printResult();
			evaluator.writeResultToFile(evalPath+"reuter2/kmeans_result.txt");
			//evaluator.writeDataAssignmentToFile(evalPath+"3d_gaussian/kmeans_assign.txt");
			
		} catch(IOException e) {
			e.printStackTrace();
			
		}
		
	}
	
	private static Settings getGaussianSettings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("k", "8"));
		settings.setParameter(new Parameter("maxRuns", "100"));
		return settings;
	}
	
	private static Settings getBanksearchSettings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("k", "11"));
		settings.setParameter(new Parameter("maxRuns", "10"));
		return settings;
	}
	
	private static Settings getReuter1Settings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("k", "11"));
		settings.setParameter(new Parameter("maxRuns", "10"));
		return settings;
	}
	
	private static Settings getReuter2Settings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("k", "11"));
		settings.setParameter(new Parameter("maxRuns", "10"));
		return settings;
	}
}

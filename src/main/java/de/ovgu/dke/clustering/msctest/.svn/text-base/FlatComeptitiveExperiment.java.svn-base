package de.ovgu.dke.clustering.msctest;

import java.io.IOException;

import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveAlgorithmType;
import de.ovgu.dke.clustering.algorithm.competitive.GrowingCellStructureAlgorithm.PruningStrategy;
import de.ovgu.dke.clustering.algorithm.flat.FlatCompetitiveClusterer;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class FlatComeptitiveExperiment 
{
	private static String evalPath = "/home/grenouille/irg_workspace/so_evaluation/clustering/";
	public static void main(String args[])
	{
		try {
			FlatClusterEvaluator evaluator = new FlatClusterEvaluator();
			ObjectSet dataset = new CSVObjectSet("/home/grenouille/irg_workspace/datasets/bs_small_stem.csv", "class");
			FlatCompetitiveClusterer clusterer = new FlatCompetitiveClusterer();
			
			clusterer.applySettings(getGNGReuter2Settings());
			clusterer.setDistanceMetric(DistanceMeasure.COSINE);
			
			evaluator.applyClusterer(clusterer);
			evaluator.applyDataset(dataset);
			evaluator.evaluate();
			evaluator.printResult();
			evaluator.writeResultToFile(evalPath+"bs/gng_noprune_result.txt");
		//	evaluator.writeDataAssignmentToFile(evalPath+"3d_gaussian/gng_assign.txt");
			
		} catch(IOException e) {
			e.printStackTrace();
			
		}
		
	}
	
	private static Settings getGCSGaussianSettings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "false"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		Settings gcsSettings = new Settings();
		gcsSettings.setParameter(new Parameter("lambdaInsert", Integer.toString(100)));
		gcsSettings.setParameter(new Parameter("lambdaPruning", Integer.toString(300)));
		gcsSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gcsSettings.setParameter(new Parameter("eta", Double.toString(0.005d)));
		gcsSettings.setParameter(new Parameter("pruningStrategy", PruningStrategy.MaximumDistantPruning.name()));
		gcsSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gcsSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.006d)));
		gcsSettings.setParameter(new Parameter("maxVertices", Integer.toString(Integer.MAX_VALUE)));
		gcsSettings.setParameter(new Parameter("maxEpochs", Integer.toString(10)));
		gcsSettings.setParameter(new Parameter("errorMinimizing", Boolean.toString(true)));
		p.setSettings(gcsSettings);
		
		settings.setParameter(p);
		return settings;
	}
	
	private static Settings getGCSBSSettings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "true"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		Settings gcsSettings = new Settings();
		gcsSettings.setParameter(new Parameter("lambdaInsert", Integer.toString(100)));
		gcsSettings.setParameter(new Parameter("lambdaPruning", Integer.toString(350)));
		gcsSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gcsSettings.setParameter(new Parameter("eta", Double.toString(0.005d)));
		gcsSettings.setParameter(new Parameter("pruningStrategy", PruningStrategy.MaximumDistantPruning.name()));
		gcsSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gcsSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.006d)));
		gcsSettings.setParameter(new Parameter("maxVertices", Integer.toString(Integer.MAX_VALUE)));
		gcsSettings.setParameter(new Parameter("maxEpochs", Integer.toString(10)));
		gcsSettings.setParameter(new Parameter("errorMinimizing", Boolean.toString(true)));
		p.setSettings(gcsSettings);
		
		settings.setParameter(p);
		return settings;
	}
	
	private static Settings getGCSReuter1Settings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "true"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		Settings gcsSettings = new Settings();
		gcsSettings.setParameter(new Parameter("lambdaInsert", Integer.toString(100)));
		gcsSettings.setParameter(new Parameter("lambdaPruning", Integer.toString(300)));
		gcsSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gcsSettings.setParameter(new Parameter("eta", Double.toString(0.005d)));
		gcsSettings.setParameter(new Parameter("pruningStrategy", PruningStrategy.MaximumDistantPruning.name()));
		gcsSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gcsSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.006d)));
		gcsSettings.setParameter(new Parameter("maxVertices", Integer.toString(Integer.MAX_VALUE)));
		gcsSettings.setParameter(new Parameter("maxEpochs", Integer.toString(10)));
		gcsSettings.setParameter(new Parameter("errorMinimizing", Boolean.toString(true)));
		p.setSettings(gcsSettings);
		
		settings.setParameter(p);
		return settings;
	}
	
	private static Settings getGCSReuter2Settings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "true"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		Settings gcsSettings = new Settings();
		gcsSettings.setParameter(new Parameter("lambdaInsert", Integer.toString(100)));
		gcsSettings.setParameter(new Parameter("lambdaPruning", Integer.toString(300)));
		gcsSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gcsSettings.setParameter(new Parameter("eta", Double.toString(0.005d)));
		gcsSettings.setParameter(new Parameter("pruningStrategy", PruningStrategy.MaximumDistantPruning.name()));
		gcsSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gcsSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.006d)));
		gcsSettings.setParameter(new Parameter("maxVertices", Integer.toString(Integer.MAX_VALUE)));
		gcsSettings.setParameter(new Parameter("maxEpochs", Integer.toString(10)));
		gcsSettings.setParameter(new Parameter("errorMinimizing", Boolean.toString(true)));
		p.setSettings(gcsSettings);
		
		settings.setParameter(p);
		return settings;
	}
	
	
	private static Settings getGNGGaussianSettings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "true"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingNeuralGas.name());
		Settings gngSettings = new Settings();
		gngSettings.setParameter(new Parameter("lambda", Integer.toString(100)));
		gngSettings.setParameter(new Parameter("alpha", Double.toString(0.5d)));
		gngSettings.setParameter(new Parameter("gamma", Double.toString(10d)));
		gngSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gngSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gngSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.006d)));
		gngSettings.setParameter(new Parameter("maxAge", Integer.toString(40)));
		gngSettings.setParameter(new Parameter("maxVertices", Integer.toString(Integer.MAX_VALUE)));
		gngSettings.setParameter(new Parameter("maxEpochs", Integer.toString(10)));
		gngSettings.setParameter(new Parameter("useVertexRemoval", Boolean.toString(false)));
		p.setSettings(gngSettings);
		
		settings.setParameter(p);
		return settings;
	}
	
	private static Settings getGNGReuter2Settings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "fals"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingNeuralGas.name());
		Settings gngSettings = new Settings();
		gngSettings.setParameter(new Parameter("lambda", Integer.toString(500)));
		gngSettings.setParameter(new Parameter("alpha", Double.toString(0.5d)));
		gngSettings.setParameter(new Parameter("gamma", Double.toString(10d)));
		gngSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gngSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gngSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.006d)));
		gngSettings.setParameter(new Parameter("maxAge", Integer.toString(200000)));
		gngSettings.setParameter(new Parameter("maxVertices", Integer.toString(Integer.MAX_VALUE)));
		gngSettings.setParameter(new Parameter("maxEpochs", Integer.toString(10)));
		gngSettings.setParameter(new Parameter("useVertexRemoval", Boolean.toString(false)));
		p.setSettings(gngSettings);
		
		settings.setParameter(p);
		return settings;
	}
}

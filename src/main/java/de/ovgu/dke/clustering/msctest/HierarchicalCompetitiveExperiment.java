package de.ovgu.dke.clustering.msctest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//import org.apache.commons.collections.map.HashedMap;

import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveAlgorithmType;
import de.ovgu.dke.clustering.algorithm.competitive.WinnerSearchMethodType;
import de.ovgu.dke.clustering.algorithm.competitive.TreeGCSAlgorithm.PruningStrategy;
import de.ovgu.dke.clustering.algorithm.flat.FlatCompetitiveClusterer;
import de.ovgu.dke.clustering.algorithm.hierarchical.HierarchicalCompetitiveClusterer;
import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.clustering.util.DatasetSamplerType;
import de.ovgu.dke.clustering.util.PrototypeInitialzerType;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class HierarchicalCompetitiveExperiment 
{
	private static String evalPath = "/home/grenouille/irg_workspace/so_evaluation/clustering/";
	public static void main(String args[])
	{
		try {
			HierarchicalClusteringEvaluator evaluator = new HierarchicalClusteringEvaluator();
			//ObjectSet dataset = new CSVObjectSet("/home/grenouille/irg_workspace/datasets/uniform_4cluster.csv", "class");
			//ObjectSet finaldataset = new CSVObjectSet("/home/grenouille/irg_workspace/datasets/uniform_5cluster.csv", "class");
			ObjectSet dataset = new CSVObjectSet("/home/grenouille/irg_workspace/datasets/2d_gaussian_clusters.csv", "class");
			HierarchicalCompetitiveClusterer clusterer = new HierarchicalCompetitiveClusterer();
			HierarchicalClusterModel model = null;
			ArrayList<String> classes = new ArrayList<String>();
			classes.add("cluster0");
			classes.add("cluster1");
			//classes.add("cluster");
			ObjectSet tmpDataset = MCSUtil.removeVectorsByClassLabels(dataset, classes);
			
			clusterer.applySettings(getGCS2dGaussianSettings());
			clusterer.setDistanceMetric(DistanceMeasure.EUCLIDEAN);
			model = clusterer.computeClusterModel(tmpDataset);
			
			evaluator.setFlattenHierarchyOnLeafs(true);
			/*
			//evaluator.applyReferenceHierarchy(ReferenceLabelHierarchies.getBanksearchHierarchy());
			
			evaluator.writeResultToFile(evalPath+"uniform/treegcs_scattering_result.txt");
			evaluator.printResult();
			*/
			evaluator.applyClusterer(clusterer);
			evaluator.applyDataset(tmpDataset);
			evaluator.evaluate();
			evaluator.applyClusterModel(model);
			evaluator.printResult();
			evaluator.writeResultToFile(evalPath+"2d_gaussian/incr/treegcs_scatter_result_first.txt");
			//evaluator.writeDataAssignmentToFile(evalPath+"uniform/treegcs_incrcs_assign_incr4.txt");
			
			
			model = clusterer.incrementalComputation(dataset);
			evaluator.applyDataset(dataset);
			evaluator.applyClusterModel(model);
			evaluator.printResult();
			evaluator.writeResultToFile(evalPath+"2d_gaussian/incr/treegcs_scatter_result_final.txt");
			/*
			evaluator = new HierarchicalClusteringEvaluator();
			evaluator.setFlattenHierarchyOnLeafs(true);
			evaluator.applyClusterer(clusterer);
			evaluator.applyDataset(tmpDataset);
			evaluator.evaluate();
			evaluator.printResult();
			*/
			
			
		} catch(IOException e) {
			e.printStackTrace();
			
		}
		
	}
	
	private static Settings getGCSUniformSettings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "false"));
		
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		Settings gcsSettings = new Settings();
		gcsSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gcsSettings.setParameter(new Parameter("gamma", Double.toString(4d)));
		gcsSettings.setParameter(new Parameter("pruningThreshold", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("stopThreshold", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("tau", Double.toString(0.0008d))); //????
		gcsSettings.setParameter(new Parameter("epsilon", Double.toString(0.4))); //????
		gcsSettings.setParameter(new Parameter("pruningStrategy", PruningStrategy.LowDensityPruning.name()));
		gcsSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gcsSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.0006d)));
		gcsSettings.setParameter(new Parameter("learnRatePath", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("learnRateError", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("initErrorThreshold", Double.toString(500d)));
		gcsSettings.setParameter(new Parameter("maxEpochs", Integer.toString(10000)));
		gcsSettings.setParameter(new Parameter("useNodePruning", Boolean.toString(false)));
		p.setSettings(gcsSettings);
		settings.setParameter(p);
		
		Settings pInitSettings = new Settings();
		//pInitSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		//pInitSettings.setParameter(new Parameter("seed", Long.toString(1229169437460L)));
		pInitSettings.setParameter(new Parameter("seed", Long.toString(1229174443336L)));
		p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(pInitSettings);
		gcsSettings.setParameter(p);
		Settings samplingSettings = new Settings();
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(1229169437462L)));
		samplingSettings.setParameter(new Parameter("seed", Long.toString(1229174443339L)));
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(samplingSettings);
		gcsSettings.setParameter(p);
		
		
		return settings;
	}
	
	private static Settings getGCS2dGaussianSettings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "false"));
		
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		Settings gcsSettings = new Settings();
		gcsSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gcsSettings.setParameter(new Parameter("gamma", Double.toString(4d)));
		gcsSettings.setParameter(new Parameter("pruningThreshold", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("stopThreshold", Double.toString(0.15d)));
		gcsSettings.setParameter(new Parameter("tau", Double.toString(0.0008d))); //????
		gcsSettings.setParameter(new Parameter("epsilon", Double.toString(0.4))); //????
		gcsSettings.setParameter(new Parameter("pruningStrategy", PruningStrategy.LowDensityPruning.name()));
		gcsSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gcsSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.0006d)));
		gcsSettings.setParameter(new Parameter("learnRatePath", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("learnRateError", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("initErrorThreshold", Double.toString(500d)));
		gcsSettings.setParameter(new Parameter("maxEpochs", Integer.toString(10000)));
		gcsSettings.setParameter(new Parameter("useNodePruning", Boolean.toString(false)));
		p.setSettings(gcsSettings);
		settings.setParameter(p);
		
		Settings pInitSettings = new Settings();
		pInitSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		//pInitSettings.setParameter(new Parameter("seed", Long.toString(1229084692547L)));
		p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(pInitSettings);
		gcsSettings.setParameter(p);
		Settings samplingSettings = new Settings();
		samplingSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(1229084692550L)));
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(samplingSettings);
		gcsSettings.setParameter(p);
		
		
		return settings;
	}
	
	private static Settings getGCSGaussianSettings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "false"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		Settings gcsSettings = new Settings();
		gcsSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gcsSettings.setParameter(new Parameter("gamma", Double.toString(4d)));
		gcsSettings.setParameter(new Parameter("pruningThreshold", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("stopThreshold", Double.toString(0.15d)));
		gcsSettings.setParameter(new Parameter("tau", Double.toString(0.0008d))); //????
		gcsSettings.setParameter(new Parameter("epsilon", Double.toString(0.4))); //????
		gcsSettings.setParameter(new Parameter("pruningStrategy", PruningStrategy.LowDensityPruning.name()));
		gcsSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gcsSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.0006d)));
		gcsSettings.setParameter(new Parameter("learnRatePath", Double.toString(0.005d)));
		gcsSettings.setParameter(new Parameter("learnRateError", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("initErrorThreshold", Double.toString(500d)));
		gcsSettings.setParameter(new Parameter("maxEpochs", Integer.toString(10000)));
		gcsSettings.setParameter(new Parameter("useNodePruning", Boolean.toString(false)));
		p.setSettings(gcsSettings);
		settings.setParameter(p);
		
		Settings pInitSettings = new Settings();
		//pInitSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		pInitSettings.setParameter(new Parameter("seed", Long.toString(8194733013488334912L)));
		p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(pInitSettings);
		gcsSettings.setParameter(p);
		Settings samplingSettings = new Settings();
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		samplingSettings.setParameter(new Parameter("seed", Long.toString(-5255778599958931874L)));
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(samplingSettings);
		gcsSettings.setParameter(p);
		
		
		return settings;
	}
	
	private static Settings getGCSBSSettings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "false"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		Settings gcsSettings = new Settings();
		gcsSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gcsSettings.setParameter(new Parameter("gamma", Double.toString(4d)));
		gcsSettings.setParameter(new Parameter("pruningThreshold", Double.toString(0.03d)));
		gcsSettings.setParameter(new Parameter("stopThreshold", Double.toString(0.02d)));
		gcsSettings.setParameter(new Parameter("tau", Double.toString(0.0008d))); //????
		gcsSettings.setParameter(new Parameter("epsilon", Double.toString(0.8d))); //????
		gcsSettings.setParameter(new Parameter("pruningStrategy", PruningStrategy.LowRelativeErrorPruning.name()));
		gcsSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gcsSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.0006d)));
		gcsSettings.setParameter(new Parameter("learnRatePath", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("learnRateError", Double.toString(0.03d)));
		gcsSettings.setParameter(new Parameter("initErrorThreshold", Double.toString(200d)));
		gcsSettings.setParameter(new Parameter("maxEpochs", Integer.toString(500)));
		gcsSettings.setParameter(new Parameter("useNodePruning", Boolean.toString(false)));
		p.setSettings(gcsSettings);
		settings.setParameter(p);
		
		Settings pInitSettings = new Settings();
		pInitSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		//pInitSettings.setParameter(new Parameter("seed", Long.toString(1228924612919L)));
		p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(pInitSettings);
		gcsSettings.setParameter(p);
		Settings samplingSettings = new Settings();
		samplingSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(1228924612921L)));
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(samplingSettings);
		gcsSettings.setParameter(p);
		
		return settings;
	}
	
	private static Settings getGCSReuter1Settings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "false"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		Settings gcsSettings = new Settings();
		gcsSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gcsSettings.setParameter(new Parameter("gamma", Double.toString(4d)));
		gcsSettings.setParameter(new Parameter("pruningThreshold", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("stopThreshold", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("tau", Double.toString(0.0008d))); //????
		gcsSettings.setParameter(new Parameter("epsilon", Double.toString(0.4d))); //????
		gcsSettings.setParameter(new Parameter("pruningStrategy", PruningStrategy.LowDensityPruning.name()));
		gcsSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gcsSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.0006d)));
		gcsSettings.setParameter(new Parameter("learnRatePath", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("learnRateError", Double.toString(0.02d)));
		gcsSettings.setParameter(new Parameter("initErrorThreshold", Double.toString(200d)));
		gcsSettings.setParameter(new Parameter("maxEpochs", Integer.toString(500)));
		gcsSettings.setParameter(new Parameter("useNodePruning", Boolean.toString(false)));
		p.setSettings(gcsSettings);
		settings.setParameter(p);
		
		Settings pInitSettings = new Settings();
		//pInitSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		pInitSettings.setParameter(new Parameter("seed", Long.toString(1228933933485L)));
		p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(pInitSettings);
		gcsSettings.setParameter(p);
		Settings samplingSettings = new Settings();
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		samplingSettings.setParameter(new Parameter("seed", Long.toString(1228933933487L)));
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(samplingSettings);
		gcsSettings.setParameter(p);
		
		return settings;
	}
	

	private static Settings getGCSReuter2Settings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "false"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		Settings gcsSettings = new Settings();
		gcsSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gcsSettings.setParameter(new Parameter("gamma", Double.toString(4d)));
		gcsSettings.setParameter(new Parameter("pruningThreshold", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("stopThreshold", Double.toString(0.02d)));
		gcsSettings.setParameter(new Parameter("tau", Double.toString(0.0008d))); //????
		gcsSettings.setParameter(new Parameter("epsilon", Double.toString(0.4d))); //????
		gcsSettings.setParameter(new Parameter("pruningStrategy", PruningStrategy.LowDensityPruning.name()));
		gcsSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gcsSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.0006d)));
		gcsSettings.setParameter(new Parameter("learnRatePath", Double.toString(0.01d)));
		gcsSettings.setParameter(new Parameter("learnRateError", Double.toString(0.03d)));
		gcsSettings.setParameter(new Parameter("initErrorThreshold", Double.toString(200d)));
		gcsSettings.setParameter(new Parameter("maxEpochs", Integer.toString(500)));
		gcsSettings.setParameter(new Parameter("useNodePruning", Boolean.toString(false)));
		p.setSettings(gcsSettings);
		settings.setParameter(p);
		
		Settings pInitSettings = new Settings();
		//pInitSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		pInitSettings.setParameter(new Parameter("seed", Long.toString(1228936143282L)));
		p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(pInitSettings);
		gcsSettings.setParameter(p);
		Settings samplingSettings = new Settings();
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(1228936143285L)));
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(samplingSettings);
		gcsSettings.setParameter(p);
		
		return settings;
	}
	
	
	
	
	
	private static Settings getGNGGaussianSettings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "false"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingNeuralGas.name());
		Settings gngSettings = new Settings();
		gngSettings.setParameter(new Parameter("alpha", Double.toString(0.5d)));
		gngSettings.setParameter(new Parameter("zeta", Double.toString(2.8d)));
		gngSettings.setParameter(new Parameter("gamma", Double.toString(6d)));
		gngSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gngSettings.setParameter(new Parameter("tau", Double.toString(0.6d))); //????
		gngSettings.setParameter(new Parameter("epsilon", Double.toString(0.8d))); //????
		gngSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gngSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.006d)));
		gngSettings.setParameter(new Parameter("learnRatePath", Double.toString(0.01d)));
		gngSettings.setParameter(new Parameter("learnRateError", Double.toString(0.01)));
		gngSettings.setParameter(new Parameter("initErrorThreshold", Double.toString(1000d)));
		gngSettings.setParameter(new Parameter("maxEpochs", Integer.toString(20)));
		gngSettings.setParameter(new Parameter("useNodePruning", Boolean.toString(false)));
		p.setSettings(gngSettings);
		
		settings.setParameter(p);
		return settings;
	}
	
	private static Settings getGNGReuter2Settings()
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "false"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingNeuralGas.name());
		Settings gngSettings = new Settings();
		gngSettings.setParameter(new Parameter("alpha", Double.toString(0.5d)));
		gngSettings.setParameter(new Parameter("zeta", Double.toString(2.6d)));
		gngSettings.setParameter(new Parameter("gamma", Double.toString(6d)));
		gngSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gngSettings.setParameter(new Parameter("tau", Double.toString(0.6d))); //????
		gngSettings.setParameter(new Parameter("epsilon", Double.toString(0.8d))); //????
		gngSettings.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		gngSettings.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.006d)));
		gngSettings.setParameter(new Parameter("learnRatePath", Double.toString(0.01d)));
		gngSettings.setParameter(new Parameter("learnRateError", Double.toString(0.01)));
		gngSettings.setParameter(new Parameter("initErrorThreshold", Double.toString(500d)));
		gngSettings.setParameter(new Parameter("maxEpochs", Integer.toString(15)));
		gngSettings.setParameter(new Parameter("useNodePruning", Boolean.toString(false)));
		p.setSettings(gngSettings);
		
		settings.setParameter(p);
		return settings;
	}
	
	
}

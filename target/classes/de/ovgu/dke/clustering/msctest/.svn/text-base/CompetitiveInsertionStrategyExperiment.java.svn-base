package de.ovgu.dke.clustering.msctest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveAlgorithmType;
import de.ovgu.dke.clustering.algorithm.competitive.TreeNodeInsertionStrategy;
import de.ovgu.dke.clustering.algorithm.competitive.TreeGCSAlgorithm.PruningStrategy;
import de.ovgu.dke.clustering.algorithm.hierarchical.HierarchicalCompetitiveClusterer;
import de.ovgu.dke.clustering.evaluation.ClusterConfusionMatrix;
import de.ovgu.dke.clustering.evaluation.NonLeafFMeasure;
import de.ovgu.dke.clustering.evaluation.StandardFMeasure;
import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.clustering.util.DatasetSamplerType;
import de.ovgu.dke.clustering.util.PrototypeInitialzerType;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.DescriptiveStatistic;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;


public class CompetitiveInsertionStrategyExperiment 
{
	public static void main(String args[])
	{
		Random rnd = new Random(3253432753L);
		StandardFMeasure<HierarchicalClusterModel> standardFMeasure = new StandardFMeasure<HierarchicalClusterModel>();
		NonLeafFMeasure nlFMeasure = new NonLeafFMeasure();
		nlFMeasure.setReferenceHierarchy(ReferenceLabelHierarchies.getReuters2Hierarchy());
		double[] fMeasureValues = new double[10];
		double[] nlFMeasureValues = new double[10];
		try {
			ObjectSet dataset = new CSVObjectSet("/home/grenouille/irg_workspace/datasets/reuters_small2_topic100_stem.csv", "class");
			
			for(int i=0; i<10; i++)
			{
				HierarchicalCompetitiveClusterer clusterer = new HierarchicalCompetitiveClusterer();
				clusterer.applySettings(getGCSReuter2Settings(rnd.nextLong(), rnd.nextLong()));
				clusterer.setDistanceMetric(DistanceMeasure.COSINE);
				HierarchicalClusterModel model = clusterer.computeClusterModel(dataset);
				ClusterConfusionMatrix matrix = ClusterConfusionMatrix.createClusterConfusionMatrix(model, true);
				fMeasureValues[i] = standardFMeasure.compute(model, matrix);
				nlFMeasureValues[i] = nlFMeasure.compute(model,matrix);
			}
			
			System.out.println("standardFMeasure\tnlFMeasure");
			System.out.println(DescriptiveStatistic.mean(fMeasureValues) + "\t" + DescriptiveStatistic.mean(nlFMeasureValues));
			System.out.println(DescriptiveStatistic.minimum(fMeasureValues) + "\t" + DescriptiveStatistic.minimum(nlFMeasureValues));
			System.out.println(DescriptiveStatistic.maximum(fMeasureValues) + "\t" + DescriptiveStatistic.maximum(nlFMeasureValues));
			System.out.println(DescriptiveStatistic.stdDeviation(fMeasureValues) + "\t" + DescriptiveStatistic.stdDeviation(nlFMeasureValues));
			
		} catch(IOException e) {
			e.printStackTrace();
			
		}
	}
	
	private static Settings getGCSBSSettings(long initSeed, long samplingSeed)
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", "false"));
		
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		Settings gcsSettings = new Settings();
		gcsSettings.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		gcsSettings.setParameter(new Parameter("gamma", Double.toString(4d)));
		gcsSettings.setParameter(new Parameter("pruningThreshold", Double.toString(0.015d)));
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
		pInitSettings.setParameter(new Parameter("seed", Long.toString(initSeed)));
		p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(pInitSettings);
		gcsSettings.setParameter(p);
		Settings samplingSettings = new Settings();
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		samplingSettings.setParameter(new Parameter("seed", Long.toString(samplingSeed)));
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(samplingSettings);
		gcsSettings.setParameter(p);
		
		
		return settings;
	}
	
	private static Settings getGCSReuter1Settings(long initSeed, long samplingSeed)
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
		pInitSettings.setParameter(new Parameter("seed", Long.toString(initSeed)));
		p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(pInitSettings);
		gcsSettings.setParameter(p);
		Settings samplingSettings = new Settings();
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		samplingSettings.setParameter(new Parameter("seed", Long.toString(samplingSeed)));
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(samplingSettings);
		gcsSettings.setParameter(p);
		
		return settings;
	}
	

	private static Settings getGCSReuter2Settings(long initSeed, long samplingSeed)
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
		pInitSettings.setParameter(new Parameter("seed", Long.toString(initSeed)));
		p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(pInitSettings);
		gcsSettings.setParameter(p);
		Settings samplingSettings = new Settings();
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		samplingSettings.setParameter(new Parameter("seed", Long.toString(samplingSeed)));
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(samplingSettings);
		gcsSettings.setParameter(p);
		
		return settings;
	}
	
	
	
	
}
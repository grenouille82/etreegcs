package de.ovgu.dke.clustering.msctest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveAlgorithmType;
import de.ovgu.dke.clustering.algorithm.competitive.TreeGCSAlgorithm.PruningStrategy;
import de.ovgu.dke.clustering.algorithm.hierarchical.HierarchicalCompetitiveClusterer;
import de.ovgu.dke.clustering.evaluation.ClusterConfusionMatrix;
import de.ovgu.dke.clustering.evaluation.NonLeafFMeasure;
import de.ovgu.dke.clustering.evaluation.StandardFMeasure;
import de.ovgu.dke.clustering.model.ClusterNode;
import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.clustering.util.ClusteringUtility;
import de.ovgu.dke.clustering.util.DatasetSamplerType;
import de.ovgu.dke.clustering.util.PrototypeInitialzerType;
import de.ovgu.dke.tree.LinkedTree;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.DescriptiveStatistic;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class IncrInsertionStrategyExperiment 
{
	public static void main(String args[])
	{
	
		Random rnd = new Random(1234876557824L);
		//Random rnd = new Random(878798432753L);
		//Random rnd = new Random(3253432753L);
		StandardFMeasure<HierarchicalClusterModel> standardFMeasure = new StandardFMeasure<HierarchicalClusterModel>();
		NonLeafFMeasure nlFMeasure = new NonLeafFMeasure();
		LinkedTree<String> referenceHierarchy = ReferenceLabelHierarchies.getReuters1Hierarchy();
		
		LinkedTree<String> beforeHierarchy = referenceHierarchy.copy();
		beforeHierarchy.removeElement("Strategy/Plans");
		beforeHierarchy.removeElement("Health");
		
		double[] beforefMeasureValues = new double[10];
		double[] beforenlFMeasureValues = new double[10];
		double[] fMeasureValues = new double[10];
		double[] nlFMeasureValues = new double[10];
		double[] completeFmeasureValues = new double[10];
		long[] initSeeds = new long[10];
		long[] samplingSeeds = new long[10];
		try {
			ObjectSet dataset = new CSVObjectSet("/home/grenouille/irg_workspace/datasets/reuters_small1_topic100_stem.csv", "class");
			ObjectSet tmpDataset = MCSUtil.removeVectorsByClassLabels(dataset, getReuters1RemLeafClasses());
			for(int i=0; i<10; i++)
			{
				boolean found = true;
				initSeeds[i] = rnd.nextLong();
				samplingSeeds[i] = rnd.nextLong();
				HierarchicalCompetitiveClusterer clusterer = new HierarchicalCompetitiveClusterer();
				clusterer.applySettings(getGCSReuter1Settings(initSeeds[i], samplingSeeds[i]));
				//clusterer.applySettings(getGCSGaussianSettings(7743868023756130248L,	2615547288901641911L));
				 
				clusterer.setDistanceMetric(DistanceMeasure.COSINE);
				HierarchicalClusterModel model = clusterer.computeClusterModel(tmpDataset);
				ClusterConfusionMatrix matrix = ClusterConfusionMatrix.createClusterConfusionMatrix(model, true);
				System.out.println(matrix);
				nlFMeasure.setReferenceHierarchy(beforeHierarchy);
				beforefMeasureValues[i] = standardFMeasure.compute(model, matrix);
				beforenlFMeasureValues[i] = nlFMeasure.compute(model,matrix);
				System.out.println("incremental call");
				model = clusterer.incrementalComputation(dataset);
				//if(8 != numberOfLeafs(model))
				//	found = false;
				matrix = ClusterConfusionMatrix.createClusterConfusionMatrix(model, true);
				System.out.println(matrix);
				System.out.println(clusterer.getSettings());
				nlFMeasure.setReferenceHierarchy(referenceHierarchy);
				fMeasureValues[i] = standardFMeasure.compute(model, matrix);
				nlFMeasureValues[i] = nlFMeasure.compute(model,matrix);
				//model = clusterer.computeClusterModel(dataset);
				//found = numberOfLeafs(model) == 8;
				//completeFmeasureValues[i] = standardFMeasure.compute(model);
				
			}
			System.out.println("first Result:");
			System.out.println("standardFMeasure\tnlFMeasure");
			System.out.println(DescriptiveStatistic.mean(beforefMeasureValues) + "\t" + DescriptiveStatistic.mean(beforenlFMeasureValues));
			System.out.println(DescriptiveStatistic.minimum(beforefMeasureValues) + "\t" + DescriptiveStatistic.minimum(beforenlFMeasureValues));
			System.out.println(DescriptiveStatistic.maximum(beforefMeasureValues) + "\t" + DescriptiveStatistic.maximum(beforenlFMeasureValues));
			System.out.println(DescriptiveStatistic.stdDeviation(beforefMeasureValues) + "\t" + DescriptiveStatistic.stdDeviation(beforenlFMeasureValues));
			System.out.println("final Result:");
			System.out.println("standardFMeasure\tnlFMeasure");
			System.out.println(DescriptiveStatistic.mean(fMeasureValues) + "\t" + DescriptiveStatistic.mean(nlFMeasureValues));
			System.out.println(DescriptiveStatistic.minimum(fMeasureValues) + "\t" + DescriptiveStatistic.minimum(nlFMeasureValues));
			System.out.println(DescriptiveStatistic.maximum(fMeasureValues) + "\t" + DescriptiveStatistic.maximum(nlFMeasureValues));
			System.out.println(DescriptiveStatistic.stdDeviation(fMeasureValues) + "\t" + DescriptiveStatistic.stdDeviation(nlFMeasureValues));
			System.out.println();
			System.out.println("init Seeds: " + Arrays.toString(initSeeds));
			System.out.println("sampling Seeds: " + Arrays.toString(samplingSeeds));
			System.out.println("final FMeasure: " + Arrays.toString(fMeasureValues));
			System.out.println("final nlFMeasure: " + Arrays.toString(nlFMeasureValues));
			
		} catch(IOException e) {
			e.printStackTrace();
			
		}
	}
	
	private static int numberOfLeafs(HierarchicalClusterModel model)
	{
		int cnt = 0;
		for(ClusterNode node : model.getClusters())
		{
			if(!node.hasSubClusters())
				cnt++;
		}
		return cnt;
	}
	
	private static Collection<String> getBanksearchRemLeafClasses()
	{
		Collection<String> retVal = new LinkedList<String>();
		retVal.add("Banking");
		retVal.add("Commercial Banks");
		retVal.add("Building Societies");
		retVal.add("Insurance Agencies");
		return retVal;
	}
	
	private static Collection<String> getBanksearchRemLeafClasses1()
	{
		Collection<String> retVal = new LinkedList<String>();
		retVal.add("Commercial Banks");
		retVal.add("Java");
		return retVal;
	}
	
	private static Collection<String> getReuters1RemLeafClasses()
	{
		Collection<String> retVal = new LinkedList<String>();
		retVal.add("STRATEGY/PLANS");
		retVal.add("HEALTH");
		return retVal;
	}
	
	private static Collection<String> getRemGaussianClusters()
	{
		Collection<String> retVal = new LinkedList<String>();
		retVal.add("cluster0");
		//retVal.add("cluster0");
		retVal.add("cluster1");
		return retVal;
	}
	
	private static Settings getGCSGaussianSettings(long initSeed, long samplingSeed)
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
		//pInitSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		pInitSettings.setParameter(new Parameter("seed", Long.toString(initSeed)));
		System.out.println("rand init" + initSeed);
		p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(pInitSettings);
		gcsSettings.setParameter(p);
		Settings samplingSettings = new Settings();
		//samplingSettings.setParameter(new Parameter("seed", Long.toString(System.currentTimeMillis())));
		samplingSettings.setParameter(new Parameter("seed", Long.toString(samplingSeed)));
		System.out.println("rand sample " + samplingSeed);
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(samplingSettings);
		gcsSettings.setParameter(p);
	
		
		
		return settings;
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

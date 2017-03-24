package de.ovgu.dke.clustering.algorithm.competitive;

import de.ovgu.dke.util.Settings;

public enum CompetitiveAlgorithmType
{
	GrowingNeuralGas,
	GrowingCellStructures;
	
	public static GraphCompetitiveLearningAlgorithm createGraphLearner(CompetitiveAlgorithmType type, 
														   			   Settings setting)
	{
		if(type == null)
			throw new NullPointerException();
		
		GraphCompetitiveLearningAlgorithm learner = null;
		switch(type)
		{
		case GrowingNeuralGas:
			learner = new GrowingNeuralGasAlgorithm();
			break;
		case GrowingCellStructures:
			learner = new GrowingCellStructureAlgorithm();
			break;
		default:
			throw new IllegalArgumentException();
		}
		learner.applySettings(setting);
		return learner;
	}
	
	public static TreeCompetitiveLearningAlgorithm createTreeLearner(CompetitiveAlgorithmType type, 
			   											  	  		 Settings setting)
	{
		if(type == null)
			throw new NullPointerException();
		
		TreeCompetitiveLearningAlgorithm learner = null;
		switch(type)
		{
		case GrowingNeuralGas:
			learner = new TreeGNGAlgorithm();
			break;
		case GrowingCellStructures:
			learner = new TreeGCSAlgorithm();
			break;
		default:
			throw new IllegalArgumentException();
		}
		learner.applySettings(setting);
		return learner;
	}
}

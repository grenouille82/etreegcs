package de.ovgu.dke.clustering.util;

import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Settings;

public enum DatasetSamplerType 
{
	Constant,
	ConstantEpoch,
	RandomEpoch;
	
	public static DatasetSampler createDatasetsampler(DatasetSamplerType type, ObjectSet dataset, int maxEpochs)
	{
		switch(type)
		{
		case Constant:
			return new ConstantDatasetSampler(dataset, maxEpochs);
		case ConstantEpoch:
			return new ConstantEpochDatasetSampler(dataset, maxEpochs);
		case RandomEpoch:
		default:
			return new RandomEpochDatasetSampler(dataset, maxEpochs);
		}
	}

	public static DatasetSampler createDatasetsampler(DatasetSamplerType type, ObjectSet dataset, Settings settings)
	{
		DatasetSampler sampler = null;
		switch(type)
		{
		case Constant:
			sampler = new ConstantDatasetSampler(dataset);
			break;
		case ConstantEpoch:
			sampler = new ConstantEpochDatasetSampler(dataset);
			break;
		case RandomEpoch:
		default:
			sampler = new RandomEpochDatasetSampler(dataset);
			break;
		}
		sampler.applySettings(settings);
		return sampler;
	}
	
}

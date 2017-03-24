package de.ovgu.dke.clustering.util;

import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Settings;

public enum PrototypeInitialzerType 
{
	RandomRange,
	RandomExample,
	RandomGaussian;
	
	public static PrototypeInitializer createPrototypeInitializer(PrototypeInitialzerType type, ObjectSet dataset)
	{
		switch(type)
		{
		case RandomRange:
			return new RandomRangeInitializer(dataset);
		case RandomGaussian:
			return new RandomGaussianInitializer(dataset);
		case RandomExample:
		default:
			return new RandomExampleInitializer(dataset);
		}
	}
	
	public static PrototypeInitializer createPrototypeInitializer(PrototypeInitialzerType type, ObjectSet dataset, 
																  Settings settings)
	{
		PrototypeInitializer retVal = null;
		switch(type)
		{
		case RandomRange:
			retVal = new RandomRangeInitializer(dataset);
			break;
		case RandomGaussian:
			retVal = new RandomGaussianInitializer(dataset);
			break;
		case RandomExample:
		default:
			retVal = new RandomExampleInitializer(dataset);
			break;
		}
		retVal.applySettings(settings);
		return retVal;
	}

}

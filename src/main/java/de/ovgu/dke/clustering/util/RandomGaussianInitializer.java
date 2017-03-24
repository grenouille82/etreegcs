package de.ovgu.dke.clustering.util;

import java.util.Random;

import de.ovgu.dke.util.DescriptiveStatistic;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

/**
 * 
 * @author mhermkes
 * 
 * attributes are handled independent
 */
public class RandomGaussianInitializer implements PrototypeInitializer
{
	private static long DEFAULT_SEED = System.currentTimeMillis();
	
	private double[] meanVector;
	private double[] stdDevVector;
	
	private long seed;
	private Random rnd;
	
	public RandomGaussianInitializer(ObjectSet dataset)
	{
		this(dataset, DEFAULT_SEED);
	}
	
	public RandomGaussianInitializer(ObjectSet dataset, long seed)
	{
		rnd = new Random(seed);
		computeStatistics(dataset);
	}
	
	public RandomGaussianInitializer(double[] meanVector, double[] stdDevVector)
	{
		this(meanVector, stdDevVector, System.currentTimeMillis());
	}
	
	public RandomGaussianInitializer(double[] meanVector, double[] stdDevVector, long seed)
	{
		if(meanVector == null)
			throw new NullPointerException();
		if(stdDevVector == null)
			throw new NullPointerException();
		if(meanVector.length != stdDevVector.length)
			throw new IllegalArgumentException("dimension of statistic vectors are different");

		rnd = new Random(seed);
		this.meanVector = meanVector;
		this.stdDevVector = stdDevVector;
	}

	public double[][] getNVectors(int n) 
	{
		if(n<0)
			throw new IllegalArgumentException("n must be greater or equal than 0: " + n);
		double[][] retVal = new double[n][];
		for(int i=0; i<n; i++)
			retVal[i] = nextVector();
		return retVal;
	}

	public double[] nextVector() 
	{
		int dim = meanVector.length;
		double[] retVal = new double[dim];
		for(int i=0; i<dim; i++)
			retVal[i] = meanVector[i] + stdDevVector[i] * rnd.nextGaussian();
		return retVal;
	}
	
	public void setSeed(long seed)
	{
		rnd.setSeed(seed);
		this.seed = seed;
	}
	
	public void setDataset(ObjectSet dataset)
	{
		computeStatistics(dataset);
	}
	
	public void setStatisticVectors(double[] meanVector, double[] stdDevVector)
	{
		if(meanVector == null)
			throw new NullPointerException();
		if(stdDevVector == null)
			throw new NullPointerException();
		if(meanVector.length != stdDevVector.length)
			throw new IllegalArgumentException("dimension of statistic vectors are different");

		this.meanVector = meanVector;
		this.stdDevVector = stdDevVector;
	}

	public void applyDefaultSettings() 
	{
		setSeed(DEFAULT_SEED);
	}

	public void applySettings(Settings settings) 
	{
		if(settings != null) {
			Parameter p = settings.getParameter("seed");
			if(p != null) {
				long value = Long.parseLong(p.getValue());
				setSeed(value);
			}
		}
	}

	public Settings getDefaultSettings() 
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("seed", Long.toString(DEFAULT_SEED)));
		return settings;
	}

	public Settings getSettings() 
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("seed", Long.toString(seed)));
		return settings;
	}
	
	private final void computeStatistics(ObjectSet dataset)
	{
		if(dataset == null)
			throw new NullPointerException();
		int dim = dataset.getObjectSize();
		int n 	= dataset.size();
		meanVector	 = new double[dim];
		stdDevVector = new double[dim];
		
		double[] v = null;
		double[] invVector = new double[n];
		for(int i=0; i<dim; i++)
		{
			for(int j=0; j<n; j++)
			{
				v = dataset.get(j).getRepresentation();
				invVector[j] = v[i];
			}
			meanVector[i] 	= DescriptiveStatistic.mean(v);
			stdDevVector[i] = DescriptiveStatistic.stdDeviation(v);
		}
	}
}

package de.ovgu.dke.clustering.util;

import java.util.Random;

import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class RandomRangeInitializer implements PrototypeInitializer
{
	private static long DEFAULT_SEED = System.currentTimeMillis();
	
	private double[] minVector;
	private double[] maxVector;
	
	private long seed;
	private Random rnd;
	
	public RandomRangeInitializer(ObjectSet dataset)
	{
		this(dataset, DEFAULT_SEED);
	}
	
	public RandomRangeInitializer(ObjectSet dataset, long seed)
	{
		rnd = new Random(seed);
		computeRangeVectors(dataset);
	}
	
	public RandomRangeInitializer(double[] minVector, double[] maxVector)
	{
		this(minVector, maxVector, System.currentTimeMillis());
	}
	
	public RandomRangeInitializer(double[] minVector, double[] maxVector, long seed)
	{
		rnd = new Random(seed);
		validateRangeVectors(minVector, maxVector);
		this.minVector = minVector;
		this.maxVector = maxVector;
	}


	public double[] nextVector() 
	{
		int dim = minVector.length;
		double[] retVal = new double[dim];
		for(int i=0; i<dim; i++)
			retVal[i] = rnd.nextDouble() * (maxVector[i]-minVector[i]) + minVector[i];
		return retVal;
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
	
	public void setSeed(long seed)
	{
		rnd.setSeed(seed);
		this.seed = seed;
	}
	
	public void setDataset(ObjectSet dataset)
	{
		computeRangeVectors(dataset);
	}
	
	public void setRangeVectors(double[] minVector, double[] maxVector)
	{
		validateRangeVectors(minVector, maxVector);
		this.minVector = minVector;
		this.maxVector = maxVector;
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
	
	private final void validateRangeVectors(double[] minV, double[] maxV)
	{
		if(minV == null)
			throw new NullPointerException();
		if(maxV == null)
			throw new NullPointerException();
		if(minV.length != maxV.length)
			throw new IllegalArgumentException("dimension of range vectors are different");
		
		int dim = minV.length;
		for(int i=0; i<dim; i++)
		{
			if(minV[i] > maxV[i])
				throw new IllegalArgumentException("range vectores are inconsistent at idx " + i);
		}
		
		
	}
	
	private final void computeRangeVectors(ObjectSet dataset)
	{
		if(dataset == null)
			throw new NullPointerException();
		
		int dim = dataset.getObjectSize();
		int n = dataset.size();
		
		minVector = new double[dim];
		maxVector = new double[dim];
		double[] v = null;
		for(int i=0; i<n; i++)
		{
			v = dataset.get(i).getRepresentation();
			for(int j=0; j<dim; j++)
			{
				minVector[j] = Math.min(minVector[j], v[j]);
				maxVector[j] = Math.max(maxVector[j], v[j]);
			}
		}
	}
}

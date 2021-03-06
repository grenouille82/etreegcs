package de.ovgu.dke.clustering.util;

import java.util.HashSet;
import java.util.Random;

import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class RandomExampleInitializer implements PrototypeInitializer
{
	private static long DEFAULT_SEED = System.currentTimeMillis();
	
	private long seed;
	
	private Random rnd;
	private ObjectSet dataset;

	/**
	 * 
	 * @param dataset
	 */
	public RandomExampleInitializer(ObjectSet dataset)
	{
		this(dataset, DEFAULT_SEED);
		System.out.println(" init " + DEFAULT_SEED);
	}
	
	/**
	 * 
	 * @param dataset
	 * @param seed
	 */
	public RandomExampleInitializer(ObjectSet dataset, long seed)
	{
		if(dataset == null)
			throw new NullPointerException();
		this.dataset = dataset;
		rnd = new Random(seed);
		this.seed = seed;
	}
	
	public double[] nextVector() 
	{
		int i = rnd.nextInt(dataset.size());
		System.out.println("example: " + i);
		return dataset.get(i).getRepresentation();
	}
	
	/**
	 * 
	 * @param n
	 * @return
	 */
	public double[][] getNVectors(int n)
	{
		if(n<0)
			throw new IllegalArgumentException("n must be greater or equal than 0: " + n);
		double[][] retVal = new double[n][];
		HashSet<Integer> indices = new HashSet<Integer>();
		int randomNumber = 0;
		for(int i=0; i<n; i++)
		{
			do {
				randomNumber = rnd.nextInt(dataset.size());
			} while(!indices.add(randomNumber));
			retVal[i] = dataset.get(randomNumber).getRepresentation();
		}
		return retVal;
	}
	
	
	
	public void setDataset(ObjectSet dataset)
	{
		if(dataset == null)
			throw new NullPointerException();
		this.dataset = dataset;
	}
	
	public void setSeed(long seed)
	{
		rnd = new Random(seed);
		this.seed = seed;
		System.out.println("seed init " + seed);
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
}

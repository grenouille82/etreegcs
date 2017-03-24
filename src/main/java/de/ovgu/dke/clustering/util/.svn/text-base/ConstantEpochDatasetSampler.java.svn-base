package de.ovgu.dke.clustering.util;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Random;

import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.ObjectWrapper;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

/**
 * 
 * @author mhermkes
 * TODO: throw an Exception if the iteration process is started and someone tries
 *       to change the internal state of the sampler (i.e. the applyXXX() methods
 *       are invoked)
 */
public class ConstantEpochDatasetSampler implements DatasetSampler
{
	private static int DEFAULT_MAX_EPOCHS = Integer.MAX_VALUE;
	
	private static long DEFAULT_SEED = System.currentTimeMillis();
	
	private int maxEpochs;
	
	private int epoch;
	
	private ObjectSet dataset;
	
	private LinkedList<ObjectWrapper> samples;
	
	private ListIterator<ObjectWrapper> lit;
	
	private long seed;
	private Random rnd;
	
	public ConstantEpochDatasetSampler(ObjectSet dataset)
	{
		this(dataset, DEFAULT_MAX_EPOCHS, DEFAULT_SEED);
	}
	
	public ConstantEpochDatasetSampler(ObjectSet dataset, int maxEpoch)
	{
		this(dataset, maxEpoch, DEFAULT_SEED);
	}
	
	public ConstantEpochDatasetSampler(ObjectSet dataset, int maxEpoch, long seed)
	{
		if(dataset == null)
			throw new NullPointerException();
		if(maxEpoch < 0)
			throw new IllegalArgumentException(
			"maximum number of epochs must be a positive number");
		
		epoch	= 0;
		rnd		= new Random(seed);
		this.seed = seed;
		this.maxEpochs = maxEpoch;
		this.dataset   = dataset;
		System.out.println(" sampler " + DEFAULT_SEED);
		populateSamples();
	}
	
	public boolean hasNext() 
	{
		return lit.hasNext();
	}

	public ObjectWrapper next() 
	{
		if(epoch<maxEpochs) {
			ObjectWrapper next = lit.next();
			if(!lit.hasNext() && epoch+1<maxEpochs) {
				lit = samples.listIterator();
				epoch++;
			}
			return next;
		}
		throw new NoSuchElementException();
	}

	public int epoch() 
	{
		return epoch;
	}
	
	
	public void applyDefaultSettings() 
	{
		maxEpochs = DEFAULT_MAX_EPOCHS;
		seed = DEFAULT_SEED;
		rnd = new Random(seed);
	}

	public void applySettings(Settings settings) 
	{
		if(settings != null) {
			Parameter p = settings.getParameter("maxEpochs");
			if(p != null) {
				maxEpochs = Integer.valueOf(p.getValue());
			}
			p = settings.getParameter("seed");
			if(p!= null) {
				long val = Long.valueOf(p.getValue());
				rnd = new Random(val);
				seed = val;
				populateSamples();
				System.out.println("seed epoch " + val);
			}
		}
	}

	public Settings getDefaultSettings() 
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("maxEpochs", Integer.toString(DEFAULT_MAX_EPOCHS)));
		settings.setParameter(new Parameter("seed", Long.toString(DEFAULT_SEED)));
		return settings;
	}

	public Settings getSettings() 
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("maxEpochs", Integer.toString(maxEpochs)));
		settings.setParameter(new Parameter("seed", Long.toString(seed)));
		return settings;
	}
	
	public ObjectSet getDataset()
	{
		return dataset;
	}
	
	protected void populateSamples()
	{
		int n 	= dataset.size();
		samples = new LinkedList<ObjectWrapper>();

		boolean[] mask = new boolean[n];
		while(samples.size() != n)
		{
			int rndNumber = 0;
			do {
				rndNumber = rnd.nextInt(n);
			} while(mask[rndNumber]);
			mask[rndNumber] = true;
			samples.add(dataset.get(rndNumber));
			//System.out.println("examples :  " + rndNumber);
		}
		lit = samples.listIterator();
	}
}

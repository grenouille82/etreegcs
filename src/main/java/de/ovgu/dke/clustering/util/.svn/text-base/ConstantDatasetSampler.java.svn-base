package de.ovgu.dke.clustering.util;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

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
public class ConstantDatasetSampler implements DatasetSampler
{
	private static int DEFAULT_MAX_EPOCHS = Integer.MAX_VALUE;
	
	private int maxEpochs;
	
	private int epoch;
	
	private ObjectSet dataset;
	
	private LinkedList<ObjectWrapper> samples;
	
	private ListIterator<ObjectWrapper> lit;
	
	public ConstantDatasetSampler(ObjectSet dataset)
	{
		this(dataset, DEFAULT_MAX_EPOCHS);
	}
	
	public ConstantDatasetSampler(ObjectSet dataset, int maxEpoch)
	{
		if(dataset == null)
			throw new NullPointerException();
		if(maxEpoch < 0)
			throw new IllegalArgumentException(
			"maximum number of epochs must be a positive number");
		
		epoch = 0;
		this.maxEpochs = maxEpoch;
		this.dataset  = dataset;
		
		populateSamples();
	}
	
	public boolean hasNext() 
	{
		return lit.hasNext();
	}

	public ObjectWrapper next() 
	{
		if(epoch<maxEpochs) {
			if(lit.hasNext()) {
				return lit.next();
			} 
			if(epoch+1<maxEpochs) {
				lit = samples.listIterator();
				epoch++;
				return lit.next();
			}
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
	}

	public void applySettings(Settings settings) 
	{
		if(settings != null) {
			Parameter p = settings.getParameter("maxEpochs");
			if(p != null) {
				maxEpochs = Integer.valueOf(p.getValue());
			}
		}
	}

	public Settings getDefaultSettings() 
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("maxEpochs", Integer.toString(DEFAULT_MAX_EPOCHS)));
		return settings;
	}

	public Settings getSettings() 
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("maxEpochs", Integer.toString(maxEpochs)));
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
		for(int i=0; i<n; i++)
			samples.add(dataset.get(i));
		lit = samples.listIterator();
	}
}

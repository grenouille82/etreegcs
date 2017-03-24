package de.ovgu.dke.matrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SparseVector extends AbstractVector
implements Vector
{
	private final static double NA = 0.0d; 
	
	
	private Map<Integer, Double> values;
	
	private int size;
	
	public SparseVector(int size)
	{
		if(size < 0)
			throw new IllegalArgumentException();
		
		values = new HashMap<Integer, Double>();
		this.size = size;
	}
	
	public SparseVector(double[] values)
	{
		if(values == null)
			throw new NullPointerException("values");
		
		size = values.length;
		this.values = new HashMap<Integer, Double>();
		
		for(int i=0; i<size; i++)
		{
			if(values[i] != NA || !Double.isInfinite(values[i]) || 
			   !Double.isInfinite(values[i])) {
				this.values.put(i, values[i]);
			}
		}
	}
	
	public SparseVector(Vector v)
	{
		if(v == null)
			throw new NullPointerException("v");
		
		size = v.size();
		
		if(v instanceof SparseVector)
			values = new HashMap<Integer, Double>(((SparseVector) v).values);
		else {
			this.values = new HashMap<Integer, Double>();
			
			double val = 0d;
			for(int i=0; i<size; i++)
			{
				val = v.getValue(i);
				if(val != NA || !Double.isInfinite(val) || !Double.isInfinite(val)) 
					this.values.put(i, val);
			}	
		}
	}
	
	public double getValue(int index)
	{
		rangeCheck(index);
		Double retVal = values.get(index);
		return (retVal == null) ? 0.0d : retVal;
	}
	
	public void setValue(int index, double value)
	{
		rangeCheck(index);
		
		if(value == NA)
			values.remove(index);
		else
			values.put(index, value);
	}
	
	public double[] toArray()
	{
		double[] retVal = new double[size];
		
		for(Entry<Integer, Double> e : values.entrySet())
			retVal[e.getKey()] = e.getValue();
		
		return retVal;
	}
	
	public int size()
	{
		return size;
	}
	
	private final void rangeCheck(int index)
	{
		
	}
}

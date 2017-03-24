package de.ovgu.dke.matrix;

import java.util.Map.Entry;


public class DenseVector extends AbstractVector
implements Vector
{
	private double[] values;
	
	private int size;
	
	public DenseVector(int size)
	{
		if(size < 0)
			throw new IllegalArgumentException();
		
		this.size = size;
		values = new double[this.size];
	}
	
	public DenseVector(double[] values)
	{
		if(values == null)
			throw new NullPointerException("values");
		
		this.values = values.clone();
		size = this.values.length;
	}
	
	public DenseVector(Vector v)
	{
		if(v == null) 
			throw new NullPointerException("v");
		
		if(v instanceof DenseVector) { 
			values 	= ((DenseVector) v).values.clone();
			size 	= values.length;
		} else {
			values 	= new double[v.size()];
			size	= values.length;
			
			for(int i=0; i<size; i++) 
				values[i] = v.getValue(i);
		}
	}
	
	public double getValue(int index)
	{
		rangeCheck(index);
		return values[index];
	}
	
	public void setValue(int index, double value)
	{
		rangeCheck(index);
		values[index] = value;
	}
	
	public double[] toArray()
	{
		return values.clone();
	}
	
	public int size()
	{
		return size;
	}
	
	private final void rangeCheck(int index)
	{
		
	}
}

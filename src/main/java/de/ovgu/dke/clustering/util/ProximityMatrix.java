package de.ovgu.dke.clustering.util;

import java.util.Arrays;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

/**
 * 
 * @author grenouille
 * TODO: - make it more generic for the using of similarity 
 *         and distance measures for hierarchical clustering methods
 *       - extract an interface
 *       - adding methods for find proximate points
 *       - implement a hac proximity matrix
 */
public class ProximityMatrix 
{
	public static ProximityMatrix createProximityMatrix(ObjectSet dataset, DistanceMeasure metric)
	{
		if(dataset == null)
			throw new NullPointerException();
		if(metric == null)
			throw new NullPointerException();
		
		int n = dataset.size();
		ProximityMatrix proxMatrix = new ProximityMatrix(n);
		
		double[] a, b;
		double val;
		for(int i=0; i<n; i++)
		{
			a = dataset.get(i).getRepresentation();
			for(int j=i+1; j<n; j++)
			{
				b = dataset.get(j).getRepresentation();
				val = metric.getDistance(a, b);
				proxMatrix.setValue(i, j, val);
			}
		}
		return proxMatrix;
	}
	
	
	private double[][] values;
	
	private int size;
	
	public ProximityMatrix(int size)
	{
		if(size<0)
			throw new IllegalArgumentException("size must be greater than 0");
		
		this.size = size;
		values = new double[size][];
		for(int i=0; i<size;i++)
			values[i] = new double[size-i];
	}
	
	public double getValue(int i, int j)
	{
		if(i>j)
			return getValue(j,i);
		
		rangeCheck(i);
		rangeCheck(j);
		return values[i][j-i];
	}
	
	public void setValue(int i, int j, double value)
	{
		if(i>j) 
			setValue(j, i, value);
		else {
			rangeCheck(i);
			rangeCheck(j);
			values[i][j-i] = value;
		}
	}
	
	public int size()
	{
		return size;
	}
	
	private final void rangeCheck(int index) 
	{
		if (index >= size)
		    throw new IndexOutOfBoundsException(
			"Index: "+index+", Size: "+size);
	}
	
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<i; j++)
				s.append(' ');
			s.append(Arrays.toString(values[i]));
			s.append('\n');
		}
		return s.toString();
	}
}

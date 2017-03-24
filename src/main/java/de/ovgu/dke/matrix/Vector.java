package de.ovgu.dke.matrix;


import de.ovgu.dke.matrix.Vector.Entry;
import de.ovgu.dke.util.Copyable;

public interface Vector extends Copyable<Vector>, Iterable<Entry>
{
	public double getValue(int index);
	
	public void setValue(int index, double value);
	
	public Vector plus(double x);
	
	public Vector plus(Vector v);
	
	public void minus(double x);
	
	public void minus(Vector v);
	
	public void times(double x);
	
	public double dotProduct(Vector v);
	
	public Matrix crossProrduct(Vector v);
	
	public void divide(double x);
	
	public double length();
	
	public double norm(double p);
	
	public void normalize();
	
	public double[] toArray();
	
	public int size();
	
	public interface Entry
	{
		public int getIndex();
		
		public double getValue();
		
		public void setValue(double x);
	}
}

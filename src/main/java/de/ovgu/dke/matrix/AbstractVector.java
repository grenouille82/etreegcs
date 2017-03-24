package de.ovgu.dke.matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractVector implements Vector 
{	
	public void plus(double x)
	{
		if(x != 0.0d) {
			double val = 0d;
			for(Entry e : this)
			{
				val = e.getValue();
				e.setValue(val+x);
			}
		}
	}
	
	public void plus(Vector v)
	{
		if(v == null)
			throw new NullPointerException("v");
		
	}
	
	public void minus(double x)
	{
		if(x != 0.0d) {
			double val = 0d;
			for(Entry e : this)
			{
				val = e.getValue();
				e.setValue(val-x);
			}
		}
	}
	
	public void minus(Vector v)
	{
		if(v == null)
			throw new NullPointerException("v");
	}
	
	public void times(double x)
	{
		double val = 0d;
		for(Entry e : this)
		{
			val = e.getValue();
			e.setValue(val*x);
		}
	}
	
	public double dotProduct(Vector v);
	
	public Matrix crossProrduct(Vector v);
	
	public void divide(double x)
	{
		double val = 0d;
		for(Entry e : this)
		{
			val = e.getValue();
			e.setValue(val/x);
		}
	}
	
	public double length()
	{
		
	}
	
	public double norm(double p);
	
	public void normalize();
	
	public double[] toArray()
	{
		
	}
	
	public int size()
	{
		int size = 0;
		Iterator<Entry> it = iterator();
		while(it.hasNext())
		{
			it.next();
			size++;
		}
		return size;
	}
	
	public Iterator<Entry> iterator()
	{
		return new SimpleIterator();
	}
	
	class SimpleIterator implements Iterator<Entry>
	{
		private int size; 
		
		private int cursor;
		
		private int lastRet = -1;
		
		private Entry current;
		
		@Override
		public boolean hasNext() 
		{
			return cursor != size;
		}

		@Override
		public Entry next() 
		{
			try {
				current = new SimpleEntry(cursor);
				lastRet = cursor++;
				return new SimpleEntry(cursor);
			} catch (IndexOutOfBoundsException e) {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() 
		{
			if(lastRet == -1)
				throw new IllegalStateException();
			
			current.setValue(0.0d);
		}
		
	}
	
	
	class SimpleEntry implements Entry
	{
		private int index;
		
		SimpleEntry(int index)
		{
			this.index = index;
		}
		
		@Override
		public int getIndex() 
		{
			return index;
		}

		@Override
		public double getValue() 
		{
			return AbstractVector.this.getValue(index);
		}

		@Override
		public void setValue(double x) 
		{
			AbstractVector.this.setValue(index, x);
		}
		
	}
}

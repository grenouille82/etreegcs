package de.ovgu.dke.matrix;

public abstract class AbstractMatrix implements Matrix
{
	public void setRowVector(int row, Vector v)
	{
		if(v == null)
			throw new NullPointerException("v");
	}
	
	public void setColumnVector(int column, Vector v)
	{
		if(v == null)
			throw new NullPointerException("v");
	}
	
	public void plus(double x)
	{
		int m = nRows();
		int n = nColumns();
		
		double v = 0d;
		for(int i=0; i<m; i++)
		{
			for(int j=0; j<n; j++)
			{
				v = getValue(i, j);
				setValue(i, j, v+x);
			}
		}
	}
	
	public void plus(Matrix a)
	{
		if(a == null)
			throw new NullPointerException("a");
		
		int m = nRows();
		int n = nColumns();
		
		if(m != a.nRows() || n != a.nColumns())
			throw new CardinalityException();

		double v = 0d;
		for(int i=0; i<m; i++)
		{
			for(int j=0; j<n; j++)
			{
				v = getValue(i, j) + a.getValue(i, j);
				setValue(i, j, v);
			}
		}
	}
	
	public void minus(double x)
	{
		int m = nRows();
		int n = nColumns();
		
		double v = 0d;
		for(int i=0; i<m; i++)
		{
			for(int j=0; j<n; j++)
			{
				v = getValue(i, j);
				setValue(i, j, v-x);
			}
		}
	}
	
	public void minus(Matrix a)
	{
		if(a == null)
			throw new NullPointerException("a");
		
		int m = nRows();
		int n = nColumns();
		
		if(m != a.nRows() || n != a.nColumns())
			throw new CardinalityException();

		double v = 0d;
		for(int i=0; i<m; i++)
		{
			for(int j=0; j<n; j++)
			{
				v = getValue(i, j) - a.getValue(i, j);
				setValue(i, j, v);
			}
		}
	}
	
	public void times(double x)
	{
		int m = nRows();
		int n = nColumns();
		
		double v = 0d;
		for(int i=0; i<m; i++)
		{
			for(int j=0; j<n; j++)
			{
				v = getValue(i, j);
				setValue(i, j, v*x);
			}
		}

	}
	
	
	public void divide(double x)
	{
		int m = nRows();
		int n = nColumns();
		
		double v = 0d;
		for(int i=0; i<m; i++)
		{
			for(int j=0; j<n; j++)
			{
				v = getValue(i, j);
				setValue(i, j, v/x);
			}
		}

	}
	
	public double determinant()
	{
		throw new UnsupportedOperationException();
	}
	
}

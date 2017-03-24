  package de.ovgu.dke.matrix;

import de.ovgu.dke.util.Copyable;

public interface Matrix extends Copyable<Matrix> 
{
	public Vector getRowVector(int row);
	
	public void setRowVector(Vector v);
	
	public Vector getColumnVector(int column);
	
	public void setColumnVector(Vector v);
	
	public double getValue(int row, int column);
	
	public void setValue(int row, int column, double value);
	
	public void plus(double x);
	
	public void plus(Matrix m);
	
	public void minus(double x);
	
	public void minus(Matrix m);
	
	public void times(double x);
	
	public Matrix times(Matrix m);
	
	public void divide(double x);
	
	public Matrix transpose();
	
	public Matrix inverse();
	
	public double determinant();
	
	public double[][] toArray();
	
	public int nRows();
	
	public int nColumns();
	
	public int[] size();
}

package de.ovgu.dke.matrix;

import java.util.Map;

public class SparseMatrix extends AbstractMatrix 
implements Matrix
{
	private Map<Integer, SparseVector> values;

	@Override
	public Vector getColumnVector(int column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector getRowVector(int row) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getValue(int row, int column) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Matrix inverse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int nColumns() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int nRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setColumnVector(Vector v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRowVector(Vector v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int row, int column, double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] size() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix times(Matrix m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[][] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix transpose() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix copy() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
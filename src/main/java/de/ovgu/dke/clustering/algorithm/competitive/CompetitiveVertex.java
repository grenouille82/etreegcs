package de.ovgu.dke.clustering.algorithm.competitive;

import de.ovgu.dke.graph.AbstractVertex;
import de.ovgu.dke.graph.Vertex;
import de.ovgu.dke.util.MathUtil;

public class CompetitiveVertex extends AbstractVertex
implements Vertex
{
	private double[] weightVector;
	
	private double error;
	
	private double relativeError;
	
	private double signalCounter;
	
	private double utility;
	
	public CompetitiveVertex(int id, double[] vector)
	{
		super(id);
		if(vector == null)
			throw new NullPointerException();
		weightVector = vector;
	}
	
	public CompetitiveVertex(CompetitiveVertex anotherVertex) 
	{
		super(anotherVertex);
		
		error		  = anotherVertex.error;
		signalCounter = anotherVertex.signalCounter;
		
		int n = anotherVertex.weightVector.length;
		weightVector  = new double[n];
		System.arraycopy(anotherVertex.weightVector, 0, weightVector, 0, n);
	}

	public double[] getWeightVector()
	{
		return weightVector;
	}
	
	public void setWeightVector(double[] vector)
	{
		if(vector == null)
			throw new NullPointerException();
		
		weightVector = vector;
	}
	
	public void adapt(double learnRate, double[] v)
	{
		if(v == null)
			throw new NullPointerException();
		if(learnRate < 0d || learnRate > 1d)
			throw new IllegalArgumentException("learnRate must be in the range of 0d and 1d");
		if(v.length != weightVector.length)
			throw new IllegalArgumentException("v must have the same dimension as the weightVector");
		
		double length =0d;
		for(int i=0; i<weightVector.length; i++)
		{
		//	length += MathUtil.square(weightVector[i]+learnRate*(v[i]-weightVector[i]));
		//	weightVector[i] += learnRate*(v[i]-weightVector[i]);
		
		length += MathUtil.square(weightVector[i]+learnRate*v[i]);
		weightVector[i] += learnRate*(v[i]);
			
		}
		
		length = Math.sqrt(length);
		for(int i=0; i<weightVector.length; i++)
			weightVector[i] /= length;
	}
	
	public double getError()
	{
		return error;
	}
	
	public void setError(double e)
	{
		if(Double.isNaN(e) || Double.isInfinite(e) || e<0d)
			throw new IllegalArgumentException("error must be a positive value");
		error = e;
	}
	
	public void addError(double e) 
	{
		if(Double.isNaN(e) || Double.isInfinite(e) || e<0d)
			throw new IllegalArgumentException("e must be a positive value");
		error += e;
	}
	
	public void decreaseError(double alpha)
	{
		if(alpha < 0d || alpha > 1d)
			throw new IllegalArgumentException("alpha must be in the range of 0d and 1d");
		error -= alpha*error;
	}
	
	public double getRelativeError()
	{
		return relativeError;
	}
	
	public void addRelativeError(double e)
	{
		if(Double.isNaN(e) || Double.isInfinite(e) || e<0d)
			throw new IllegalArgumentException("e must be a positive value " + e);
		relativeError += e;
	}
	
	public void setRelativeError(double e)
	{
		if(Double.isNaN(e) || Double.isInfinite(e) || e<0d)
			throw new IllegalArgumentException("error must be a positive value");
		relativeError = e;
	}
	
	public void decreaseRelativeError(double alpha)
	{
		if(alpha < 0d || alpha > 1d)
			throw new IllegalArgumentException("alpha must be in the range of 0d and 1d");
		relativeError -= alpha*relativeError;
	}
	
	public double getSignalCounter()
	{
		return signalCounter;
	}
	
	public void setSignalCounter(double sc)
	{
		if(Double.isNaN(sc) || Double.isInfinite(sc) || sc<0d)
			throw new IllegalArgumentException("signal counter must be a positive value");
		signalCounter = sc;
	}
	
	public void incrementSignalCounter()
	{
		signalCounter += 1d;
	}
	
	public void decreaseSignalCounter(double alpha)
	{
		if(alpha < 0d || alpha > 1d)
			throw new IllegalArgumentException("alpha must be in the range of 0d and 1d");
		signalCounter -= alpha*signalCounter;
	}
	
	public double getUtility()
	{
		return utility;
	}
	
	public void addUtility(double u)
	{
		if(Double.isNaN(u) || Double.isInfinite(u) || u<0d)
			throw new IllegalArgumentException("u must be a positive value");
		utility += u;
	}
	
	public void decreaseUtility(double alpha)
	{
		if(alpha < 0d || alpha > 1d)
			throw new IllegalArgumentException("alpha must be in the range of 0d and 1d");
		utility -= alpha*utility;
	}
	
	public void setUtility(double u)
	{
		if(u < 0d)
			throw new IllegalArgumentException();
		this.utility = u;
	}
	
	public CompetitiveVertex copy() 
	{
		return new CompetitiveVertex(this);
	}
	
	public static void main(String args[])
	{
		System.out.println(Math.acos(0.1)*180d/Math.PI);
		System.out.println(Math.acos(0.2)*180d/Math.PI);
	}
}

package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Iterator;

import de.ovgu.dke.util.DistanceMeasure;

public final class CompetitiveTreeNode
{
	/**
	 * 
	 * @param id
	 * @param graph
	 * @return
	 * Note: utility value error threshold are not initialized
	 */
	public static CompetitiveTreeNode createTreeNode(int id, CompetitiveGraph graph)
	{
		if(id < 0)
			throw new IllegalArgumentException("id must be a positive integer");
		if(graph == null)
			throw new NullPointerException();
		if(graph.isEmpty())
			throw new IllegalArgumentException("graph cannot be empty");
		
		double weightVector[] = null;
		double totalSC = 0d;
		double totalE  = 0d;
		
		Iterator<CompetitiveVertex> it = graph.vertices();
		while(it.hasNext()) 
		{
			CompetitiveVertex vertex = it.next();
			double[] vector = vertex.getWeightVector();
			double sc = vertex.getSignalCounter();
			if(weightVector == null)
				weightVector = new double[vector.length];
			
			totalSC += sc;
			totalE += vertex.getError();
			for(int i=0; i<weightVector.length; i++)
				weightVector[i] += sc*vector[i];
		}
		for(int i=0; i<weightVector.length; i++)
			weightVector[i] /= totalSC;
		
		CompetitiveTreeNode node = new CompetitiveTreeNode(id, weightVector);
		node.setError(totalE);
		node.setSignalCounter(totalSC);
		node.setGraph(graph);
		return node;
	}
	
	public static CompetitiveTreeNode createTreeNode(int id, CompetitiveGraph graph, DistanceMeasure metric)
	{
		if(id < 0)
			throw new IllegalArgumentException("id must be a positive integer");
		if(graph == null)
			throw new NullPointerException();
		if(graph.isEmpty())
			throw new IllegalArgumentException("graph cannot be empty");
		
		double weightVector[] = null;
		double totalSC = 0d;
		double totalE  = 0d;
		
		Iterator<CompetitiveVertex> it = graph.vertices();
		while(it.hasNext()) 
		{
			CompetitiveVertex vertex = it.next();
			double[] vector = vertex.getWeightVector();
			double sc = vertex.getSignalCounter();
			if(weightVector == null)
				weightVector = new double[vector.length];
			
			totalSC += sc;
			totalE += vertex.getError();
			for(int i=0; i<weightVector.length; i++)
				weightVector[i] += sc*vector[i];
		}
		for(int i=0; i<weightVector.length; i++)
			weightVector[i] /= totalSC;
		
		double estError = 0d;
		it = graph.vertices();
		while(it.hasNext())
		{
			CompetitiveVertex vertex = it.next();
			double dist = metric.getDistance(weightVector, vertex.getWeightVector());
			estError += dist*vertex.getSignalCounter();
		}
		System.out.println("cum err " + totalE + " est err" + estError);
		CompetitiveTreeNode node = new CompetitiveTreeNode(id, weightVector);
		node.setError(estError);
		node.setSignalCounter(totalSC);
		node.setGraph(graph);
		return node;
	}
	
	private final int id;
	
	private double errorThreshold;
	
	private double movingAvgError;
	
	private double[] weightVector;
	
	private double signalCounter;
	
	private double error;
	
	private double utility;
	
	private CompetitiveGraph graph;
	
	public CompetitiveTreeNode(int id, double[] v)
	{
		if(id < 0)
			throw new IllegalArgumentException("id must be a positive integer");
		if(v == null)
			throw new NullPointerException();
		
		this.id = id;
		weightVector = v;
	}
	
	public int getId()
	{
		return id;
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
	
	public double getError()
	{
		return error;
	}
	
	public void setError(double e)
	{
		//if(Double.isNaN(e) || Double.isInfinite(e) || e<0d)
			//throw new IllegalArgumentException("error must be a positive value");
		error = e;
	}
	
	public void addError(double e) 
	{
		if(Double.isNaN(e) || Double.isInfinite(e) || e<0d)
			throw new IllegalArgumentException("e must be a positive value: " + e);
		error += e;
	}
	
	public void decreaseError(double alpha)
	{
		if(alpha < 0d || alpha > 1d)
			throw new IllegalArgumentException("alpha must be in the range of 0d and 1d");
		error -= alpha*error;
	}
	
	public double getSignalCounter()
	{
		return signalCounter;
	}
	
	public void setSignalCounter(double sc)
	{
		if(Double.isNaN(sc) || Double.isInfinite(sc) || sc<0d)
			throw new IllegalArgumentException("signal counter must be a positive value:" + sc);
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
	
	public void adapt(double learnRate, double[] v)
	{
		if(v == null)
			throw new NullPointerException();
		if(learnRate < 0d || learnRate > 1d)
			throw new IllegalArgumentException("learnRate must be in the range of 0d and 1d");
		if(v.length != weightVector.length)
			throw new IllegalArgumentException("v must have the same dimension as the weightVector");
		
		for(int i=0; i<weightVector.length; i++)
			weightVector[i] += learnRate*(v[i]-weightVector[i]);
			//weightVector[i] += learnRate*(v[i]);
	}
	
	public void setGraph(CompetitiveGraph graph)
	{
		this.graph = graph;
	}
	
	public CompetitiveGraph getGraph()
	{
		return graph;
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
	
	public double getErrorThreshold()
	{
		return errorThreshold;
	}
	
	public void setErrorThreshold(double t)
	{
		if(Double.isNaN(t) || Double.isInfinite(t) || t<0d)
			throw new IllegalArgumentException();
		errorThreshold = t;
	}
	
	public void adaptErrorThreshold(double learnRate, double e)
	{
		if(learnRate < 0d || learnRate > 1d)
			throw new IllegalArgumentException("learnRate must be in the range of 0d and 1d");
		errorThreshold += learnRate*(e-errorThreshold);
	}
	
	public void increaseErrorThreshold(double epsilon)
	{
		if(epsilon < 0d || epsilon > 1d)
			throw new IllegalArgumentException("epsilon must be in the range of 0d and 1d");
		errorThreshold += epsilon*errorThreshold;
	}
	
	public double getMovingAvgError()
	{
		return movingAvgError;
	}
	
	public void setMovingAvgError(double e)
	{
		if(Double.isNaN(e) || Double.isInfinite(e) || e<0d)
			throw new IllegalArgumentException();
		movingAvgError = e;
	}
	
	public void adaptMovingAvgError(double learnRate, double e)
	{
		if(learnRate < 0d || learnRate > 1d)
			throw new IllegalArgumentException("learnRate must be in the range of 0d and 1d");
		movingAvgError += learnRate*(e-movingAvgError);
	}
	
	
	public boolean equals(Object anotherObject)
	{
		if(anotherObject instanceof CompetitiveTreeNode) {
			return equals((CompetitiveTreeNode) anotherObject);
		}
		return false;
	}
	
	public boolean equals(CompetitiveTreeNode anotherNode)
	{
		if(this == anotherNode)
			return true;
		if(anotherNode != null) {
			return id == anotherNode.getId();
		}
		return false;
	}
	
	public int hashCode()
	{
		return id;
	}
	
	public String toString()
	{
		return "Node_"+id;
	}
	
}

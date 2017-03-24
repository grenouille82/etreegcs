package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.ovgu.dke.clustering.util.ProximityMatrix;
import de.ovgu.dke.util.DistanceMeasure;

public final class CompetitiveUtil
{
	private CompetitiveUtil() {};
	
	public static double[] interpolate(double[] x, double[] y)
	{
		if(x == null || y == null)
			throw new NullPointerException();
		if(x.length != y.length)
			throw new IllegalArgumentException("dimension must be the same for x and y");
		
		int dim = x.length;
		double z[] = new double[dim];
		for(int i=0; i<dim; i++)
			z[i] = (x[i]+y[i])/2d;
		return z;
	}
	
	public static double[] interpolate(double[] x, double[] y, double weight)
	{
		if(x == null || y == null)
			throw new NullPointerException();
		if(x.length != y.length)
			throw new IllegalArgumentException("dimension must be the same for x and y");
		
		int dim = x.length;
		double z[] = new double[dim];
		for(int i=0; i<dim; i++)
			z[i] = (weight*x[i])+((1d-weight)*y[i]);
		return z;
	}
	
	public static ArrayIndexedMST<CompetitiveTreeNode> computeMinimumSpanningTree(CompetitiveTreeNode[] elements, DistanceMeasure metric)
	{
		if(elements == null)
			throw new NullPointerException();
		
		int n = elements.length;
	
		double[] minDistances = new double[n];
		Arrays.fill(minDistances, Double.POSITIVE_INFINITY);
		int[] fringe = new int[n];
		ProximityMatrix proxMatrix = computeProximityMatrix(elements, metric);
		//System.out.println(proxMatrix.toString());
		ArrayIndexedMST<CompetitiveTreeNode> mst = new ArrayIndexedMST<CompetitiveTreeNode>(elements);
		for(int v=0, min=-1; min!=0; v=min)
		{
			min = 0;
			for(int w=1; w<n; w++)
			{
				if(mst.parentElementIndex(w) == -1) {
					double dist = proxMatrix.getValue(v, w);
					if(dist<minDistances[w]) {
						minDistances[w] = dist;
						fringe[w] = v;
					}
					if(minDistances[w]<minDistances[min])
						min = w;
				}
			}
			if(min != 0)
				mst.setParentLink(min, fringe[min], minDistances[min]);
		}
		
		return mst;
	}
	
	public static ProximityMatrix computeProximityMatrix(CompetitiveTreeNode[] elements, DistanceMeasure metric)
	{
		if(elements == null)
			throw new NullPointerException();
		
		int n = elements.length;
		ProximityMatrix matrix = new ProximityMatrix(n);
		for(int i=0; i<n; i++) 
		{
			for(int j=i+1; j<n; j++)
			{
				double dist = metric.getDistance(elements[i].getWeightVector(), elements[j].getWeightVector());
				matrix.setValue(i, j, dist);
			}
		}
		return matrix;
	}
	
	public static void printCompetitiveTree(CompetitiveTree tree)
	{
		printCompetitiveTreeRec(tree.getRoot(), tree, 0);
	}
	
	public static void main(String args[])
	{
		CompetitiveTreeNode[] elements = createRandomData(6, 2);
		
		long time = System.currentTimeMillis();
		ArrayIndexedMST<CompetitiveTreeNode> mst = computeMinimumSpanningTree(elements, DistanceMeasure.EUCLIDEAN);
		System.out.println(System.currentTimeMillis()-time);
		System.out.println(mst);
		int cutIdx = mst.maxDistancedElementIndex();
		System.out.println("cutIdx" + cutIdx);
		List<ArrayIndexedMST<CompetitiveTreeNode>> cutSet = mst.cutAt(cutIdx);
		for(ArrayIndexedMST<CompetitiveTreeNode> cutMst : cutSet)
			System.out.println(cutMst);
		
		time = System.currentTimeMillis();
		computeProximityMatrix(elements, DistanceMeasure.EUCLIDEAN);
		System.out.println(System.currentTimeMillis()-time);
		System.out.println(mst.avgDistance()/mst.maxDistance());
		System.out.println(mst.minDistance()/mst.maxDistance());
	}
	
	private static void printCompetitiveTreeRec(CompetitiveTreeNode node, CompetitiveTree tree, int level)
	{
		for(int i=0; i<level; i++)
			System.out.print("| ");
		System.out.print("|-" + node + "\t[sc=" + node.getSignalCounter() + ";e=" + node.getError());
		System.out.println(";et=" + node.getErrorThreshold() +";avgE=" + node.getMovingAvgError() + ";u=" + node.getUtility() + "]" );//+ "\t" + Arrays.toString(node.getWeightVector()));
		if(tree.numberOfChildren(node) > 0) {
			for(CompetitiveTreeNode child : tree.childNodes(node))
				printCompetitiveTreeRec(child, tree, level+1);
		}
		
	}
	
	private static CompetitiveTreeNode[] createRandomData(int n, int dim)
	{
		Random rnd = new Random();
		CompetitiveTreeNode[] data = new CompetitiveTreeNode[n];
		
		for(int i=0; i<n; i++)
		{
			double v[] = new double[dim];
			for(int j=0; j<dim; j++)
				v[j] = rnd.nextDouble()*100d;
			data[i] = new CompetitiveTreeNode(i, v);
		}
		
		return data;
	}
}

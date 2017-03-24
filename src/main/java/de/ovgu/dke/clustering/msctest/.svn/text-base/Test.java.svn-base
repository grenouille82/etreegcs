package de.ovgu.dke.clustering.msctest;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import de.ovgu.dke.tree.LinkedTree;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.DoubleVector;
import de.ovgu.dke.util.MathUtil;

public class Test
{
	public static void main(String args[])
	{
		double c[] =  {4d };
		double d[] =  {8d};  
		double y[] =  {10d};
		
		double dis = DistanceMeasure.EUCLIDEAN.getDistance(c, d);
		double idis = incrDistEucl(dis, c, d, y, 0.5d, 0.5d, DistanceMeasure.EUCLIDEAN.getDistance(c, y), DistanceMeasure.EUCLIDEAN.getDistance(d, y));
		adaptEucl(c, y, 0.5d);
		adaptEucl(d, y, 0.5d);
		double ndis = DistanceMeasure.EUCLIDEAN.getDistance(c, d);
		System.out.println(dis + "\t" + idis + "\t" + ndis);
		
		
		Random rnd = new Random();
		for(int i=0; i<100; i++)
		{
			double a[] = createRandomVector(1000, rnd);
			double b[] = createRandomVector(1000, rnd);
			double x[] = createRandomVector(1000, rnd);
			double dist = DistanceMeasure.EUCLIDEAN.getDistance(a, b);
			double distAX = DistanceMeasure.EUCLIDEAN.getDistance(a,x);
			double distBX = DistanceMeasure.EUCLIDEAN.getDistance(b,x);
			double newIncrDist =0d;
			if(distAX < distBX)
				newIncrDist = incrDistEucl1(dist, a, b, x, 0.5, 0.005, distAX, distBX);
			else
				newIncrDist = incrDistEucl1(dist, b, a, x, 0.5, 0.005, distBX, distAX);
			double testDist = testDistEucl(dist, a, b, x, 0.5, 0.005);
				//double testDist = incrDistCos(dist, a, b, x, 0.5, 0.005);
			//System.out.println(Arrays.toString(a) + "\t"+ Arrays.toString(b) + "\t" + Arrays.toString(x));
			
			
			double cosDist = DistanceMeasure.COSINE.getDistance(a, b);
			normalize(a);
			normalize(b);
			normalize(x);
			dist = DistanceMeasure.EUCLIDEAN.getDistance(a, b);
			System.out.println(dist + "\t" + (0.5*dist*dist) + "\t"  + Math.sqrt(2*cosDist) + "\t"+ DoubleVector.cosine(a, b) + "\t" +"\t"+DistanceMeasure.COSINE.getDistance(a, b)+"\t"+DistanceMeasure.INNER_PRODUCT.getDistance(a, b));
			
			distAX = DistanceMeasure.COSINE.getDistance(a,x);
			distAX = Math.sqrt(2*distAX);
			double deltaAX = distAX-distAX*0.5;
			
			if(distAX < distBX) {
			adaptCos(a, x, 0.5);
			adaptCos(b, x, 0.005);
			deltaAX = distAX-distAX*0.5;
			} else {
				adaptCos(a, x, 0.005);
				adaptCos(b, x, 0.5);
				deltaAX = distAX-distAX*0.005;
			}
			
			deltaAX = 0.5*deltaAX*deltaAX;
			
			//normalize(a);
			//	normalize(b);
			//normalize(x);
			
			System.out.println("delta " + deltaAX + " ax " + DistanceMeasure.COSINE.getDistance(a,x)+ " dist " + distAX);

			//System.out.println(Arrays.toString(a) + "\t"+ Arrays.toString(b) + "\t" + Arrays.toString(x));
			double newDist = DistanceMeasure.EUCLIDEAN.getDistance(a, b);
			System.out.println(dist + "\t" + newIncrDist + "\t" + newDist + "\t"+DistanceMeasure.COSINE.getDistance(a,x) + "\t" + testDist);

		}
		
		
		LinkedTree<String> tree = ReferenceLabelHierarchies.getBanksearchHierarchy();
		printTree(tree);
		
		System.out.println(tree.getPath("Java", "C"));
		System.out.println(tree.getPath("Sport", "C"));
		System.out.println(tree.getPath("root", "C"));
		System.out.println(tree.getPath("C", "root"));
		System.out.println(tree.getPath("C", "Programming"));
		System.out.println(tree.getPath("Programming", "Java"));
		System.out.println(tree.getPath("Programming", "Banking"));
		Collection<String> children = new LinkedList<String>();
		//children.add("Java");
		children.add("C");
		//children.add("Visual Basic");
		tree.insertElementBetween("Programming", children, "X");
		System.out.println();
		printTree(tree);
		System.out.println(tree.getPath("Soccer", "C"));
		System.out.println(tree.getPath("C", "Soccer"));
		for(String label : tree)
			System.out.println(label);
		
		
	}
	
	public static double seperateDist(double a[], double b[])
	{
		double sumA = 0d;
		double sumB = 0d;
		for(int i=0; i<a.length; i++)
		{
			sumA += a[i];
			sumB += b[i];
		}
		return Math.abs(sumA-sumB);
		
	}
	
	public static void adaptEucl(double[] v, double[] x, double learnRate)
	{
		for(int i=0; i<v.length; i++)
			v[i] += learnRate*(x[i]-v[i]);
	}
	
	public static void adaptCos(double[] v, double[] x, double learnRate)
	{
		for(int i=0; i<v.length; i++)
			v[i] += learnRate*x[i];
	}
	
	public static void normalize(double[] v)
	{
		double length = 0d;
		for(int i=0; i<v.length; i++)
			length += v[i]*v[i];
		length = Math.sqrt(length);
		for(int i=0; i<v.length; i++)
			v[i] /= length;
	}
	

	public static double adaptDeltaEucl(double[] v, double[] x, double learnRate)
	{
		double delta = 0d;
		for(int i=0; i<v.length; i++)
			delta += learnRate*(x[i]-v[i]);
		System.out.println(delta);
		return delta;
	}
	
	public static double adaptDeltaEuclSquare(double[] v, double[] x, double learnRate)
	{
		double delta = 0d;
		for(int i=0; i<v.length; i++)
			delta += (learnRate*MathUtil.square(x[i]-v[i]));
		System.out.println(delta);
		return Math.sqrt(delta);
	}
	
	public static double adaptDeltaCos(double[] v, double[] x, double learnRate)
	{
		double delta = 0d;
		for(int i=0; i<v.length; i++)
			delta += learnRate*x[i];
		return delta;
	}
	
	public static double incrDistEucl(double dist, double[] a, double[] b, double[] x, double beta1, double beta2,  double distAX, double distBX)
	{
		System.out.println("dist1 " +  (dist - (adaptDeltaEucl(a, x, beta1) - adaptDeltaEucl(b, x, beta2))));
		System.out.println("dist2 " +  (dist + (adaptDeltaEucl(a, x, beta1) - adaptDeltaEucl(b, x, beta2))));
		if(dist > distAX && dist > distBX)
		{
			System.out.println("fuck");
			return dist - adaptDeltaEucl(a, x, beta1) + adaptDeltaEucl(b, x, beta2);
			
		}
		else
			return (dist + (adaptDeltaEucl(a, x, beta1) - adaptDeltaEucl(b, x, beta2)));
	}
	
	public static double incrDistEucl1(double dist, double[] a, double[] b, double[] x, double beta1, double beta2,  double distAX, double distBX)
	{
		double deltaDistAX = distAX-distAX*beta1;
		double deltaDistBX = distBX-distBX*beta2;
		double cos = (MathUtil.square(distAX)+MathUtil.square(distBX)-MathUtil.square(dist))/ (2d*distAX*distBX);
		//System.out.println("cos " + cos);
		double newDist = Math.sqrt(MathUtil.square(deltaDistAX)+MathUtil.square(deltaDistBX)-(2d*deltaDistAX*deltaDistBX*cos));
		return newDist;
	}
	
	public static double testDistEucl(double dist, double[] a, double[] b, double[] x, double beta1, double beta2)
	{
		double retVal = 0d;
		for(int i=0; i<a.length; i++)
		{
			retVal += Math.sqrt(MathUtil.square((a[i]+beta1*(x[i]-a[i]))-b[i]));
		}
		return retVal;
	}
	
	public static double incrDistCos(double dist, double[] a, double[] b, double[] x, double beta1, double beta2)
	{
		double retVal = dist + adaptDeltaCos(a, x, beta1) - adaptDeltaCos(b, x, beta2);
		return retVal;
	}
	
	public static double[] createRandomVector(int n, Random rnd)
	{
		double[] v = new double[n];
		for(int i=0; i<n; i++)
			v[i] = rnd.nextDouble()*1000;
		return v;
	}
	
	private static void printTree(LinkedTree<String> tree)
	{
		printRec(tree.getRoot(), tree, 0);
	}
	
	private static void printRec(String label, LinkedTree<String> tree, int level)
	{
		for(int i=0; i<level; i++)
			System.out.print("| ");
		
		System.out.println("|-"+label);
		if(!tree.isLeaf(label)) {
			for(String child : tree.childElements(label))
				printRec(child, tree, level+1);
		}
	}
}

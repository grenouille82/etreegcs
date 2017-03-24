package de.ovgu.dke.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.*;

/**
 * 
 * @author mhermkes
 *
 */
public final class DescriptiveStatistic 
{	
	
	/**
	 * 
	 *
	 */
	private DescriptiveStatistic() {}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double minimum(double[] a)
	{
		if(a.length == 0)
			return Double.NaN;
		int middle	= a.length >> 1;
		double xmin = a[0];
		
		double min = 0.0d;
		for(int i=1; i<=middle; i++)
		{
			min = a[i] < a[a.length-i] ? a[i] : a[a.length-i]; 
			if(min < xmin)
				xmin = min;
		}
		return xmin;
	}
	
	public static <T extends Comparable<T>> T minimum(List<T> a)
	{
		if(a.size() == 0)
			return null;
		int size	= a.size();
		int middle	= size >> 1;
		T xmin = a.get(0);
		int minIndex = 0;
		
		T x1 = null;
		T x2 = null;
		for(int i=1; i<=middle; i++)
		{
			x1 = a.get(i);
			x2 = a.get(size-i);
			if(x1.compareTo(x2) < 0) {
				if(x1.compareTo(xmin) < 0) {
					xmin = x1;
					minIndex = i;
				}
			} else {
				if(x2.compareTo(xmin) < 0) {
					xmin = x2;
					minIndex = size-i;
				}
			}
		}
		return a.get(minIndex);
	}
	
	public static double maximum(double[] a)
	{
		if(a.length == 0)
			return Double.NaN;
		int middle	= a.length >> 1;
		double xmax = a[0];
		
		double max = 0.0d;
		for(int i=1; i<=middle; i++)
		{
			max = a[i] > a[a.length-i] ? a[i] : a[a.length-i]; 
			if(max > xmax)
				xmax = max;
		}
		return xmax;
	}
	
	public static <T extends Comparable<T>> T maximum(List<T> a)
	{
		if(a.size() == 0)
			return null;
		int size	= a.size();
		int middle	= size >> 1;
		T xmax = a.get(0);
		int maxIndex = 0;
		
		T x1 = null;
		T x2 = null;
		for(int i=1; i<=middle; i++)
		{
			x1 = a.get(i);
			x2 = a.get(size-i);
			if(x1.compareTo(x2) > 0) {
				if(x1.compareTo(xmax) > 0) {
					xmax = x1;
					maxIndex = i;
				}
			} else {
				if(x2.compareTo(xmax) > 0) {
					xmax = x2;
					maxIndex = size-i;
				}
			}
		}
		return a.get(maxIndex);
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double mean(double[] a)
	{
		if(a.length == 0)
			return Double.NaN;
		double sum = 0.0d;
		for(double x : a) 
			sum += x;
		return sum/a.length;
	}

	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Number> Double mean(List<T> a)
	{
		if(a.size() == 0)
			return Double.valueOf(Double.NaN);
		double sum = 0.0d;
		for(T element : a)
			sum += element.doubleValue();
		return Double.valueOf(sum/a.size());
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double median(double[] a)
	{
		if(a.length == 0)
			return Double.NaN;
		double med = 0.0d;
		Arrays.sort(a);
		if((a.length%2) == 0)
			med = (a[a.length>>1]+a[(a.length>>1)-1]) / 2;
		else
			med = a[a.length>>1];
		return med;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Number & Comparable<T>> Double median(List<T> a)
	{
		if(a.size() == 0)
			return Double.valueOf(Double.NaN);
		double med 	= 0.0d;
		int size 	= a.size();
		Collections.sort(a);
		if((size%2) == 0)
			med = (a.get(size>>1).doubleValue()+a.get((size>>1)-1).doubleValue()) / 2;
		else 
			med = a.get(size>>1).doubleValue();
		return Double.valueOf(med);
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double mode(double[] a)
	{
		if(a.length == 0)
			return Double.NaN;
		Arrays.sort(a);
		double mode = a[0];
		double preX = a[0];
		int absFreq	= 0;
		int currAbsFreq = 0;
			
		for(double x : a)
		{
			if(preX == x)
				currAbsFreq++;
			else {
				if(currAbsFreq > absFreq) {
					mode = preX;
					absFreq = currAbsFreq;
					currAbsFreq = 1;
				}
			}
			preX = x;
		}
		if(currAbsFreq > absFreq)
			mode = preX;
		
		return mode;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Comparable<T>> T mode(List<T> a)
	{
		if(a.size() == 0)
			return null;
		Collections.sort(a);
		T mode = null;
		T preElement = a.get(0);
		int absFreq	= 0;
		int currAbsFreq = 0;

		
		for(T element : a)
		{
			if(preElement.equals(element))
				currAbsFreq++;
			else {
				if(currAbsFreq > absFreq) {
					mode = preElement;
					absFreq = currAbsFreq;
					currAbsFreq = 1;
				}
			}
			preElement = element;
		}
		if(currAbsFreq > absFreq)
			mode = preElement;

		
		return mode;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double variance(double[] a)
	{
		return variance(a, mean(a));
	}
	
	/**
	 * 
	 * @param a
	 * @param mean
	 * @return
	 */
	public static double variance(double[] a, double mean)
	{
		if(a.length == 0 || Double.isNaN(mean))
			return Double.NaN;
		double dev = 0.0d;
		for(double x : a)
			dev += pow(x-mean, 2.0d);
		return dev/a.length;
	}

	/** Computes the variance with the mean of the values and the mean of the squared values.
	 * 
	 * @param mean The mean of the values.
	 * @param meanSquares The mean of the squared values.
	 * @return The variance.
	 */
	public static double variance(double mean, double meanSquares) {
		return (meanSquares - mean*mean);
	}

	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Number> Double variance(List<T> a)
	{
		return variance(a, mean(a));
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @param mean
	 * @return
	 */
	public static <T extends Number> Double variance(List<T> a, Double mean)
	{
		if(a.size() == 0 || mean.isNaN())
			return Double.valueOf(Double.NaN);
		double dev 		= 0.0d;
		double primMean	= mean.doubleValue();
		for(T element : a)
			dev += pow(element.doubleValue()-primMean, 2.0d);
		return Double.valueOf(dev/a.size());
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double stdDeviation(double[] a)
	{
		return stdDeviation(variance(a));
	}
	
	/**
	 * 
	 * @param stdDev
	 * @return
	 */
	public static double stdDeviation(double var)
	{
		return sqrt(var);
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double absDeviation(double[] a)
	{
		return absDeviation(a, mean(a));
	}

	/**
	 * 
	 * @param a
	 * @param mean
	 * @return
	 */
	public static double absDeviation(double[] a, double mean)
	{
		if(a.length == 0 || Double.isNaN(mean))
			return Double.NaN;
		double dev = 0.0d;
		for(double x : a)
			dev += abs(x-mean);
		return dev/a.length;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Number> Double absDeviation(List<T> a)
	{
		return absDeviation(a, mean(a));
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @param mean
	 * @return
	 */
	public static <T extends Number> Double absDeviation(List<T> a, Double mean)
	{
		if(a.size() == 0 || mean.isNaN())
			return Double.valueOf(Double.NaN);
		double dev 		= 0.0d;
		double primMean	= mean.doubleValue();
		for(T element : a)
			dev += abs(element.doubleValue()-primMean);
		return Double.valueOf(dev/a.size());
	}

	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Number> Double stdDeviation(List<T> a)
	{
		return stdDeviation(variance(a));
	}

	/**
	 * 
	 * @param <T>
	 * @param stdDev
	 * @return
	 */
	public static <T extends Number> Double stdDeviation(T var)
	{
		return Double.valueOf(sqrt(var.doubleValue()));
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double range(double[] a)
	{
		if(a.length == 0)
			return 0.0d;
		int middle	= a.length>>1;
		double xmin = a[0];
		double xmax = a[a.length-1];
		
		double min = 0.0d;
		double max = 0.0d;
		for(int i=1; i<=middle; i++)
		{
			min = a[i] < a[a.length-1-i] ? a[i] : a[a.length-1-i]; 
			max = a[i] > a[a.length-1-i] ? a[i] : a[a.length-1-i];
			if(min < xmin)
				xmin = min;
			if(max > xmax)
				xmax = max;
		}
		return xmax-xmin;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Number & Comparable<T>> Double range(List<T> a)
	{
		if(a.size() == 0)
			return Double.valueOf(0.0d);
		int size 	= a.size();
		int middle	= size>>1;
		double xmin = a.get(0).doubleValue();
		double xmax = a.get(size-1).doubleValue();
		
		double a1 = 0.0d;
		double a2 = 0.0d;
		for(int i=1; i<=middle; i++)
		{
			a1 = a.get(i).doubleValue();
			a2 = a.get(size-1-i).doubleValue();
			xmin = (a1 < a2) ? (a1 < xmin ? a1 : xmin) : (a2 < xmin ? a2 : xmin); 
			xmax = (a1 > a2) ? (a1 > xmax ? a1 : xmax) : (a2 > xmax ? a2 : xmax);
		}
		return Double.valueOf(xmax-xmin);
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double variationCoefficient(double[] a)
	{
		double mean 	= mean(a);
		double stdDev	= stdDeviation(variance(a, mean));
		return stdDev/mean;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Number> Double variationCoefficient(List<T> a)
	{
		double mean 	= mean(a).doubleValue();
		double stdDev	= stdDeviation(variance(a, Double.valueOf(mean)));
		return Double.valueOf(stdDev/mean);
	}
	
	/**
	 * 
	 * @param a
	 * @param factor
	 * @return
	 */
	public static double quartile(double a[], double factor)
	{
		if(a.length == 0 || (factor < 0.0d || factor > 1.0d))
			return Double.NaN;
		Arrays.sort(a);
		int np = (int) (a.length*factor);
		return a[np];
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @param factor
	 * @return
	 */
	public static <T extends Comparable<T>> T quartile(List<T> a, Double factor)
	{
		if(a.size() == 0 || (factor.doubleValue() < 0.0d || factor.doubleValue() > 1.0d))
			return null;
		Collections.sort(a);
		int np = (int) (a.size()*factor.doubleValue());
		if(np == a.size())
			np -= 1;
		return a.get(np);
	}

	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double lowQuartile(double a[])
	{
		return quartile(a, 0.25d);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Comparable<T>> T lowQuartile(List<T> a)
	{
		return quartile(a, 0.25d);
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double highQuartile(double a[])
	{
		return quartile(a, 0.75d);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Comparable<T>> T highQuartile(List<T> a)
	{
		return quartile(a, 0.75d);
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double interquartileRange(double a[])
	{
		if(a.length == 0)
			return 0.0d;
		double xl = lowQuartile(a);
		double xh = highQuartile(a);
		return xh-xl;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Number & Comparable<T>> Double interquartileRange(List<T> a)
	{
		if(a.size() == 0)
			return Double.valueOf(0.0d);
		double xl = lowQuartile(a).doubleValue();
		double xh = highQuartile(a).doubleValue();
		return Double.valueOf(xh-xl);
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double quantileCoefficient(double[] a)
	{
		if(a.length == 0)
			return Double.NaN;
		double med 	= median(a);
		double xl	= lowQuartile(a);
		double xh	= highQuartile(a);
		return ((xh-med)-(med-xl))/(xh-xl);
	}

	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Number & Comparable<T>> Double quantileCoefficient(List<T> a)
	{
		if(a.size() == 0)
			return Double.NaN;
		double med	= median(a).doubleValue();
		double xl 	= lowQuartile(a).doubleValue();
		double xh 	= highQuartile(a).doubleValue();
		return Double.valueOf(((xh-med)-(med-xl))/(xh-xl));
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double momentsCoefficient(double [] a)
	{
		if(a.length == 0)
			return Double.NaN;
		double mean = mean(a);
		double m = 0.0d;
		for(double x : a)
			m += pow(x-mean, 3.0d);
		m /= a.length;
		return m;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Number> Double momentsCoefficient(List<T> a)
	{
		if(a.size() == 0)
			return Double.NaN;
		double mean = mean(a).doubleValue();
		double m = 0.0d;
		for(T element : a)
			m += pow(element.doubleValue()-mean, 3.0d);
		m /= a.size();
		return Double.valueOf(m);
	}
	
	/**
	 * 
	 * @param x
	 * @param mean
	 * @param stdDev
	 * @return
	 */
	public static double normalTightFunction(double x, double mean, double stdDev)
	{
		double exp = pow((x-mean)/stdDev, 2.0d) / -2;
		double tight = exp(exp) / (sqrt(2*PI)*stdDev);
		return tight;
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public static double[] normalTightFunction(double[] a)
	{
		double[] retArray = new double[a.length];
		double mean = mean(a);
		double stdDev = stdDeviation(a);
		for(int i=0; i<a.length; i++)
			retArray[i] = normalTightFunction(a[i], mean, stdDev);
		return retArray;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param x
	 * @param mean
	 * @param stdDev
	 * @return
	 */
	public static <T extends Number> Double normalTightFunction(T x, Double mean, Double stdDev)
	{
		double tight = normalTightFunction(x.doubleValue(), 
						mean.doubleValue(), stdDev.doubleValue());
		return Double.valueOf(tight);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @return
	 */
	public static <T extends Number> List<Double> normalTightFunction(List<T> a)
	{
		List<Double> retList = new LinkedList<Double>();
		Double mean	= mean(a);
		Double stdDev = stdDeviation(a);
		for(T element : a)
			retList.add(normalTightFunction(element, mean, stdDev));
		return retList;
	}
	
	public static void main(String args[])
	{
		double[] array1 = new double[]{ 45, 23, 55, 32, 51, 91, 74, 53, 70, 84};
		double[] array2 = new double[]{ 864, 675, 795, 756, 644, 630, 606, 707, 877, 715 };
		double[] arrayF = new double[]{ 45, 23, 55, 32, 51, 91, 74, 53, 70, 84, 864, 675, 795, 756, 644, 630, 606, 707, 877, 715};
		
		System.out.println("array1: mean=" + mean(array1) + ",var="+ variance(array1) + ",err=" + (variance(array1)*array1.length));
		System.out.println("array2: mean=" + mean(array2) + ",var="+ variance(array2) + ",err=" + (variance(array2)*array2.length));
		System.out.println("arrayF: mean=" + mean(arrayF) + ",var="+ variance(arrayF) + ",err=" + (variance(arrayF)*arrayF.length));
		
	}
}

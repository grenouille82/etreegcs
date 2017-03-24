package de.ovgu.dke.util;

import static java.lang.Math.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class MathUtil 
{
	private MathUtil() {}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static int square(int x)
	{
		return x*x;
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static long square(long x)
	{
		return x*x;
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static float square(float x)
	{
		return x*x;
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static double square(double x)
	{
		return x*x;
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static double log2(double x)
	{
		return log(x)/log(2d);
	}
	
	/**
	 * 
	 * @param p
	 * @return
	 */
	public static double binaryEntropy(double p)
	{
		if(p < 0d || p > 1d)
			return Double.NaN;
		
		return (p==0d || p==1d) ? 0d : p*log2(p) + (1d-p)*log2(1d-p);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 * 
	 * @throws NullPointerException
	 */
	public static <T> Set<T> union(Set<T> a, Set<T> b)
	{
		Set<T> minorSet = a;
		Set<T> majorSet = b;
		if(a.size() > b.size()) {
			minorSet = b;
			majorSet = a;
		}
		
		Set<T> retVal = new HashSet<T>(majorSet);
		retVal.addAll(minorSet);
		return retVal;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> Set<T> union(Collection<T> a, Collection<T> b)
	{
		Collection<T> minorList = a;
		Collection<T> majorList = b;
		if(a.size() > b.size()) {
			majorList = a;
			minorList = b;
		}
		
		Set<T> retVal = new HashSet<T>(majorList);
		retVal.addAll(minorList);
		return retVal;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param c
	 * @return
	 * 
	 * @throws NullPointerException
	 */
	public static <T> Set<T> union(Collection<? extends Set<T>> c)
	{
		Set<T> retVal = new HashSet<T>();
		for(Set<T> set : c)
			retVal.addAll(set);
		return retVal;
	}
	

	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 * 
	 * @throws NullPointerException
	 */
	public static <T> Set<T> intersection(Set<T> a, Set<T> b)
	{
		Set<T> minorSet = a;
		Set<T> majorSet = b;
		if(a.size() > b.size()) {
			minorSet = b;
			majorSet = a;
		}
		
		Set<T> retVal = new HashSet<T>(minorSet.size());
		for(T t : minorSet)
		{
			if(majorSet.contains(t))
				retVal.add(t);
		}
		return retVal;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> Set<T> intersection(Collection<T> a, Collection<T> b)
	{
		Collection<T> minorList = a;
		Collection<T> majorList = b;
		if(a.size() > b.size()) {
			majorList = a;
			minorList = b;
		}
		
		Set<T> retVal = new HashSet<T>(minorList.size());
		for(T t : majorList)
		{
			if(minorList.contains(t))
				retVal.add(t);
		}
		return retVal;
	}
	
	public static <T> Set<T> difference(Collection<T> a, Collection<T> b)
	{
		Set<T> retVal = new HashSet<T>(a);
		retVal.removeAll(b);
		return retVal;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param c
	 * @return
	 * 
	 *  @throws NullPointerException
	 */
	public static <T> Set<T> intersection(Collection<? extends Set<T>> c)
	{
		Set<T> minorSet = null;
		for(Set<T> set : c)
		{
			if(minorSet == null || minorSet.size() > set.size())
				minorSet = set;
		}

		Set<T> retVal = new HashSet<T>();
		for(T t : minorSet)
		{
			boolean contained = true;
			for(Set<T> set : c)
			{
				if(minorSet != set && !set.contains(t)) {
					contained = false;
					break;
				}
			}
			if(contained)
				retVal.add(t);
		}

		return retVal;
	}

	/**
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 * 
	 * @throws NullPointerException
	 */
	public static <T> int cardUnion(Set<T> a, Set<T> b)
	{
		Set<T> minorSet = a;
		Set<T> majorSet = b;
		if(a.size() > b.size()) {
			minorSet = b;
			majorSet = a;
		}
		
		int card = majorSet.size();
		for(T t : minorSet) 
		{
			if(!majorSet.contains(t))
				card++;
		}
		return card;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param c
	 * @return
	 * 
	 * @throws NullPointerException
	 */
	public static <T> int cardUnion(Collection<? extends Set<T>> c)
	{
		Set<T> unionSet = union(c);
		return unionSet.size();
	}
	
	/**
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 * 
	 * @throws NullPointerException
	 */
	public static <T> int cardIntersection(Set<T> a, Set<T> b)
	{
		Set<T> minorSet = a;
		Set<T> majorSet = b;
		if(a.size() > b.size()) {
			minorSet = b;
			majorSet = a;
		}
		
		int card = 0;
		for(T t : minorSet)
		{
			if(majorSet.contains(t))
				card++;
		}
		return card;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param c
	 * @return
	 * 
	 *  @throws NullPointerException
	 */
	public static <T> int cardIntersection(Collection<? extends Set<T>> c)
	{
		Set<T> minorSet = null;
		for(Set<T> set : c)
		{
			if(minorSet == null || minorSet.size() > set.size())
				minorSet = set;
		}

		int card = minorSet.size();
		for(T t : minorSet)
		{
			for(Set<T> set : c)
			{
				if(minorSet != set && !set.contains(t)) {
					card--;
					break;
				}
			}
		}

		return card;
	}

	/**
	 * 
	 * @param <T>
	 * @param <N>
	 * @param a
	 * @param b
	 * @return
	 * 
	 * @throws NullPointerException
	 */
	public static <T, N extends Number> double scalarProduct(Map<T, N> a, Map<T, N> b)
	{
		Map<T,N> minorVector = a;
		Map<T,N> majorVector = b;
		if(a.size() > b.size()) {
			minorVector = b;
			majorVector = a;
		}

		double scalar = 0d;
		for(Entry<T, N> e : minorVector.entrySet())
		{
			N value = majorVector.get(e.getKey());
			if(value != null) {
				scalar += value.doubleValue()*e.getValue().doubleValue();
			}
		}
		
		return scalar;
	}
	
	/**
	 * 
	 * @param <N>
	 * @param a
	 * @param p
	 * @return
	 * 
	 * @throws NullPointerException
	 */
	public static <N extends Number> double pNorm(Collection<N> a, double p)
	{
		if(p<1d)
			return Double.NaN;
		
		double result = 0d;
		if(p==1d) {
			for(N n : a)
				result += abs(n.doubleValue());
		} else {
			for(N n : a)
				result += pow(abs(n.doubleValue()), p);
			result = pow(result, 1d/p);
		}
		return result;
	}
	
	/**
	 * 
	 * @param <N>
	 * @param a
	 * @param p
	 * @return
	 * 
	 * @throws NullPointerException
	 */
	public static double pNorm(double[] a, double p)
	{
		if(p<1d)
			return Double.NaN;
		
		double result = 0d;
		if(p==1d) {
			for(double x : a)
				result += abs(x);
		} else {
			for(double x : a)
				result += pow(abs(x), p);
			result = pow(result, 1d/p);
		}
		return result;
	}

	
}

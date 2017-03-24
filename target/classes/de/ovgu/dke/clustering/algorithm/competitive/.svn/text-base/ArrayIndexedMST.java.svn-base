package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author mhermkes
 * TODO: - extract a MinimumSpanningTree Interface with only readable-access methods
 */
public final class ArrayIndexedMST<E>
{
	private E[] elements;
	
	private double[] distances;
	private int[] parentLinks;
	
	private int size;
	
	public ArrayIndexedMST(E[] elements)
	{
		if(elements == null)
			throw new NullPointerException();
		
		size = elements.length;
		
		this.elements = elements;
		distances	  = new double[size];
		parentLinks	  = new int[size];
		
		Arrays.fill(distances, Double.NaN);
		Arrays.fill(parentLinks, -1);
	}
	
	public E element(int idx)
	{
		rangeCheck(idx);
		return elements[idx];
	}
	
	public E parentElement(int idx)
	{
		rangeCheck(idx);
		int parentIdx = parentLinks[idx];
		return (parentIdx == -1) ? null : elements[parentIdx];
	}
	
	public E parentElement(Object element)
	{
		int idx = indexOf(element);
		return parentElement(idx);
	}
	
	public int parentElementIndex(int idx)
	{
		rangeCheck(idx);
		return parentLinks[idx];
	}
	
	public int parentElementIndex(Object element)
	{
		int idx = indexOf(element);
		return parentElementIndex(idx);
	}
	
	public void setParentLink(E element, E parent, double distance)
	{
		int eIdx = indexOf(element);
		int pIdx = indexOf(parent);
		setParentLink(eIdx, pIdx, distance);
	}
	
	public void setParentLink(int elementIdx, int parentIdx, double distance)
	{
		rangeCheck(elementIdx);
		rangeCheck(parentIdx);
		distances[elementIdx] 	= distance;
		parentLinks[elementIdx]	= parentIdx;
	}
	
	public double distance(int idx)
	{
		rangeCheck(idx);
		return distances[idx];
	}
	
	public double distance(Object element)
	{
		int idx = indexOf(element);
		return distance(idx);
	}
	
	public double minDistance()
	{
		int idx = minDistancedElementIndex();
		return distances[idx];
	}
	
	public E minDistancedElement()
	{
		int idx = minDistancedElementIndex();
		return elements[idx];
	}
	
	public int minDistancedElementIndex()
	{
		double minDist = Double.POSITIVE_INFINITY;
		int elementIdx = -1;
		for(int i=0; i<size; i++)
		{
			if(!Double.isNaN(distances[i]) && distances[i]<minDist) {
				elementIdx = i;
				minDist = distances[i];
			}
		}
		return elementIdx;
	}
	
	public double maxDistance()
	{
		int idx = maxDistancedElementIndex();
		return distances[idx];
	}
	
	public E maxDistancedElement()
	{
		int idx = maxDistancedElementIndex();
		return elements[idx];
	}
	
	public int maxDistancedElementIndex()
	{
		double maxDist = Double.NEGATIVE_INFINITY;
		int elementIdx = -1;
		for(int i=0; i<size; i++)
		{
			if(!Double.isNaN(distances[i]) && distances[i]>maxDist) {
				elementIdx = i;
				maxDist = distances[i];
			}
		}
		return elementIdx;
	}
	
	public List<ArrayIndexedMST<E>> cutAt(int index)
	{
	//	if(index == 0 || index == size)
	//		throw new IndexOutOfBoundsException("split index must be in the range of [1,size-1]");
	
		List<E> beforeElements = new LinkedList<E>();
		List<E> afterElements  = new LinkedList<E>();
		short visited[] = new short[size];
		for(int i=0; i<size; i++)
		{
			if(visited[i]==0) {
				if(i==index) {
					visited[i] = 1;
					int parent = parentLinks[i];
					while(parent!=-1 && visited[parent]!=0)
					{
						visited[parent] = -1;
						parent = parentLinks[parent];
					}
				} else {
					short p = traverseForCut(i, index, visited);
					visited[i] = p;
				}	
			}
		}
		for(int i=0; i<size; i++)
		{
			if(visited[i] == -1)
				beforeElements.add(elements[i]);
			else
				afterElements.add(elements[i]);
		}
		
		ArrayIndexedMST<E> beforeMST = new ArrayIndexedMST<E>(beforeElements.toArray((E[])new Object[0]));
		ArrayIndexedMST<E> afterMST	 = new ArrayIndexedMST<E>(afterElements.toArray((E[])new Object[0]));
		for(int i=0; i<size; i++) 
		{
			if(visited[i] == -1) {
				if(parentLinks[i]!=-1) {

					System.out.println("before " +  distances[i]);
					beforeMST.setParentLink(elements[i], elements[parentLinks[i]], distances[i]);
				}
			} else {
				if(i != index) {
					System.out.println("after " +  distances[i]);
					afterMST.setParentLink(elements[i], elements[parentLinks[i]], distances[i]);
				}
			}
		}
		List<ArrayIndexedMST<E>> retVal = new ArrayList<ArrayIndexedMST<E>>(2);
		retVal.add(beforeMST);
		retVal.add(afterMST);
		return retVal;
	}
	
	private final short traverseForCut(int i, int cutIdx, short[] visited)
	{
		int parent = parentLinks[i];
		if(parent == -1)
			return -1;
		else {
			if(visited[parent] != 0)
				return visited[parent];
			else {
				if(parent == cutIdx) {
					visited[parent] = 1;
					return 1;
				} else {
					short p = traverseForCut(parent, cutIdx, visited);
					visited[parent] = p;
					return p;
				}
			}
			
		}
	}
	
	public double avgDistance()
	{
		double retVal = 0;
		for(int i=0; i<size; i++) 
		{
			if(!Double.isNaN(distances[i]))
				retVal += distances[i];
		}
		return retVal/(size-1d);
	}
	
	public int indexOf(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		int retVal = -1;
		for(int i=0; i<size; i++)
		{
			if(elements[i].equals(element)) {
				retVal = i;
				break;
			}
		}
		return retVal;
	}
	
	public E[] elements() 
	{
		return elements;
	}
	
	public int size()
	{
		return size;
	}
	
	public void validate()
	{
		
	}
	
	private final void rangeCheck(int index)
	{
		
	}
	
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		s.append(Arrays.toString(parentLinks));
		s.append('\n');
		s.append(Arrays.toString(elements));
		s.append('\n');
		s.append(Arrays.toString(distances));
		return s.toString();
	}
	
}

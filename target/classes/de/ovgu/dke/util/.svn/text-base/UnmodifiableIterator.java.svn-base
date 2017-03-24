package de.ovgu.dke.util;

import java.util.Collection;
import java.util.Iterator;

public class UnmodifiableIterator<E> implements Iterator<E> 
{
	private Iterator<E> it;

	/**
	 * 
	 * @param c
	 * @throws NullPointerException
	 */
	public UnmodifiableIterator(Collection<E> c)
	{
		it = c.iterator();
	}
	
	public UnmodifiableIterator(Iterator<E> it)
	{
		if(it == null)
			throw new NullPointerException();
		this.it = it;
	}
	
	public boolean hasNext() 
	{
		return it.hasNext();
	}

	public E next() 
	{
		return it.next();
	}

	public void remove() 
	{
		throw new UnsupportedOperationException();
	}

}

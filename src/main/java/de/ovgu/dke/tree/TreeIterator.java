package de.ovgu.dke.tree;

import java.util.Iterator;

/**
 * The <tt>TreeIterator</tt> interface provides a systematic way of accessing
 * all elements of a tree. In notion of tree processing it is mentioned as tree 
 * traversal.<p> 
 * 
 * The tree iterator extends the <tt>Iterator</tt> interface about tree specific 
 * methods <tt>isLeaf</tt> and <tt>level</tt>. Furthermore the tree iterator is 
 * resetable, that means that you can restart the traversal process at the 
 * beginning.
 *
 * 
 * @author Marcel Hermkes
 *
 */
public interface TreeIterator<E> extends Iterator<E>
{	
	/**
	 * Returns <tt>true</tt> if the last returned element a tree node of the 
	 * underlying tree. This method can called only, after the first call of 
	 * <tt>next()</tt>.
	 * 
	 * @return <tt>true</tt> if the last returned element is a leaf, otherwise 
	 * 		   <tt>false</tt>.
	 * @throws IllegalStateException if the <tt>next</tt> method has not
     *		   yet been called
	 */
	public boolean isLeaf();
	
	/**
	 * Returns the level from the last returned element of the underlying tree.
	 * This method can called only, after the first call of <tt>next()</tt>.
	 *  
	 * @return the level from the last return element. 
	 * 		   by <tt>next</tt>.
	 * @throws IllegalStateException if the <tt>next</tt> method has not
     *		   yet been called
	 */
	public int level();
	
	/**
	 * Resets this iterator to starting state of the traversal process.
	 *
	 */
	public void reset();
}

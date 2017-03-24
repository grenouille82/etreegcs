package de.ovgu.dke.tree;


import java.util.NoSuchElementException;

/**
 * This class provides a skeletal implementation of the <tt>TreeIterator</tt> interface
 * to minimize the effort required to implement this interface. An implementation of this
 * class is required to implement only the 
 * {@link #buildTraverseOrder(de.unimd.irgroup.carsa.datatypes.tree.Tree.Node, int)} method.
 * <p>
 *
 * Todo: make a copy of the underlying tree. This makes a tree modification
 * and iteration against concurrent 
 *  
 * @author Marcel Hermkes
 *
 * @param <E>
 * @param <T>
 * @param <N>
 */
public abstract class AbstractTreeIterator<E, T extends Tree<E>, N extends Tree.Node<E>>
implements TreeIterator<E>
{
	/**
	 * The tree to be traversed.
	 */
	protected T tree;
	
	/**
	 * The start node of a traversed tree. 
	 */
	protected N startNode;
	
	/**
	 * The array of nodes which stores the traversing order. The length of
	 * the array can be greater than the number of traversed nodes.
	 */
	protected N[] nodes;
	
	/**
	 * An array of integer which saves the levels of the corresponding node.
	 */
	protected int[] levels;
	
	/**
	 * Index of element to be returned by subsequent call to next.
	 */
	protected int cursor;
	
	/**
	 * Index of element returned by most recent call to next. Reset to -1 if 
	 * this iterator is deleted by a call to reset.
	 */
	protected int lastRet;
	
	/**
	 * Index of the last element in the node array.
	 */
	protected int lastIndex;
	
	/**
	 * Constructs a tree iterator whose starting element is the root element
	 * of the specified tree. 
	 * 
	 * @param tree the tree to be traversed.
	 * 
	 * @throws NullPointerException if the tree is <tt>null</tt>.
	 * @throws EmptyTreeException if the specified tree is empty.
	 */
	protected AbstractTreeIterator(T tree) 
	{
		if(tree == null)
			throw new NullPointerException();
		if(tree.isEmpty())
			throw new EmptyTreeException();
		
		this.tree = tree;
		E root = tree.getRoot();

		//TODO: Fix the cast
		this.startNode = (N) tree.getNodeByElement(root);
		this.levels = new int[this.tree.size()];
		this.nodes 	= (N[]) new Tree.Node[this.tree.size()];
		
		//overwrite this template method in concrete classes
		buildTraverseOrder(startNode, 0);
		reset();
		
	}
	
	/**
	 * Constructs a tree iterator, whose starting element is the specified node. 
	 * The tree traversal includes the starting node and all his successor elements.
	 * elements.<p>
	 * It is essential that the tree contains the specified start node.
	 * 
	 * @param tree the tree to be traversed.
	 * @param startNode
	 *  
	 * @throws NullPointerException if the tree is <tt>null</tt>.
	 * @throws EmptyTreeException if the specified tree is empty.
	 * @throws IllegalArgumentException if the tree doesn't contain	 the start node.
	 */
	protected AbstractTreeIterator(T tree, N startNode)
	{
		if(tree == null)
			throw new NullPointerException();
		if(startNode == null)
			throw new NullPointerException();
		if(tree.isEmpty())
			throw new EmptyTreeException();
		if(!tree.containsElement(startNode.getElement()))
			throw new IllegalArgumentException("Tree doesnt contain startNode: " + 
											   startNode.getElement());
		
		this.startNode = startNode;
		this.tree 	= tree;
		
		this.levels = new int[this.tree.size()];
		this.nodes 	= (N[]) new Tree.Node[this.tree.size()];
		
		buildTraverseOrder(startNode, 0);
		reset();
	}
	
	/**
     * Returns <tt>true</tt> if the tree iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
	public boolean hasNext()
	{
		return cursor != lastIndex;
	}
	
	/**
     * Returns the next element in the tree iteration. Calling this method
     * repeatedly until the {@link #hasNext()} method returns false will
     * return each element in the underlying tree exactly once. The ordering
     * of the traversal is bounded on the concrete sub classes which extends 
     * this skeleton implementation.  
     *
     * @return the next element in the tree iteration.
     * @exception NoSuchElementException tree iteration has no more elements.
     */
	public E next()
	{
		if(hasNext()) {
			E next = nodes[cursor].getElement();
			lastRet = cursor++;
			return next;
		}
		throw new NoSuchElementException();
	}
	
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
	public boolean isLeaf()
	{
		try {
			return nodes[lastRet].isLeaf();
		} catch(IndexOutOfBoundsException e) {
			throw new IllegalStateException();
		}
	}
	
	/**
	 * Returns the level from the last returned element of the underlying tree.
	 * This method can called only, after the first call of <tt>next()</tt>.
	 *  
	 * @return the level from the last return element. 
	 * 		   by <tt>next</tt>.
	 * @throws IllegalStateException if the <tt>next</tt> method has not
     *		   yet been called
	 */
	public int level() 
	{
		try {
			return levels[lastRet];
		} catch(IndexOutOfBoundsException e) {
			throw new IllegalStateException();
		}
	}
	
	/**
	 * Resets this iterator to starting state of the traversal process.
	 *
	 */
	public void reset() 
	{
		cursor 	= 0;
		lastRet = -1;
	}
	
	/**
	 * Calling this method throws always an <tt>UnsupportedOperationException</tt>. 
	 * <p>
	 * Note that this method is declared as final so that no implementation can make 
	 * modifiable the underlying tree.
	 * 
	 * @throws UnsupportedOperationException always
	 */
	public final void remove()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Build the traversal ordering of the underlying tree. The ordering is stored in 
	 * the node array of the tree iteration.<p>
	 * This template method is invoked by the different constructors of this class at 
	 * the last step, so that all declared instance variables are initialized. Note that
	 * new declared variables in subclasses couldn't invoked by this method, because 
	 * they are not initialized at this point.
	 *  
	 * @param node the node which is appended to the node array.
	 * @param level the level of the specified node.
	 */
	protected abstract void buildTraverseOrder(N node, int level);	
}

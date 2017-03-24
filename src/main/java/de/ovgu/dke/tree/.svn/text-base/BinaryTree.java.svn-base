package de.ovgu.dke.tree;

import java.util.NoSuchElementException;

/**
 * Is the top level interface for all different binary tree implementations. A tree is
 * a ADT that stores elements hierarchically. With the exception of the top element
 * (typically called root), each elemnt in a tree has a <b>parent</b> element and 
 * zero or more children elements.<p>
 * 
 * In difference to the extended <tt>Tree</tt> a binary tree, each node in the tree has 
 * at most two child nodes. We are speaking about a proper binary tree when the tree is 
 * an ordered tree in which each internal node has two children. An ordered tree is, if 
 * there is a linear ordering defined for the children of each node, that is, we can 
 * identify children of a node as being the first, second and so on.<p>
 * 
 * Each element in the tree is internally represented as node {@link Node}, which 
 * are immutable. The internal representation of the tree can only modified with 
 * the methods specified in <tt>Tree</tt> interface or all its subclasses.<p>
 * 
 * The tree cannot contain duplicate elements. Note that great care must be exercised 
 * if mutable objects are stored. In this case the tree cannot gurantee that all 
 * inserted elements are unique.<p>  
 *
 * 
 * @author Marcel Hermkes
 * @see BinaryTree
 * @see AbstractTree
 * @see AbstractBinaryTree
 * @see LinkedTree
 * @see LinkedBinaryTree
 *
 */
public interface BinaryTree<E> extends Tree<E> 
{	
	/**
	 * Returns the <tt>BinaryTree.BinaryNode</tt> which is associated with the 
	 * specified element in the <tt>BinaryTree</tt>.
	 * 
	 * @param element the element whose associate <tt>BinaryTree.BinaryNode</tt> 
	 * 		  is to be returned.
	 * @return the associated <tt>Tree.Node</tt> for the element.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws NoSuchElementException if the <tt>BinaryTree</tt> doesn't contain the 
	 * 		   specified element.
	 * @throws EmptyTreeException if the <tt>BinaryTree</tt> is empty.
	 */
	public BinaryNode<E> getNodeByElement(Object element);
	
	/**
	 * Appends an element to the specified parent element in this tree as left child 
	 * element. If this tree contains already the specified element then the method 
	 * do nothing and returns <tt>false</tt>.
	 *   
	 * @param parent the parent element at which the specified element is to be appended. 
	 * @param element the element to be appended in this tree as left child element..
	 * @return <tt>true</tt> if the element appended in this tree, otherwise <tt>false</tt>
	 * 
	 * @throws NullPointerException if the one of the specified elements are <tt>null</tt>.
	 * @throws NoSuchElementException if the <tt>BinaryTree</tt> doesn't contain the 
	 * 		   specified parent element.
	 * @throws EmptyTreeException if the <tt>BinaryTree</tt> is empty.
	 */
	public boolean appendLeftElement(E parent, E leftElement);
	
	/**
	 * Appends an element to the specified parent element in this tree as right child 
	 * element. If this tree contains already the specified element then the method 
	 * do nothing and returns <tt>false</tt>.
	 *   
	 * @param parent the parent element at which the specified element is to be appended. 
	 * @param element the element to be appended in this tree as right child element..
	 * @return <tt>true</tt> if the element appended in this tree, otherwise <tt>false</tt>
	 * 
	 * @throws NullPointerException if the one of the specified elements are <tt>null</tt>.
	 * @throws NoSuchElementException if the <tt>BinaryTree</tt> doesn't contain the 
	 * 		   specified parent element.
	 * @throws EmptyTreeException if the <tt>BinaryTree</tt> is empty.
	 */
	public boolean appendRightElement(E parent, E rightElement);
	
	/**
	 * Removes the specified element and his parent from this <tt>Tree</tt>, if 
	 * present and an external node. An external node means that 
	 * <tt>isLeaf(element) == true</tt>. This method gurantees that that it leaves the
	 * <tt>BinaryTree</tt> being a proper binary tree.
	 * 
	 * @param element the element and his parent to be removed.
	 * @return <tt>true</tt> if the specified element is present and an external node
	 * 		   in the <tt>BinaryTree</tt>, otherwise <tt>false</tt>.
 	 * @throws NullPointerException if the specified element is <tt>null</tt>.
 	 * @throws NoSuchElementException if the <tt>BinaryTree</tt> doesn't contain the 
	 * 		   specified element
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.		 
	 */
	public boolean removeAboveElement(E element);
	
	/**
	 * Checks whether the specified element has a the left child. 
	 * 
	 * @param element the element whose presence of a left child to be tested.
	 * @return <tt>true</tt> if the specified element has a left child.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
 	 * @throws NoSuchElementException if the <tt>BinaryTree</tt> doesn't contain the 
	 * 		   specified element
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.		 
	 */
	public boolean hasLeftElement(Object element);
	
	/**
	 * Checks whether the specified element has a the right child. 
	 * 
	 * @param element the element whose presence of a right child to be tested.
	 * @return <tt>true</tt> if the specified element has a right child.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
 	 * @throws NoSuchElementException if the <tt>BinaryTree</tt> doesn't contain the 
	 * 		   specified element
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.		 
	 */
	public boolean hasRightElement(Object element);
	
	
	
	/**
	 * A binary tree node which stores the elements of a <tt>BinaryTree</tt>. The <tt>Tree.nodes</tt>
	 * returns a collection-view of the tree, whose elements of this class. Another way
	 * to get a node object is to invoke the method <tt>BinaryTree.getNodeByElement</tt>.<p>
	 *  
	 *  
	 * @author Marcel Hermkes
	 */
	interface BinaryNode<E> extends Tree.Node<E>
	{
		/**
		 * Returns the parent node of this node. If the node a root node, the
		 * method returns <tt>null<tt/>.
		 * 
		 * @return the parent node of the node. If the node a root node the 
		 * 		   return value is <tt>null</tt>
		 */
		public BinaryNode<E> parent();
		
		/**
		 * Returns the left child node of this nod. If the node hasn't a left child
		 * node, then the method returns <tt>null</tt>
		 * @return return the left child node of this node. If the node hasn't a left
		 * 		   child node the return value is <tt>null</tt>.
		 */
		public BinaryNode<E> getLeftChild();
		
		/**
		 * Returns the right child node of this nod. If the node hasn't a right child
		 * node, then the method returns <tt>null</tt>
		 * @return return the left child node of this node. If the node hasn't a right
		 * 		   child node the return value is <tt>null</tt>.
		 */
		public BinaryNode<E> getRightChild();
	}
}

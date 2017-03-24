package de.ovgu.dke.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;	
import java.util.NoSuchElementException;

/**
 * Is the top level interface for all different tree implementations. A tree is
 * a ADT that stores elements hierarchically. With the exception of the top element
 * (typically called root), each elemnt in a tree has a <b>parent</b> element and 
 * zero or more children elements.<p>
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
public interface Tree<E> extends Iterable<E>
{	
	/**
	 * Returns the root element of the <tt>Tree</tt>.
	 * 
	 * @return the root element.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 */
	public E getRoot();
	
	/**
	 * Sets (if the <tt>Tree</tt> is empty) or replaces the root element in the 
	 * tree with the specified element. If the <tt>Tree</tt> previously contained 
	 * the specified element, the element won't replaced. 
	 *  
	 * @param element element to be stored as root element.
	 * @return <tt>true</tt> if the element is stored as root element. This is the case
	 * 		   when the tree doesn't contains the specified element.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 */
	public boolean setRoot(E element);
	
	/**
	 * Returns the parent of the specified element. If the specified element the root element
	 * the methods should be return <tt>null</tt>.
	 * 
	 * @param element the element whose associated root element is to be returned.
	 * @return the parent element of specified element or <tt>null</tt> if the 
	 * 		   element is the root element of the <tt>Tree</tt>.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the specified element.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 */
	public E parentElement(Object element);
	
	
	/**
	 * Returns a <tt>List</tt> of child elements for the specified element. If the element is a
	 * leaf element this method should return an empty <tt>List</tt>.
	 *  
	 * @param element the element whose associated child elements is to be returned.
	 * @return a <tt>List</tt> of child elements for the specified element.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the specified element.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 */
	public List<E> childElements(Object element);
	
	/**
	 * Todo: move to ClassHierarchy
	 * @param element
	 * @return
	 */
	public List<E> getPathFromRoot(Object element);
	
	/**
	 * Todo: move to ClassHierarchy
	 * @param node1
	 * @param node2
	 * @return
	 */
	public int getPathLength(Object node1, Object node2);

	/**
	 * Todo: move to ClassHierarchy
	 * @param node1
	 * @param node2
	 * @return
	 */
	public E getCommonAncestor(Object node1, Object node2);
	
	/**
	 * Returns the <tt>Tree.Node</tt> which is associated with the specified element 
	 * in the <tt>Tree</tt>.
	 * 
	 * @param element the element whose associate <tt>Tree.Node</tt> is to be returned.
	 * @return the associated <tt>Tree.Node</tt> for the element.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the specified element.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 */
	public Tree.Node<E> getNodeByElement(Object element);
	
	/**
	 * Appends an element to the specified parent element in this tree. If 
	 * parent element is null then the specified element is setting to the root 
	 * element and the old root element is appending on the new root element.
	 * If this tree contains already the specified element then the method do 
	 * nothing and returns <tt>false</tt>.
	 *   
	 * @param parent the parent element at which the specified element is to be appended.
	 * 		  Null indicates that the element is appending as the root element of this tree. 
	 * @param element the element to be appended in this tree.
	 * @return <tt>true</tt> if the element appended in this tree, otherwise <tt>false</tt>
	 * 
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 */
	public boolean appendElement(Object parent, E element);
	
	/**
	 * Removes the specified element from this <tt>Tree</tt>, if present.
	 * 
	 * @param element the element to be removed.
	 * @return <tt>true</tt> if the specified element is present in the <tt>Tree</tt>, 
	 * 		   otherwise <tt>false</tt>.
 	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.		 
	 */
	public boolean removeElement(Object element);
	
	/**
	 * Removes the specified element and all elements below the specified element, 
	 * if present. If the following condition holds: <tt>isRoot(element) == true</tt>, 
	 * then the method is equivalent to the method <tt>clear()<tt>. 
	 *  
	 * @param element the element and all succesor elements to be removed.
	 * @return <tt>true</tt> if the specified element is present in the <tt>Tree</tt>,
	 * 		   otherwise <tt>false</tt>
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.		
	 */
	public Tree<E> removeSubTree(Object element);
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	public Tree<E> getSubTree(Object element);
	
	/**
	 * Swaps the specified elements in this tree. If the specified elements are equal, 
	 * invoking this method leaves this tree unchanged.
	 * 
	 * @param element1 the one element to be swapped.
	 * @param element2 the other element to be swapped.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the specified element.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 */
	public void swapElements(E element1, E element2);
	
	/**
	 * Replaces an presence element in this tree with a new element which is not 
	 * presence at the list. 
	 *  
	 * @param oldElement the element to be replaced.
	 * @param newElement the element to be stored at the position of the old element.
	 * @return <tt>true</tt> if the old element replaced with the new element. If this tree
	 *         already contained the new element then the method returns <tt>false</tt>.
	 * @throws NullPointerException if one of the elements is <tt>null</tt>
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the old element.
	 *         
	 */
	public boolean replaceElement(Object oldElement, E newElement);
	
	/**
     * Returns <tt>true</tt> if this tree contains the specified element.
     *
     * @param o element whose presence in this tree is to be tested.
     * @return <tt>true</tt> if this set contains the specified element.
   	 * @throws NullPointerException if one of the elements is <tt>null</tt>
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 */
	public boolean containsElement(Object element);
	
	/**
	 * Checks whether the specified element is the root element of this tree.
	 * 
	 * @param element the element to be tested as root element of this tree.
	 * @return <tt>true</tt> if the specified element is the root element.
 	 * @throws NullPointerException if one of the elements is <tt>null</tt>
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the element.
	 */
	public boolean isRoot(Object element);
	
	/**
	 * Checks whether the specified element is a leaf element of this tree.
	 * 
	 * @param element the element to be tested as leaf element of this tree.
	 * @return <tt>true</tt> if the specified element is a leaf element.
 	 * @throws NullPointerException if one of the elements is <tt>null</tt>
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the element.
	 */
	public boolean isLeaf(Object element);
	
	/**
	 * Checks whether both specified elements are siblings. They are siblings when
	 * both elements have the same parent.
	 * 
	 * @param element1 the element to be tested as sibling to another element.
	 * @param element2 the element to be tested as sibling to another element.
	 * @return <tt>true</tt> if both specified elements are siblings.
	 * @throws NullPointerException if one of the elements is <tt>null</tt>
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain one of the 
	 * 		   specified elements.
	 */
	public boolean areSiblings(Object element1, Object element2);
	
	/**
	 * Checks whether the ancestor element is an ancestor of the specified element.
	 * 
	 * @param ancestor the element to be tested as ancestor of the another element.
	 * @param element the element to be tested that an another element is the ancestor of him.
	 * @return <tt>true</tt> if the ancestor element is an ancestor of the specified element.
	 * @throws NullPointerException if one of the elements is <tt>null</tt>
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the element.
	 */
	public boolean isAncestor(Object ancestor, Object element);
	
	/**
	 * Returns the depth for the specified element in this tree. The depth of an
	 * element is the number of ancestors elements, excluding itselfs. The depth
	 * for the root element of the tree is defined with 0.
	 * 
	 * @param element the element whose depth is to be computed.
	 * @return the depth of the specified element in this tree.
 	 * @throws NullPointerException if the specified element is <tt>null</tt>.
 	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the 
	 * 		   specified element.
	 */
	public int depth(Object element);
	
	/**
	 * Returns the height of a subtree of this tree at the specified element.
	 * 
	 * @param 
	 * @return 
  	 * @throws NullPointerException if the specified element is <tt>null</tt>.
 	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the 
	 * 		   specified element.
	 */
	public int height(Object element);
	
	/**
	 * Returns the number elements in this tree.
	 * @return the number of elements in this tree.
	 */
	public int size();
	
	/**
	 * Returns the number of child elements for the specified element.
	 * 
	 * @param element the element whose number of children is to be returned.
	 * @return the number of child elements.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
 	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the 
	 * 		   specified element.
	 */
	public int numberOfChildren(Object element);
	
	/**
	 * Returns the number of leaf elements in this tree. A leaf element is
	 * an element who hasn't child elements.
	 * 
	 * @return the number of leaf elements in this tree.
	 */
	public int numberOfLeaves();
	
	/**
	 * Check whether this tree contains no elements.
	 * 
	 * @return <tt>true</tt> if this tree is empty.
	 */
	public boolean isEmpty();
	
	/**
	 * Removes all of the elements from this tree. The tree will be empty after 
	 * this call returns.
	 */
	public void clear();
	
	/**
	 * Rerurns a collection of elements contained in this tree. Each element in the
	 * returned collection is from type <tt>E</tt>. If this tree is empty, the method
	 * should return a empty collection of elements.
	 * 
	 * @return a collection of elements from type <tt>E</tt>.
	 */
	public Collection<E> elements();
	
	/**
	 * Returns a collection of nodes contained in this tree. Each element in 
	 * the returned collection is a <tt>Tree.Node</tt>. If this tree is empty, 
	 * the method should return an empty collection of
     * nodes.
     * 
	 * @return a collection of nodes from type <tt>Tree.Node</tt>.
	 */
	public Collection<Node<E>> nodes();
	
	/**
	 * A tree node which stores the elements of a <tt>Tree</tt>. The <tt>Tree.nodes</tt>
	 * returns a collection-view of the tree, whose elements of this class. Another way
	 * to get a node object is to invoke the method <tt>Tree.getNodeByElement</tt>. 
	 *  
	 * @author Marcel Hermkes
	 */
	interface Node<E>
	{
		/**
		 * Returns true if the node a root node of a tree.
		 * 
		 * @return <tt>true</tt> if the node a root node.
		 */
		public boolean isRoot();
		
		/**
		 * Returns true if the node a leaf node of a tree.
		 * 
		 * @return <tt>true</tt> if the node a leaf node.
		 */
		public boolean isLeaf();
		
		
		/**
		 * Returns the element corresponding to this node.
		 *  
		 * @return the element corresponing to this node.
		 */
		public E getElement();
		
		/**
		 * Returns the level in the tree for this node.
		 *   
		 * @return the level in the tree.
		 */
		public int getLevel();
		
		/**
		 * Returns the number of child nodes.
		 * 
		 * @return the number of child nodes.
		 */
		public int numberOfChildren();
		
		/**
		 * Returns the parent node of this node. If the node a root node, the
		 * method returns <tt>null<tt/>.
		 * 
		 * @return the parent node of the node. If the node a root node the 
		 * 		   return value is <tt>null</tt>
		 */
		public Node<E> parent();	
		
		/**
		 * Returns an iterator over the child nodes of this node in proper sequence.
		 * 
		 * @return an iterator over the child nodes.
		 */
		public Iterator<Node<E>> children();
	}
}

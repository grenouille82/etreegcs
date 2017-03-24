package de.ovgu.dke.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveTreeNode;

/**
 * This class provides a skeletal implementation of the <tt>Tree</tt> interface 
 * to minimize the effort required to implement this interface.<p>
 * 
 * The documentation for each non-abstract methods in this class describes its
 * implementation in detail.  Each of these methods may be overridden if the
 * tree being implemented admits a more efficient implementation.<p>
 * 
 * @author Marcel Hermkes
 *
 * @param <E>
 */
public abstract class AbstractTree<E> 
implements Tree<E>
{
	/**
	 * 
	 */
	protected transient Tree.Node<E> root;
	
	/**
	 * 
	 */
	protected transient int size;
	
	 /**
     * Sole constructor. (For invocation by subclass constructors, typically
     * implicit.)
     */
	protected AbstractTree() {}
	
	

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
	public abstract boolean appendElement(Object parent, E element);
	
	/**
	 * Removes the specified element from this <tt>Tree</tt>, if present.
	 * 
	 * @param element the element to be removed.
	 * @return <tt>true</tt> if the specified element is present in the <tt>Tree</tt>, 
	 * 		   otherwise <tt>false</tt>.
 	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.		 
	 */
	public abstract boolean removeElement(Object element);
	
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
	public abstract void swapElements(E element1, E element2);
	
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
	public abstract boolean replaceElement(Object oldElement, E newElement);
	
	/**
	 * Rerurns a collection of elements contained in this tree. Each element in the
	 * returned collection is from type <tt>E</tt>. If this tree is empty, the method
	 * should return a empty collection of elements.
	 * 
	 * @return a collection of elements from type <tt>E</tt>.
	 */
	public abstract Collection<E> elements(); 
	
	/**
	 * Returns a collection of nodes contained in this tree. Each element in 
	 * the returned collection is a <tt>Tree.Node</tt>. If this tree is empty, 
	 * the method should return an empty collection of
     * nodes.
     * 
	 * @return a collection of nodes from type <tt>Tree.Node</tt>.
	 */
	public abstract Collection<Tree.Node<E>> nodes();
	
	/**
	 * Sets (if this tree is empty) or replaces the root element in the 
	 * tree with the specified element. If the <tt>Tree</tt> previously contained 
	 * the specified element, the element won't replaced. 
	 *  
	 * @param element element to be stored as root element.
	 * @return <tt>true</tt> if the element is stored as root element. This is the case
	 * 		   when the tree doesn't contains the specified element.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 */
	public abstract boolean setRoot(E element);
	
	/**
	 * Removes the specified element and all elements below the specified element, 
	 * if present. If the following condition holds: <tt>isRoot(element) == true</tt>, 
	 * then the method is equivalent to the method <tt>clear()<tt>.<p> 
	 *  
	 * This implementation always throws an <tt>UnsupportedOperationException</tt>.
	 * 
	 * @param element the element and all succesor elements to be removed.
	 * @return <tt>true</tt> if the specified element is present in the <tt>Tree</tt>,
	 * 		   otherwise <tt>false</tt>
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws EmptyTreeException if the tree is empty.	
	 * @throws UnsupportedOperationException if the tree method is not supported by 
	 * 		   tree.	
	 */
	public Tree<E> removeSubTree(Object element)
	{
		throw new UnsupportedOperationException();
	}
	
	public Tree<E> getSubTree(Object element)
	{
		throw new UnsupportedOperationException();
	}
	/**
	 * Returns the parent of the specified element. If the specified element the root element
	 * the methods returns <tt>null</tt>.<p>
	 * 
	 * This method calls <tt>containsCheck(element)</tt> to check whether the tree contains the 
	 * element and retrieves the corresponding <tt>Node</tt>. The runtime depends on the runtime
	 * of this mehtod {@link #containsCheck(Object)}.   
	 * 
	 * @param element the element whose associated root element is to be returned.
	 * @return the parent element of specified element or <tt>null</tt> if the 
	 * 		   element is the root element of the <tt>Tree</tt>.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the specified element.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 */
	public E parentElement(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		Tree.Node<E> parentNode = containsCheck(element).parent();
		return (parentNode == null) ? null : parentNode.getElement();	 
	}
	
	/**
	 * Returns the <tt>Tree.Node</tt> which is associated with the specified element 
	 * in the <tt>Tree</tt>.<p>
	 * 
	 * This method calls <tt>containsCheck(element)</tt> to check whether the tree contains the 
	 * element and retrieves the corresponding <tt>Node</tt>. The runtime depends on the runtime
	 * of this mehtod {@link #containsCheck(Object)}.   
	 * 
	 * @param element the element whose associate <tt>Tree.Node</tt> is to be returned.
	 * @return the associated <tt>Tree.Node</tt> for the element.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the specified element.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 */
	public Tree.Node<E> getNodeByElement(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		Tree.Node<E> retVal = containsCheck(element);
		return retVal;
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	public LinkedList<E> getPathFromRoot(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();

		LinkedList<E> path= new LinkedList<E>();
		
		Tree.Node<E> node = containsCheck(element);
		while(node != null) 
		{
			path.addFirst(node.getElement());
			node = node.parent();
		}
		
		return path;
	}
	
	/** Calculates the path length between two nodes in the same tree in number of nodes along
	 * the path. I.e. the minimum path length is 1, if both nodes are identical.
	 * 
	 * @param other The node, to which the path length shall be determined.
	 * @return The path length between the two nodes.
	 */
	public int getPathLength(Object node1, Object node2) {
		LinkedList<E> node1Path = this.getPathFromRoot(node1);
		LinkedList<E> node2Path = this.getPathFromRoot(node2);
		int returnValue = 1;
		E curNode1;
		E curNode2;
		while (node1Path.size()>0 && node2Path.size()>0) {
			curNode1 = node1Path.removeFirst();
			curNode2 = node2Path.removeFirst();
			if (!curNode1.equals(curNode2)) {
				returnValue = 3;
				break;
			}
		}
		while (node1Path.size()>0) {
			curNode1 = node1Path.removeFirst();
			returnValue++;
		}
		while (node2Path.size()>0) {
			curNode2 = node2Path.removeFirst();
			returnValue++;
		}
		return returnValue;
	}
	
	public List<E> getPath(Object from, Object to) 
	{
		LinkedList<E> path = new LinkedList<E>();
		E commonAnc = getCommonAncestor(from, to);
		System.out.println(commonAnc);
		Tree.Node<E> node = getNodeByElement(from);
		E element = node.getElement();
		path.add(element);
		while(!element.equals(commonAnc))
		{
			node = node.parent();
			element = node.getElement();
			path.add(element);
		}
		
		LinkedList<E> tmpPath = new LinkedList<E>();
		node = getNodeByElement(to);
		element = node.getElement();
		while(!element.equals(commonAnc))
		{
			tmpPath.addFirst(element);
			node = node.parent();
			element = node.getElement();
		}
		path.addAll(tmpPath);
		
		return path;
	}

	//move to class hierarchy
	public E getCommonAncestor(Object node1, Object node2) {
		LinkedList<E> node1Path = this.getPathFromRoot(node1);
		LinkedList<E> node2Path = this.getPathFromRoot(node2);
		E returnValue = null;
		E curNode1;
		E curNode2;
		while (node1Path.size()>0 && node2Path.size()>0) {
			curNode1 = node1Path.removeFirst();
			curNode2 = node2Path.removeFirst();
			if (curNode1.equals(curNode2)) {
				returnValue = curNode1;
			} else {
				return returnValue;
			}
		}
		return returnValue;
	}
	
	/**
	 * Returns a <tt>List</tt> of child elements for the specified element. If the element is a
	 * leaf element this method should return an empty <tt>List</tt>.<p>
	 * 
	 * The list is a shallow copy of child elements, so that changes aren't reflected in the
	 * tree.<p> 
	 *
  	 * This method calls <tt>containsCheck(element)</tt> to check whether the tree contains the 
	 * element and retrieves the corresponding <tt>Node</tt>. The runtime depends on the runtime
	 * of this mehtod {@link #containsCheck(Object)}.   
	 * 
	 * @param element the element whose associated child elements is to be returned.
	 * @return a <tt>List</tt> of child elements for the specified element.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the specified element.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 */
	public List<E> childElements(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		Tree.Node<E> node = containsCheck(element);
		List<E> retVal = null;
		
		if(node != null) {
			retVal = new ArrayList<E>(node.numberOfChildren());
			Tree.Node<E> child = null;
			Iterator<Tree.Node<E>> it = node.children();
			
			while(it.hasNext())
			{
				child = it.next();
				retVal.add(child.getElement());
			}
		} else
			retVal = new ArrayList<E>(0);
		
		return retVal;
	}
	
	/**
     * Returns <tt>true</tt> if this tree contains the specified element.
     *
     * @param o element whose presence in this tree is to be tested.
     * @return <tt>true</tt> if this set contains the specified element.
   	 * @throws NullPointerException if one of the elements is <tt>null</tt>
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 */
	public boolean containsElement(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		try {
			containsCheck(element);
			return true;
		} catch(NoSuchElementException e) {
			return false;
		} 
	}
	
	/**
	 * Returns the root element of this tree.
	 * 
	 * @return the root element.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 */
	public E getRoot()
	{
		checkForEmptiness();
		return root.getElement();
	}
	
	/**
	 * Checks whether the specified element is the root element of this tree.<p>
	 * 
	 * This method calls <tt>containsCheck(element)</tt> to check whether the tree contains the 
	 * element and retrieves the corresponding <tt>Node</tt>. The runtime depends on the runtime
	 * of this mehtod {@link #containsCheck(Object)}.   
	 * 
	 * @param element the element to be tested as root element of this tree.
	 * @return <tt>true</tt> if the specified element is the root element.
 	 * @throws NullPointerException if one of the elements is <tt>null</tt>
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the element.
	 */
	public boolean isRoot(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		return root.getElement().equals(element);
	}
	
	/**
	 * Checks whether the specified element is a leaf element of this tree.<p>
	 * 
	 * This method calls <tt>containsCheck(element)</tt> to check whether the tree contains the 
	 * element and retrieves the corresponding <tt>Node</tt>. The runtime depends on the runtime
	 * of this mehtod {@link #containsCheck(Object)}.   
	 * 
	 * @param element the element to be tested as leaf element of this tree.
	 * @return <tt>true</tt> if the specified element is a leaf element.
 	 * @throws NullPointerException if one of the elements is <tt>null</tt>
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the element.
	 */
	public boolean isLeaf(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		Tree.Node<E> node = containsCheck(element);
		return node.isLeaf();
	}
	
	/**
	 * Checks whether both specified elements are siblings. They are siblings when
	 * both elements have the same parent.<p>
	 * 
 	 * This method calls <tt>containsCheck(element)</tt> to check whether the tree contains the 
	 * element and retrieves the corresponding <tt>Node</tt>. The runtime depends on the runtime
	 * of this mehtod {@link #containsCheck(Object)}.
	 * 
	 * @param element1 the element to be tested as sibling to another element.
	 * @param element2 the element to be tested as sibling to another element.
	 * @return <tt>true</tt> if both specified elements are siblings.
	 * @throws NullPointerException if one of the elements is <tt>null</tt>
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain one of the 
	 * 		   specified elements.
	 */
	public boolean areSiblings(Object element1, Object element2)
	{
		if(element1 == null)
			throw new NullPointerException();
		if(element2 == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		Tree.Node<E> node1 = containsCheck(element1);
		Tree.Node<E> node2 = containsCheck(element2);
		return node1.parent() == node2.parent();
	}
	
	/**
	 * Checks whether the anchestor element is an ancestor of the specified element.<p>
	 * 
	 * This method calls <tt>containsCheck(element)</tt> to check whether the tree contains the 
	 * element and retrieves the corresponding <tt>Node</tt>. The runtime depends on the runtime
	 * of this mehtod {@link #containsCheck(Object)}.
	 * 
	 * @param ancestor the element to be tested as ancestor of the another element.
	 * @param element the element to be tested that an another element is the ancestor of him.
	 * @return <tt>true</tt> if the ancestor element is an ancestor of the specified element.
	 * @throws NullPointerException if one of the elements is <tt>null</tt>
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the element.
	 */
	public boolean isAncestor(Object ancestor, Object element)
	{
		if(ancestor == null)
			throw new NullPointerException();
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		
		Tree.Node<E> ancestorNode = containsCheck(ancestor);
		Tree.Node<E> node 	= containsCheck(element);
		Tree.Node<E> parent = node.parent();
		
		while(parent != null) 
		{
			if(ancestorNode == parent)
				return true;
			
			parent = parent.parent();
		}
		return false;
	}
	
	/**
	 * Returns the number of child elements for the specified element.<p>
	 * 
	 * This method calls <tt>containsCheck(element)</tt> to check whether the tree contains the 
	 * element and retrieves the corresponding <tt>Node</tt>. The runtime depends on the runtime
	 * of this mehtod {@link #containsCheck(Object)}.
	 * 
	 * @param element the element whose number of children is to be returned.
	 * @return the number of child elements.
	 * @throws NullPointerException if the specified element is <tt>null</tt>.
 	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the 
	 * 		   specified element.
	 */
	public int numberOfChildren(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		Tree.Node<E> node = containsCheck(element);
		return node.numberOfChildren();
	}
	
	/**
	 * Returns the number of leaf elements in this tree. A leaf element is
	 * an element who hasn't child elements.
	 * 
	 * @return the number of leaf elements in this tree.
	 */
	public int numberOfLeaves()
	{
		Collection<Tree.Node<E>> nodes = nodes();
		int leaves = 0; 
		
		
		for(Tree.Node<E> n : nodes)
		{
			if(n.isLeaf())
				leaves++;
		}
		return leaves;
	}
	
	
	/**
	 * Returns the depth for the specified element in this tree. The depth of an
	 * element is the number of ancestors elements, excluding itselfs. The depth
	 * for the root element of the tree is defined with 0.<p>
	 *
 	 * This method calls <tt>containsCheck(element)</tt> to check whether the tree contains the 
	 * element and retrieves the corresponding <tt>Node</tt>. The runtime depends on the runtime
	 * of this mehtod {@link #containsCheck(Object)}.
	 * 
	 * @param element the element whose depth is to be computed.
	 * @return the depth of the specified element in this tree.
 	 * @throws NullPointerException if the specified element is <tt>null</tt>.
 	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the 
	 * 		   specified element.
	 */
	public int depth(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		Tree.Node<E> node = containsCheck(element);
		return node.getLevel();
	}
	
	/**
	 * Returns the height of a subtree of this tree at the specified element.
	 * Computes the height in level order manner.
	 * 
	 * This method calls <tt>containsCheck(element)</tt> to check whether the tree contains the 
	 * element and retrieves the corresponding <tt>Node</tt>. The runtime depends on the runtime
	 * of this mehtod {@link #containsCheck(Object)}.
	 *
	 * @param 
	 * @return 
  	 * @throws NullPointerException if the specified element is <tt>null</tt>.
 	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.
	 * @throws NoSuchElementException if the <tt>Tree</tt> doesn't contain the 
	 * 		   specified element.
	 */
	public int height(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		int h = 0;
		Tree.Node<E> posNode = containsCheck(element);
		
		if(!posNode.isLeaf()) {
			Queue<Tree.Node<E>> subNodes = new LinkedList<Tree.Node<E>>();
			int currLevelCnt 	= 1;
			int subLevelCnt		= 0;
			
			do {
				Iterator<Tree.Node<E>> it = posNode.children();
					
				while(it.hasNext())
				{
					Tree.Node<E> currNode = it.next();
					if(!currNode.isLeaf()) {
						subLevelCnt++;
						subNodes.offer(currNode);
					}
				}
				
				posNode = subNodes.poll();
				currLevelCnt--;
				
				//change level of the tree
				if(currLevelCnt == 0) {
					currLevelCnt = subLevelCnt;
					subLevelCnt = 0;
					h++;
				}
			} while(!subNodes.isEmpty());
		}
		
		return h;
	}
	
	/**
	 * 
	 * @return
	 */
	public final int size()
	{
		return size;
	}
	
	/**
	 * Check whether this tree contains no elements. An tree is empty
	 * if <tt>size == 0</tt>.
	 * 
	 * @return <tt>true</tt> if this tree is empty.
	 */
	public final boolean isEmpty()
	{
		return size == 0;
	}
	
	/**
	 * Removes all of the elements from this tree. The tree will be empty after 
	 * this call returns.
	 */
	public void clear()
	{
		root = null;
		size = 0;
	}
	
	/**
	 * Returns an iterator of the elements in this tree. By default the returned 
	 * iterator is the <tt>PreOrderTreeIterator</tt>. The iterations starts by the
	 * root element.
	 * 
	 * @return an iterator of the elements in this tree.
	 * @throws EmptyTreeException if the tree is empty.
	 * 
	 * @see PreOrderTreeIterator
	 */
	public Iterator<E> iterator() 
	{
		return new PreOrderTreeIterator<E>(this, root);
	}
	
	public ArrayList<E> getLeafNodes() {
		ArrayList<E> returnValue = new ArrayList<E>();
		this.getLeafNodesRec(this.root, returnValue);
		return returnValue;
	}
	
	private void getLeafNodesRec(Node<E> curNode, ArrayList<E> nodes) {
		if (curNode.isLeaf()) {
			nodes.add(curNode.getElement());
			return;
		}
		Iterator<Node<E>> childIt = curNode.children();
		while (childIt.hasNext()) {
			this.getLeafNodesRec(childIt.next(), nodes);
		}
	}
	
	/**
	 * Checks whether this tree contains the specified element and returs the 
	 * corresponding node. In the case that the tree doesn't contain the element,
	 * this method throws a <tt>NoSuchElementException</tt>.
	 * <p>
	 * For perfomance reasons this method should be overwritten in concrete 
	 * implementations. Because the many methods depends on the runtime of this
	 * method. By default the search for the corresponding node is linear.
	 */
	protected Tree.Node<E> containsCheck(Object element)
	{
		Collection<Tree.Node<E>> nodes = nodes();
		for(Tree.Node<E> n : nodes)
		{
			if(n.getElement().equals(element))
				return n;
		}
			
		throw new NoSuchElementException("Node Element: " + element.toString());
	}
	
	/**
	 * Checks whether the tree is empty. If the tree is empty then the method throws
	 * an <tt>EmptyTreeException</tt>
	 * @throws EmptyTreeException if the tree is empty.
	 */
	protected final void checkForEmptiness()
	{
		if(size == 0)
			throw new EmptyTreeException();
	}
	
	
	/**
	 * This class provides a skeletal implementation of the <tt>Node</tt> interface 
	 * to minimize the effort required to implement this interface.<p>
	 *	 
	 * The documentation for each non-abstract methods in this class describes its
	 * implementation in detail.  Each of these methods may be overridden if the
	 * tree being implemented admits a more efficient implementation.<p>
	 * 
	 * @author Marcel Hermkes
	 *
	 * @param <E>
	 */
	static abstract class AbstractNode<E> implements Tree.Node<E>
	{
		/**
		 * The associated element.
		 */
		E element;
		
		AbstractNode<E> parent;
		
		/**
		 * The childen of this node.
		 */
		List<AbstractNode<E>> children;


		/**
	     * Sole constructor. (For invocation by subclass constructors, typically
	     * implicit.)
	     */
		protected AbstractNode () {}
		
		/**
		 * {@inheritDoc}
		 */
		public Iterator<Tree.Node<E>> children() 
		{
			/**
			 * 
			 */
			return new Iterator<Node<E>>() {
				Iterator<AbstractNode<E>> it = children.iterator();
				
				public boolean hasNext() {
					return it.hasNext();
				}

				public Tree.Node<E> next() {
					return it.next();
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}

		/**
		 * {@inheritDoc}
		 */
		public E getElement() 
		{
			return element;
		}

		/**
		 * {@inheritDoc}
		 */
		public int getLevel() 
		{
			int level = 0;
			Tree.Node<E> posNode = this;
			
			while(!posNode.isRoot()) 
			{
				level++;
				posNode = posNode.parent();
			}
			
			return level;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isLeaf() 
		{
			return numberOfChildren() == 0;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isRoot() 
		{
			return parent == null;
		}

		/**
		 * {@inheritDoc}
		 */
		public int numberOfChildren() 
		{
			return children.size();
		}

		/**
		 * {@inheritDoc} 
		 */
		public Tree.Node<E> parent() 
		{
			return parent;
		}

		/**
		 * Sets the element.
		 */
		void setElement(E element) 
		{
			if(element == null)
				throw new NullPointerException();
			this.element = element;
		}
	}
}

package de.ovgu.dke.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * 
 * @author Marcel Hermkes
 *
 * @param <E>
 */
public class LinkedBinaryTree<E> extends AbstractBinaryTree<E>
{
	/**
	 * 
	 */
	private HashMap<E, SimpleNode<E>> elementNodeMap;

	/**
	 * 
	 *
	 */
	public LinkedBinaryTree()
	{
		elementNodeMap = new HashMap<E, SimpleNode<E>>();
	}
	
	/**
	 * 
	 * @param rootElement
	 * 
	 * @throws NullPointerException
	 */
	public LinkedBinaryTree(E rootElement)
	{
		this();
		
		if(rootElement == null)
			throw new NullPointerException();
		
		SimpleNode<E> node = new SimpleNode<E>(rootElement);
		
		size = 1;
		root = node;
		elementNodeMap.put(rootElement, node);
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 * 
	 * @throws NullPointerException
	 */
	public boolean setRoot(E element) 
	{
		if(element == null)
			throw new NullPointerException();
		
		if(root == null) {
			
		} else  {
			if(elementNodeMap.containsKey(element))
				return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param parent 
	 * @param element
	 * @return
	 * 
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 */
	public boolean appendElement(Object parent, E element) 
	{
		if(element == null)
			throw new NullPointerException();
		
		SimpleNode<E> node = null;
		
		if(parent == null) {
			node = new SimpleNode<E>(element);
			
			if(root != null) {
				//make root to child for the new root
				AbstractBinaryNode<E> oldRoot = (AbstractBinaryNode<E>) root;
				oldRoot.parent = node;
				node.leftChild = oldRoot;
			}
				
			root = node;
			elementNodeMap.put(element, node);
		} else {
			SimpleNode<E> parentNode = containsCheck(parent);
			
			if(!elementNodeMap.containsKey(element)) {
				//figure out a free branch
				if(parentNode.leftChild == null)
					node = new SimpleNode<E>(parentNode, element, BranchType.LEFT);
				else if(parentNode.rightChild == null)
					node = new SimpleNode<E>(parentNode, element, BranchType.RIGHT);
			}
		}
		
		if(node != null) {
			size++;
			elementNodeMap.put(element, node);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param parent
	 * @param leftElement
	 * @return
	 * 
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 * @throws EmptyTreeException
	 */
	public boolean appendLeftElement(E parent, E leftElement) 
	{
		if(parent == null)
			throw new NullPointerException();
		if(leftElement == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> parentNode = containsCheck(parent);	
		
		if(parentNode.leftChild == null && !elementNodeMap.containsKey(leftElement)) {
			SimpleNode<E> node = new SimpleNode<E>(parentNode, leftElement, 
												   BranchType.LEFT);
			size++;
			elementNodeMap.put(leftElement, node);
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param parent
	 * @param leftElement
	 * @return
	 * 
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 * @throws EmptyTreeException
	 */
	public boolean appendRightElement(E parent, E rightElement) 
	{
		if(parent == null)
			throw new NullPointerException();
		if(rightElement == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> parentNode = containsCheck(parent);	
		
		if(parentNode.rightChild == null && !elementNodeMap.containsKey(rightElement)) {
			SimpleNode<E> node = new SimpleNode<E>(parentNode, rightElement, 
												   BranchType.RIGHT);
			size++;
			elementNodeMap.put(rightElement, node);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param element
	 * @return
	 * 
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 * @throws EmptyTreeException
	 */
	public boolean hasLeftElement(Object element) 
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> node = containsCheck(element);
		return node.leftChild != null;
	}

	/**
	 * 
	 * @param element
	 * @return
	 * 
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 * @throws EmptyTreeException
	 */
	public boolean hasRightElement(Object element) 
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> node = containsCheck(element);
		return node.rightChild != null;
	}
	
	/**
	 * 
	 * @param element
	 * @return 
	 * 
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 * @throws EmptyTreeException
	 */
	public BinaryNode<E> getNodeByElement(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> retVal = containsCheck(element);
		return retVal;
	}

	/**
	 * 
	 * @return
	 * 
	 * @throws EmptyTreeException
	 */
	public Collection<E> elements()
	{
		checkForEmptiness();
		return elementNodeMap.keySet();
	}
	
	/**
	 * 
	 * @return
	 * 
	 * @throws EmptyTreeException
	 */
	public Collection<Tree.Node<E>> nodes() 
	{
		checkForEmptiness();
		Collection<SimpleNode<E>> nodes = elementNodeMap.values();
		return new ArrayList<Tree.Node<E>>(nodes);
	}

	/**
	 * 
	 * @param element
	 * @return
	 * 
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 * @throws EmptyTreeException
	 */
	public boolean removeElement(Object element) 
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> remNode = containsCheck(element);
		
		if(remNode.isLeaf()) {
			AbstractBinaryNode<E> parent = remNode.parent;
			if(parent == null) {
				root = null;
			} else {
				if(parent.leftChild == remNode)
					parent.leftChild = null;
				else
					parent.rightChild = null;
			}
			elementNodeMap.remove(remNode.element);
			size--;
			return true;
		}
		
		return false;
	}

	/**
	 * 
	 * @param element
	 * @return
	 * 
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 * @throws EmptyTreeException
	 */
	public boolean removeAboveElement(E element) 
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> node = containsCheck(element);
		
		if(node.isLeaf()) {
			AbstractBinaryNode<E> parent = node.parent;
			AbstractBinaryNode<E> sibling = node.leftChild;
			if(sibling == node) 
				sibling = node.rightChild;
			
			if(!node.isRoot()) {
				if(parent.isRoot()) {
					sibling.parent = null;
					root = sibling;
				} else {
					AbstractBinaryNode<E> grandpa = parent.parent;
					if(grandpa.leftChild == parent)
						grandpa.leftChild = sibling;
					else
						grandpa.rightChild = sibling;
					sibling.parent = grandpa;
				}
			} else
				root = null;
			
			elementNodeMap.remove(node.element);
			elementNodeMap.remove(parent.element);
			size -= 2;
			return true;
		}
		
		return false;
	}

	/**
	 * 
	 * @param element
	 * @return
	 * 
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 * @throws EmptyTreeException
	 */
	public Tree<E> removeSubTree(Object element)
	{
		return super.removeSubTree(element);
		/*
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> node = containsCheck(element);
		
		if(node.isRoot())
			clear();
		else {
			if(node.parent.leftChild == node) 
				node.parent.leftChild = null;
			else
				node.parent.rightChild = null;
			fastRecursiveRemove(node);
		}
		return true;*/
	}


	/**
	 * 
	 * @param oldElement
	 * @param newElement
	 * @return
	 * 
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 * @throws EmptyTreeException
	 */
	public boolean replaceElement(Object oldElement, E newElement) 
	{
		if(oldElement == null)
			throw new NullPointerException();
		if(newElement == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		if(elementNodeMap.containsKey(newElement)) 
			return false;
		
		SimpleNode<E> node = elementNodeMap.remove(oldElement);
		
		if(node == null) {
			throw new NoSuchElementException("Node Element: " + 
					 						 oldElement.toString());
		}
		
		elementNodeMap.put(newElement, node);
		node.setElement(newElement);
		return true;
	}

	/**
	 * 
	 * @param element1
	 * @param element2
	 * @return
	 * 
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 * @throws EmptyTreeException
	 */
	public void swapElements(E element1, E element2) 
	{
		if(element1 == null)
			throw new NullPointerException();
		if(element2 == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> node1 = containsCheck(element1);
		SimpleNode<E> node2 = containsCheck(element2);
		
		if(element1.equals(element2)) {
			node1.element = element2;
			node2.element = element1;
			elementNodeMap.put(element1, node2);
			elementNodeMap.put(element2, node1);
		}
	}
	
	/**
	 * 
	 * 
	 */
	public void clear()
	{
		super.clear();
		elementNodeMap = new HashMap<E, SimpleNode<E>>();
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 * 
	 * @throws NoSuchElementException
	 * @throws EmptyTreeException
	 */
	protected final SimpleNode<E> containsCheck(Object element)
	{	
		SimpleNode<E> retVal = elementNodeMap.get(element);
		if(retVal == null) {
			throw new NoSuchElementException("Node Element: " + 
											 element.toString());
		}
		return retVal;
	}
	
	/**
	 * 
	 * @param node
	 */
	private final void fastRecursiveRemove(AbstractBinaryNode<E> node)
	{
		if(node != null) {
			size--;
			elementNodeMap.remove(node);
			fastRecursiveRemove(node.leftChild);
			fastRecursiveRemove(node.rightChild);
		}
	}
	
	/**
	 * 
	 * @author Marcel Hermkes
	 *
	 */
	enum BranchType { LEFT, RIGHT }
	
	/**
	 * 
	 * @author Marcel Hermkes
	 *
	 * @param <E>
	 */
	static class SimpleNode<E> extends AbstractBinaryNode<E>
	{
		/**
		 * 
		 * @param element
		 */
		SimpleNode(E element)
		{	
			this.element = element;
		}
		
		/**
		 * 
		 * @param parent
		 * @param element
		 * @param type
		 */
		SimpleNode(SimpleNode<E> parent, E element, BranchType type) 
		{
			this(element);
			this.parent = parent;
			if(type == BranchType.LEFT)
				this.parent.leftChild = this;
			else
				this.parent.rightChild = this;
		}
		
		/**
		 * 
		 * @param parent
		 * @param leftChild
		 * @param rightChild
		 * @param element
		 * @param type
		 */
		SimpleNode(SimpleNode<E> parent, SimpleNode<E> leftChild, 
				   SimpleNode<E> rightChild, E element, BranchType type) 
		{
			this(parent, element, type);
			this.leftChild 	= leftChild;
			this.rightChild	= rightChild;
		}
	}
}

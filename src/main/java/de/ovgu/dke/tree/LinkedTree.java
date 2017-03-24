package de.ovgu.dke.tree;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import de.ovgu.dke.util.Copyable;

/**
 * An general tree implemenation of the <tt>Tree</tt> interface. The 
 * <tt>LinkedTree</tt> stores the children of his nodes in a <tt>LinkedList</tt>.
 * <p>
 * 
 * Each element in the tree is internally represented as nodes {@link Node}, which 
 * are immutable. The internal representation of the tree can only modified with 
 * the methods specified in <tt>Tree</tt> interface or all its subclasses.<p>
 * 
 * The tree cannot contain duplicate elements. Note that great care must be exercised 
 * if mutable objects are stored. In this case the tree cannot gurantee that all 
 * inserted elements are unique.<p> 
 *   
 * @author Marcel Hermkes
 *
 * @param <E>
 */
public class LinkedTree<E> extends AbstractTree<E>
implements Tree<E>, Serializable, Copyable<LinkedTree<E>>
{
	
	private static final long serialVersionUID = -4008292968513186073L;
	
	/**
	 *  
	 */
	private transient HashMap<E, SimpleNode<E>> elementNodeMap;

	/**
	 * Standard constructor, which creates an empty tree.
	 *
	 */
	public LinkedTree()
	{
		elementNodeMap = new HashMap<E, SimpleNode<E>>();
	}
	
	/**
	 * Creates an tree,
	 * @param rootElement the root element of this tree.
	 * 
	 * @throws NullPointerException
	 */
	public LinkedTree(E rootElement)
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
	 * {@inheritDoc}
	 */
	public boolean setRoot(E element) 
	{
		if(element == null)
			throw new NullPointerException();
		
		if(root == null) {
			SimpleNode<E> node = new SimpleNode<E>(element);
			root = node;
			elementNodeMap.put(element, node);
			size++;
		} else  {
			if(elementNodeMap.containsKey(element))
				return false;
			
			SimpleNode<E> tmpRoot = elementNodeMap.remove(root.getElement());
			tmpRoot.setElement(element);
			root = tmpRoot;
			elementNodeMap.put(element, tmpRoot);
		}
		
		return true;
	}
	
	/**
	 * 
	 * {@inheritDoc}
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
				AbstractNode<E> oldRoot = (AbstractNode<E>) root;
				oldRoot.parent = node;
				node.children.add(oldRoot);
			}
				
			root = node;
			
			elementNodeMap.put(element, node);
		} else {
			SimpleNode<E> parentNode = containsCheck(parent);
			
			if(!elementNodeMap.containsKey(element)) 
				node = new SimpleNode<E>(parentNode, element);
		}
		
		if(node != null) {
			size++;
			elementNodeMap.put(element, node);
			return true;
		}
		return false;
	}

	public boolean moveElement(E newParent, E element) 
	{
		SimpleNode<E> elementNode = this.elementNodeMap.get(element);
		if (elementNode==null)
			return false;
		
		SimpleNode<E> parentNode = this.elementNodeMap.get(newParent);
		if (parentNode==null)
			return false;
		
		elementNode.parent.children.remove(elementNode);
		elementNode.parent = parentNode;
		parentNode.children.add(elementNode);
		return true;
	}

	public boolean toogleChilds(E parent, int index1, int index2) {
		SimpleNode<E> parentNode = this.elementNodeMap.get(parent);
		if (parentNode==null || parentNode.children.size()<index1 || parentNode.children.size()<index2)
			return false;
		
		AbstractNode<E> helpNode = parentNode.children.get(index1);
		parentNode.children.set(index1, parentNode.children.get(index2));
		parentNode.children.set(index2, helpNode);
		
		return true;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public Collection<E> elements()
	{
		checkForEmptiness();
		return elementNodeMap.keySet();
	}
	
	/**
	 * 
	 *{@inheritDoc}
	 * 
	 */
	public Collection<Tree.Node<E>> nodes() 
	{
		Collection<SimpleNode<E>> nodes = elementNodeMap.values();
		return new ArrayList<Tree.Node<E>>(nodes);
	}

	/**
	 * NOTE: child elements not contained in the tree yet are appended as children of
	 * the element. if a child in children not be a direct child node of the parent element
	 * then it will be ignorred.
	 * @param parent
	 * @param children
	 * @param element
	 */
	public void insertElementBetween(Object parent, Collection<? extends E> children, E element)
	{
		if(element == null)
			throw new NullPointerException();
		if(parent == null)
			throw new IllegalArgumentException("cannot insert before root element");
		
		checkForEmptiness();
		SimpleNode<E> parentNode = containsCheck(parent);
		SimpleNode<E> node = null;
		
		if(!elementNodeMap.containsKey(element)) {
			node = new SimpleNode<E>(parentNode, element);
			size++;
			elementNodeMap.put(element, node);
	
			if(children != null) {
				for(E childElement : children) {
					SimpleNode<E> childNode = elementNodeMap.get(childElement);
					if(childNode == null) {
						childNode = new SimpleNode<E>(node, childElement);
						size++;
						elementNodeMap.put(childElement, childNode);
					} else {
						if(childNode.parent == parentNode) {
							childNode.parent = node;
							parentNode.children.remove(childNode);
							node.children.add(childNode);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Removes the specified element from this <tt>Tree</tt>, if present.<p>
	 * All children of the removed node are appended at the parent of him.
	 * If the removed element was the root element of this tree, then the 
	 * leftmost child element was set as root element and the other children 
	 * appended to the new root element.  
	 * 
	 * 
	 * @param element the element to be removed.
	 * @return <tt>true</tt> if the specified element is present in the <tt>Tree</tt>, 
	 * 		   otherwise <tt>false</tt>.
 	 * @throws NullPointerException if the specified element is <tt>null</tt>.
	 * @throws EmptyTreeException if the <tt>Tree</tt> is empty.		 
	 */
	public boolean removeElement(Object element) 
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> remNode = elementNodeMap.get(element);
		
		if(remNode == null)
			return false;
		
		if(remNode.isRoot()) {
			root = null;
			if(!remNode.isLeaf()) {
				//append the leftmost child as root
				Iterator<AbstractNode<E>> it = remNode.children.iterator();
				AbstractNode<E> newRoot  = it.next();
				joinParentChildren(newRoot, remNode.children);
				root = newRoot;
			}
		} else {
			AbstractNode<E> newParent = remNode.parent;
			newParent.children.remove(remNode);
			joinParentChildren(newParent, remNode.children);
		}
		
		elementNodeMap.remove(element);
		size--;
		return true;
		
	}

	/**
	 * 
	 * {@inheritDoc}
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
	 *{@inheritDoc}
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
		
		if(!element1.equals(element2)) {
			node1.element = element2;
			node2.element = element1;
			elementNodeMap.put(element1, node2);
			elementNodeMap.put(element2, node1);
		}
	}
	
	public LinkedTree<E> getSubTree(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> node = containsCheck(element);
		LinkedTree<E> retVal = new LinkedTree<E>(node.getElement());
		
		Queue<Tree.Node<E>> queue = new LinkedList<Tree.Node<E>>();
		queue.offer(node);
		
		Tree.Node<E> parentNode = null;
		Tree.Node<E> childNode	= null;
		Iterator<Tree.Node<E>> it = null;
		while(!queue.isEmpty())
		{
			parentNode = queue.poll();
			it = parentNode.children();
			
			while(it.hasNext())
			{
				childNode = it.next();
				retVal.appendElement(parentNode.getElement(), childNode.getElement());
				queue.offer(childNode);
			}
		}
		
		return retVal;
	}
	
	/**
	 * 
	 *{@inheritDoc}
	 */
	public LinkedTree<E> removeSubTree(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		SimpleNode<E> node = containsCheck(element);
		
		LinkedTree<E> retVal = getSubTree(element);
		
		if(node.isRoot()) 
			clear();
		else {
			node.parent.children.remove(node);
			node.parent = null;		
			fastRecursiveRemove(node);
		}
		return retVal;
	}
	
	public List<E> siblingElements(Object element)
	{
		if(element == null)
			throw new NullPointerException();
		
		checkForEmptiness();
		
		List<E> retVal = new LinkedList<E>();
		SimpleNode<E> node  = containsCheck(element);
		if(!node.isRoot()) {
			Tree.Node<E> parent = node.parent();
			Iterator<Tree.Node<E>> it = parent.children();
			while(it.hasNext())
			{
				Tree.Node<E> candidate = it.next();
				if(candidate != node)
					retVal.add(candidate.getElement());
			}
		}
		return retVal;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void clear()
	{
		super.clear();
		elementNodeMap = new HashMap<E, SimpleNode<E>>();
	}
	
	/**
	 * 
	 * @return
	 */
	public LinkedTree<E> copy()
	{
		LinkedTree<E> copy = new LinkedTree<E>();
		copy.setRoot(root.getElement());
		
		Queue<Tree.Node<E>> queue = new LinkedList<Tree.Node<E>>();
		queue.offer(root);
		
		Tree.Node<E> parentNode = null;
		Tree.Node<E> childNode	= null;
		Iterator<Tree.Node<E>> it = null;
		while(!queue.isEmpty())
		{
			parentNode = queue.poll();
			it = parentNode.children();
			
			while(it.hasNext())
			{
				childNode = it.next();
				copy.appendElement(parentNode.getElement(), childNode.getElement());
				queue.offer(childNode);
			}
		}
		
		return copy;
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
	 * @param parent
	 * @param children
	 */
	private final void joinParentChildren(AbstractNode<E> parent, 
											 Collection<AbstractNode<E>> children)
	{
		Iterator<AbstractNode<E>> it = children.iterator();
		AbstractNode<E> child = null;
		while(it.hasNext()) 
		{
			child = it.next();
			parent.children.add(child);
			child.parent = parent;
		}
	}
	
	/**
	 * 
	 * @param node
	 */
	private final void fastRecursiveRemove(AbstractNode<E> node)
	{
		size--;
		elementNodeMap.remove(node);
		for(AbstractNode<E> n : node.children)
			fastRecursiveRemove(n);
	}
	
	/**
	 * 
	 * @param s
	 * 
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream s)
	throws IOException
	{
		s.defaultWriteObject();
		
		Object values[] = new Object[size*2];
		
		// save first the root element
		values[0] = "root";
		values[1] = root.getElement();
		
		//save all other elements
		PreOrderTreeIterator<E> it = new PreOrderTreeIterator<E>(this, root);
	
		if(it.hasNext()) {
			it.next();
			
			int i = 2;
			E element;
			while(it.hasNext())
			{
				element = it.next();
				values[i++] = this.parentElement(element); 
				values[i++] = element;

			}
		}
		s.writeObject(values);
	}
	
	/**
	 * 
	 * @param s
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream s)
	throws IOException, ClassNotFoundException
	{
		s.defaultReadObject();
		
		Object values[] = (Object[]) s.readObject();
		if(values.length > 0) {
			elementNodeMap = new HashMap<E, SimpleNode<E>>();
			
			setRoot((E) values[1]);
			E parent, child = null;
			for(int i=2; i<values.length;) 
			{
				parent = (E) values[i++];
				child	= (E) values[i++];
				appendElement(parent, child);
			}
		}
	}
	
	/**
	 * 
	 * @author Marcel Hermkes
	 *
	 * @param <E>
	 */
	static class SimpleNode<E> extends AbstractNode<E>
	{
		/**
		 * 
		 * @param element
		 */
		SimpleNode(E element)
		{	
			this.element = element;
			children = new LinkedList<AbstractNode<E>>();
		}
		
		/**
		 * 
		 * @param parentNode
		 * @param element
		 */
		SimpleNode(SimpleNode<E> parentNode, E element) 
		{
			this(element);
			parent = parentNode;
			parent.children.add(this);
		}
	}
}

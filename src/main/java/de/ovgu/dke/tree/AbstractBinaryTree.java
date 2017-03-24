package de.ovgu.dke.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * This class provides a skeletal implementation of the <tt>BinaryTree</tt> interface 
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
public abstract class AbstractBinaryTree<E> extends AbstractTree<E>
implements BinaryTree<E>
{
	
	/**
	 * {@inheritDoc}
	 */
	public abstract boolean appendElement(Object parent, E element); 
	
	/**
	 * {@inheritDoc}
	 */
	public abstract Collection<Tree.Node<E>> nodes();
	
	/**
	 * {@inheritDoc}
	 */
	public abstract boolean removeElement(Object element);

	/**
	 * {@inheritDoc}
	 */
	public abstract boolean replaceElement(Object oldElement, E newElement);

	/**
	 * {@inheritDoc}
	 */
	public abstract void swapElements(E element1, E element2);
	
	/**
	 * {@inheritDoc}
	 */
	public abstract boolean appendLeftElement(E parent, E leftElement);
	
	/**
	 * {@inheritDoc}
	 */
	public abstract boolean appendRightElement(E parent, E rightElement);
	
	/**
	 * {@inheritDoc}
	 */
	public abstract boolean removeAboveElement(E parent);
	
	/**
	 * {@inheritDoc}
	 */
	public abstract boolean hasLeftElement(Object element);
	
	/**
	 * {@inheritDoc}
	 */
	public abstract boolean hasRightElement(Object element);
	
	/**
	 * {@inheritDoc}
	 */
	public abstract BinaryNode<E> getNodeByElement(Object element);
	
	/**
	 * This class provides a skeletal implementation of the <tt>BinaryNode</tt> interface 
	 * to minimize the effort required to implement this interface.<p>
	 *	 
	 * @author Marcel Hermkes
	 *
	 * @param <E>
	 */
	static abstract class AbstractBinaryNode<E> implements BinaryTree.BinaryNode<E>
	{
		E element;
			
		AbstractBinaryNode<E> parent;
		AbstractBinaryNode<E> leftChild;
		AbstractBinaryNode<E> rightChild;

		/**
	     * Sole constructor. (For invocation by subclass constructors, typically
	     * implicit.)
	     */
		protected AbstractBinaryNode () {}
		
		/**
		 * Returns an iterator over the left and right child node of this node in proper sequence.
		 * 
		 * @return an iterator over the child nodes.
		 */
		public Iterator<Tree.Node<E>> children() 
		{
			int numberOfChilds = 0;
			if(leftChild != null) numberOfChilds++;
			if(rightChild != null) numberOfChilds++;
			
			final int constNumber = numberOfChilds;
			
			/**
			 * 
			 */
			return new Iterator<Node<E>>() {
				int n = constNumber;
				
				public boolean hasNext() {
					return n != 0;
				}

				public Tree.Node<E> next() {
					if(n==2) { 
						n--; 
						return leftChild; 
					} else if(n==1) { 
						n--;
						if(rightChild == null) 
							return leftChild;
						return rightChild; 
					}
					throw new NoSuchElementException();
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
			return leftChild != null && rightChild != null;
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
			int n = 0;
			if(leftChild != null) n++;
			if(rightChild != null) n++;
			return n;
		}

		/**
		 * {@inheritDoc}
		 */
		public BinaryNode<E> parent() 
		{
			return parent;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public BinaryNode<E> getLeftChild()
		{
			return leftChild;
		}
		
		/**
		 * 
		 * {@inheritDoc}
		 */
		public BinaryNode<E> getRightChild()
		{
			return rightChild;
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

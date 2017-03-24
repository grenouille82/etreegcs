package de.ovgu.dke.tree;

import java.util.Iterator;

import de.ovgu.dke.tree.Tree.Node;


/**
 * This concrete iterator implements the preorder traversal of a tree.
 * <p>
 * In a preorder traversal of a given tree, the root of a tree is visited 
 * first and then rooted at its children are traversed recursively. If the
 * tree is ordered, then the subtrees are traversed according to the order
 * of the children.
 *  
 * @author Marcel Hermkes
 * @see TreeIterator
 * @see AbstractTreeIterator
 * @see Iterator
 */
public class PreOrderTreeIterator<E> 
extends AbstractTreeIterator<E, Tree<E>, Tree.Node<E>>
{
	
	/**
	 * Constructs a preorder iterator whose starting element is the root element
	 * of the specified tree. 
	 * 
	 * @param tree the tree to be traversed.
	 * 
	 * @throws NullPointerException if the tree is <tt>null</tt>.
	 * @throws EmptyTreeException if the specified tree is empty.
	 */
	public PreOrderTreeIterator(Tree<E> tree) 
	{
		super(tree);
	}
	
	/**
	 * Constructs a preorder iterator, whose starting element is the specified node. 
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
	public PreOrderTreeIterator(Tree<E> tree, Tree.Node<E> startNode)
	{
		super(tree, startNode);
	}

	/**
	 * Build the preorder traversal ordering. 
	 *  
	 * @param node the node which is appended to the node array.
	 * @param level the level of the specified node.
	 */
	protected final void buildTraverseOrder(Node<E> node, int level) 
	{
		nodes[cursor] 	= node;
		levels[cursor] 	= level;
		cursor++;
		lastIndex++;
		Iterator<Tree.Node<E>> it = node.children();
		while(it.hasNext())
			buildTraverseOrder(it.next(), level+1);
	}
}

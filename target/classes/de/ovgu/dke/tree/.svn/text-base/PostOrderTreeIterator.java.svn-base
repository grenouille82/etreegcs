package de.ovgu.dke.tree;

import java.util.Iterator;

import de.ovgu.dke.tree.Tree.Node;

/**
 * This concrete iterator implements the postorder traversal of a tree.
 * <p>
 * In a postorder traversal of a given tree can be viewed as the opposite of the 
 * preorder traversal, because it recursively traverses the subtrees rooted at
 * the children of root first and then visits the root.
 *  
 * @author Marcel Hermkes
 * @see TreeIterator
 * @see AbstractTreeIterator
 * @see Iterator
 */
public class PostOrderTreeIterator<E> 
extends AbstractTreeIterator<E, Tree<E>, Tree.Node<E>>
{
	/**
	 * Constructs a postorder iterator whose starting element is the root element
	 * of the specified tree. 
	 * 
	 * @param tree the tree to be traversed.
	 * 
	 * @throws NullPointerException if the tree is <tt>null</tt>.
	 * @throws EmptyTreeException if the specified tree is empty.
	 */
	public PostOrderTreeIterator(Tree<E> tree) 
	{
		super(tree);
	}
	
	/**
	 * Constructs a postorder iterator, whose starting element is the specified node. 
	 * The tree traversal includes the starting node and all his successor elements.
	 * elements.<p>
	 * It is essential that the tree contains the specified start node.
	 * 
	 * @param tree the tree to be traversed.
	 * @param startNode
	 *  
	 * @throws NullPointerException if the tree is <tt>null</tt>.
	 * @throws EmptyTreeException if the specified tree is empty.
	 * @throws IllegalArgumentException if the tree doesn't contain	the start node.
	 */
	public PostOrderTreeIterator(Tree<E> tree, Tree.Node<E> startNode) 
	{
		super(tree, startNode);
	}

	/**
	 * Build the postorder traversal ordering. 
	 *  
	 * @param node the node which is appended to the node array.
	 * @param level the level of the specified node.
	 */
	protected final void buildTraverseOrder(Node<E> node, int level) 
	{
		Iterator<Tree.Node<E>> it = node.children();
		while(it.hasNext())
			buildTraverseOrder(it.next(), level+1);
		nodes[cursor] 	= node;
		levels[cursor] 	= level;
		cursor++;
		lastIndex++;
	}

}

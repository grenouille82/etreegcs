package de.ovgu.dke.tree;



/**
 * This concrete iterator implements the inorder traversal of a binary tree.
 * This iterator is only applicable on binary trees.
 * <p>
 * In a inorder traversal of a given tree a node between the recursive traversals 
 * of its left and right subtrees.
 *  
 * @author Marcel Hermkes
 * @see TreeIterator
 * @see AbstractTreeIterator
 * @see Iterator
 */
public class InOrderTreeIterator<E>
extends AbstractTreeIterator<E, BinaryTree<E>, BinaryTree.BinaryNode<E>>
{

	/**
	 * Constructs a inorder iterator whose starting element is the root element
	 * of the specified tree. 
	 * 
	 * @param tree the tree to be traversed.
	 * 
	 * @throws NullPointerException if the tree is <tt>null</tt>.
	 * @throws EmptyTreeException if the specified tree is empty.
	 */
	public InOrderTreeIterator(BinaryTree<E> tree) 
	{
		super(tree);
	}
	
	/**
	 * Constructs a inorder iterator, whose starting element is the specified node. 
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
	public InOrderTreeIterator(BinaryTree<E> tree, BinaryTree.BinaryNode<E> startNode) 
	{
		super(tree);
	}


	/**
	 * Build the inorder traversal ordering. 
	 *  
	 * @param node the node which is appended to the node array.
	 * @param level the level of the specified node.
	 */
	protected void buildTraverseOrder(BinaryTree.BinaryNode<E> node, int level) 
	{
		if(node.getLeftChild() != null) 
			buildTraverseOrder(node.getLeftChild(), level+1);
		nodes[cursor] 	= node;
		levels[cursor] 	= level;
		cursor++;
		lastIndex++;
		if(node.getRightChild() != null) 
			buildTraverseOrder(node.getRightChild(), level+1);
		
	}
	
}

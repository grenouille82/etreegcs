package de.ovgu.dke.tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This concrete iterator implements the levelorder traversal of a tree.
 * <p>
 * In a levelorder traversal of a given tree, the root of a tree is visited 
 * first and then its children. 
 *  
 * @author Marcel Hermkes
 * 
 * @see TreeIterator
 * @see AbstractTreeIterator
 * @see Iterator
 */
public class LevelOrderTreeIterator<E> 
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
	public LevelOrderTreeIterator(Tree<E> tree) 
	{
		super(tree);
	}
	
	/**
	 * Constructs a levelorder iterator, whose starting element is the specified node. 
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
	public LevelOrderTreeIterator(Tree<E> tree, Tree.Node<E> startNode) 
	{
		super(tree, startNode);
	}

	/**
	 * Build the levelorder traversal ordering. 
	 *  
	 * @param node the node which is appended to the node array.
	 * @param level the level of the specified node.
	 */
	protected final void buildTraverseOrder(Tree.Node<E> root, int level) 
	{
		Queue<Tree.Node<E>> queue = new LinkedList<Tree.Node<E>>();
		queue.offer(root);
		
		int childCnt = 1;
		int subChildCnt = 0;
		
		while(!queue.isEmpty())
		{
			Tree.Node<E> node = queue.poll();
			nodes[cursor] 	= node;
			levels[cursor] 	= level;
			cursor++;
		
			Iterator<Tree.Node<E>> it = node.children();
			while(it.hasNext()) {
				subChildCnt++;
				queue.offer(it.next());
			}
			
			childCnt--;
			
			if(childCnt == 0) {
				childCnt = subChildCnt;
				subChildCnt = 0;
				level++;
			}
			
			
			lastIndex++;
		}
	}

}

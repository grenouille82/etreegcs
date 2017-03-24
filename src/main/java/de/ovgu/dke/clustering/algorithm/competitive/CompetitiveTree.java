package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.ovgu.dke.tree.LinkedTree;

public class CompetitiveTree implements CompetitiveDataStructure
{
	private LinkedTree<CompetitiveTreeNode> treeStructure;
	
	private List<CompetitiveTreeNode> leafNodes;

	public CompetitiveTree(CompetitiveTreeNode root)
	{
		treeStructure = new LinkedTree<CompetitiveTreeNode>(root);
	}
	
	public boolean appendNode(CompetitiveTreeNode parent, 
							  CompetitiveTreeNode node) 
	{
		return treeStructure.appendElement(parent, node);
	}

	public List<CompetitiveTreeNode> childNodes(CompetitiveTreeNode node) 
	{
		return treeStructure.childElements(node);
	}

	public void clear() 
	{
		treeStructure.clear();
	}

	public boolean containsNode(CompetitiveTreeNode node) 
	{
		return treeStructure.containsElement(node);
	}

	public int depth(CompetitiveTreeNode node) 
	{
		return treeStructure.depth(node);
	}

	public List<CompetitiveTreeNode> nodes() 
	{
		return new ArrayList<CompetitiveTreeNode>(treeStructure.elements());
	}

	public CompetitiveTreeNode commonAncestor(CompetitiveTreeNode node1, CompetitiveTreeNode node2) 
	{
		return treeStructure.getCommonAncestor(node1, node2);
	}

	public List<CompetitiveTreeNode> getPathFromRoot(CompetitiveTreeNode node) 
	{
		return treeStructure.getPathFromRoot(node);
	}
	
	public List<CompetitiveTreeNode> getPath(CompetitiveTreeNode from, 
											 CompetitiveTreeNode to) 
	{
		return treeStructure.getPath(from, to);
	}
	
	public List<CompetitiveTreeNode> siblings(CompetitiveTreeNode node)
	{
		return treeStructure.siblingElements(node);
	}
	
	

	public int getPathLength(CompetitiveTreeNode from, 
							 CompetitiveTreeNode to)
	{
		return treeStructure.getPathLength(from, to);
	}

	public CompetitiveTreeNode getRoot() 
	{
		return treeStructure.getRoot();
	}

	public int height(CompetitiveTreeNode node) 
	{
		return 0;
	}

	public boolean isAncestor(CompetitiveTreeNode ancestor, 
							  CompetitiveTreeNode node) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() 
	{
		return treeStructure.isEmpty();
	}

	public boolean isLeaf(CompetitiveTreeNode node) 
	{
		return treeStructure.isLeaf(node);
	}

	public boolean isRoot(CompetitiveTreeNode node) 
	{
		return treeStructure.isRoot(node);
	}

	public int numberOfChildren(CompetitiveTreeNode node) 
	{
		return treeStructure.numberOfChildren(node);
	}

	public int numberOfLeaves() 
	{
		return treeStructure.numberOfLeaves();
	}

	public CompetitiveTreeNode parentNode(CompetitiveTreeNode node) 
	{
		return treeStructure.parentElement(node);
	}

	public boolean removeNode(CompetitiveTreeNode node) 
	{
		return treeStructure.removeElement(node);
	}

	public LinkedTree<CompetitiveTreeNode> removeSubTree(CompetitiveTreeNode node) 
	{
		return treeStructure.removeSubTree(node);
	}
	
	public LinkedTree<CompetitiveTreeNode> getSubTree(CompetitiveTreeNode node)
	{
		return treeStructure.getSubTree(node);
	}
	
	public void insertNodeBetween(CompetitiveTreeNode parent, Collection<CompetitiveTreeNode> children, CompetitiveTreeNode node)
	{
		treeStructure.insertElementBetween(parent, children, node);
	}

	public boolean replaceNode(CompetitiveTreeNode oldNode,
							   CompetitiveTreeNode newNode) 
	{
		return treeStructure.replaceElement(oldNode, newNode);
	}

	public boolean setRoot(CompetitiveTreeNode node) 
	{
		return treeStructure.setRoot(node);
	}

	public int size()
	{
		return treeStructure.size();
	}

	public void swapElements(CompetitiveTreeNode node1, CompetitiveTreeNode node2) 
	{
		treeStructure.swapElements(node1, node2);
		
	}

	public Iterator<CompetitiveTreeNode> iterator() 
	{
		return treeStructure.iterator();
	}
	
	public int numberOfLeafs()
	{
		return treeStructure.numberOfLeaves();
	}
	
	public List<CompetitiveTreeNode> getLeafs()
	{
		return treeStructure.getLeafNodes();
	}

}

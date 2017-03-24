package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Collection;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.Settings;

public class SimpleInsertionStrategy implements TreeNodeInsertionStrategy 
{

	public void insert(Collection<CompetitiveTreeNode> insNodes, CompetitiveTreeNode parent, CompetitiveTree tree) 
	{
		if(insNodes == null)
			throw new NullPointerException();
		if(parent == null)
			throw new NullPointerException();
		if(tree == null)
			throw new NullPointerException();
		if(insNodes.isEmpty())
			throw new IllegalArgumentException("the number of inserted nodes must be greater than 0");
		
		if(insNodes.size() == 1) {
			CompetitiveTreeNode grand = tree.parentNode(parent);
			tree.removeNode(parent);
			CompetitiveTreeNode node = insNodes.iterator().next();
			tree.appendNode(grand, node);
		} else {
			for(CompetitiveTreeNode node : insNodes)
				tree.appendNode(parent, node);
		}
	}
	
	public void insert(Collection<CompetitiveTreeNode> insNodes, CompetitiveTreeNode parent, 
					   CompetitiveTree tree, DistanceMeasure metric) 
	{
		insert(insNodes, parent, tree);
	}

	public void setDistanceMeasure(DistanceMeasure metric) {}

	public void applyDefaultSettings() {}

	public void applySettings(Settings settings) {}

	public Settings getDefaultSettings() {	return null; }

	public Settings getSettings() {	return null; }
}

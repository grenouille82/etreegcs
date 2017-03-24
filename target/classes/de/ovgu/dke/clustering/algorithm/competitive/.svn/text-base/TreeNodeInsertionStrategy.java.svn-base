package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Collection;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.Parametrizable;

public interface TreeNodeInsertionStrategy extends Parametrizable
{
	public void setDistanceMeasure(DistanceMeasure metric);
	
	public void insert(Collection<CompetitiveTreeNode> insNodes, CompetitiveTreeNode parent, 
					   CompetitiveTree tree);
	
	public void insert(Collection<CompetitiveTreeNode> insNodes, CompetitiveTreeNode parent, 
					   CompetitiveTree tree, DistanceMeasure metric);
}

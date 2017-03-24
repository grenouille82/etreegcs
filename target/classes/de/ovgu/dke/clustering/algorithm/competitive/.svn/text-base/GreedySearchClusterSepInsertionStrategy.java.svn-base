package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.Settings;

public class GreedySearchClusterSepInsertionStrategy implements TreeNodeInsertionStrategy 
{
	private final static double DEFAULT_LOWER_BOUND = 0.75d;
	private final static double DEFAULT_UPPER_BOUND = 1.25d;

	private final static DistanceMeasure DEFAULT_METRIC = DistanceMeasure.EUCLIDEAN;
	
	private double lowerBound;
	private double upperBound;
	
	private DistanceMeasure metric;
	
	private KLevelUpClusterSepInsertionStrategy insertionStrategy;

	public GreedySearchClusterSepInsertionStrategy()
	{
		insertionStrategy = new KLevelUpClusterSepInsertionStrategy();
		insertionStrategy.setK(5);
		applyDefaultSettings();
	}
	
	public void insert(Collection<CompetitiveTreeNode> insNodes, CompetitiveTreeNode parent,
					   CompetitiveTree tree) 
	{
		insert(insNodes, parent, tree, metric);
	}
	
	//TODO: implement incremental mst for time efficience reason
	public void insert(Collection<CompetitiveTreeNode> insNodes, CompetitiveTreeNode parent, 
					   CompetitiveTree tree, DistanceMeasure metric) 
	{
		if(insNodes == null)
			throw new NullPointerException();
		if(parent == null)
			throw new NullPointerException();
		if(tree == null)
			throw new NullPointerException();
		if(metric == null)
			throw new NullPointerException();
		if(insNodes.isEmpty())
			throw new IllegalArgumentException("the number of inserted nodes must be greater than 0");
		
		if(insNodes.size() == 1) {
			CompetitiveTreeNode grand = tree.parentNode(parent);
			tree.removeNode(parent);
			CompetitiveTreeNode node = insNodes.iterator().next();
			tree.appendNode(grand, node);
		} else {
			if(tree.isRoot(parent)) {
				if(insNodes.size() == 2) {
					for(CompetitiveTreeNode node : insNodes)
						tree.appendNode(parent, node);
				} else {
					//TODO: compute the mst of the nodes to be inserted and check the cs criterion 
					//if this nodes should be splitted
					for(CompetitiveTreeNode node : insNodes)
						tree.appendNode(parent, node);
				}
			} else {
				
				CompetitiveTreeNode[] siblings  = tree.siblings(parent).toArray(new CompetitiveTreeNode[0]);
				CompetitiveTreeNode[] children  = tree.childNodes(tree.parentNode(parent)).toArray(new CompetitiveTreeNode[0]);
			//	if(siblings.length == 1) {
			//		insertionStrategy.insert(insNodes, parent, tree, metric);
			//	} else { 
					ArrayIndexedMST<CompetitiveTreeNode> mst = 
						CompetitiveUtil.computeMinimumSpanningTree(siblings, metric);
					//ArrayIndexedMST<CompetitiveTreeNode> mst = 
					//	CompetitiveUtil.computeMinimumSpanningTree(children, metric);
					double cs = computeClusterSeperation(parent, mst, metric);
					//double cs = mst.minDistance()/mst.maxDistance();
					System.out.println(parent + " levelCS " + cs);
					if(cs < lowerBound || cs > upperBound)
					{
						//TODO check the last element. what is to do with it?
						LinkedList<CompetitiveTreeNode> sortedNodes = sortByDistance(insNodes, tree.parentNode(parent), metric);
						for(CompetitiveTreeNode node : sortedNodes)
							System.out.println(node + "\t" + metric.getDistance(node.getWeightVector(),tree.parentNode(parent).getWeightVector()));
						while(!sortedNodes.isEmpty())
						{
							boolean mstUpdate = false;
							CompetitiveTreeNode candidate = sortedNodes.removeFirst();
							System.out.println("parentSC:" +parent.getSignalCounter());
							System.out.println("childSC:" +candidate.getSignalCounter());
							System.out.println("dist before: " + metric.getDistance(parent.getWeightVector(), candidate.getWeightVector()));
							updateMinusNode(parent, candidate);
							System.out.println("dist after: " + metric.getDistance(parent.getWeightVector(), candidate.getWeightVector()));
							mst = CompetitiveUtil.computeMinimumSpanningTree(children, metric);
							double candidateCS = computeClusterSeperation(candidate, mst, metric);
							System.out.println(candidate+" candidateCS: " + candidateCS);
							//System.out.println("siblings: " + siblings.length);
							if(candidateCS > upperBound)
								propagateUp(candidate, tree.parentNode(parent), tree, metric);
							else if(candidateCS < lowerBound) {
								CompetitiveTreeNode nearestNode = findNearestNode(candidate, tree.siblings(parent).toArray(new CompetitiveTreeNode[0]), metric);
								propagateDown(candidate, nearestNode, tree, metric);
								mstUpdate = true;
							} else {
								tree.appendNode(tree.parentNode(parent), candidate);
								//siblings = addNode(candidate, siblings);
								children = addNode(candidate, children);
								mstUpdate = true;
							}
							
							if(mstUpdate)
								//mst = CompetitiveUtil.computeMinimumSpanningTree(siblings, metric);
								mst = CompetitiveUtil.computeMinimumSpanningTree(children, metric);
							//cs = computeClusterSeperation(parent, mst, metric);
							cs = mst.minDistance()/mst.maxDistance();
							if(cs >= lowerBound && cs <= upperBound) {
								for(CompetitiveTreeNode n : sortedNodes)
									tree.appendNode(parent, n);
								break;
							}
						} 
						if(tree.numberOfChildren(parent) <= 1) 
							tree.removeNode(parent);
						
					} else {
						//insertionStrategy.insert(insNodes, parent, tree);
						
						Collection<CompetitiveTreeNode> tmpCandidates = tree.siblings(parent);
						LinkedList<CompetitiveTreeNode> candidates = new LinkedList<CompetitiveTreeNode>(tmpCandidates);
						candidates.addAll(insNodes);
						mst = CompetitiveUtil.computeMinimumSpanningTree(candidates.toArray(new CompetitiveTreeNode[0]), metric);
						System.out.println("mst cs: " +  mst.minDistance()/mst.maxDistance());
						if(mst.minDistance()/mst.maxDistance() > lowerBound) {
							CompetitiveTreeNode grand = tree.parentNode(parent);
							tree.removeNode(parent);
							for(CompetitiveTreeNode node : insNodes)
								tree.appendNode(grand, node);
							System.out.println("yes " + mst.minDistance() + "\t" + mst.maxDistance());
						} else
							for(CompetitiveTreeNode node : insNodes)
								tree.appendNode(parent, node);
						
					}
				
				}
			//}
		}
	}

	public void setDistanceMeasure(DistanceMeasure metric) 
	{
		if(metric == null)
			throw new NullPointerException();
		this.metric = metric;
	}

	public void applyDefaultSettings() 
	{
		metric = DEFAULT_METRIC;
		lowerBound = DEFAULT_LOWER_BOUND;
		upperBound = DEFAULT_UPPER_BOUND;
	}

	public void applySettings(Settings settings) {
		// TODO Auto-generated method stub
		
	}

	public Settings getDefaultSettings()
	{
		return null;
	}

	public Settings getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param candidate
	 * @param siblings
	 * @param metric
	 */
	private void propagateDown(CompetitiveTreeNode candidate, CompetitiveTreeNode parent, 
							   CompetitiveTree tree, DistanceMeasure metric) 
	{	
		if(tree.isLeaf(parent)) {
			CompetitiveTreeNode newParent = interpolateNode(candidate, parent);
			CompetitiveTreeNode grand = tree.parentNode(parent);
			tree.removeNode(parent);
			tree.appendNode(grand, newParent);
			tree.appendNode(newParent, candidate);
			tree.appendNode(newParent, parent);
		} else {
			updateAddNode(parent, candidate);
			CompetitiveTreeNode[] children = tree.childNodes(parent).toArray(new CompetitiveTreeNode[0]);
			ArrayIndexedMST<CompetitiveTreeNode> mst = 
				CompetitiveUtil.computeMinimumSpanningTree(children, metric);
			double cs = computeClusterSeperation(candidate, mst, metric);
			if(cs < lowerBound) {
				CompetitiveTreeNode nearestNode = findNearestNode(candidate, children, metric);
				propagateDown(candidate, nearestNode, tree, metric);
			} else if(cs > upperBound) {
				CompetitiveTreeNode newParent = interpolateNode(children);
				tree.insertNodeBetween(parent, Arrays.asList(children), newParent);
				tree.appendNode(parent, candidate);
			} else 
				//found the true insertion point
				tree.appendNode(parent, candidate);
		}
	}

	/**
	 * 
	 * @param candidate
	 * @param parent
	 * @param tree
	 * @param metric
	 */
	private void propagateUp(CompetitiveTreeNode candidate, CompetitiveTreeNode parent, CompetitiveTree tree, DistanceMeasure metric) 
	{
		if(tree.isRoot(parent)) {
			CompetitiveTreeNode[] children = tree.childNodes(parent).toArray(new CompetitiveTreeNode[0]);
			CompetitiveTreeNode intermediateNode = interpolateNode(children);
			tree.insertNodeBetween(parent, Arrays.asList(children), intermediateNode);
			tree.appendNode(parent, candidate);
		} else {
			updateMinusNode(parent, candidate);
			CompetitiveTreeNode grand = tree.parentNode(parent);
			CompetitiveTreeNode[] siblingCandidates = tree.childNodes(grand).toArray(new CompetitiveTreeNode[0]);
			ArrayIndexedMST<CompetitiveTreeNode> mst = 
				CompetitiveUtil.computeMinimumSpanningTree(siblingCandidates, metric);
			double cs = computeClusterSeperation(candidate, mst, metric);
			if(cs > upperBound) {
				propagateUp(candidate, grand, tree, metric);
			} else if(cs < lowerBound) {
				CompetitiveTreeNode intermediateNode = interpolateNode(siblingCandidates);
				tree.insertNodeBetween(grand, Arrays.asList(siblingCandidates), intermediateNode);
				tree.appendNode(grand, candidate);
			} else {
				tree.appendNode(grand, candidate);
			}
		}
		
	}

	/**
	 * 
	 * @param insNodes
	 * @param parent
	 * @param metric
	 * @return
	 */
	private LinkedList<CompetitiveTreeNode> sortByDistance(Collection<CompetitiveTreeNode> insNodes, 
														   CompetitiveTreeNode parent, DistanceMeasure metric) 
	{
		int n = insNodes.size();
		WeightedNode[] wNodes = new WeightedNode[n];
		int i=0;
		for(CompetitiveTreeNode candidate : insNodes)
		{
			double dist = metric.getDistance(parent.getWeightVector(), candidate.getWeightVector());
			wNodes[i++] = new WeightedNode(candidate, dist);
		}
		Arrays.sort(wNodes);
		LinkedList<CompetitiveTreeNode> retVal = new LinkedList<CompetitiveTreeNode>();
		for(int j=0; j<n; j++)
			retVal.add(wNodes[j].node);
		return retVal;
	}

	/**
	 *  
	 * @param node
	 * @param mst
	 * @param metric
	 * @return
	 */
	private final double minimumDistance(CompetitiveTreeNode node, ArrayIndexedMST<CompetitiveTreeNode> mst, 
								   		 DistanceMeasure metric) 
	{
		double minDist = Double.POSITIVE_INFINITY;
		double v[] = node.getWeightVector();
		for(int i=0, n=mst.size(); i<n; i++)
		{
			CompetitiveTreeNode candidate = mst.element(i);
			double dist = metric.getDistance(v, candidate.getWeightVector());
			if(dist<minDist) 
				minDist = dist;
		}
		return minDist;
	}
	
	private final CompetitiveTreeNode findNearestNode(CompetitiveTreeNode node, CompetitiveTreeNode[] array, 
													  DistanceMeasure metric)
	{
		CompetitiveTreeNode retVal = null;
		double minDist = Double.POSITIVE_INFINITY;
		double v[] = node.getWeightVector();
		for(int i=0, n=array.length; i<n; i++)
		{
			double dist = metric.getDistance(v, array[i].getWeightVector());
			if(dist<minDist) { 
				minDist = dist;
				retVal 	= array[i];
			}
		}
		return retVal;
	}

	private final double computeClusterSeperation(CompetitiveTreeNode node, ArrayIndexedMST<CompetitiveTreeNode> mst, 
											DistanceMeasure metric)
	{
		double avgDist = mst.avgDistance();
		double minDist = minimumDistance(node, mst, metric);
		return minDist/avgDist;
	}
	
	private final CompetitiveTreeNode[] addNode(CompetitiveTreeNode node, CompetitiveTreeNode[] array)
	{
		int n = array.length;
		CompetitiveTreeNode[] retVal = new CompetitiveTreeNode[n+1];
		System.arraycopy(array, 0, retVal, 0, n);
		retVal[n] = node;
		return retVal;
	}
	
	private final void updateAddNode(CompetitiveTreeNode a, CompetitiveTreeNode b)
	{
		double weightA = a.getSignalCounter();
		double weightB = b.getSignalCounter();
		a.setError(a.getError()+b.getError());
		a.setUtility(a.getUtility()+b.getUtility());
		a.setSignalCounter(weightA+weightB);
		double[] wvA = a.getWeightVector();
		double[] wvB = b.getWeightVector();
		for(int i=0, n=wvA.length; i<n; i++)
		{
			wvA[i] *= weightA;
			wvA[i] += weightB*wvB[i]; 
			wvA[i] /= a.getSignalCounter();
		}
	}
	
	private final void updateMinusNode(CompetitiveTreeNode a, CompetitiveTreeNode b)
	{
		double weightA = a.getSignalCounter();
		double weightB = b.getSignalCounter();
		a.setError(a.getError()-b.getError());
		a.setUtility(a.getUtility()-b.getUtility());
		if(weightA<weightB) {
			a.setSignalCounter(0d);
		} else
			a.setSignalCounter(weightA-weightB);
		double[] wvA = a.getWeightVector();
		double[] wvB = b.getWeightVector();
		for(int i=0, n=wvA.length; i<n; i++)
		{
			wvA[i] *= weightA;
			wvA[i] -= weightB*wvB[i]; 
			wvA[i] /= a.getSignalCounter();
		}
	}
	
	private final CompetitiveTreeNode interpolateNode(CompetitiveTreeNode... nodes)
	{
		double weightVec[] = null;
		double totalSC = 0d;
		double totalError = 0d;
		double totalUtility = 0d;
		double maxErrorThreshold = Double.NEGATIVE_INFINITY;
		double maxMvgAvgError = Double.NEGATIVE_INFINITY;
		for(int i=0, n=nodes.length; i<n; i++)
		{
			double sc = nodes[i].getSignalCounter();
			totalSC 	 += sc;
			totalError 	 += nodes[i].getError();
			totalUtility += nodes[i].getUtility();
			double eThreshold = nodes[i].getErrorThreshold();
			double mAvgError  = nodes[i].getMovingAvgError();
			
			if(eThreshold > maxErrorThreshold)
				maxErrorThreshold = eThreshold;
			if(mAvgError > maxMvgAvgError)
				maxMvgAvgError = mAvgError;
			
			double[] v = nodes[i].getWeightVector();
			if(weightVec == null)
				weightVec = new double[v.length];
			for(int j=0, m=v.length; j<m; j++)
				weightVec[j] += sc*v[j];
		}
		for(int i=0, n=weightVec.length; i<n; i++)
			weightVec[i] /= totalSC;
	//	System.out.println("interpolated WV: "  + Arrays.toString(weightVec));
		//TODO: id generation
		CompetitiveTreeNode retVal = new CompetitiveTreeNode(id_generator++, weightVec);
		System.out.println(retVal);
		retVal.setError(totalError);
		retVal.setErrorThreshold(maxErrorThreshold);
		retVal.setMovingAvgError(maxMvgAvgError);
		retVal.setUtility(totalUtility);
		retVal.setSignalCounter(totalSC);
		return retVal;
	}
	
	private int id_generator=200000;
	
	private static final class WeightedNode implements Comparable<WeightedNode>
	{
		double weight;
		CompetitiveTreeNode node;
		
		public WeightedNode(CompetitiveTreeNode node, double weight) 
		{
			this.node	= node;
			this.weight = weight;
		}

		public int compareTo(WeightedNode o) 
		{
			if(this.weight < o.weight)
				return 1;
			else if(this.weight > o.weight)
				return -1;
			else
				return 0;
		}
		
		
	}
}

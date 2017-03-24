package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.Settings;

public class IncrementalClusterSepInsStrategy 
implements TreeNodeInsertionStrategy
{
	private final static double DEFAULT_INSERTION_THRESHOLD = 0.8d;

	private final static DistanceMeasure DEFAULT_METRIC = DistanceMeasure.COSINE;
	
	private double insertionThreshold;
	
	private DistanceMeasure metric;

	public IncrementalClusterSepInsStrategy()
	{
		applyDefaultSettings();
	}
	
	public void insert(Collection<CompetitiveTreeNode> insNodes, CompetitiveTreeNode parent,
					   CompetitiveTree tree) 
	{
		insert(insNodes, parent, tree, metric);
	}
	
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
				for(CompetitiveTreeNode node : insNodes)
					tree.appendNode(parent, node);
			} else {
				List<CompetitiveTreeNode> testNodes = tree.siblings(parent);
				testNodes.addAll(insNodes);
				ArrayIndexedMST<CompetitiveTreeNode> mst = 
					CompetitiveUtil.computeMinimumSpanningTree(testNodes.toArray(new CompetitiveTreeNode[0]), metric);
				
				double cs = mst.minDistance()/mst.maxDistance();
				if(cs >= insertionThreshold) {
					CompetitiveTreeNode grand = tree.parentNode(parent);
					tree.removeNode(parent);
					for(CompetitiveTreeNode node : insNodes)
						tree.appendNode(grand, node);
				} else {
					
					testNodes = tree.siblings(parent);
					testNodes.add(parent);
					ArrayIndexedMST<CompetitiveTreeNode> siblingMST =
						CompetitiveUtil.computeMinimumSpanningTree(testNodes.toArray(new CompetitiveTreeNode[0]), metric);
					ArrayIndexedMST<CompetitiveTreeNode> insMST =
						CompetitiveUtil.computeMinimumSpanningTree(insNodes.toArray(new CompetitiveTreeNode[0]), metric);
					
					double minDist = siblingMST.minDistance();
					double maxDist = insMST.maxDistance();
					if(maxDist > minDist) {
						System.out.println("max " +maxDist+ " min " + minDist);
						int maxIdx = insMST.maxDistancedElementIndex();
						List<ArrayIndexedMST<CompetitiveTreeNode>> cutMSTs = insMST.cutAt(maxIdx);
						HashMap<CompetitiveTreeNode, ArrayIndexedMST<CompetitiveTreeNode>> interpolatedMSTNodes = 
							new HashMap<CompetitiveTreeNode, ArrayIndexedMST<CompetitiveTreeNode>>();
						for(ArrayIndexedMST<CompetitiveTreeNode> tmpMST : cutMSTs)
						{
							System.out.println("cuts");
							System.out.println(tmpMST);
							CompetitiveTreeNode node = null;
							if(tmpMST.size() == 1)
								node = tmpMST.element(0);
							else {
								List<CompetitiveTreeNode> mstNodes = convertMSTToList(tmpMST);
								System.out.println(mstNodes);
								node = interpolateNode(mstNodes.toArray(new CompetitiveTreeNode[0]));
							}
							interpolatedMSTNodes.put(node, tmpMST);	
						}

						maxDist = Double.NEGATIVE_INFINITY;
						CompetitiveTreeNode maxDistNode = null;
						Collection<CompetitiveTreeNode> siblings = tree.siblings(parent);
						for(CompetitiveTreeNode node : interpolatedMSTNodes.keySet())
						{
							double dist = minimumDistance(node, siblings, metric);
							if(dist > maxDist) {
								maxDist = dist;
								maxDistNode = node;
							}
						}
						List<CompetitiveTreeNode> propUpCand = convertMSTToList(interpolatedMSTNodes.get(maxDistNode));
						updateMinusNode(parent, maxDistNode);
						System.out.println("maxdist " +  maxDist);
						System.out.println(interpolatedMSTNodes.get(maxDistNode));
						System.out.println(siblingMST);
						if(maxDist > minDist)  {
							propagateUp(maxDistNode, propUpCand, parent, tree, metric);
						} else {
							CompetitiveTreeNode nearestNode = findNearestNode(maxDistNode, testNodes.toArray(new CompetitiveTreeNode[0]), metric);
							if(nearestNode.equals(parent))
								tree.appendNode(parent, maxDistNode);
							else {
								CompetitiveTreeNode intermediateNode = interpolateNode(nearestNode, maxDistNode);
								LinkedList<CompetitiveTreeNode> tmpChild = new LinkedList<CompetitiveTreeNode>();
								tmpChild.add(nearestNode);
								tmpChild.add(maxDistNode);
								tree.insertNodeBetween(tree.parentNode(parent), tmpChild, intermediateNode);
								if(propUpCand.size() > 0)
									for(CompetitiveTreeNode cand : propUpCand)
										tree.appendNode(maxDistNode, cand);
								
							}
							
						}
						LinkedList<CompetitiveTreeNode> tmpList = new LinkedList<CompetitiveTreeNode>(insNodes);
						tmpList.removeAll(propUpCand);
						if(tmpList.size() != 0)
							insert(tmpList, parent, tree, metric);
						
					} else {
						for(CompetitiveTreeNode node : insNodes)
							tree.appendNode(parent, node);
					}
					
					
				}
			}
		}
	}
	
	//TODO: simplify
	private final void propagateUp(CompetitiveTreeNode node, Collection<CompetitiveTreeNode> candidates, CompetitiveTreeNode parent, CompetitiveTree tree, DistanceMeasure metric)
	{
		System.out.println("propUp");
		CompetitiveTreeNode grand = tree.parentNode(parent);
		List<CompetitiveTreeNode> children = tree.childNodes(grand);
		ArrayIndexedMST<CompetitiveTreeNode> mst = 
			CompetitiveUtil.computeMinimumSpanningTree(children.toArray(new CompetitiveTreeNode[0]), metric);
		double minDist = mst.minDistance();
		double maxDist = mst.maxDistance();
		if(tree.isRoot(grand)) {
	
			double tmpMinDist = minimumDistance(node, children, metric);
			if(minDist>tmpMinDist)
				minDist = tmpMinDist;
			if(maxDist<tmpMinDist)
				maxDist = tmpMinDist;
			if(minDist/maxDist < insertionThreshold) {
				CompetitiveTreeNode intermedNode = interpolateNode(children.toArray(new CompetitiveTreeNode[0]));
				tree.insertNodeBetween(grand, children, intermedNode);
			}
			tree.appendNode(grand, node);
			for(CompetitiveTreeNode cand : candidates)
				tree.appendNode(node, cand);
		} else {
			List<CompetitiveTreeNode> testNodes = tree.siblings(grand);
			testNodes.add(grand);
			ArrayIndexedMST<CompetitiveTreeNode> testMST = 
				CompetitiveUtil.computeMinimumSpanningTree(testNodes.toArray(new CompetitiveTreeNode[0]), metric);
			double tmpMinDist = minimumDistance(node, mst, metric);
			System.out.println("testMST " + testMST);
			System.out.println("mst " + mst);
			System.out.println("minDist " + tmpMinDist);
			if(tmpMinDist > testMST.minDistance()) {
				updateMinusNode(grand, node);
				propagateUp(node, candidates, grand, tree, metric);
			}
			else {
				if(minDist>tmpMinDist)
					minDist = tmpMinDist;
				if(maxDist<tmpMinDist)
					maxDist = tmpMinDist;
				if(minDist/maxDist < insertionThreshold) {
					CompetitiveTreeNode intermedNode = interpolateNode(children.toArray(new CompetitiveTreeNode[0]));
					tree.insertNodeBetween(grand, children, intermedNode);
				}
				tree.appendNode(grand, node);
				for(CompetitiveTreeNode cand : candidates)
					tree.appendNode(node, cand);
			}
			
		}

		CompetitiveUtil.printCompetitiveTree(tree);
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
		insertionThreshold = DEFAULT_INSERTION_THRESHOLD;
		
	}

	public void applySettings(Settings settings) {
		// TODO Auto-generated method stub
		
	}

	public Settings getDefaultSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	public Settings getSettings() {
		// TODO Auto-generated method stub
		return null;
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
			if(a.getSignalCounter() != 0d)
				wvA[i] /= a.getSignalCounter();
			else
				wvA[i] = 0d;
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
	
	private final List<CompetitiveTreeNode> convertMSTToList(ArrayIndexedMST<CompetitiveTreeNode> mst)
	{
		List<CompetitiveTreeNode> retVal = new LinkedList<CompetitiveTreeNode>();
		for(int i=0; i<mst.size(); i++)
			retVal.add(mst.element(i));
		return retVal;
	}
	
	
	private final Set<List<CompetitiveTreeNode>> findOptimalCutSet(ArrayIndexedMST<CompetitiveTreeNode> mst)
	{
		System.out.println(mst);
		Set<List<CompetitiveTreeNode>> retVal = new HashSet<List<CompetitiveTreeNode>>();
		
		LinkedList<ArrayIndexedMST<CompetitiveTreeNode>> candidateMST = new LinkedList<ArrayIndexedMST<CompetitiveTreeNode>>();
		candidateMST.add(mst);
		while(!candidateMST.isEmpty())
		{
			ArrayIndexedMST<CompetitiveTreeNode> cand = candidateMST.removeFirst();
			System.out.println(cand);
			if(cand.size() < 2)
				retVal.add(convertMSTToList(cand)); 
			else {
				double cs = cand.minDistance()/cand.maxDistance();
				if( cs >= insertionThreshold) {
					retVal.add(convertMSTToList(cand));
				} else {
					int maxIdx = cand.maxDistancedElementIndex();
					System.out.println("cutIdx " + maxIdx);
					candidateMST.addAll(cand.cutAt(maxIdx));
				}
			}
		}
		return retVal;
	}
	
	private int id_generator=400000;
	
	

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
	
	private final double maximumDistance(CompetitiveTreeNode node, Collection<CompetitiveTreeNode> coll, DistanceMeasure metric) 
	{
		double maxDist = Double.NEGATIVE_INFINITY;
		double v[] = node.getWeightVector();
		for(CompetitiveTreeNode candidate : coll)
		{
			double dist = metric.getDistance(v, candidate.getWeightVector());
			if(dist>maxDist) 
				maxDist = dist;
		}
		return maxDist;
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

	private final CompetitiveTreeNode findNearestNode(CompetitiveTreeNode node, CompetitiveTreeNode[] array, 
			  DistanceMeasure metric, CompetitiveTreeNode exclNode)
	{
		CompetitiveTreeNode retVal = null;
		double minDist = Double.POSITIVE_INFINITY;
		double v[] = node.getWeightVector();
		for(int i=0, n=array.length; i<n; i++)
		{
			if(!array[i].equals(exclNode)) {
				double dist = metric.getDistance(v, array[i].getWeightVector());
				if(dist<minDist) { 
						minDist = dist;
						retVal 	= array[i];
				}
			}
		}
		return retVal;
	}
		

	private final double minimumDistance(CompetitiveTreeNode node, Collection<CompetitiveTreeNode> coll, 
										 DistanceMeasure metric)
	{
		double minDist = Double.POSITIVE_INFINITY;
		double v[] = node.getWeightVector();
		for(CompetitiveTreeNode candidate : coll)
		{
			double dist = metric.getDistance(v, candidate.getWeightVector());
			if(dist<minDist) { 
				minDist = dist;
			}
		}
		return minDist;
	}

	

	private final double computeClusterSeperation(CompetitiveTreeNode node, ArrayIndexedMST<CompetitiveTreeNode> mst, 
											DistanceMeasure metric)
	{
		double avgDist = mst.avgDistance();
		double minDist = minimumDistance(node, mst, metric);
		return minDist/avgDist;
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

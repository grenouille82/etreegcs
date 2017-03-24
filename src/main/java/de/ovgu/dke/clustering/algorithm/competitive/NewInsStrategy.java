package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.MathUtil;
import de.ovgu.dke.util.Settings;

public class NewInsStrategy implements TreeNodeInsertionStrategy
{
	private final static double DEFAULT_INSERTION_THRESHOLD = 0.8d;

	private final static DistanceMeasure DEFAULT_METRIC = DistanceMeasure.EUCLIDEAN;
	
	private double insertionThreshold;
	private double upperBound;
	
	private int k=5;
	
	private DistanceMeasure metric;

	public NewInsStrategy ()
	{
		applyDefaultSettings();
	}
	
	public void insert(Collection<CompetitiveTreeNode> insNodes, CompetitiveTreeNode parent,
					   CompetitiveTree tree) 
	{
		insert(insNodes, parent, tree, metric);
	}
	
	public void insert(Collection<CompetitiveTreeNode> insNodes, final CompetitiveTreeNode parent, 
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
			CompetitiveTreeNode node = insNodes.iterator().next();
			tree.appendNode(parent, node);
			tree.removeNode(parent);
		} else {
			if(tree.isRoot(parent)) {
				ArrayIndexedMST<CompetitiveTreeNode> mst = CompetitiveUtil.computeMinimumSpanningTree(insNodes.toArray(new CompetitiveTreeNode[0]), metric);
				Set<List<CompetitiveTreeNode>> cutSet = findOptimalCutSet(mst);
				if(cutSet.size() == 1) {
					for(CompetitiveTreeNode node : insNodes)
						tree.appendNode(parent, node);
				} else {
					for(List<CompetitiveTreeNode> cut : cutSet)
					{
						CompetitiveTreeNode newParent = parent;
						if(cut.size() > 1) {
							newParent = interpolateNode(cut.toArray(new CompetitiveTreeNode[0]));
							tree.appendNode(parent, newParent);
						}
						for(CompetitiveTreeNode node : cut)
							tree.appendNode(newParent, node);
					}
				}
			} else {
			
				List<CompetitiveTreeNode> testNodes = tree.siblings(parent);
				testNodes.addAll(insNodes);
				ArrayIndexedMST<CompetitiveTreeNode> mst = 
					CompetitiveUtil.computeMinimumSpanningTree((CompetitiveTreeNode[]) testNodes.toArray(new CompetitiveTreeNode[0]), metric);
				
				double cs = mst.minDistance()/mst.maxDistance();
				System.out.println("sep="+cs +"\t"+ insNodes.size());
				System.out.println("parent " + parent);
				if(cs >= insertionThreshold) {
					CompetitiveTreeNode grand = tree.parentNode(parent);
					tree.removeNode(parent);
					for(CompetitiveTreeNode node : insNodes)
						tree.appendNode(grand, node);
				} else {
					Set<List<CompetitiveTreeNode>> cutSet = findOptimalCutSet(mst);
					Set<Collection<CompetitiveTreeNode>> parentCutSet = new HashSet<Collection<CompetitiveTreeNode>>();
					if(cutSet.size() == 1)
						throw new IllegalStateException("couldnt be");
					
					
					LinkedList<CompetitiveTreeNode> testCandidates = new LinkedList<CompetitiveTreeNode>(tree.siblings(parent));
					ArrayIndexedMST<CompetitiveTreeNode> testMST = 
						CompetitiveUtil.computeMinimumSpanningTree(testCandidates.toArray(new CompetitiveTreeNode[0]), metric);
					for(List<CompetitiveTreeNode> cut : cutSet)
					{
						System.out.println("the cut " + cut.toString());
						CompetitiveUtil.printCompetitiveTree(tree);
						Set<CompetitiveTreeNode> intersectionSet = MathUtil.intersection(cut, insNodes);
						Set<CompetitiveTreeNode> differenceSet = MathUtil.difference(cut, insNodes);
						if(intersectionSet.size() != 0) {
							System.out.println("lalallal");
							//create an intermediate node of the difference set
							if(differenceSet.size() > 0) {
								for(CompetitiveTreeNode candidate : intersectionSet)
									updateMinusNode(parent, candidate);
								CompetitiveTreeNode intermediateNode = interpolateNode(cut.toArray(new CompetitiveTreeNode[0]));
								tree.insertNodeBetween(tree.parentNode(parent), cut, intermediateNode);
								testCandidates.removeAll(differenceSet);
								testCandidates.add(intermediateNode);
								testMST = CompetitiveUtil.computeMinimumSpanningTree(testCandidates.toArray(new CompetitiveTreeNode[0]), metric);
								
							} else {
								System.out.println(testMST);
								CompetitiveTreeNode grand = tree.parentNode(parent);
								if(tree.isRoot(grand))
									parentCutSet.add(intersectionSet); 
								else {
									CompetitiveTreeNode intermediateNode = interpolateNode(intersectionSet.toArray(new CompetitiveTreeNode[0]));
									updateMinusNode(grand, intermediateNode);
									tree.siblings(grand);
									LinkedList<CompetitiveTreeNode> testNodes1 = new LinkedList<CompetitiveTreeNode>();
									testNodes1.add(grand);
									testMST = CompetitiveUtil.computeMinimumSpanningTree(testNodes.toArray(new CompetitiveTreeNode[0]), metric);
									double minDist = minimumDistance(intermediateNode, testMST, metric);
									//propagate up
									if(testMST.maxDistance()/minDist < insertionThreshold) {
										updateMinusNode(parent, intermediateNode);
										propagateUp(intersectionSet, intermediateNode, tree.parentNode(grand), tree, metric);
									} else {
										updateAddNode(grand, intermediateNode);
										parentCutSet.add(intersectionSet);
									}
								}
							}
							
							insNodes.removeAll(intersectionSet);
							if(insNodes.isEmpty())
								break;
						}
							
						
					}
					
				
					
					if(parentCutSet.isEmpty()) {
						CompetitiveTreeNode grand = tree.parentNode(parent);
						tree.removeNode(parent);
						if(tree.numberOfChildren(grand) == 1)
							tree.removeNode(grand);
					} else {
						if(parentCutSet.size() == 1) {
							for(CompetitiveTreeNode node : parentCutSet.iterator().next())
								tree.appendNode(parent, node);
						} else {
							for(Collection<CompetitiveTreeNode> cut : parentCutSet)
							{
								CompetitiveTreeNode newParent = parent;
								if(cut.size() > 1) {
									newParent = interpolateNode(cut.toArray(new CompetitiveTreeNode[0]));
									tree.appendNode(parent, newParent);
								}
								for(CompetitiveTreeNode node : cut)
									tree.appendNode(newParent, node);
							}
						}
						
						if(tree.numberOfChildren(parent) == 1)
							tree.removeNode(parent);
						
					}
				}
			}
		}
	}
	
	public void setK(int k)
	{
		this.k = k;
	}
	
	private final void propagateUp(Collection<CompetitiveTreeNode> candidates, CompetitiveTreeNode intermediateNode, CompetitiveTreeNode node, CompetitiveTree tree, DistanceMeasure metric)
	{
		System.out.println("propUp");

		if(tree.isRoot(node)) {
			List<CompetitiveTreeNode> children = tree.childNodes(node);
			List<CompetitiveTreeNode> testCandidates = new LinkedList<CompetitiveTreeNode>(children);
			testCandidates.add(intermediateNode);
			ArrayIndexedMST<CompetitiveTreeNode> childMST = 
				CompetitiveUtil.computeMinimumSpanningTree(testCandidates.toArray(new CompetitiveTreeNode[0]), metric);
			double cs = childMST.minDistance()/childMST.maxDistance();
			if(cs < insertionThreshold) {
				CompetitiveTreeNode tmpIntermediate = interpolateNode(children.toArray(new CompetitiveTreeNode[0]));
				tree.insertNodeBetween(node, children, tmpIntermediate);
				
			}
			
			if(candidates.size() == 1) {
				tree.appendNode(node, candidates.iterator().next());
			} else {
				tree.insertNodeBetween(node, candidates, intermediateNode);
			}
		} else {
			updateMinusNode(node, intermediateNode);
			List<CompetitiveTreeNode> siblings = tree.siblings(node);
			List<CompetitiveTreeNode> mstCandidates = new LinkedList<CompetitiveTreeNode>(siblings);
			mstCandidates.add(node);
			mstCandidates.addAll(candidates);
			ArrayIndexedMST<CompetitiveTreeNode> mst = 
				CompetitiveUtil.computeMinimumSpanningTree(mstCandidates.toArray(new CompetitiveTreeNode[0]), metric);
			double minDist = minimumDistance(intermediateNode, mst, metric);
			
			if(mst.maxDistance()/minDist < insertionThreshold) {
				propagateUp(candidates, intermediateNode, tree.parentNode(node), tree, metric);
			} else {
				double maxDist = minDist;
				if(minDist > mst.minDistance())
					minDist = mst.minDistance();
				if(maxDist < mst.maxDistance())
					maxDist = mst.maxDistance();
				double cs = minDist/maxDist;
				if(cs < insertionThreshold) {
					CompetitiveTreeNode tmpIntermediate = interpolateNode(mstCandidates.toArray(new CompetitiveTreeNode[0]));
					tree.insertNodeBetween(node, mstCandidates, tmpIntermediate);
				}
				if(candidates.size() == 1) {
					tree.appendNode(node, candidates.iterator().next());
				} else {
					tree.insertNodeBetween(node, candidates, intermediateNode);
				}
			}
		}
		

	}
	
	private final void propagateUp(Collection<CompetitiveTreeNode> candidates, CompetitiveTreeNode node, CompetitiveTree tree, DistanceMeasure metric)
	{
		System.out.println("propUp");

		if(tree.isRoot(node)) {
			List<CompetitiveTreeNode> children = tree.childNodes(node);
			CompetitiveTreeNode intermediateNode = interpolateNode(children.toArray(new CompetitiveTreeNode[0]));
			tree.insertNodeBetween(node, children, intermediateNode);
			if(candidates.size() == 1) {
				tree.appendNode(node, candidates.iterator().next());
			} else {
				intermediateNode = interpolateNode(candidates.toArray(new CompetitiveTreeNode[0]));;
				tree.insertNodeBetween(node, candidates, intermediateNode);
			}
		} else {
			for(CompetitiveTreeNode candidate : candidates)
				updateMinusNode(node, candidate);
			CompetitiveTreeNode parent = tree.parentNode(node);
			List<CompetitiveTreeNode> children = tree.childNodes(parent);
			List<CompetitiveTreeNode> mstCandidates = new LinkedList<CompetitiveTreeNode>(children);
			mstCandidates.addAll(candidates);
			ArrayIndexedMST<CompetitiveTreeNode> mst = 
				CompetitiveUtil.computeMinimumSpanningTree(mstCandidates.toArray(new CompetitiveTreeNode[0]), metric);

			double cs = mst.minDistance()/mst.maxDistance();
			if(cs >= insertionThreshold) {
				for(CompetitiveTreeNode candidate : candidates)
					tree.appendNode(parent, candidate);
			} else {
				
				Set<List<CompetitiveTreeNode>> cutSet = findOptimalCutSet(mst);
				if(cutSet.size() == 1)
					throw new IllegalStateException("couldnt be");
				
			
				for(List<CompetitiveTreeNode> cut : cutSet)
				{
					Set<CompetitiveTreeNode> intersectionSet = MathUtil.intersection(cut, candidates);
					Set<CompetitiveTreeNode> differenceSet = MathUtil.difference(cut, candidates);
					if(intersectionSet.size() != 0) {
						ArrayIndexedMST<CompetitiveTreeNode> childMST = 
							CompetitiveUtil.computeMinimumSpanningTree(children.toArray(new CompetitiveTreeNode[0]), metric);
						//create an intermediate node of the difference set
						if(differenceSet.size() > 0) {
							CompetitiveTreeNode intermediateNode = interpolateNode(cut.toArray(new CompetitiveTreeNode[0]));
							tree.insertNodeBetween(parent, cut, intermediateNode);
						} else {
							CompetitiveTreeNode intermediateNode = interpolateNode(intersectionSet.toArray(new CompetitiveTreeNode[0]));
							double minDist = minimumDistance(intermediateNode, childMST, metric);
						
							//propagate up
							if(childMST.maxDistance()/minDist < insertionThreshold) {
								propagateUp(intersectionSet, parent, tree, metric);
							} else {
								for(CompetitiveTreeNode candidate : intersectionSet)
									tree.appendNode(parent, candidate);
								
							}
						}
						
						candidates.removeAll(intersectionSet);
						if(candidates.isEmpty())
							break;
					}
				}

			
			
			}
		}
		

		
	
		
		
		
		
	}
	
	private final void checkAndRepairPath(Collection<CompetitiveTreeNode> path, CompetitiveTree tree)
	{
		for(CompetitiveTreeNode node : path)
		{
			if(!tree.isRoot(node)) {
				List<CompetitiveTreeNode> candidates = tree.childNodes(tree.parentNode(node));
				ArrayIndexedMST<CompetitiveTreeNode> mst = 
					CompetitiveUtil.computeMinimumSpanningTree(candidates.toArray(new CompetitiveTreeNode[0]), metric);
				System.out.println(mst.minDistance()/mst.maxDistance() + " jahduahds");
				System.out.println(candidates);
				System.out.println(mst);
				if(mst.minDistance()/mst.maxDistance() < this.insertionThreshold)
				{
					List<CompetitiveTreeNode> siblings = tree.siblings(node);
					if(siblings.size() > 1) {
						CompetitiveTreeNode interNode = interpolateNode(siblings.toArray(new CompetitiveTreeNode[0]));
						CompetitiveTreeNode grand = tree.parentNode(node);
						tree.insertNodeBetween(grand,siblings, interNode);
					}
				}
				
			}
		}
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
	
	private final List<CompetitiveTreeNode> convertMSTToList(ArrayIndexedMST<CompetitiveTreeNode> mst)
	{
		List<CompetitiveTreeNode> retVal = new LinkedList<CompetitiveTreeNode>();
		for(int i=0; i<mst.size(); i++)
			retVal.add(mst.element(i));
		return retVal;
	}

	private final void kLevelUpInsertion(Collection<CompetitiveTreeNode> insNodes, CompetitiveTreeNode parent, 
					   CompetitiveTree tree, DistanceMeasure metric)
	{
		System.out.println("insert " + Arrays.toString(parent.getWeightVector()));
		CompetitiveUtil.printCompetitiveTree(tree);
		if(!insNodes.isEmpty()) {
			if(insNodes.size() > 2) {
				ArrayIndexedMST<CompetitiveTreeNode> mst = 
					CompetitiveUtil.computeMinimumSpanningTree(insNodes.toArray(new CompetitiveTreeNode[0]), metric);
				if(mst.minDistance()/mst.maxDistance() > insertionThreshold) {
					CompetitiveTreeNode tmpParent = parent;
					if(tree.numberOfChildren(parent) > 0) {
						tmpParent = interpolateNode(insNodes.toArray(new CompetitiveTreeNode[0]));
					}
					for(CompetitiveTreeNode node : insNodes)
						tree.appendNode(tmpParent, node);
					
					return;
				}
			}
			LinkedList<CompetitiveTreeNode> sortedNodes = sortByDistance(insNodes, tree.parentNode(parent), metric);
			CompetitiveTreeNode candidate = sortedNodes.pollFirst();
			updateMinusNode(parent, candidate);
			double distThreshold = maximumDistance(candidate, sortedNodes, metric);
				
			//k-level up
			CompetitiveTreeNode newParent = parent;
			for(int i=0; i<k; i++) {
				newParent = tree.parentNode(newParent);
				if(tree.isRoot(newParent))
					break;
			}
			
			CompetitiveTreeNode insLeaf = findNearestNode(candidate, tree.getSubTree(newParent).getLeafNodes().toArray(new CompetitiveTreeNode[0]), metric, parent);
			System.out.println("cond: " +distThreshold + "\t" + metric.getDistance(insLeaf.getWeightVector(), candidate.getWeightVector()));
			if(sortedNodes.isEmpty() || distThreshold > metric.getDistance(insLeaf.getWeightVector(), candidate.getWeightVector())) {
				List<CompetitiveTreeNode> insPath = tree.getPath(insLeaf, newParent);
				System.out.println("insLeaf " + insLeaf);
				System.out.println("insPath " + insPath);
				if(insPath.contains(parent)) {
					if(insLeaf.equals(parent)) {
						updateAddNode(parent, candidate);
						tree.appendNode(parent, candidate);
					} else {
						insPath = tree.getPath(insLeaf, parent);
						System.out.println("insPath " + insPath);
						CompetitiveTreeNode insPoint = findBestInsertionPoint(candidate, insLeaf, insPath, tree, metric);
						List<CompetitiveTreeNode> updatePath = tree.getPath(insPoint, parent);
						System.out.println("upPath " + updatePath);
						updateAddPath(updatePath, candidate);
					}
				} else {
					System.out.println("fafafaf");
					CompetitiveTreeNode insPoint = findBestInsertionPoint(candidate, insLeaf, insPath, tree, metric);
					//TODO: new parent is update twice
					List<CompetitiveTreeNode> updatePath = tree.getPath(tree.parentNode(parent), newParent);
					updateMinusPath(updatePath, candidate);
					updatePath = tree.getPath(insPoint, newParent);
					updateAddPath(updatePath, candidate);
				}
			} else {
				updateAddNode(parent, candidate);
				tree.appendNode(parent, candidate);
			}
				
			//recursive call
			kLevelUpInsertion(sortedNodes, parent, tree, metric);
		} else {
			Collection<CompetitiveTreeNode> children = tree.childNodes(parent);
			if(children.size() < 2) {
				CompetitiveTreeNode grand = tree.parentNode(parent);
				for(CompetitiveTreeNode child : children)
					tree.appendNode(grand, child);
				tree.removeNode(parent);
				
				if(tree.numberOfChildren(grand) == 1) {
					tree.removeNode(grand);
				}
			}
		}
	}
	
	private final CompetitiveTreeNode findBestInsertionPoint(CompetitiveTreeNode node, CompetitiveTreeNode leaf, List<CompetitiveTreeNode> path, 
															 CompetitiveTree tree, DistanceMeasure metric)
	{
		CompetitiveTreeNode retVal = null;
		double maxCSValue = Double.NEGATIVE_INFINITY;
		for(CompetitiveTreeNode candidate : path)
		{
			CompetitiveTreeNode parent = tree.parentNode(candidate);
			if(parent != null) {
				List<CompetitiveTreeNode> testNodes = tree.childNodes(parent);
				testNodes.add(node);
				ArrayIndexedMST<CompetitiveTreeNode> mst = 
					CompetitiveUtil.computeMinimumSpanningTree(testNodes.toArray(new CompetitiveTreeNode[0]), metric);
				double cs = mst.minDistance()/mst.maxDistance();
				System.out.println("maxCSValue :" + cs + " at " + candidate);
				if(cs > maxCSValue) {
					retVal = parent;
					maxCSValue = cs;
				}
			}
		}
	
		
		if(maxCSValue >= insertionThreshold)
			tree.appendNode(retVal, node);
		else {
			//create an intermediate node
			CompetitiveTreeNode intermediateNode = interpolateNode(node, leaf);
			CompetitiveTreeNode parent = tree.parentNode(leaf);
			tree.removeNode(leaf);
			tree.appendNode(parent, intermediateNode);
			tree.appendNode(intermediateNode, node);
			tree.appendNode(intermediateNode, leaf);
			retVal = parent;
		}
		return retVal;
	}
	
	private final void updateMinusPath(List<CompetitiveTreeNode> path, CompetitiveTreeNode node)
	{
		for(CompetitiveTreeNode candidate : path)
			updateMinusNode(candidate, node);
	}
	
	private final void updateAddPath(List<CompetitiveTreeNode> path, CompetitiveTreeNode node)
	{
		for(CompetitiveTreeNode candidate : path)
			updateAddNode(candidate, node);
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


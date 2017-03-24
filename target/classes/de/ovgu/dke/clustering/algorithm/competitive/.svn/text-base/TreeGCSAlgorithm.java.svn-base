package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sun.net.idn.Punycode;

import de.ovgu.dke.clustering.util.DatasetSampler;
import de.ovgu.dke.clustering.util.DatasetSamplerType;
import de.ovgu.dke.clustering.util.PrototypeInitializer;
import de.ovgu.dke.clustering.util.PrototypeInitialzerType;
import de.ovgu.dke.graph.GraphConnectivityAlgorithm;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.MathUtil;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class TreeGCSAlgorithm implements TreeCompetitiveLearningAlgorithm
{
	public static enum PruningStrategy { MaximumDistantPruning, LowDensityPruning, LowRelativeErrorPruning, None }
	
	private final static Settings DEFAULT_SETTINGS = new Settings();
	
	private static Settings DEFAULT_PROTOTYPE_INITIALIZER_SETTINGS = new Settings();
	
	private static Settings DEFAULT_DATASET_SAMPLER_SETTINGS = new Settings();
	
	private static Settings DEFAULT_WINNER_SEARCH_METHOD_SETTINGS = new Settings();
	
	private final static DistanceMeasure DEFAULT_METRIC = DistanceMeasure.EUCLIDEAN;
	
	
	{
	//	DEFAULT_PROTOTYPE_INITIALIZER_SETTINGS.setParameter(new Parameter("seed", Long.toString(1228910649676L)));
		
	//	DEFAULT_DATASET_SAMPLER_SETTINGS.setParameter(new Parameter("seed", Long.toString(1228910649686L)));
		
		DEFAULT_SETTINGS.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("gamma", Double.toString(4d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("pruningThreshold", Double.toString(0.005d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("stopThreshold", Double.toString(0.01d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("tau", Double.toString(0.0008d))); //????
		DEFAULT_SETTINGS.setParameter(new Parameter("epsilon", Double.toString(0.8d))); //????
		DEFAULT_SETTINGS.setParameter(new Parameter("pruningStrategy", PruningStrategy.LowDensityPruning.name()));
		DEFAULT_SETTINGS.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.0006d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("learnRatePath", Double.toString(0.01d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("learnRateError", Double.toString(0.01d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("initErrorThreshold", Double.toString(500d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("maxEpochs", Integer.toString(10)));
		DEFAULT_SETTINGS.setParameter(new Parameter("useNodePruning", Boolean.toString(false)));
		Parameter p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(DEFAULT_PROTOTYPE_INITIALIZER_SETTINGS);
		DEFAULT_SETTINGS.setParameter(p);
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(DEFAULT_DATASET_SAMPLER_SETTINGS);
		DEFAULT_SETTINGS.setParameter(p);
		p = new Parameter("searchMethodType", WinnerSearchMethodType.NaiveSearch.name());
		p.setSettings(DEFAULT_WINNER_SEARCH_METHOD_SETTINGS);
		DEFAULT_SETTINGS.setParameter(p);
	}
	
	private DistanceMeasure metric;
	
	private double beta;
	
	private double stopThreshold;
	
	private double pruningThreshold;
	
	private double gamma;
	
	private double learnRateWinner;
	
	private double learnRateNeighbor;
	
	private double learnRatePath;
	
	private boolean useNodePruning;
	
	private int maxEpochs;
	
	//new stuff for growing
	private double initErrorThreshold;
	private double learnRateError;
	private double epsilon;
	
	private PruningStrategy pruningStrategy;
	
	private PrototypeInitialzerType prototypeInitType;
	private Settings prototypeInitializerSetting;
	
	private DatasetSamplerType samplingType;
	private Settings datasetSamplerSetting;
	
	private WinnerSearchMethodType searchMethodType;
	private Settings winnerSearchMethodSetting;
	
	private GraphConnectivityAlgorithm<CompetitiveGraph> connectivityAlgorithm;
	
	private TreeNodeInsertionStrategy updateStrategy = new IncrementalClusterSepInsStrategy();
	
	private int nodeIdGenerator;
	private int vertexIdGenerator;
	
	public TreeGCSAlgorithm()
	{
		applyDefaultSettings();
		connectivityAlgorithm = new GraphConnectivityAlgorithm<CompetitiveGraph>();
		vertexIdGenerator = 0;
		nodeIdGenerator   = 0;
	}
	
	public CompetitiveTree learn(ObjectSet dataset) 
	{
		PrototypeInitializer pInitializer = 
			PrototypeInitialzerType.createPrototypeInitializer(prototypeInitType, dataset, prototypeInitializerSetting);
		DatasetSampler sampler = DatasetSamplerType.createDatasetsampler(samplingType, dataset, maxEpochs);
		sampler.applySettings(datasetSamplerSetting);
		WinnerSearchMethod searchMethod = 
			WinnerSearchMethodType.createWinnerSearchMethod(searchMethodType, winnerSearchMethodSetting);
		
		CompetitiveTree tree = initTree(pInitializer);
		//initialPhase(tree, dataset, searchMethod, metric);
		compute(tree, sampler, searchMethod);
		return tree;
	}

	public CompetitiveTree relearn(CompetitiveTree tree, ObjectSet dataset) 
	{
		DatasetSampler sampler = DatasetSamplerType.createDatasetsampler(samplingType, dataset, maxEpochs);
		sampler.applySettings(datasetSamplerSetting);
		WinnerSearchMethod searchMethod = 
			WinnerSearchMethodType.createWinnerSearchMethod(searchMethodType, winnerSearchMethodSetting);
		
		compute(tree, sampler, searchMethod);
		return tree;
	}

	public void applyDefaultSettings()
	{
		applySettings(DEFAULT_SETTINGS);
		metric = DEFAULT_METRIC;
	}

	public void applySettings(Settings settings)
	{
		if(settings != null) {
			
			
			Parameter p = settings.getParameter("beta");
			if(p!=null)
				setParameterDecayRate(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("gamma");
			if(p!=null)
				setNodePruningThreshold(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("epsilon");
			if(p!=null)
				setGrowingThresholdIncreaseRate(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("pruningThreshold");
			if(p!=null)
				setPruningThreshold(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("stopThreshold");
			if(p!=null)
				setStopThreshold(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("pruningStrategy");
			if(p!=null) 
				setPruningStrategy(PruningStrategy.valueOf(p.getValue()));
			
			p = settings.getParameter("useNodePruning");
			if(p!=null)
				setUseNodePruning(Boolean.valueOf(p.getValue()));
			
			p = settings.getParameter("learnRateWinner");
			if(p!=null)
				setLearningRateWinner(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("learnRateNeighbor");
			if(p!=null)
				setLearningRateNeighbor(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("learnRatePath");
			if(p!=null)
				setLearningRatePath(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("learnRateError");
			if(p!=null)
				setLearningRateErrorThreshold(Double.valueOf(p.getValue()));

			p = settings.getParameter("initErrorThreshold");
			if(p!=null)
				setInitErrorThreshold(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("maxEpochs");
			if(p!=null)
				setMaxEpochs(Integer.valueOf(p.getValue()));
			
			p = settings.getParameter("prototypeInitType");
			if(p!=null) 
				setPrototypeInitializerType(PrototypeInitialzerType.valueOf(p.getValue()), p.getSettings());

			p = settings.getParameter("samplingType");
			if(p!=null) 
				setSamplingType(DatasetSamplerType.valueOf(p.getValue()), p.getSettings());
			
			p = settings.getParameter("searchMethodType");
			if(p!=null) 
				setWinnerSearchMethodType(WinnerSearchMethodType.valueOf(p.getValue()), p.getSettings());
		}
	}

	public Settings getDefaultSettings() 
	{
		return DEFAULT_SETTINGS;
	}

	public Settings getSettings() 
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("beta", Double.toString(beta)));
		settings.setParameter(new Parameter("gamma", Double.toString(gamma)));
		settings.setParameter(new Parameter("pruningThreshold", Double.toString(pruningThreshold)));
		settings.setParameter(new Parameter("stopThreshold", Double.toString(stopThreshold)));
		settings.setParameter(new Parameter("epsilon", Double.toString(epsilon)));
		settings.setParameter(new Parameter("pruningStrategy", pruningStrategy.name()));
		settings.setParameter(new Parameter("learnRateWinner", Double.toString(learnRateWinner)));
		settings.setParameter(new Parameter("learnRateNeighbor", Double.toString(learnRateNeighbor)));
		settings.setParameter(new Parameter("learnRatePath", Double.toString(learnRatePath)));
		settings.setParameter(new Parameter("learnRateError", Double.toString(learnRateError)));
		settings.setParameter(new Parameter("initErrorThreshold", Double.toString(initErrorThreshold)));
		settings.setParameter(new Parameter("maxEpochs", Integer.toString(maxEpochs)));
		settings.setParameter(new Parameter("useNodePruning", Boolean.toString(useNodePruning)));
		Parameter p = new Parameter("prototypeInitType", prototypeInitType.name());
		p.setSettings(prototypeInitializerSetting);
		settings.setParameter(p);
		p = new Parameter("samplingType", samplingType.name());
		p.setSettings(datasetSamplerSetting);
		settings.setParameter(p);
		p = new Parameter("searchMethodType", searchMethodType.name());
		p.setSettings(winnerSearchMethodSetting);
		settings.setParameter(p);
		return settings;
	} 
	
	public double getInitErrorThreshold()
	{
		return initErrorThreshold;
	}
	
	public void setInitErrorThreshold(double threshold)
	{
		if(Double.isNaN(threshold) || Double.isInfinite(threshold))
			throw new IllegalArgumentException("threshold must be a number");
		initErrorThreshold = threshold;
	}
	
	public double getLearningRateErrorThreshold()
	{
		return learnRateError;
	}
	
	public void setLearningRateErrorThreshold(double lr)
	{
		if(Double.isNaN(lr) || Double.isInfinite(lr) || lr < 0d || lr > 1d)
			throw new IllegalArgumentException("lr must be in the intervall [0,1]");
		learnRateError = lr;
	}
	
	public double getGrowingThresholdIncreaseRate()
	{
		return epsilon;
	}
	
	public void setGrowingThresholdIncreaseRate(double epsilon)
	{
		if(Double.isNaN(epsilon) || Double.isInfinite(epsilon) || epsilon <= 0d)
			throw new IllegalArgumentException("lr must be in the intervall (0,inf]");
		this.epsilon = epsilon;
	}
	
	public double getParameterDecayRate()
	{
		return beta;
	}
	
	
	public void setParameterDecayRate(double beta)
	{
		if(Double.isNaN(beta) || Double.isInfinite(beta) || beta < 0d || beta > 1d)
			throw new IllegalArgumentException("beta must be in the intervall [0,1]");
		this.beta = beta;
	}
	
	public double getPruningThreshold()
	{
		return pruningThreshold;
	}
	
	public void setPruningThreshold(double threshold)
	{
		if(Double.isNaN(threshold) || Double.isInfinite(threshold) || threshold < 0d || threshold > 1d)
			throw new IllegalArgumentException("threshold must be in the intervall [0,1]");
		this.pruningThreshold = threshold;
	}
	
	public PruningStrategy getPruningStrategy()
	{
		return pruningStrategy;
	}
	
	public void setStopThreshold(double threshold)
	{
		if(Double.isNaN(threshold) || Double.isInfinite(threshold) || threshold < 0d || threshold > 1d)
			throw new IllegalArgumentException("threshold must be in the intervall [0,1]");
		this.stopThreshold = threshold;
	}
	
	public double getStopThreshold()
	{
		return stopThreshold;
	}
	
	public void setPruningStrategy(PruningStrategy strategy)
	{
		if(strategy == null)
			throw new NullPointerException();
		pruningStrategy = strategy;
	}
	
	public double getLearningRateWinner()
	{
		return learnRateWinner;
	}
	
	public void setLearningRateWinner(double lr)
	{
		if(Double.isNaN(lr) || Double.isInfinite(lr) || lr < 0d || lr > 1d)
			throw new IllegalArgumentException("lr must be in the intervall [0,1]");
		learnRateWinner = lr;
	}
	
	public double getLearningRateNeighbor()
	{
		return learnRateNeighbor;
	}
	
	public void setLearningRateNeighbor(double lr)
	{
		if(Double.isNaN(lr) || Double.isInfinite(lr) || lr < 0d || lr > 1d)
			throw new IllegalArgumentException("lr must be in the intervall [0,1]");
		learnRateNeighbor = lr;
	}
	
	public double getLearningRatePath()
	{
		return learnRatePath;
	}
	
	public void setLearningRatePath(double lr)
	{
		if(Double.isNaN(lr) || Double.isInfinite(lr) || lr < 0d || lr > 1d)
			throw new IllegalArgumentException("lr must be in the intervall [0,1]");
		learnRatePath = lr;
	}
	
	public int getMaxEpochs()
	{
		return maxEpochs;
	}
	
	public void setMaxEpochs(int n)
	{
		if(n < 1)
			throw new IllegalArgumentException("n must be a positive integer");
		maxEpochs = n;
	}
	
	public boolean getUseNodePruning()
	{
		return useNodePruning;
	}
	
	public void setUseNodePruning(boolean b)
	{
		useNodePruning = b;
	}
	
	public double getNodePruningThreshold()
	{
		return gamma;
	}
	
	public void setNodePruningThreshold(double gamma)
	{
		if(Double.isNaN(gamma) || Double.isInfinite(gamma) || gamma <= 0d)
			throw new IllegalArgumentException("gamma must be greater than 0d");
		this.gamma = gamma;
	}
	
	public DatasetSamplerType getSamplingType()
	{
		return samplingType;
	}
	
	public Settings getSamplingSetting()
	{
		return datasetSamplerSetting;
	}
	
	public void setSamplingType(DatasetSamplerType type, Settings setting)
	{
		if(type == null)
			throw new NullPointerException();
		samplingType = type;
		datasetSamplerSetting = setting;
	}
	
	public PrototypeInitialzerType getPrototypeInitializerType()
	{
		return prototypeInitType;
	}
	
	public Settings getPrototypeInitializerSetting()
	{
		return prototypeInitializerSetting;
	}
	
	public void setPrototypeInitializerType(PrototypeInitialzerType type, Settings setting)
	{
		if(type == null)
			throw new NullPointerException();
		prototypeInitType = type;
		prototypeInitializerSetting = setting;
	}
	
	public WinnerSearchMethodType getWinnerSearchMethodType()
	{
		return searchMethodType;
	}
	
	public Settings getWinnerSearchMethodSetting()
	{
		return winnerSearchMethodSetting;
	}
	
	public void setWinnerSearchMethodType(WinnerSearchMethodType type, Settings setting)
	{
		if(type == null)
			throw new NullPointerException();
		searchMethodType = type;
		winnerSearchMethodSetting = setting;
	}
	
	public DistanceMeasure getMetric()
	{
		return metric;
	}
	
	public void setMetric(DistanceMeasure metric)
	{
		if(metric == null)
			throw new NullPointerException();
		this.metric = metric;
	}
	
	private final void initialPhase(CompetitiveTree tree, ObjectSet dataset, WinnerSearchMethod searchMethod, DistanceMeasure metric)
	{
		for(int i=0; i<dataset.size(); i++)
		{
			double[] input = dataset.get(i).getRepresentation();
			SearchResult result = searchMethod.findWinner(input, tree, metric);
			CompetitiveVertex v = result.getWinnerVertex();
			CompetitiveTreeNode n = result.getWinnerNode();
			v.adapt(learnRateWinner, input);
			n.adapt(learnRatePath, input);
		}
	}
	
	private final void compute(CompetitiveTree tree, DatasetSampler sampler, WinnerSearchMethod searchMethod)
	{
		int iteration = 1;
		double currError = 0d;
		double errorBefore = Double.NaN;
		int lastEpoch = 0;
		while(sampler.hasNext())
		{
			//stopping criterion
			if(lastEpoch != sampler.epoch()) {
				System.out.println("stopping criterion:" + lastEpoch + "\t"+currError + "\t"+errorBefore);
				if(!Double.isNaN(errorBefore)) {
					
					if(Math.abs((currError-errorBefore)/errorBefore) < stopThreshold) {
						System.out.println("is met");
						break;
					}
				}
				errorBefore = currError;
				currError = 0d;
				lastEpoch++;
			}
				
			
			//1. generate an input signal
			double[] input = sampler.next().getRepresentation();
			
			//2. locate the best matching unit
			SearchResult winnerResult = searchMethod.findWinner(input, tree, metric);
			CompetitiveVertex winnerVertex = winnerResult.getWinnerVertex();
			CompetitiveTreeNode winnerNode = winnerResult.getWinnerNode();
			CompetitiveGraph winnerGraph   = winnerNode.getGraph();
			double minDist = winnerResult.getDistance();
			
			//3. adapt the weight vectors of the winner and its direct topological 
			//neighbors
			winnerVertex.adapt(learnRateWinner, input);
			for(Iterator<CompetitiveEdge> eIt = winnerGraph.incidentEdges(winnerVertex); eIt.hasNext(); )
			{
				CompetitiveEdge edge = eIt.next();
				CompetitiveVertex v = edge.getOpposite(winnerVertex);
				v.adapt(learnRateNeighbor, input);
			}
			
			//4.increment signal counters and error variable of winner vertex
			winnerVertex.incrementSignalCounter();
			//winnerVertex.addError(MathUtil.square(minDist));
			winnerVertex.addError(minDist);
			
			//update total Error
			currError += MathUtil.square(minDist);
			//currError += metric.getDistance(input, winnerNode.getWeightVector());
			
			//5. apapt the weight vectors and variables of the nodes in the path to the winner
			//vertex
			List<CompetitiveTreeNode> winnerPath = tree.getPathFromRoot(winnerNode);
			for(CompetitiveTreeNode node : winnerPath)
			{
				node.incrementSignalCounter();
				double dist = metric.getDistance(node.getWeightVector(), input);
				//node.addError(MathUtil.square(dist));
				node.addError(dist);
				node.adapt(learnRatePath, input);
			}
			
			//update the moving avg error of the winner node
			if(iteration==1) {
				winnerNode.setMovingAvgError(MathUtil.square(minDist));
			} else {
				winnerNode.adaptMovingAvgError(learnRateError, MathUtil.square(minDist));
			}
			
			//adapt error threshold
			winnerVertex.addRelativeError(MathUtil.square(minDist)/winnerNode.getMovingAvgError());
			winnerNode.adaptErrorThreshold(learnRateError, winnerVertex.getRelativeError());
			
			//7. compute the utility value of the winner node, if node prunig is used
			if(useNodePruning && tree.numberOfLeafs() >= 2) {
				//A. find the second nearest leaf node to the input signal
				List<CompetitiveTreeNode> leafs = tree.getLeafs();
				double minNodeDist = Double.POSITIVE_INFINITY;
				for(CompetitiveTreeNode candidate : leafs)
				{
					if(!candidate.equals(winnerNode)) {
						double dist = metric.getDistance(input, candidate.getWeightVector());
						if(dist<minNodeDist) {
							minNodeDist = dist;
						}
					}
				}
				
				//B. compute the utility value of the winnerNode
				double winnerLeafDist = metric.getDistance(input, winnerNode.getWeightVector());
				winnerNode.addUtility(MathUtil.square(minNodeDist)-MathUtil.square(winnerLeafDist));
			}
			//System.out.println(iteration +"\t" + winnerNode + "\t"+winnerNode.getMovingAvgError()  + "\t" + MathUtil.square(minDist)/winnerNode.getMovingAvgError());
			//System.out.println(winnerVertex + "\t" + winnerVertex.getRelativeError());
			//5. 
			//System.out.println("insert ( " +iteration +"):" + winnerNode.getErrorThreshold() +"\t"+ winnerVertex.getRelativeError());
			if(winnerNode.getErrorThreshold() < winnerVertex.getRelativeError()) {
				System.out.println("insert ( " +iteration +"):" + winnerNode.getErrorThreshold() +"\t"+ winnerVertex.getRelativeError());
				//A. set the vertex q with the highest resource value
				double qError = winnerVertex.getError();
				CompetitiveVertex q = winnerVertex;
				
				//B. determine the furthest-distant neighbor of the vertex with the highest ressource value
				double maxDist = Double.NEGATIVE_INFINITY;
				CompetitiveVertex f = null;
				Collection<CompetitiveVertex> qNeighbors = winnerGraph.getNeighbors(q);
				for(CompetitiveVertex candidate : qNeighbors)
				{
					double dist = metric.getDistance(candidate.getWeightVector(), q.getWeightVector());
					if(dist>maxDist) {
						maxDist = dist;
						f = candidate;
					}
				}
				
				//C. determine shared neighbors of q and f
				Collection<CompetitiveVertex> fNeighbors = winnerGraph.getNeighbors(f);
				Collection<CompetitiveVertex> sharedNeighbors = MathUtil.intersection(qNeighbors, fNeighbors);
				//D. create a new vector r by interpolating the weight vectors of q and f
				//double[] weightVec = CompetitiveUtil.interpolate(q.getWeightVector(), f.getWeightVector());
				double interpolWeight = q.getSignalCounter()/(q.getSignalCounter()+f.getSignalCounter());
				double[] weightVec = CompetitiveUtil.interpolate(q.getWeightVector(), f.getWeightVector(),interpolWeight);
				CompetitiveVertex r = createVertex(weightVec);
				//E. insert new vertex r
				Collection<CompetitiveSimplex> removeSimplices = new LinkedList<CompetitiveSimplex>();
				for(Iterator<CompetitiveSimplex> sIt = winnerGraph.simplices(); sIt.hasNext(); )
				{
					CompetitiveSimplex simplex = sIt.next();
					if(simplex.contains(q) && simplex.contains(f)) 
						removeSimplices.add(simplex);
				}
				
				
				for(CompetitiveVertex candidate : sharedNeighbors)
				{
					winnerGraph.addSimplex(new CompetitiveSimplex(r, q, candidate));
					winnerGraph.addSimplex(new CompetitiveSimplex(r, f, candidate));
				}
				for(CompetitiveSimplex simplex : removeSimplices)
					winnerGraph.removeSimplex(simplex);
				
				//F. decrease the resource values of the r-neighborhood
				double totalRelativeError = 0d;
				double totalError = 0d;
				double totalSC = 0d;
				Collection<CompetitiveVertex> rNeighbors = winnerGraph.getNeighbors(r);
				int cardNeighbors = rNeighbors.size();
				for(CompetitiveVertex candidate : rNeighbors)
				{
					double sc 	 = candidate.getSignalCounter();
					double error = candidate.getError();
					double relError = candidate.getRelativeError();
					sc 	  -= sc/cardNeighbors;
					error -= error/cardNeighbors;
					relError -= relError/cardNeighbors;
					candidate.setSignalCounter(sc);
					candidate.setError(error);
					candidate.setRelativeError(relError);
					
					totalRelativeError += relError;
					totalSC	   += sc;
					totalError += error;
				}
				r.setRelativeError(totalRelativeError/cardNeighbors);
				r.setError(totalError/cardNeighbors);
				r.setSignalCounter(totalSC/cardNeighbors);
				winnerNode.increaseErrorThreshold(epsilon);
			}
			
			//6. pruning phase
			if(winnerGraph.numberOfVertices() > 3) {
				if(pruningStrategy == PruningStrategy.MaximumDistantPruning)
					maximalDistantPruningStrategy(winnerGraph, pruningThreshold);
				else if(pruningStrategy == PruningStrategy.LowDensityPruning){
					lessDensityPruningStrategy(winnerGraph, pruningThreshold);
				} else
					this.relativeErrorPruningStrategy(winnerGraph, pruningThreshold);
				
				// check whether the graph was splitted by vertex removal
				connectivityAlgorithm.process(winnerGraph);
				if(connectivityAlgorithm.numberOfComponents() > 1) {
					LinkedList<CompetitiveTreeNode> newNodes = 
						new LinkedList<CompetitiveTreeNode>();
					Iterator<CompetitiveGraph> gIt = connectivityAlgorithm.components();
					while(gIt.hasNext()) 
					{
						CompetitiveTreeNode node = createNode(gIt.next());
						//initially the utiliy value is set to the error variable
						node.setUtility((node.getSignalCounter()/winnerNode.getSignalCounter())*winnerNode.getUtility());
						node.setErrorThreshold(winnerNode.getErrorThreshold());
						node.setMovingAvgError(winnerNode.getMovingAvgError());
						newNodes.add(node);
					}
					System.out.println("split occurs: ( " + iteration +") " + newNodes.toString() + " at " + winnerNode);
					System.out.println("tree before update:");
					CompetitiveUtil.printCompetitiveTree(tree);
					updateStrategy.insert(newNodes, winnerNode, tree, metric);
					if(!tree.containsNode(winnerNode) || !tree.isLeaf(winnerNode))
						winnerNode.setGraph(null);
					System.out.println("tree after update:");
					CompetitiveUtil.printCompetitiveTree(tree);
				    
				}
			}
			
			//11. remove the tree node with the smallest utility if the fraction E_high/U_small > gamma
			if(useNodePruning && tree.numberOfLeafs()>2) {
				
				List<CompetitiveTreeNode> leafs = tree.getLeafs();
				
				//A. determine  the vertex with the smallest utility value
				double minUtility = Double.POSITIVE_INFINITY;
				CompetitiveTreeNode remCandidate = null;
				for(CompetitiveTreeNode candidate : leafs)
				{
					double utility = candidate.getUtility();
					if(utility < minUtility) {
						minUtility = utility;
						remCandidate = candidate;
					}
				}
				
				//B. determine  the node with the highest Error
				double hError = Double.NEGATIVE_INFINITY;
				for(CompetitiveTreeNode candidate : leafs)
				{
					if(candidate.getError() > hError) {
						hError = candidate.getError();
					}
				}
				//System.out.println(qError/minUtility);
				//C. remove candidate if the ration between highest error and lowest
				//utility is larger than gamma
				if(minUtility == 0d || (hError/minUtility) > gamma)  {
					tree.removeNode(remCandidate);
					//System.out.println("rem" + remCandidate);
				}
			}
			//12. decrease all error variables
			for(Iterator<CompetitiveTreeNode> nIt = tree.iterator(); nIt.hasNext(); )
			{
				CompetitiveTreeNode node = nIt.next();
				node.decreaseError(beta);
				node.decreaseSignalCounter(beta);
				if(tree.isLeaf(node)) {
					node.decreaseUtility(beta);
					CompetitiveGraph graph = node.getGraph();
					for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
					{
						CompetitiveVertex vertex = vIt.next();
						vertex.decreaseUtility(beta);
						vertex.decreaseError(beta);
						vertex.decreaseSignalCounter(beta);
						vertex.decreaseRelativeError(beta);
					}
				}
			}
			iteration++;
			
		}
		
	}
	
	private final CompetitiveTree initTree(PrototypeInitializer pInitializer)
	{
		nodeIdGenerator = 0;
		CompetitiveGraph graph  = initGraph(pInitializer);
		double weightVector[] = null;
		for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
		{
			double v[] = vIt.next().getWeightVector();
			if(weightVector == null)
				weightVector = new double[v.length];
			for(int i=0; i<v.length; i++)
				weightVector[i] += v[i];	
		}
		for(int i=0, n=graph.numberOfVertices(); i<weightVector.length; i++)
			weightVector[i] /= n;
		
		CompetitiveTreeNode root = createNode(weightVector);
		root.setErrorThreshold(initErrorThreshold);
		root.setGraph(graph);
		return new CompetitiveTree(root);
	}
	
	private final CompetitiveGraph initGraph(PrototypeInitializer pInitializer)
	{
		CompetitiveGraph graph = new CompetitiveGraph();
		vertexIdGenerator = 0;
		CompetitiveVertex x = createVertex(pInitializer.nextVector());
		CompetitiveVertex y = createVertex(pInitializer.nextVector());
		CompetitiveVertex z = createVertex(pInitializer.nextVector());
		CompetitiveSimplex simplex = new CompetitiveSimplex(x,y,z);
		graph.addSimplex(simplex);
		return graph;
	}
	
	private final CompetitiveVertex createVertex(double[] v)
	{
		return new CompetitiveVertex(vertexIdGenerator++, v);
	}
	
	private final CompetitiveTreeNode createNode(double v[])
	{
		return new CompetitiveTreeNode(nodeIdGenerator++, v);
	}
	
	private final CompetitiveTreeNode createNode(CompetitiveGraph graph)
	{
		//return CompetitiveTreeNode.createTreeNode(nodeIdGenerator++, graph);
		return CompetitiveTreeNode.createTreeNode(nodeIdGenerator++, graph, metric);
	}
	
	private final void maximalDistantPruningStrategy(CompetitiveGraph graph, double pruningThreshold)
	{
		if(graph.numberOfVertices() > 3) {
			//A. determine removal candidate (check whether this is the original method)
			CompetitiveVertex candidate = null;
			double maxMeanDist = Double.NEGATIVE_INFINITY;
			double globalMean = 0d;
			for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
			{
				CompetitiveVertex vertex = vIt.next();
				Collection<CompetitiveVertex> neighbors = graph.getNeighbors(vertex);
				double meanDist = 0d;
				for(CompetitiveVertex neighbor : neighbors) 
					meanDist += metric.getDistance(vertex.getWeightVector(), neighbor.getWeightVector());
				globalMean += meanDist;
				meanDist /= neighbors.size();
				if(meanDist > maxMeanDist) {
					maxMeanDist	 = meanDist;
					candidate = vertex;
				}
			}
			globalMean /= graph.numberOfEdges();
			
			
			if(maxMeanDist/globalMean > 1.5d) {
			//	System.out.println(candidate + ":" +globalMean + "\t" + maxMeanDist);
				removeCandidate(candidate, graph);
			}
		}
	}
	
	private final void relativeErrorPruningStrategy(CompetitiveGraph graph, double pruningThreshold)
	{
		if(graph.numberOfVertices() > 3) {
			double totalSC = 0d;
			for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
			{
				CompetitiveVertex v = vIt.next();
				totalSC += v.getSignalCounter();
			}
			
			double totalDensity = 0d;
			for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
			{
				CompetitiveVertex v = vIt.next();
				totalDensity += v.getSignalCounter()/(totalSC*v.getRelativeError());
			}
			
			LinkedList<CompetitiveVertex> remCandidates = new LinkedList<CompetitiveVertex>();
			for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
			{
				CompetitiveVertex v = vIt.next();
				double p = v.getSignalCounter()/(totalSC*v.getRelativeError());
				p /= totalDensity;
				//System.out.println("dens est: " + p);
				if(p < pruningThreshold) {
					System.out.println("rem " + v + "\t" + p);
					remCandidates.add(v);
				}
			}
			
			//TODO sorting the candidates by their density estimation
			for(CompetitiveVertex candidate : remCandidates)
			{
				removeCandidate(candidate, graph);
				if(graph.numberOfVertices()<=3)
					break;
			}
		}
	}
	
	private final void lessDensityPruningStrategy(CompetitiveGraph graph, double pruningThreshold)
	{
		if(graph.numberOfVertices() > 3) {
			double totalSC = 0d;
			HashMap<CompetitiveVertex, Double> avgEdgeDistances = new HashMap<CompetitiveVertex, Double>(graph.numberOfVertices());
			for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
			{
				CompetitiveVertex v = vIt.next();
				totalSC += v.getSignalCounter();
				Collection<CompetitiveVertex> neighbors = graph.getNeighbors(v);
				double meanDist = 0d;
				for(CompetitiveVertex neighbor : neighbors) 
					meanDist += Math.pow(metric.getDistance(v.getWeightVector(), neighbor.getWeightVector()), 1d);
				avgEdgeDistances.put(v, meanDist/neighbors.size());
			}
			
			double totalDensity = 0d;
			for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
			{
				CompetitiveVertex v = vIt.next();
				totalDensity += v.getSignalCounter()/(totalSC*avgEdgeDistances.get(v));
			}
			
			LinkedList<CompetitiveVertex> remCandidates = new LinkedList<CompetitiveVertex>();
			for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
			{
				CompetitiveVertex v = vIt.next();
				double p = v.getSignalCounter()/(totalSC*avgEdgeDistances.get(v));
				p /= totalDensity;
				if(p < pruningThreshold) {
					//System.out.println("rem " + v + "\t" + p);
					remCandidates.add(v);
				}
			}
			
			//TODO sorting the candidates by their density estimation
			for(CompetitiveVertex candidate : remCandidates)
			{
				removeCandidate(candidate, graph);
				if(graph.numberOfVertices()<=3)
					break;
			}
		}
	}
	
	
	private final void removeCandidate(CompetitiveVertex candidate, CompetitiveGraph graph)
	{
		//mark the simplices for removal that contains the candidate vertex
		Collection<CompetitiveSimplex> removeSimplices = new LinkedList<CompetitiveSimplex>();
		for(Iterator<CompetitiveSimplex> sIt = graph.simplices(); sIt.hasNext(); )
		{
			CompetitiveSimplex simplex = sIt.next();
			if(simplex.contains(candidate)) 
				removeSimplices.add(simplex);
		}
		//System.out.println("remSimpl: "+ removeSimplices.size());
		
		//remove the marked simplices for removal
		if(removeSimplices.size() < graph.numberOfSimplices()) {
			for(CompetitiveSimplex simplex : removeSimplices) 
			{
				graph.removeSimplex(simplex);
				if(graph.numberOfVertices()<=3)
					break;
			}
		}
		
		//check for inconsistency (shouldn't occur)
		if(graph.numberOfVertices()<3) {
			while(graph.numberOfVertices()<3) {
				//create random vertex
			}
			CompetitiveVertex[] vertices = new CompetitiveVertex[3];
			int i=0;
			for(Iterator<CompetitiveVertex> vIt1 = graph.vertices(); vIt1.hasNext(); )
			{
				vertices[i] = vIt1.next();
				for(Iterator<CompetitiveVertex> vIt2 = graph.vertices(); vIt2.hasNext(); )
					graph.addEdge(new CompetitiveEdge(vertices[i], vIt2.next()));
			}
			graph.addSimplex(new CompetitiveSimplex(vertices));
		}
	}
	
}

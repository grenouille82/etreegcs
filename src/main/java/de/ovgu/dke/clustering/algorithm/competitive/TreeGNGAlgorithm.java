package de.ovgu.dke.clustering.algorithm.competitive;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import de.ovgu.dke.clustering.algorithm.hierarchical.HAClusterer;
import de.ovgu.dke.clustering.util.DatasetSampler;
import de.ovgu.dke.clustering.util.DatasetSamplerType;
import de.ovgu.dke.clustering.util.PrototypeInitializer;
import de.ovgu.dke.clustering.util.PrototypeInitialzerType;
import de.ovgu.dke.graph.GraphConnectivityAlgorithm;
import de.ovgu.dke.graph.GraphUtil;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.MathUtil;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.ObjectWrapper;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;
import de.ovgu.dke.util.SimpleObjectSet;
import de.ovgu.dke.util.SimpleObjectWrapper;

public class TreeGNGAlgorithm implements TreeCompetitiveLearningAlgorithm
{
	private final static Settings DEFAULT_SETTINGS = new Settings();
	
	private static Settings DEFAULT_PROTOTYPE_INITIALIZER_SETTINGS = new Settings();
	
	private static Settings DEFAULT_DATASET_SAMPLER_SETTINGS = new Settings();
	
	private static Settings DEFAULT_WINNER_SEARCH_METHOD_SETTINGS = new Settings();
	
	private final static DistanceMeasure DEFAULT_METRIC = DistanceMeasure.COSINE;
	
	{
		DEFAULT_PROTOTYPE_INITIALIZER_SETTINGS.setParameter(new Parameter("seed", Integer.toString(1)));
		
		DEFAULT_DATASET_SAMPLER_SETTINGS.setParameter(new Parameter("seed", Integer.toString(1)));
		
		DEFAULT_SETTINGS.setParameter(new Parameter("alpha", Double.toString(0.5d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("zeta", Double.toString(3.5d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("gamma", Double.toString(6d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("tau", Double.toString(0.6d))); //????
		DEFAULT_SETTINGS.setParameter(new Parameter("epsilon", Double.toString(0.8d))); //????
		DEFAULT_SETTINGS.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.006d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("learnRatePath", Double.toString(0.01d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("learnRateError", Double.toString(0.01)));
		DEFAULT_SETTINGS.setParameter(new Parameter("initErrorThreshold", Double.toString(500d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("maxEpochs", Integer.toString(10)));
		DEFAULT_SETTINGS.setParameter(new Parameter("useNodePruning", Boolean.toString(false)));
		Parameter p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(DEFAULT_PROTOTYPE_INITIALIZER_SETTINGS);
		DEFAULT_SETTINGS.setParameter(p);
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(DEFAULT_PROTOTYPE_INITIALIZER_SETTINGS);
		DEFAULT_SETTINGS.setParameter(p);
		p = new Parameter("searchMethodType", WinnerSearchMethodType.NaiveSearch.name());
		p.setSettings(DEFAULT_WINNER_SEARCH_METHOD_SETTINGS);
		DEFAULT_SETTINGS.setParameter(p);
	}
	
	private double alpha; 
	
	private double beta;
	
	private double gamma;
	
	private double zeta;
	
	private double tau;
	
	private double learnRatePath;
	
	private double learnRateWinner;
	
	private double learnRateNeighbor;
	
	private int maxEpochs;
	
	private boolean useNodePruning;
	
	//new stuff for growing
	private double initErrorThreshold;
	private double learnRateError;
	private double epsilon;
	
	private DistanceMeasure metric;
	
	private PrototypeInitialzerType prototypeInitType;
	private Settings prototypeInitializerSetting;
	
	private DatasetSamplerType samplingType;
	private Settings datasetSamplerSetting;
	
	private WinnerSearchMethodType searchMethodType;
	private Settings winnerSearchMethodSetting;
	
	private GraphConnectivityAlgorithm<CompetitiveGraph> connectivityAlgorithm;
	
	private TreeNodeInsertionStrategy updateStrategy = new ClusterSepInsertionStrategy();
	
	private int vertexIdGenerator;
	private int nodeIdGenerator;
	
	private double globalThreshold = 5000d;
	
	public TreeGNGAlgorithm()
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
			
			Parameter p = settings.getParameter("alpha");
			if(p!=null)
				setInterpolationWeight(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("beta");
			if(p!=null)
				setParameterDecayRate(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("gamma");
			if(p!=null)
				setNodePruningThreshold(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("tau");
			if(p!=null)
				setGrowingThreshold(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("zeta");
			if(p!=null)
				setEdgeRemovalThreshold(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("epsilon");
			if(p!=null)
				setGrowingThresholdIncreaseRate(Double.valueOf(p.getValue()));
			
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
		settings.setParameter(new Parameter("alpha", Double.toString(alpha)));
		settings.setParameter(new Parameter("zeta", Double.toString(zeta)));
		settings.setParameter(new Parameter("gamma", Double.toString(gamma)));
		settings.setParameter(new Parameter("beta", Double.toString(beta)));
		settings.setParameter(new Parameter("tau", Double.toString(tau)));
		settings.setParameter(new Parameter("epsilon", Double.toString(epsilon)));
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
	
	public double getInterpolationWeight()
	{
		return alpha;
	}
	
	public void setInterpolationWeight(double alpha)
	{
		if(Double.isNaN(alpha) || Double.isInfinite(alpha) || alpha <= 0d || alpha >= 1d)
			throw new IllegalArgumentException("alpha must be in the intervall (0,1)");

		this.alpha = alpha;
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
	
	public double getGrowingThreshold()
	{
		return tau;
	}
	
	
	public void setGrowingThreshold(double tau)
	{
		if(Double.isNaN(tau) || Double.isInfinite(tau) || tau < 0d || tau > 1d)
			throw new IllegalArgumentException("tau must be in the intervall [0,1]");
		this.tau = tau;
	}
	
	public double getEdgeRemovalThreshold()
	{
		return zeta;
	}
	
	
	public void setEdgeRemovalThreshold(double zeta)
	{
		if(Double.isNaN(zeta) || Double.isInfinite(zeta) || zeta < 0d)
			throw new IllegalArgumentException("zeta must be in the intervall [0,inf]");
		this.zeta = zeta;
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
	
	private final void compute(CompetitiveTree tree, DatasetSampler sampler, WinnerSearchMethod searchMethod)
	{
		int iteration = 1;
		while(sampler.hasNext())
		{
			//1. generate an input signal
			double[] input = sampler.next().getRepresentation();

			//2. determine the winner 
			SearchResult searchResult = searchMethod.findWinner(input, tree, metric);
			CompetitiveVertex winner  = searchResult.getWinnerVertex();
			double winnerDist = searchResult.getDistance();
			CompetitiveTreeNode winnerNode = searchResult.getWinnerNode();
			CompetitiveGraph winnerGraph = winnerNode.getGraph();
			
		//	System.out.println("winnerNode: " + winnerNode.getId());
		//	System.out.println("winnerGraphNodes: " + winnerGraph.numberOfVertices());
		//	System.out.println("winnerGraphEdges: " + winnerGraph.numberOfEdges());
	//		System.out.println(winnerDist);
		//	System.out.println(Arrays.toString(winnerNode.getWeightVector()) + "\t" + Arrays.toString(createNode(winnerGraph).getWeightVector()));
			
	
			//3. determine the second nearest vertex. search in the graphs of 
			//the winner node and of the sibling.
			List<CompetitiveTreeNode> siblings = tree.siblings(winnerNode);
			CompetitiveVertex second  = null;
			double secondDist = Double.POSITIVE_INFINITY;
			CompetitiveTreeNode secondNode = winnerNode;
			
			for(Iterator<CompetitiveVertex> it = winnerGraph.vertices(); it.hasNext(); )
			{
				CompetitiveVertex candidate = it.next();
				if(candidate != winner) {
					double dist = metric.getDistance(input, candidate.getWeightVector());
					if(dist<secondDist) {
						secondDist = dist;
						second = candidate;
					}
				}
			}
			for(CompetitiveTreeNode node : siblings)
			{
				if(tree.isLeaf(node)) {
					CompetitiveGraph graph = node.getGraph();
					boolean found = false;
					Iterator<CompetitiveVertex> it = graph.vertices();
					while(it.hasNext())
					{
						CompetitiveVertex candidate = it.next();
						double dist = metric.getDistance(input, candidate.getWeightVector());
						if(dist<secondDist) {
							secondDist = dist;
							second = candidate;
							found = true;
						}
					}
					if(found)
						secondNode = node;
				}
				
			}
			
			//4. add the squared error to the local error variable of the winner
			
			if(iteration==1) {
				winnerNode.setMovingAvgError(MathUtil.square(winnerDist));
			} else {
				winnerNode.adaptMovingAvgError(learnRateError, MathUtil.square(winnerDist));
			}
		//	System.out.println(winnerNode + "\t error=" + winnerNode.getMovingAvgError()+ "\t errorThreshold="+ winnerNode.getErrorThreshold());
			
			winner.addError(MathUtil.square(winnerDist)/winnerNode.getMovingAvgError());
			winner.incrementSignalCounter();
		//	System.out.println(winner + "\t error=" + winner.getError());
			
			//adapt error threshold
			winnerNode.adaptErrorThreshold(learnRateError, winner.getError());
			globalThreshold += learnRateError*(winner.getError()-globalThreshold);
			
			//5. adapt the weight vectors of the winner and its direct topological 
			//neighbors and increment the age of all edges eminating from winner
			winner.adapt(learnRateWinner, input);
			for(Iterator<CompetitiveEdge> eIt = winnerGraph.incidentEdges(winner); eIt.hasNext(); )
			{
				CompetitiveEdge edge = eIt.next();
				edge.incrementAge();
				CompetitiveVertex v = edge.getOpposite(winner);
				v.adapt(learnRateNeighbor, input);
			}
			
			//6. apapt the weight vectors and variables of the nodes in the path to the winner
			//vertex
			List<CompetitiveTreeNode> winnerPath = tree.getPathFromRoot(winnerNode);
			for(CompetitiveTreeNode node : winnerPath)
			{
				node.incrementSignalCounter();
				double dist = metric.getDistance(node.getWeightVector(), input);
				//System.out.println(node + " "+Arrays.toString(node.getWeightVector()));
				node.addError(MathUtil.square(dist));
				node.adapt(learnRatePath, input);
	
			}
			
			//7. compute the utility value of the winner node, if node prunig is used
			if(useNodePruning && tree.numberOfLeafs() >= 2) {
				//A. find the second nearest leaf node to the input signal
				List<CompetitiveTreeNode> leafs = tree.getLeafs();
				double minDist = Double.POSITIVE_INFINITY;
				for(CompetitiveTreeNode candidate : leafs)
				{
					if(!candidate.equals(winnerNode)) {
						double dist = metric.getDistance(input, candidate.getWeightVector());
						if(dist<minDist) {
							minDist = dist;
						}
					}
				}
				
				//B. compute the utility value of the winnerNode
				double winnerLeafDist = metric.getDistance(input, winnerNode.getWeightVector());
				double utility = MathUtil.square(minDist)-MathUtil.square(winnerLeafDist);
				//System.out.println("minDist:" + minDist + "\tdist:" + winnerLeafDist);
				//System.out.println("utility:" + utility + "\tdist:" + MathUtil.square(winnerLeafDist));
			    if(utility > 0d)
					winnerNode.addUtility(utility);
			}
			
			//8. create an edge between the winner and the second nearest unit 
			//if not present and set the age to 0.
			if(winnerNode == secondNode) {
				CompetitiveEdge wEdge = winnerGraph.getEdge(winner, second);
				if(wEdge != null) {
					wEdge.resetAge();
				}
				else {
					//System.out.println(winner + " " + second);
					//System.out.println("add edge");
					//System.out.println(winnerGraph.numberOfEdges());
					winnerGraph.addEdge(new CompetitiveEdge(winner,second));
					//System.out.println(winnerGraph.numberOfEdges());
				}
			} else {
				
				
				//System.out.println("merging is proceeded");
				CompetitiveGraph secondGraph = secondNode.getGraph();
				//check whether the second unit is near to the input signal. this could be
				//depending on the winner search method
				if(secondDist < winnerDist) {
					CompetitiveVertex tmpWinner	= winner;
					double tmpDist		= winnerDist;
					CompetitiveGraph tmpGraph 	= winnerGraph;
					CompetitiveTreeNode tmpNode = winnerNode;
					winner 		= second;
					winnerDist 	= secondDist;
					winnerGraph = secondGraph;
					winnerNode 	= secondNode;
					second		= tmpWinner;
					secondDist	= tmpDist;
					secondGraph	= tmpGraph;
					secondNode 	= tmpNode;
				}
				
				GraphUtil.merge(winnerGraph, secondGraph);
				System.out.println("merge ( " + iteration +")" +  winnerNode + secondNode);
				CompetitiveTreeNode parent = tree.parentNode(winnerNode);
				if(siblings.size() == 1) { 
					parent.setGraph(winnerGraph);
					tree.removeNode(winnerNode);
					tree.removeNode(secondNode);
					//estimate the utility value (i'm not sure what is the best choice sum or harmonic mean)
					parent.setUtility(winnerNode.getUtility()+secondNode.getUtility());
					parent.setErrorThreshold(Math.max(winnerNode.getErrorThreshold(), secondNode.getErrorThreshold()));
					parent.setMovingAvgError(Math.max(winnerNode.getMovingAvgError(), secondNode.getMovingAvgError()));
					winnerNode = parent;
				} else {
					CompetitiveTreeNode node = createNode(winnerGraph);
					tree.appendNode(parent, node);
					tree.removeNode(winnerNode);
					tree.removeNode(secondNode);
					//estimate the utility value (i'm not sure what is the best choice sum or harmonic mean)
					node.setUtility(winnerNode.getUtility()+secondNode.getUtility());
					node.setErrorThreshold(Math.max(winnerNode.getErrorThreshold(), secondNode.getErrorThreshold()));
					node.setMovingAvgError(Math.max(winnerNode.getMovingAvgError(), secondNode.getMovingAvgError()));
					winnerNode = node;
				}
				winnerGraph.addEdge(new CompetitiveEdge(winner,second));
			}
			

			
			
			//10.If the number of input signals generated so far is
            //an integer multiple of lambda, insert a new unit
			double vertexAVGError = winner.getError()/winner.getSignalCounter();
			double nodeAVGError	  = winnerNode.getError()/winnerNode.getSignalCounter();
			//System.out.println(vertexAVGError + "\t" + tau*nodeAVGError);
			//System.out.println(winner.getSignalCounter() + "\t" + winnerNode.getSignalCounter());
		//	if(winner.getError() > winnerNode.getError()*tau) {
		//	if(vertexAVGError > tau*nodeAVGError) {
//			if(iteration%200 ==0) {
			//if(globalThreshold < winner.getError()) {
			if(winnerNode.getErrorThreshold() < winner.getError()) {
				System.out.println("insert ( " +iteration +"):" + winnerNode.getErrorThreshold() +"\t"+ winner.getError());
				//A. 
				CompetitiveVertex q = winner;
				
				//B. determine among the neighbors of q the vertex f with maximum error
				CompetitiveVertex f	  = null;
				double fError = Double.NEGATIVE_INFINITY;
				for(Iterator<CompetitiveEdge> eIt = winnerGraph.incidentEdges(q); eIt.hasNext(); )
				{
					CompetitiveVertex candidate = eIt.next().getOpposite(q);
					if(candidate.getError() > fError) {
						fError = candidate.getError();
						f = candidate;
					}
				}
				
				//C. create a new vector r by interpolating the weight vectors of q and f
				//double[] weightVec = CompetitiveUtil.interpolate(q.getWeightVector(), f.getWeightVector());
				double interpolWeight = q.getSignalCounter()/(q.getSignalCounter()+f.getSignalCounter());
				double[] weightVec = CompetitiveUtil.interpolate(q.getWeightVector(), f.getWeightVector(),interpolWeight);
				CompetitiveVertex r = createVertex(weightVec);
				
				//D. decrease the error variable of q and f, and interpolate the error of r
				q.decreaseError(alpha-0.25);
				q.decreaseSignalCounter(alpha-0.25);
				f.decreaseError(alpha-0.25);
				f.decreaseSignalCounter(alpha-0.25);
				r.setError((q.getError()+f.getError())/3d);
				r.setSignalCounter((q.getSignalCounter()+f.getSignalCounter())/3d);
				
				//E. update edge connections
				
				winnerGraph.addEdge(new CompetitiveEdge(r,q));
				winnerGraph.addEdge(new CompetitiveEdge(r,f));
				winnerGraph.removeEdge(q,f);
				
				winnerNode.increaseErrorThreshold(epsilon);
				globalThreshold += globalThreshold*epsilon;
			}
			
			//9. remove all edges with an age larger than maxAge and remove all vertices
			//with a degree of zero
			//TODO: to avoid heavy computation, prune the graph only if their weren't growing
			if(winnerGraph.degree(winner)>2) {
				int maxAge = Integer.MIN_VALUE;
				double meanAge = 0d;
				CompetitiveEdge remCandidate = null;
				for(Iterator<CompetitiveEdge> eIt = winnerGraph.incidentEdges(winner); eIt.hasNext(); )
				{
					CompetitiveEdge edge = eIt.next();
					//System.out.println("edge " + edge.getAge());
					if(edge.getAge()>maxAge) {
						meanAge = edge.getAge();
						maxAge = edge.getAge();
						remCandidate = edge;
					}
				}
				meanAge /= winnerGraph.degree(winner);
				if(maxAge/meanAge > zeta) {
					winnerGraph.removeEdge(remCandidate);
					if(winnerGraph.degree(remCandidate.getSource()) == 0) {
						winnerGraph.removeVertex(remCandidate.getSource());
						System.out.println("remVertex ");
					}
					if(winnerGraph.degree(remCandidate.getDestination()) == 0) {
						winnerGraph.removeVertex(remCandidate.getDestination());
						System.out.println("remVertex ");
					}
					
					connectivityAlgorithm.process(winnerGraph);
					if(connectivityAlgorithm.numberOfComponents() > 1) {
						//System.out.println("nComponents: " + connectivityAlgorithm.numberOfComponents() );
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
					//	System.out.println("update");
					//	printTreeStats(tree);
					//	System.out.println("ins Nodes");
						updateStrategy.insert(newNodes, winnerNode, tree);
						if(!tree.containsNode(winnerNode) || !tree.isLeaf(winnerNode))
							winnerNode.setGraph(null);
					    System.out.println("split occurs: ( " + iteration +") " + newNodes.toString());
					}
				}
			}

			//11. remove the tree node with the smallest utility if the fraction E_high/U_small > gamma
			if(useNodePruning && tree.numberOfLeafs()>2000) {
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
					CompetitiveTreeNode parent = tree.parentNode(remCandidate);
					if(tree.numberOfChildren(parent) == 2) {
						CompetitiveTreeNode sibling = tree.siblings(remCandidate).get(0);
						CompetitiveTreeNode grand = tree.parentNode(parent);
						tree.removeNode(parent);
						tree.removeNode(remCandidate);
						tree.appendNode(grand, sibling);
					} else {
						tree.removeNode(remCandidate);
					}
					
					//System.out.println("remove Node : " + remCandidate + "\terror:" + hError + "\tutil:" + minUtility);
				}
			}
			//System.out.println("treesize: " +tree.size());
			//12. decrease all error variables
			for(Iterator<CompetitiveTreeNode> nIt = tree.iterator(); nIt.hasNext(); )
			{
				CompetitiveTreeNode node = nIt.next();
				node.decreaseError(beta);
				node.decreaseSignalCounter(beta);
				//bug: if the tree has only a root node, then the root node isnt declared as leaf
				if(tree.isLeaf(node) || tree.size()==1) {
					node.decreaseUtility(beta);
					CompetitiveGraph graph = node.getGraph();
					for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
					{
						CompetitiveVertex vertex = vIt.next();
				//		vertex.decreaseError(beta);
						vertex.decreaseSignalCounter(beta);
					}
				}
			}

	//		System.out.println(iteration);	
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
		CompetitiveVertex v = createVertex(pInitializer.nextVector());
		CompetitiveVertex w = createVertex(pInitializer.nextVector());
		graph.addEdge(new CompetitiveEdge(v,w));
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
		return CompetitiveTreeNode.createTreeNode(nodeIdGenerator++, graph);
	}
	
	public static void main(String args[])
	{
		try {
		//ObjectSet dataset = createDataset(500);
		ObjectSet dataset = new CSVObjectSet("/home/grenouille/irg_workspace/datasets/bs_small_stem.csv", "class");
		System.out.println("loaded");
		TreeGNGAlgorithm algo = new TreeGNGAlgorithm();
		long time = System.currentTimeMillis();
		CompetitiveTree tree = algo.learn(dataset);
		//new HAClusterer().computeClusterModel(dataset);
		System.out.println("time: " + (System.currentTimeMillis()-time));
	//	System.out.println(tree.size());
		//GraphConnectivityAlgorithm<CompetitiveGraph> connectAlgo = new GraphConnectivityAlgorithm<CompetitiveGraph>();
		//connectAlgo.process(graph);
		//System.out.println(connectAlgo.numberOfComponents());
		//printGraphStats(graph);
		//printFinalAssignments(graph, dataset);
		printTreeStats(tree);
		computeClassDistributionLeafNodes(tree, dataset);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printTreeStats(CompetitiveTree tree)
	{
		Iterator<CompetitiveTreeNode> it = tree.iterator();
		while(it.hasNext())
			printNodeStats(it.next(), tree);
	}
	
	public static void printNodeStats(CompetitiveTreeNode node, CompetitiveTree tree)
	{
		System.out.println("node_"+node.getId() + "paren_node_" + tree.parentNode(node) +": e=" + node.getError() + " sc=" + node.getSignalCounter() + " u=" + node.getUtility()); //+" weight: " + Arrays.toString(node.getWeightVector()) );
		if(node.getGraph() != null)
			printGraphStats(node.getGraph());
	}
	
	public static void printGraphStats(CompetitiveGraph graph)
	{
		System.out.println("vertices: " + graph.numberOfVertices());
		System.out.println("edges: " + graph.numberOfEdges());
		for(Iterator<CompetitiveVertex> it=graph.vertices(); it.hasNext(); )
		{
			CompetitiveVertex v = it.next();
			System.out.println(v + ": e: " + v.getError() + " sc:" + v.getSignalCounter() + " u: " + v.getUtility());//+ " weight: " +  Arrays.toString(v.getWeightVector()));
		}
	}
	
	private static void printFinalAssignments(CompetitiveGraph graph, ObjectSet dataset)
	{
		HashMap<CompetitiveVertex, Integer> assignments = new HashMap<CompetitiveVertex, Integer>();
		for(Iterator<CompetitiveVertex> it=graph.vertices(); it.hasNext(); )
			assignments.put(it.next(), 0);
		
		for(int i=0; i<dataset.size(); i++)
		{
			CompetitiveVertex v = findNearestVertex(dataset.get(i).getRepresentation(), graph);
			assignments.put(v, assignments.get(v)+1);
		}
		
		for(Entry<CompetitiveVertex, Integer> e : assignments.entrySet())
			System.out.println(e.getKey() +": " + e.getValue());
	}
	
	private static CompetitiveVertex findNearestVertex(double[] input, CompetitiveGraph graph)
	{
		double minDist = Double.POSITIVE_INFINITY;
		CompetitiveVertex v = null;
		for(Iterator<CompetitiveVertex> it=graph.vertices(); it.hasNext(); )
		{
			CompetitiveVertex candidate = it.next();
			double dist = DistanceMeasure.COSINE.getDistance(input, candidate.getWeightVector());
			if(dist<minDist) {
				minDist = dist;
				v = candidate;
			}
		}
		return v;
	}
	
	public static ObjectSet createDataset(int n)
	{
		Random rnd = new Random(1);
		ObjectWrapper[] wrapper = new ObjectWrapper[n];
		for(int i=0; i<100; i++)
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*20d), rnd.nextDouble()*20d});
		for(int i=100; i<350; i++)
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*20d+100d), rnd.nextDouble()*20d+100d} );

		for(int i=350; i<500; i++)
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*20d+50d), rnd.nextDouble()*20d+50d});
		
		/*
		for(int i=450; i<500; i++)
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*20d+200d), rnd.nextDouble()*20d+200d});

*/
		//TODO: set Attributes
		return new SimpleObjectSet(wrapper, null);
	}
	
	public static void computeClassDistributionLeafNodes(CompetitiveTree tree, ObjectSet dataset)
	{
		HashMap<CompetitiveTreeNode, HashMap<String, Integer>> classDistributionsLeafNodes = new HashMap<CompetitiveTreeNode, HashMap<String,Integer>>();
		WinnerSearchMethod searchMethod = new NaiveSearchMethod();
		
		for(int i=0, n=dataset.size(); i<n; i++)
		{
			double[] input = dataset.get(i).getRepresentation();
			String classLabel = dataset.get(i).getClassLabel();
			SearchResult result = searchMethod.findWinner(input, tree, DistanceMeasure.COSINE);
			CompetitiveTreeNode winner = result.getWinnerNode();
			HashMap<String, Integer> clDistr = classDistributionsLeafNodes.get(winner);
			if(clDistr == null) {
				clDistr = new HashMap<String, Integer>();
				classDistributionsLeafNodes.put(winner, clDistr);
			}
			
			Integer freq = clDistr.get(classLabel);
			if(freq == null)
				clDistr.put(classLabel, 1);
			else
				clDistr.put(classLabel, freq+1);
			
		}
		
		System.out.println("\ndistribution " );
		for(Entry<CompetitiveTreeNode, HashMap<String, Integer>> nodeEntry : classDistributionsLeafNodes.entrySet())
		{
			System.out.print(nodeEntry.getKey()+": ");
			for(Entry<String, Integer> freqEntry : nodeEntry.getValue().entrySet())
				System.out.print(freqEntry.getKey()+"="+freqEntry.getValue()+"\t");
			System.out.println();
		}
		
		
		
		
		
	}
}

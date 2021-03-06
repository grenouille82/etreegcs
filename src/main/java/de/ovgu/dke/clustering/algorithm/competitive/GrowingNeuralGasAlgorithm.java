package de.ovgu.dke.clustering.algorithm.competitive;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Map.Entry;

import de.ovgu.dke.clustering.util.DatasetSampler;
import de.ovgu.dke.clustering.util.DatasetSamplerType;
import de.ovgu.dke.clustering.util.PrototypeInitializer;
import de.ovgu.dke.clustering.util.PrototypeInitialzerType;
import de.ovgu.dke.graph.GraphConnectivityAlgorithm;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.MathUtil;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.ObjectWrapper;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;
import de.ovgu.dke.util.SimpleObjectSet;
import de.ovgu.dke.util.SimpleObjectWrapper;

public final class GrowingNeuralGasAlgorithm implements GraphCompetitiveLearningAlgorithm
{
	private final static Settings DEFAULT_SETTINGS = new Settings();
	
	private static Settings DEFAULT_PROTOTYPE_INITIALIZER_SETTINGS = new Settings();
	
	private static Settings DEFAULT_DATASET_SAMPLER_SETTINGS = new Settings();
	
	private final static DistanceMeasure DEFAULT_METRIC = DistanceMeasure.COSINE;
	
	{
		DEFAULT_PROTOTYPE_INITIALIZER_SETTINGS.setParameter(new Parameter("seed", Integer.toString(1)));
		
		DEFAULT_DATASET_SAMPLER_SETTINGS.setParameter(new Parameter("seed", Integer.toString(1)));
		
		DEFAULT_SETTINGS.setParameter(new Parameter("lambda", Integer.toString(200)));
		DEFAULT_SETTINGS.setParameter(new Parameter("alpha", Double.toString(0.5d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("gamma", Double.toString(10d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("beta", Double.toString(0.0008d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("learnRateWinner", Double.toString(0.05d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("learnRateNeighbor", Double.toString(0.007d)));
		DEFAULT_SETTINGS.setParameter(new Parameter("maxAge", Integer.toString(20)));
		DEFAULT_SETTINGS.setParameter(new Parameter("maxVertices", Integer.toString(Integer.MAX_VALUE)));
		DEFAULT_SETTINGS.setParameter(new Parameter("maxEpochs", Integer.toString(10)));
		DEFAULT_SETTINGS.setParameter(new Parameter("useVertexRemoval", Boolean.toString(false)));
		Parameter p = new Parameter("prototypeInitType", PrototypeInitialzerType.RandomExample.name());
		p.setSettings(DEFAULT_PROTOTYPE_INITIALIZER_SETTINGS);
		DEFAULT_SETTINGS.setParameter(p);
		p = new Parameter("samplingType", DatasetSamplerType.ConstantEpoch.name());
		p.setSettings(DEFAULT_PROTOTYPE_INITIALIZER_SETTINGS);
		DEFAULT_SETTINGS.setParameter(p);
	}
	
	private DistanceMeasure metric;
	
	private int lambda;
	
	private double alpha;
	
	private double beta;
	
	private double learnRateWinner;
	
	private double learnRateNeighbor;
	
	private int maxAge;
	
	private int maxVertices;
	
	private int maxEpochs;
	
	private boolean useVertexRemoval;
	
	private double gamma;
	
	private PrototypeInitialzerType prototypeInitType;
	private Settings prototypeInitializerSetting;
	
	private DatasetSamplerType samplingType;
	private Settings datasetSamplerSetting;
	
	private int vertexIdGenerator;
	
	public GrowingNeuralGasAlgorithm()
	{
		applyDefaultSettings();
	}
	
	
	public CompetitiveGraph learn(ObjectSet dataset) 
	{
		PrototypeInitializer pInitializer = 
			PrototypeInitialzerType.createPrototypeInitializer(prototypeInitType, dataset, prototypeInitializerSetting);
		DatasetSampler sampler = DatasetSamplerType.createDatasetsampler(samplingType, dataset, maxEpochs);
		sampler.applySettings(datasetSamplerSetting);
		
		CompetitiveGraph graph = initGraph(pInitializer);
		compute(graph, sampler);
		return graph;
	}
	
	public CompetitiveGraph relearn(CompetitiveGraph graph, ObjectSet dataset)
	{
		DatasetSampler sampler = DatasetSamplerType.createDatasetsampler(samplingType, dataset, maxEpochs);
		sampler.applySettings(datasetSamplerSetting);
		compute(graph, sampler);
		return graph;
	}

	public void applyDefaultSettings() 
	{
		applySettings(DEFAULT_SETTINGS);
		metric = DEFAULT_METRIC;
	}

	public void applySettings(Settings settings)
	{
		if(settings != null) {
			Parameter p = settings.getParameter("lambda");
			if(p!=null)
				setInsertionStepSize(Integer.valueOf(p.getValue()));
			
			p = settings.getParameter("alpha");
			if(p!=null)
				setInterpolationWeight(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("beta");
			if(p!=null)
				setParameterDecayRate(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("gamma");
			if(p!=null)
				setRemovalThreshold(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("useVertexRemoval");
			if(p!=null)
				setUseVertexRemoval(Boolean.valueOf(p.getValue()));
			
			p = settings.getParameter("learnRateWinner");
			if(p!=null)
				setLearningRateWinner(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("learnRateNeighbor");
			if(p!=null)
				setLearningRateNeighbor(Double.valueOf(p.getValue()));
			
			p = settings.getParameter("maxEpochs");
			if(p!=null)
				setMaxEpochs(Integer.valueOf(p.getValue()));
			
			p = settings.getParameter("maxAge");
			if(p!=null)
				setMaxEdgeAge(Integer.valueOf(p.getValue()));
			
			p = settings.getParameter("maxVertices");
			if(p!=null)
				setMaxNVertices(Integer.valueOf(p.getValue()));
			
			p = settings.getParameter("prototypeInitType");
			if(p!=null) 
				setPrototypeInitializerType(PrototypeInitialzerType.valueOf(p.getValue()), p.getSettings());

			p = settings.getParameter("samplingType");
			if(p!=null) 
				setSamplingType(DatasetSamplerType.valueOf(p.getValue()), p.getSettings());

		}
			
		
	}

	public Settings getDefaultSettings() 
	{
		return DEFAULT_SETTINGS;
	}

	public Settings getSettings() 
	{
		Settings settings = new Settings();
		
		settings.setParameter(new Parameter("lambda", Integer.toString(lambda)));
		settings.setParameter(new Parameter("alpha", Double.toString(alpha)));
		settings.setParameter(new Parameter("beta", Double.toString(beta)));
		settings.setParameter(new Parameter("gamma", Double.toString(gamma)));
		settings.setParameter(new Parameter("useVertexRemoval", Boolean.toString(useVertexRemoval)));
		settings.setParameter(new Parameter("learnRateWinner", Double.toString(learnRateWinner)));
		settings.setParameter(new Parameter("learnRateNeighbor", Double.toString(learnRateNeighbor)));
		settings.setParameter(new Parameter("maxAge", Integer.toString(maxAge)));
		settings.setParameter(new Parameter("maxVertices", Integer.toString(maxVertices)));
		settings.setParameter(new Parameter("maxEpochs", Integer.toString(maxEpochs)));
		Parameter p = new Parameter("prototypeInitType", prototypeInitType.name());
		p.setSettings(prototypeInitializerSetting);
		settings.setParameter(p);
		p = new Parameter("samplingType", samplingType.name());
		p.setSettings(datasetSamplerSetting);
		settings.setParameter(p);
		return settings;
	}
	
	public int getInsertionStepSize()
	{
		return lambda;
	}
	
	public void setInsertionStepSize(int lambda)
	{
		this.lambda = lambda;
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
	
	public int getMaxEdgeAge()
	{
		return maxAge;
	}
	
	public void setMaxEdgeAge(int age)
	{
		if(age < 1)
			throw new IllegalArgumentException("age must be a positive integer");
		maxAge = age;
	}
	
	public int getMaxNVertices()
	{
		return maxVertices;
	}
	
	public void setMaxNVertices(int n)
	{
		if(n < 1)
			throw new IllegalArgumentException("n must be a positive integer");
		maxVertices = n;
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
	
	public boolean getUseVertexRemoval()
	{
		return useVertexRemoval;
	}
	
	public void setUseVertexRemoval(boolean b)
	{
		useVertexRemoval = b;
	}
	
	public double getRemovalThreshold()
	{
		return gamma;
	}
	
	public void setRemovalThreshold(double gamma)
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
	
	private final void compute(CompetitiveGraph graph, DatasetSampler sampler)
	{	
		int iteration = 1;
		
		while(sampler.hasNext() && graph.numberOfVertices()<maxVertices)
		{
			//1. generate an input signal
			double[] input = sampler.next().getRepresentation();

			//2. determine the winner and the second nearest unit
			double winnerDist = Double.POSITIVE_INFINITY;
			double secondDist = Double.POSITIVE_INFINITY;
			CompetitiveVertex winner = null;
			CompetitiveVertex second = null;
			for(Iterator<CompetitiveVertex> it = graph.vertices(); it.hasNext(); )
			{
				CompetitiveVertex candidate = it.next();
				double dist = metric.getDistance(input, candidate.getWeightVector());
				if(dist < secondDist) {
					secondDist = dist;
					second  = candidate;
					if(secondDist < winnerDist) {
						double tmpDist = secondDist;
						CompetitiveVertex tmpV = second;
						secondDist = winnerDist;
						second	 = winner;
						winnerDist = tmpDist;
						winner	 = tmpV;
					}
				}
			}
			//System.out.println(winner);
			//3. add the squared error to the local error variable of the winner
			winner.addError(MathUtil.square(winnerDist));
			winner.addUtility(MathUtil.square(secondDist)-MathUtil.square(winnerDist));
			winner.incrementSignalCounter();
			System.out.println(winnerDist +"\t"+ secondDist);
			
			//System.out.println(winner + "\t" + second);
			
			//4. adapt the weight vectors of the winner and its direct topological 
			//neighbors and increment the age of all edges eminating from winner
			winner.adapt(learnRateWinner, input);
			for(Iterator<CompetitiveEdge> eIt = graph.incidentEdges(winner); eIt.hasNext(); )
			{
				CompetitiveEdge edge = eIt.next();
				edge.incrementAge();
				edge.getOpposite(winner).adapt(learnRateNeighbor, input);
			
			}
			
			//5. create an edge between the winner and the second nearest unit 
			//if not present and set the age to 0.
			CompetitiveEdge wEdge = graph.getEdge(winner, second);
			if(wEdge == null) 
				graph.addEdge(new CompetitiveEdge(winner, second));
			else
				wEdge.resetAge();
			
			//6. remove all edges with an age larger than maxAge and remove all vertices
			//with a degree of zero
			if(graph.numberOfVertices()>2) {
				HashSet<CompetitiveEdge> removeEdges = new HashSet<CompetitiveEdge>();
				for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
				{
					for(Iterator<CompetitiveEdge> eIt = graph.incidentEdges(vIt.next()); eIt.hasNext(); )
					{
						CompetitiveEdge edge = eIt.next();
						if(edge.getAge()>maxAge) {
							removeEdges.add(edge);
							//System.out.println(edge);
						}
					}
				}
				for(CompetitiveEdge e : removeEdges)
					graph.removeEdge(e);
				
				Collection<CompetitiveVertex> removeVertices = new LinkedList<CompetitiveVertex>();
				for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
				{
					CompetitiveVertex vertex = vIt.next();
					if(graph.degree(vertex) == 0) 
						removeVertices.add(vertex);
				}
				for(CompetitiveVertex vertex : removeVertices)
					graph.removeVertex(vertex);
				
				//System.out.println("remVert: " + removeVertices.size());
				//check whether the graph is inconsistent
				if(graph.numberOfVertices()<2) {
					while(graph.numberOfVertices()<2) {
						//create random vertex
					}
					Iterator<CompetitiveVertex> vIt = graph.vertices();
					CompetitiveVertex predecessor = null; 
					while(vIt.hasNext()) {
						CompetitiveVertex current = vIt.next();
						if(predecessor != null) {
							graph.addEdge(new CompetitiveEdge(predecessor, current));
						}
						predecessor = current;
					}
				}
			}

			
			//7.If the number of input signals generated so far is
            //an integer multiple of lambda, insert a new unit
			if(iteration%lambda == 0) {
				
				//A. determine the vertex q with the highest error
				CompetitiveVertex q	  = null;
				double qError = Double.NEGATIVE_INFINITY;
				for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
				{
					CompetitiveVertex candidate = vIt.next();
					if(candidate.getError() > qError) {
						qError = candidate.getError();
						q = candidate;
					}
				}
				
				//B. determine among the neighbors of q the vertex f with maximum error
				CompetitiveVertex f	  = null;
				double fError = Double.NEGATIVE_INFINITY;
				for(Iterator<CompetitiveEdge> eIt = graph.incidentEdges(q); eIt.hasNext(); )
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
				
				//D. decrease the  variables of q and f, and interpolate the variable for r
				q.decreaseError(alpha);
				q.decreaseSignalCounter(alpha);
				q.decreaseUtility(alpha);
				f.decreaseError(alpha);
				f.decreaseSignalCounter(alpha);
				f.decreaseUtility(alpha);
				r.setError((q.getError()+f.getError())/2d);
				r.setSignalCounter((q.getSignalCounter()+f.getSignalCounter())/2d);
				r.setUtility((q.getUtility()+f.getUtility())/2d);
				
				
				//E. update edge connections
				graph.addEdge(new CompetitiveEdge(r,q));
				graph.addEdge(new CompetitiveEdge(r,f));
				graph.removeEdge(q,f);
			}
			
			//7. remove the vertex with the smalles utility if the fraction E_high/U_small > gamma
			if(useVertexRemoval && graph.numberOfVertices()>2) {
				//A. determine  the vertex with the smallest utility value
				double minUtility = Double.POSITIVE_INFINITY;
				CompetitiveVertex remCandidate = null;
				for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
				{
					CompetitiveVertex candidate = vIt.next();
					double utility = candidate.getUtility();
					if(utility < minUtility) {
						minUtility = utility;
						remCandidate = candidate;
					}
				}
				
				//B. determine  the vertex with the highest Error
				CompetitiveVertex q	  = null;
				double qError = Double.NEGATIVE_INFINITY;
				for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
				{
					CompetitiveVertex candidate = vIt.next();
					if(candidate.getError() > qError) {
						qError = candidate.getError();
						q = candidate;
					}
				}
				//System.out.println(qError/minUtility);
				//C. remove candidate if the ration between highest error and lowest
				//utility is larger than gamma
				if(minUtility == 0d || (qError/minUtility) > gamma)  {
					graph.removeVertex(remCandidate);
					//System.out.println("rem" + remCandidate);
				}
			}
			
			//9. decrease all error variables
			for(Iterator<CompetitiveVertex> vIt = graph.vertices(); vIt.hasNext(); )
			{
				CompetitiveVertex vertex = vIt.next();
				vertex.decreaseError(beta);
				vertex.decreaseSignalCounter(beta);
				vertex.decreaseUtility(beta);
			}
			
			iteration++;
		}
		
		System.out.println("iter: " +  iteration);
		
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


	public static void main(String args[])
	{
		//ObjectSet dataset = createDataset(500);
		try {
		ObjectSet dataset = new CSVObjectSet("/home/grenouille/irg_workspace/datasets/bs_small_stem.csv", "class");
		GrowingNeuralGasAlgorithm algo = new GrowingNeuralGasAlgorithm();
		long time = System.currentTimeMillis();
		CompetitiveGraph graph = algo.learn(dataset);
		System.out.println(System.currentTimeMillis()-time);
		System.out.println(graph.numberOfVertices());
		System.out.println("edges " + graph.numberOfEdges());
		GraphConnectivityAlgorithm<CompetitiveGraph> connectAlgo = new GraphConnectivityAlgorithm<CompetitiveGraph>();
		connectAlgo.process(graph);
		System.out.println(connectAlgo.numberOfComponents());
		printGraphStats(graph);
		printFinalAssignments(graph, dataset);
		printClassDistribution(graph, dataset);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printGraphStats(CompetitiveGraph graph)
	{
		for(Iterator<CompetitiveVertex> it=graph.vertices(); it.hasNext(); )
		{
			CompetitiveVertex v = it.next();
			System.out.println(v + ": e: " + v.getError() + " sc:" + v.getSignalCounter() + " u: " + v.getUtility());// + " weight: " +  Arrays.toString(v.getWeightVector()));
		}
	}
	
	public static void printFinalAssignments(CompetitiveGraph graph, ObjectSet dataset)
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
	
	public static void printClassDistribution(CompetitiveGraph graph, ObjectSet dataset)
	{
		HashMap<CompetitiveVertex, HashMap<String,Integer>> assignments = new HashMap<CompetitiveVertex, HashMap<String,Integer>>();
		for(Iterator<CompetitiveVertex> it=graph.vertices(); it.hasNext(); )
			assignments.put(it.next(), new HashMap<String, Integer>());
		
		for(int i=0; i<dataset.size(); i++)
		{
			String label = dataset.get(i).getClassLabel();
			CompetitiveVertex v = findNearestVertex(dataset.get(i).getRepresentation(), graph);
			HashMap<String, Integer> distr = assignments.get(v);
			Integer freq = distr.get(label);
			if(freq == null)
				distr.put(label, 1);
			else
				distr.put(label, freq+1);
		}
		
		System.out.println("\ndistribution " );
		for(Entry<CompetitiveVertex, HashMap<String, Integer>> nodeEntry : assignments.entrySet())
		{
			System.out.print(nodeEntry.getKey()+": ");
			for(Entry<String, Integer> freqEntry : nodeEntry.getValue().entrySet())
				System.out.print(freqEntry.getKey()+"="+freqEntry.getValue()+"\t");
			System.out.println();
		}
	}
	
	
	
	public static CompetitiveVertex findNearestVertex(double[] input, CompetitiveGraph graph)
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
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*20d+400d), rnd.nextDouble()*20d+400d});
*/

		/*
		for(int i=0; i<100; i++)
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*20d), rnd.nextDouble()*20d});
		for(int i=100; i<250; i++)
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*20d+100d), rnd.nextDouble()*20d} );

		for(int i=250; i<400; i++)
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*20d), rnd.nextDouble()*20d+50d});
		for(int i=400; i<500; i++)
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*20d+100), rnd.nextDouble()*20d+100d});
*/
		//TODO: set Attributes
		return new SimpleObjectSet(wrapper, null);
	}
}

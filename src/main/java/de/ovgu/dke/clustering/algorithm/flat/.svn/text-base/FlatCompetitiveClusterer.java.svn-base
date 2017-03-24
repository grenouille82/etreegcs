package de.ovgu.dke.clustering.algorithm.flat;

import java.util.Iterator;
import java.util.LinkedList;

import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveAlgorithmType;
import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveGraph;
import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveVertex;
import de.ovgu.dke.clustering.algorithm.competitive.GraphCompetitiveLearningAlgorithm;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.MutableCluster;
import de.ovgu.dke.clustering.model.SimpleFlatClusterModel;
import de.ovgu.dke.clustering.model.SimplePrototypeCluster;
import de.ovgu.dke.graph.GraphConnectivityAlgorithm;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class FlatCompetitiveClusterer implements FlatClusterer<FlatClusterModel> 
{
	private final static DistanceMeasure DEFAULT_METRIC = DistanceMeasure.EUCLIDEAN;
	
	private final static Settings DEFAULT_LEARNER_SETTINGS = null;
	
	private final static Settings DEFAULT_SETTINGS = new Settings();
	
	{	
		DEFAULT_SETTINGS.setParameter(new Parameter("useGraphPartitioning", Boolean.toString(true)));
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		p.setSettings(DEFAULT_LEARNER_SETTINGS);
		DEFAULT_SETTINGS.setParameter(p);
	}
	
	private boolean useGraphPartitioning;
	
	private GraphConnectivityAlgorithm<CompetitiveGraph> connectivityAlgo;
	
	private DistanceMeasure metric;
	private CompetitiveAlgorithmType competitiveType;
	private Settings cAlgorithmSettings;
	
	public FlatCompetitiveClusterer()
	{
		connectivityAlgo = new GraphConnectivityAlgorithm<CompetitiveGraph>();
		applyDefaultSettings();
	}

	public FlatClusterModel computeClusterModel(ObjectSet dataset) 
	{
		GraphCompetitiveLearningAlgorithm learner = 
			CompetitiveAlgorithmType.createGraphLearner(competitiveType, cAlgorithmSettings);
		learner.setMetric(metric);
		CompetitiveGraph graph = learner.learn(dataset);
		removeUnassignedGraphVertices(graph, dataset);
		SimpleFlatClusterModel clusterModel = new SimpleFlatClusterModel(dataset);
		if(useGraphPartitioning)
			extractClustersFromParitions(clusterModel, graph);
		else
			extractClustersFromVertices(clusterModel, graph);
		assignData(clusterModel, dataset);
		clusterModel.setDistanceMetric(metric);
		return clusterModel;
	}

	public void applyDefaultSettings() 
	{
		applySettings(DEFAULT_SETTINGS);
		metric = DEFAULT_METRIC;
	}

	public void applySettings(Settings settings) 
	{
		if(settings != null)
		{
			Parameter p = settings.getParameter("useGraphPartitioning");
			if(p!=null)
				useGraphPartitioning(Boolean.valueOf(p.getValue()));
			
			p = settings.getParameter("competitiveType");
			if(p!=null)
				setCompetitiveAlgoType(CompetitiveAlgorithmType.valueOf(p.getValue()), p.getSettings());
		}
	}

	public Settings getDefaultSettings() 
	{
		return DEFAULT_SETTINGS;
	}

	public Settings getSettings() 
	{
		Settings settings = new Settings();
		settings.setParameter(new Parameter("useGraphPartitioning", Boolean.toString(useGraphPartitioning)));
		Parameter p = new Parameter("competitiveType", competitiveType.name());
		p.setSettings(cAlgorithmSettings);
		settings.setParameter(p);
		return settings;
	}
	
	public boolean useGraphPartitioning()
	{
		return useGraphPartitioning;
	}
	
	public void useGraphPartitioning(boolean bool)
	{
		useGraphPartitioning = bool;
	}
	
	public void setCompetitiveAlgoType(CompetitiveAlgorithmType type, Settings settings)
	{
		if(type == null)
			throw new NullPointerException();
		competitiveType = type;
		cAlgorithmSettings = settings;
	}
	
	public CompetitiveAlgorithmType getCompetitiveAlgoType()
	{
		return competitiveType;
	}
	
	public Settings getCompetitiveAlgoSettings()
	{
		return cAlgorithmSettings;
	}
	
	public void setDistanceMetric(DistanceMeasure metric)
	{
		if(metric == null)
			throw new NullPointerException();
		this.metric = metric;
	}
	
	public DistanceMeasure getDistanceMetric()
	{
		return metric;
	}
	
	private final void extractClustersFromParitions(SimpleFlatClusterModel model,CompetitiveGraph graph)
	{
		connectivityAlgo.process(graph);
		Iterator<CompetitiveGraph> it = connectivityAlgo.components();
		System.out.println("nComponents = " + connectivityAlgo.numberOfComponents());
		while(it.hasNext()) {
			
			double centroid[] = computeCentroid(it.next());
			SimplePrototypeCluster cluster = new SimplePrototypeCluster(centroid);
			model.addCluster(cluster);
		}
	}
	
	private final void extractClustersFromVertices(SimpleFlatClusterModel model,CompetitiveGraph graph)
	{
		Iterator<? extends CompetitiveVertex> it = graph.vertices();
		while(it.hasNext())
		{
			CompetitiveVertex vertex = it.next();
			SimplePrototypeCluster cluster = new SimplePrototypeCluster(vertex.getWeightVector());
			model.addCluster(cluster);
		}
	}
	
	//TODO: the cast is just a workaround to compute the centroid by the weighted mean of the weight vectors
	//      the dimension of the centroid should be known before ... stored in dataset
	private final double[] computeCentroid(CompetitiveGraph graph)
	{
		double centroid[] = null;
		double totalWeight = 0;
		Iterator<? extends CompetitiveVertex> it = graph.vertices();
		while(it.hasNext()) 
		{
			CompetitiveVertex vertex = it.next();
			double[] weightVector = vertex.getWeightVector();
			if(centroid == null)
				centroid = new double[weightVector.length];
			
			double weight = vertex.getSignalCounter();
			for(int i=0, n=centroid.length; i<n; i++)
				centroid[i] += weight*weightVector[i];
			totalWeight += weight;
		}
		
		for(int i=0, n=centroid.length; i<n; i++)
			centroid[i] /= totalWeight;
		return centroid;
	}
	
	private final void assignData(SimpleFlatClusterModel model, ObjectSet dataset)
	{
		for(int i=0, n=dataset.size(); i<n; i++)
		{
			double[] data = dataset.get(i).getRepresentation();
			double minDist = Double.POSITIVE_INFINITY;
			int clusterIdx = -1;
			for(int j=0, m=model.numberOfClusters(); j<m; j++)
			{
				//TODO: remove this bad-styled cast
				SimplePrototypeCluster cluster = (SimplePrototypeCluster) model.getCluster(j);
				double dist = metric.getDistance(cluster.getCentroid(), data);
				if(dist < minDist) {
					minDist = dist;
					clusterIdx = j;
				}
			}
			
			MutableCluster nearestCluster = model.getCluster(clusterIdx);
			nearestCluster.assignData(i);
		}
	}
	
	private final void removeUnassignedGraphVertices(CompetitiveGraph graph, ObjectSet dataset)
	{
		LinkedList<CompetitiveVertex> remVertices = new LinkedList<CompetitiveVertex>();
		for(Iterator<? extends CompetitiveVertex> it = graph.vertices(); it.hasNext(); )
			remVertices.add(it.next());
	
		for(int i=0, n=dataset.size(); i<n; i++)
		{
			double v[] = dataset.get(i).getRepresentation();
			double minDist = Double.POSITIVE_INFINITY;
			CompetitiveVertex minVertex = null;
			for(Iterator<? extends CompetitiveVertex> it = graph.vertices(); it.hasNext(); )
			{
				CompetitiveVertex candidate = it.next();
				double dist = metric.getDistance(v, candidate.getWeightVector());
				if(dist<minDist) {
					minDist = dist;
					minVertex = candidate;
				}
			}
			remVertices.remove(minVertex);
			if(remVertices.isEmpty())
				break;
		}
		
		for(CompetitiveVertex vertex : remVertices)
			graph.removeVertex(vertex);
	}
	
	

}

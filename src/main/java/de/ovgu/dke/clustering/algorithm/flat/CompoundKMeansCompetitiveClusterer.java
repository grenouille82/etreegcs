package de.ovgu.dke.clustering.algorithm.flat;

import java.util.Iterator;
import java.util.LinkedList;

import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveGraph;
import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveVertex;
import de.ovgu.dke.clustering.algorithm.competitive.GraphCompetitiveLearningAlgorithm;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.MutableCluster;
import de.ovgu.dke.clustering.model.SimpleFlatClusterModel;
import de.ovgu.dke.clustering.model.SimplePrototypeCluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Settings;
import de.ovgu.dke.util.SimpleObjectSet;
import de.ovgu.dke.util.SimpleObjectWrapper;

//TODO: make the prototype clusterer more generic, so that is possible to change its by a parameter
public class CompoundKMeansCompetitiveClusterer implements FlatClusterer<FlatClusterModel> 
{
	private DistanceMeasure metric;
	private KMeansClusterer clusterer;
	
	private GraphCompetitiveLearningAlgorithm graphLearner;
	
	public CompoundKMeansCompetitiveClusterer()
	{
		
	}
	
	public CompoundKMeansCompetitiveClusterer(KMeansClusterer clusterer, 
			GraphCompetitiveLearningAlgorithm graphLearner)
	{
		if(clusterer == null)
			throw new NullPointerException();
		if(graphLearner == null)
			throw new NullPointerException();
	}
	
	public FlatClusterModel computeClusterModel(ObjectSet dataset) 
	{
		if(dataset == null)
			throw new NullPointerException();
		GraphCompetitiveLearningAlgorithm learner = null;
		
		
		learner.applySettings(null);
		CompetitiveGraph graph = learner.learn(dataset);
		
		ObjectSet pseudoDataset = transformVerticesToPseudoDataset(graph);
		KMeansModel pseudoModel = clusterer.computeClusterModel(pseudoDataset);
		SimpleFlatClusterModel originalModel = transformClusterModel(pseudoModel, dataset);
		assignData(originalModel, dataset);
		return originalModel;
	}

	public void applyDefaultSettings() 
	{
		// TODO Auto-generated method stub
		
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
	
	public void setKMeansSettings(Settings setting)
	{
		clusterer.applySettings(setting);
	}
	
	public Settings getKMeansSettings()
	{
		return clusterer.getSettings();
	}
	
	public KMeansClusterer getKMeansClusterer()
	{
		return clusterer;
	}
	
	public void setKMeansClusterer(KMeansClusterer clusterer)
	{
		if(clusterer == null)
			throw new NullPointerException();
		this.clusterer = clusterer;
	}
	
	
	private final SimpleFlatClusterModel transformClusterModel(KMeansModel model, ObjectSet origDataset)
	{
		SimpleFlatClusterModel retVal = new SimpleFlatClusterModel(origDataset);
		retVal.setDistanceMetric(model.getDistanceMetric());
		
		for(int i=0, n=model.numberOfClusters(); i<n; i++)
		{
			MutableCluster cluster = model.getClusterAt(i);
			cluster.clearData();
			retVal.addCluster(cluster);
		}
		return retVal;
	}
	
	private final ObjectSet transformVerticesToPseudoDataset(CompetitiveGraph graph)
	{
		int n = graph.numberOfVertices();
		SimpleObjectWrapper[] data = new SimpleObjectWrapper[n];
		Iterator<CompetitiveVertex> it = graph.vertices();
		int i=0;
		while(it.hasNext()) 
		{
			CompetitiveVertex v = it.next();
			//TODO: if useful, copy the weight vector.in case if the graph is changed in the future
			//      should be the object of the wrapper set to the vertex???
			data[i++] = new SimpleObjectWrapper(v, v.getWeightVector());
		}
		
		//TODO: set Attributes
		return new SimpleObjectSet(data, null);
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

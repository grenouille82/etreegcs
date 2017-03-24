package de.ovgu.dke.clustering.algorithm.hierarchical;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveGraph;
import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveVertex;
import de.ovgu.dke.clustering.algorithm.competitive.GraphCompetitiveLearningAlgorithm;
import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Settings;
import de.ovgu.dke.util.SimpleObjectSet;
import de.ovgu.dke.util.SimpleObjectWrapper;

public class CompoundHACompetitiveClusterer implements HierarchicalClusterer<HierarchicalClusterModel> 
{
	private DistanceMeasure metric;
	private HAClusterer clusterer;
	
	public HierarchicalClusterModel computeClusterModel(ObjectSet dataset) 
	{
		GraphCompetitiveLearningAlgorithm learner = null;
		
		
		learner.applySettings(null);
		CompetitiveGraph graph = learner.learn(dataset);
	
		removeUnassignedGraphVertices(graph, dataset);	
		ObjectSet pseudoDataset = transformVerticesToPseudoDataset(graph);
		Map<Integer, List<Integer>> pseudoDataMapping = computePseudoMapping(pseudoDataset, dataset);
		HAClusterModel pseudoModel = clusterer.computeClusterModel(pseudoDataset);
		assignOriginalData(pseudoModel, pseudoDataMapping);
		pseudoModel.setDataset(dataset);
		return pseudoModel;
	}

	public void applyDefaultSettings() {
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
	
	private final ObjectSet transformVerticesToPseudoDataset(CompetitiveGraph graph)
	{
		int n = graph.numberOfVertices();
		SimpleObjectWrapper[] data = new SimpleObjectWrapper[n];
		Iterator<? extends CompetitiveVertex> it = graph.vertices();
		int i=0;
		while(it.hasNext()) 
		{
			CompetitiveVertex v = it.next();
			//TODO: if useful, copy the weight vector.in case if the graph is changed in the future
			//      should be the object of the wrapper set to the vertex???
			data[i++] = new SimpleObjectWrapper(v, v.getWeightVector());
		}
		
		//TODO: set attributes
		return new SimpleObjectSet(data, null);
	}
	
	private final void assignOriginalData(HAClusterModel model, Map<Integer, List<Integer>> pseudoMapping)
	{
		LinkedList<HAClusterNode> candidateQueue = new LinkedList<HAClusterNode>();
		candidateQueue.add(model.getRootCluster());
		while(!candidateQueue.isEmpty())
		{
			HAClusterNode cluster = candidateQueue.removeFirst();
			Collection<Integer> pseudoData = cluster.getData();
			cluster.clearData();
			for(Integer pseudoId : pseudoData)
			{
				List<Integer> originalData = pseudoMapping.get(pseudoId);
				for(Integer originalId : originalData)
					cluster.assignData(originalId);
			}
			candidateQueue.addAll(cluster.getSubClusters());
		}
	}
	
	private Map<Integer, List<Integer>> computePseudoMapping(ObjectSet pseudoDataset, ObjectSet origDataset)
	{
		Map<Integer, List<Integer>> retVal = new HashMap<Integer, List<Integer>>();
		for(int i=0, n=origDataset.size(); i<n; i++)
		{
			double origV[] = origDataset.get(i).getRepresentation();
			
			double minDist = Double.POSITIVE_INFINITY;
			int minIdx = -1;
			for(int j=0, m=pseudoDataset.size(); j<m; j++)
			{
				double pseudoV[] = pseudoDataset.get(j).getRepresentation();
				double dist = metric.getDistance(origV, pseudoV);
				if(dist < minDist) {
					minDist = dist;
					minIdx = j;
				}
			}
			
			List<Integer> origValues = retVal.get(minIdx);
			if(origValues == null) {
				origValues = new LinkedList<Integer>();
				retVal.put(minIdx, origValues);
			}
			origValues.add(i);
		}
		return retVal;
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

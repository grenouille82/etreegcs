package de.ovgu.dke.clustering.algorithm.hierarchical;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveAlgorithmType;
import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveGraph;
import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveTree;
import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveTreeNode;
import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveVertex;
import de.ovgu.dke.clustering.algorithm.competitive.TreeCompetitiveLearningAlgorithm;
import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.clustering.model.SimpleHierarchicalClusterModel;
import de.ovgu.dke.clustering.model.SimplePrototypeClusterNode;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class HierarchicalCompetitiveClusterer implements HierarchicalClusterer<HierarchicalClusterModel>
{
	private final static DistanceMeasure DEFAULT_METRIC = DistanceMeasure.EUCLIDEAN;
	
	private final static Settings DEFAULT_LEARNER_SETTINGS = null;
	
	private final static Settings DEFAULT_SETTINGS = new Settings();
	
	{	
		DEFAULT_SETTINGS.setParameter(new Parameter("useVerticesAsLeafs", Boolean.toString(false)));
		Parameter p = new Parameter("competitiveType", CompetitiveAlgorithmType.GrowingCellStructures.name());
		p.setSettings(DEFAULT_LEARNER_SETTINGS);
		DEFAULT_SETTINGS.setParameter(p);
	}
	
	private DistanceMeasure metric;
	private CompetitiveAlgorithmType competitiveType;
	private Settings cAlgorithmSettings;
	
	private boolean useVerticesAsLeafs;
	
	
	public HierarchicalCompetitiveClusterer()
	{
		applyDefaultSettings();
	}

	private TreeCompetitiveLearningAlgorithm learner;
	private CompetitiveTree tree;
	
	public HierarchicalClusterModel computeClusterModel(ObjectSet dataset) 
	{
		learner = CompetitiveAlgorithmType.createTreeLearner(competitiveType, cAlgorithmSettings);
		learner.setMetric(metric);
		tree = learner.learn(dataset);
		HierarchicalClusterModel model = extractClusterModelFromTree(tree, dataset);		
		return model;
	}
	
	public HierarchicalClusterModel incrementalComputation(ObjectSet dataset)
	{
		tree = learner.relearn(tree, dataset);
		HierarchicalClusterModel model = extractClusterModelFromTree(tree, dataset);
		return model;
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
			Parameter p = settings.getParameter("useVerticesAsLeafs");
			if(p!=null)
				useVerticesAsLeafs(Boolean.valueOf(p.getValue()));
			
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
		settings.setParameter(new Parameter("useVerticesAsLeafs", Boolean.toString(useVerticesAsLeafs)));
		Parameter p = new Parameter("competitiveType", competitiveType.name());
		p.setSettings(cAlgorithmSettings);
		settings.setParameter(p);
		return settings;
	}
	
	public boolean useVerticesAsLeafs()
	{
		return useVerticesAsLeafs;
	}
	
	public void useVerticesAsLeafs(boolean bool)
	{
		useVerticesAsLeafs = bool;
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
	
	private final HierarchicalClusterModel extractClusterModelFromTree(CompetitiveTree tree, ObjectSet dataset)
	{
		SimpleHierarchicalClusterModel model = new SimpleHierarchicalClusterModel(dataset);
		model.setDistanceMetric(metric);
		
		Map<CompetitiveVertex, List<Integer>> dataAssignment = computeDataAssignment(tree, dataset);
		SimplePrototypeClusterNode root = buildClusterModelRecursive(tree.getRoot(), tree, dataAssignment);
		//if(root != null) {
		if(root == null)
			throw new IllegalStateException();
			model.setRootCluster(root);
		//}
		return model;
	}
	
	
	private final SimplePrototypeClusterNode buildClusterModelRecursive(CompetitiveTreeNode node, CompetitiveTree tree, 
															  			Map<CompetitiveVertex, List<Integer>> assignments)
	{
		SimplePrototypeClusterNode cluster = new SimplePrototypeClusterNode(node.getWeightVector());

		if(tree.isLeaf(node)) {
			System.out.println("fuck ");
			CompetitiveGraph graph = node.getGraph();
			Iterator<CompetitiveVertex> it = graph.vertices();
			while(it.hasNext())
			{
				CompetitiveVertex vertex = it.next();
				List<Integer> assignedData = assignments.get(vertex);
				if(assignedData != null) {
					if(useVerticesAsLeafs) {
						SimplePrototypeClusterNode subCluster = new SimplePrototypeClusterNode(vertex.getWeightVector()); 
						for(Integer id : assignedData)
							subCluster.assignData(id);
						cluster.addSubCluster(cluster);
					}
					for(Integer id : assignedData)
						cluster.assignData(id);
				}
			}
		} else {
			List<CompetitiveTreeNode> children = tree.childNodes(node);
			for(CompetitiveTreeNode child : children)
			{
				SimplePrototypeClusterNode subCluster = buildClusterModelRecursive(child, tree, assignments);
				if(subCluster != null)
					cluster.addSubCluster(subCluster);
			}
			
		}
		if(cluster.size() == 0) {
			if(cluster.numberOfSubClusters() == 0) {
				System.out.println(node);
				cluster = null;
			}
			else if(cluster.numberOfSubClusters() == 1) {
				Collection<? extends SimplePrototypeClusterNode> subClusters = cluster.getSubClusters();
				Iterator<? extends SimplePrototypeClusterNode> it = subClusters.iterator();
				cluster = it.next();
			}
		}
		
		return cluster;
	}

	private final Map<CompetitiveVertex, List<Integer>> computeDataAssignment(CompetitiveTree tree, ObjectSet dataset) 
	{
		Map<CompetitiveVertex, List<Integer>> retVal = new HashMap<CompetitiveVertex, List<Integer>>();
		List<CompetitiveTreeNode> leafs = tree.getLeafs();
		for(int i=0, n=dataset.size(); i<n; i++)
		{
			double[] v = dataset.get(i).getRepresentation();
			double minDist = Double.POSITIVE_INFINITY;
			CompetitiveVertex nearestVertex = null;
			for(CompetitiveTreeNode node : leafs)
			{
				CompetitiveGraph graph = node.getGraph();
				for(Iterator<CompetitiveVertex> it = graph.vertices(); it.hasNext(); )
				{
					CompetitiveVertex candidate = it.next();
					double dist = metric.getDistance(v, candidate.getWeightVector());
					if(dist<minDist) {
						minDist = dist;
						nearestVertex = candidate;
					}
				}
			}
			
			List<Integer> vertexAssignments = retVal.get(nearestVertex);
			if(vertexAssignments == null) {
				vertexAssignments = new LinkedList<Integer>();
				retVal.put(nearestVertex, vertexAssignments);
			}
			vertexAssignments.add(i);
		}
		return retVal;
	}	
}

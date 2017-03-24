package de.ovgu.dke.clustering.msctest;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import de.ovgu.dke.clustering.algorithm.competitive.ArrayIndexedMST;
import de.ovgu.dke.clustering.algorithm.competitive.CompetitiveUtil;
import de.ovgu.dke.clustering.algorithm.hierarchical.HierarchicalClusterer;
import de.ovgu.dke.clustering.evaluation.BCubedFMeasure;
import de.ovgu.dke.clustering.evaluation.ClusterConfusionMatrix;
import de.ovgu.dke.clustering.evaluation.DaviesBouldinIndex;
import de.ovgu.dke.clustering.evaluation.Entropy;
import de.ovgu.dke.clustering.evaluation.MeanSquaredError;
import de.ovgu.dke.clustering.evaluation.NonLeafFMeasure;
import de.ovgu.dke.clustering.evaluation.Purity;
import de.ovgu.dke.clustering.evaluation.SilhouetteCoefficient;
import de.ovgu.dke.clustering.evaluation.StandardFMeasure;
import de.ovgu.dke.clustering.evaluation.SumSquaredError;
import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.ClusterNode;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.HierarchicalClusterModel;
import de.ovgu.dke.clustering.model.PrototypeCluster;
import de.ovgu.dke.clustering.model.PrototypeClusterNode;
import de.ovgu.dke.clustering.model.SimplePrototypeClusterNode;
import de.ovgu.dke.clustering.util.ClusteringUtility;
import de.ovgu.dke.tree.LinkedTree;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public class HierarchicalClusteringEvaluator 
{
	//TODO: make evaluation measures more generic
	private final StandardFMeasure<HierarchicalClusterModel> standardFMeasure = new StandardFMeasure<HierarchicalClusterModel>();
	private final NonLeafFMeasure nlFMeasure = new NonLeafFMeasure();
	private final BCubedFMeasure bcFMeasure = new BCubedFMeasure();
	//private final InstanceBasedFMeasure ibFMeasure = new InstanceBasedFMeasure();
	private final Entropy entropy = new Entropy();
	private final Purity purity = new Purity();
	private final SilhouetteCoefficient sCoefficient = new SilhouetteCoefficient();
	private final DaviesBouldinIndex dbIndex = new DaviesBouldinIndex();
	private final MeanSquaredError mse = new MeanSquaredError();
	private final SumSquaredError sse = new SumSquaredError();
	
	private ObjectSet dataset;
	private HierarchicalClusterer<? extends HierarchicalClusterModel> clusterer;
	
	private LinkedTree<String> referenceHierarchy;
	
	//results
	private HierarchicalClusterModel model;
	private FlatClusterModel flatModel;
	private ClusterConfusionMatrix confMatrix;
	private ClusterConfusionMatrix cumConfMatrix;
	
	private boolean flattenHierarchyOnLeafs = false;
	
	public void evaluate()
	{
		if(dataset == null)
			throw new IllegalStateException("no dataset applied");
		if(clusterer == null)
			throw new IllegalStateException("no clusterer applied");
		
		//build cluster model and compute the confusion matrix
		model = clusterer.computeClusterModel(dataset);
		confMatrix = ClusterConfusionMatrix.createClusterConfusionMatrix(model, false);
		cumConfMatrix = ClusterConfusionMatrix.createClusterConfusionMatrix(model, true);
		

		standardFMeasure.compute(model, cumConfMatrix);
		if(referenceHierarchy != null)
			nlFMeasure.compute(model, cumConfMatrix);
		if(flattenHierarchyOnLeafs) {
			flatModel = ClusteringUtility.flattenHierarchyOnLeafs(model);
			confMatrix = ClusterConfusionMatrix.createClusterConfusionMatrix(flatModel);
			entropy.compute(flatModel, confMatrix);
			bcFMeasure.compute(flatModel, confMatrix);
			purity.compute(flatModel, confMatrix);
			sCoefficient.compute(flatModel);
			dbIndex.compute(flatModel);
			mse.compute(flatModel);
			sse.compute(flatModel);	
		}
	}
	
	public void applyClusterModel(HierarchicalClusterModel model)
	{
		this.model = model;
		confMatrix = ClusterConfusionMatrix.createClusterConfusionMatrix(model, false);
		cumConfMatrix = ClusterConfusionMatrix.createClusterConfusionMatrix(model, true);
		

		standardFMeasure.compute(model, cumConfMatrix);
		if(referenceHierarchy != null)
			nlFMeasure.compute(model, cumConfMatrix);
		if(flattenHierarchyOnLeafs) {
			flatModel = ClusteringUtility.flattenHierarchyOnLeafs(model);
			confMatrix = ClusterConfusionMatrix.createClusterConfusionMatrix(flatModel);
			entropy.compute(flatModel, confMatrix);
			bcFMeasure.compute(flatModel, confMatrix);
			purity.compute(flatModel, confMatrix);
			sCoefficient.compute(flatModel);
			dbIndex.compute(flatModel);
			mse.compute(flatModel);
			sse.compute(flatModel);
		}
	}
	
	public void setFlattenHierarchyOnLeafs(boolean bool)
	{
		flattenHierarchyOnLeafs = bool;
	}
	
	public void applyClusterer(HierarchicalClusterer<? extends HierarchicalClusterModel> clusterer)
	{
		if(clusterer == null)
			throw new NullPointerException();
		this.clusterer = clusterer;
	}
	
	public void applyReferenceHierarchy(LinkedTree<String> hierarchy)
	{
		referenceHierarchy = hierarchy;
		if(referenceHierarchy != null)
			nlFMeasure.setReferenceHierarchy(hierarchy);
	}
	
	public void applyDataset(ObjectSet dataset)
	{
		if(dataset == null)
			throw new NullPointerException();
		this.dataset = dataset;
	}
	
	public void printResult()
	{
		System.out.println(createResultString());
	}
	
	public void writeResultToFile(String pathname)
	throws FileNotFoundException, IOException
	{
		FileWriter writer = new FileWriter(pathname);
		writer.write(createResultString());
		writer.flush();
		writer.close();
	}
	
	public void writeDataAssignmentToFile(String pathname)
	throws FileNotFoundException, IOException
	{
		assignData(dataset, model);
		dataset.writeToFile(pathname);
	}
	
	private final String createResultString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Settings:\n");
		builder.append(clusterer.getSettings().toString());
		builder.append("\n\nCluster Hierarchy:\n");
		builder.append(createHierarchicalClusterString(model.getRootCluster()));
		builder.append("\n\nConfusion Matrix:\n");
		builder.append(confMatrix.toString());
		builder.append("\n\nCumulated Confusion Matrix:\n");
		builder.append(cumConfMatrix.toString());
		if(flattenHierarchyOnLeafs) {
			builder.append("\n\nCluster-based Measures:\n");
			builder.append(createClusterBasedMeasureString());
		}
		builder.append("\n\nSupervised Measures:\n");
		builder.append(createClassBasedMeasureString());
		builder.append(createClusterCenterEstimationString(model));
		builder.append(createClusterSeperationValidString(model));
		return builder.toString();
	}
	
	private final String createClusterBasedMeasureString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Cluster\tSSE\tMSE\tDavies Bouldin Index\tSilhouette Coefficient\tPurity\tEntropy\n");
		Collection<? extends Cluster> clusters = flatModel.getClusters();
		for(Cluster cluster : clusters)
		{
			builder.append(cluster.toString());
			builder.append("\t");
			builder.append(sse.getValue(cluster.getId()));
			builder.append("\t");
			builder.append(mse.getValue(cluster.getId()));
			builder.append("\t");
			builder.append(dbIndex.getValue(cluster.getId()));
			builder.append("\t");
			builder.append(sCoefficient.getValue(cluster.getId()));
			builder.append("\t");
			builder.append(purity.getValue(cluster.getId()));
			builder.append("\t");
			builder.append(entropy.getValue(cluster.getId()));
			builder.append("\n");
		}
		builder.append("Overall:\t");
		builder.append(sse.getOverallValue());
		builder.append("\t");
		builder.append(mse.getOverallValue());
		builder.append("\t");
		builder.append(dbIndex.getOverallValue());
		builder.append("\t");
		builder.append(sCoefficient.getOverallValue());
		builder.append("\t");
		builder.append(purity.getOverallValue());
		builder.append("\t");
		builder.append(entropy.getOverallValue());
		builder.append("\n");
		
		return builder.toString();
	}
	
	private final String createHierarchicalClusterString(ClusterNode root)
	{
		StringBuilder sb = new StringBuilder();
		buildHierarchicalStrinRec(root, 0, sb);
		return sb.toString();
	}
	
	private final void buildHierarchicalStrinRec(ClusterNode node, int level, StringBuilder sb)
	{
		for(int i=0, n=level; i<n; i++)
			sb.append("| ");
		sb.append("|-");
		sb.append(node.toString());
		sb.append(":\t[");
		sb.append(node.size());
		sb.append(';');
		sb.append(node.numberOfDataInSubTree());
		sb.append("]\n");
	//	sb.append("]");
//		sb.append(Arrays.toString(((PrototypeClusterNode)node).getCentroid()) + "\n");
		
		Collection<? extends ClusterNode> children = node.getSubClusters();
		for(ClusterNode child : children)
			buildHierarchicalStrinRec(child, level+1, sb);
	}
	
	private final String createClassBasedMeasureString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Class\tStandard FMeasure\tBCubed FMeasure\n");
		Collection<String> classLabels = confMatrix.getClassLabels();
		for(String label : classLabels)
		{
			builder.append(label);
			builder.append("\t");
			builder.append(standardFMeasure.getValue(label));
			if(flattenHierarchyOnLeafs) {
				builder.append("\t");
				builder.append(bcFMeasure.getValue(label));
			}
			builder.append("\n");
		}
		builder.append("Overall:\t");
		builder.append(standardFMeasure.getOverallValue());
		if(flattenHierarchyOnLeafs) {
			builder.append("\t");
			builder.append(bcFMeasure.getOverallValue());
		}
		builder.append("\n");
		
		if(referenceHierarchy != null) {
			builder.append("\n");
			builder.append("Class\tNonLeaf Standard FMeasure\n");
			for(String label : referenceHierarchy)
			{
				if(!referenceHierarchy.isLeaf(label) && !referenceHierarchy.isRoot(label))
				{
					builder.append(label);
					builder.append('\t');
					builder.append(nlFMeasure.getValue(label));
					builder.append('\n');
				}
			}
			builder.append("Overall:\t");
			builder.append(nlFMeasure.getOverallValue());
			builder.append('\n');
		}
		
		return builder.toString();
	}
	
	private final void assignData(ObjectSet dataset, HierarchicalClusterModel model)
	{
		for(Cluster cluster : model.getClusters())
		{
			Collection<Integer> data = cluster.getData();
			for(Integer id : data)
				dataset.get(id).setLabel(cluster.toString());
		}
	}
	
	private final String createClusterCenterEstimationString(HierarchicalClusterModel model)
	{
		StringBuilder builder = new StringBuilder("\nCentroid Estimation\n");
		ObjectSet dataset = model.getDataset();
		DistanceMeasure metric = model.getDistanceMetric();
		Collection<? extends ClusterNode> clusters = model.getClusters();
		double overall = 0d;
		for(ClusterNode cluster : clusters)
		{
			double[] realCentroid = ClusteringUtility.computeCentroid(cluster.getDataInSubTree(), dataset);
			double dist = metric.getDistance(realCentroid, ((SimplePrototypeClusterNode)cluster).getCentroid());
			builder.append(cluster .toString()+ ":\t" + dist + "\n");
			overall += dist;
		}
		overall /= model.numberOfClusters();
		builder.append("Overall:\t" + overall + "\n\n");
		return builder.toString();
		
	}
	
	private final String createClusterSeperationValidString(HierarchicalClusterModel model)
	{
		StringBuilder sb = new StringBuilder("\nCluster Seperation Validation\n");
		DistanceMeasure metric = model.getDistanceMetric();
		LinkedList<ClusterNode> queue = new LinkedList<ClusterNode>();
		queue.add(model.getRootCluster());
		while(!queue.isEmpty())
		{
			ClusterNode cluster = queue.removeFirst();
			if(cluster.hasSubClusters()) {
				Collection<? extends ClusterNode> children = cluster.getSubClusters();
				sb.append(cluster.toString() + ":\n");
				PrototypeClusterNode[] prototypes = new PrototypeClusterNode[children.size()];
				int i=0;
				for(ClusterNode node : children)
					prototypes[i++] = (PrototypeClusterNode) node;
				ArrayIndexedMST<PrototypeClusterNode> mst = ClusteringUtility.computeMinimumSpanningTree(prototypes, metric);
				double cs = mst.minDistance()/mst.maxDistance();
				sb.append("cs = " + cs + "\n");
				sb.append(mst.toString());
				sb.append("\n");
				queue.addAll(children);
			}
		}
		
		return sb.toString();
	}
}

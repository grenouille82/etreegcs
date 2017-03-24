package de.ovgu.dke.clustering.msctest;

import java.io.IOException;

import de.ovgu.dke.clustering.algorithm.flat.KMeansClusterer;
import de.ovgu.dke.clustering.algorithm.hierarchical.HAClusterer;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public class HACExperiment 
{
	private static String evalPath = "/home/grenouille/irg_workspace/so_evaluation/clustering/";
	public static void main(String args[])
	{
		try {
			HierarchicalClusteringEvaluator evaluator = new HierarchicalClusteringEvaluator();
			ObjectSet dataset = new CSVObjectSet("/home/grenouille/irg_workspace/datasets/2d_gaussian_clusters.csv", "class");
			HAClusterer clusterer = new HAClusterer();
			
			//clusterer.applySettings(getReuter2Settings());
			clusterer.setMetric(DistanceMeasure.COSINE);
			
			evaluator.setFlattenHierarchyOnLeafs(true);
			//evaluator.applyReferenceHierarchy(ReferenceLabelHierarchies.getBanksearchHierarchy());
			evaluator.applyClusterer(clusterer);
			evaluator.applyDataset(dataset);
			evaluator.evaluate();
			evaluator.printResult();
			evaluator.writeResultToFile(evalPath+"2d_gaussian/hac_prune_result.txt");
			//evaluator.writeDataAssignmentToFile(evalPath+"3d_gaussian/kmeans_assign.txt");
			
		} catch(IOException e) {
			e.printStackTrace();
			
		}
		
	}
}

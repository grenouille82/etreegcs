package de.ovgu.dke.clustering.algorithm.flat;

import java.util.HashMap;

import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.SimpleCluster;
import de.ovgu.dke.clustering.model.SimpleFlatClusterModel;
import de.ovgu.dke.clustering.model.SimplePrototypeCluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Settings;

public class FakeSupervisedClusterer implements FlatClusterer<FlatClusterModel> 
{

	/**
	 * Note: The returned model has no distance metric.
	 */
	public FlatClusterModel computeClusterModel(ObjectSet dataset) 
	{
		if(dataset == null)
			throw new NullPointerException();
		if(!dataset.dataClassified())
			throw new IllegalArgumentException("dataset has no class information");
		
		SimpleFlatClusterModel model = new SimpleFlatClusterModel(dataset);
		HashMap<String, SimpleCluster> labelClusterMap = new HashMap<String, SimpleCluster>();
		for(int i=0, n=dataset.size(); i<n; i++)
		{
			String label = dataset.get(i).getClassLabel();
			if(label != null) {
				SimpleCluster cluster = labelClusterMap.get(label);
				if(cluster == null) {
					cluster = new SimpleCluster();
					labelClusterMap.put(label, cluster);
				}
				cluster.assignData(i);
			}
		}
		
		//TODO: use a flag to distinguish between prototype and non-prototype cluster
		for(SimpleCluster cluster : labelClusterMap.values())
		{
			SimplePrototypeCluster pCluster = new SimplePrototypeCluster(cluster);
			pCluster.computeCentroid(dataset);
			model.addCluster(pCluster);
		}
		model.setDistanceMetric(DistanceMeasure.COSINE);
		return model;
	}

	public void applyDefaultSettings() {}

	public void applySettings(Settings settings) {}

	public Settings getDefaultSettings() { return null; }

	public Settings getSettings() {	return null; }

}

package de.ovgu.dke.clustering.algorithm;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.ClusterModel;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parametrizable;

public interface Clusterer<T extends ClusterModel<? extends Cluster>> extends Parametrizable
{
	T computeClusterModel(ObjectSet dataset);
}

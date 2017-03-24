package de.ovgu.dke.clustering.algorithm.competitive;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parametrizable;

public interface CompetitiveLearningAlgorithm<DS extends CompetitiveDataStructure>
extends Parametrizable
{
	public DS learn(ObjectSet dataset);
	
	public DS relearn(DS dataStructure, ObjectSet dataset);
	
	//TODO: remove this, if the metric is integrated in the parametrizing framework
	public void setMetric(DistanceMeasure metric);
	
	public DistanceMeasure getMetric();
}

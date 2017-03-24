package de.ovgu.dke.clustering.algorithm.hierarchical;


public class CompleteLinkDistance implements ClusterDistanceMeasure
{

	public double calculateUnionDistance(double distanceXZ, double distanceYZ, double distanceXY, 
										 int sizeX, int sizeY, int sizeZ) 
 
	{
		return Math.max(distanceXZ, distanceYZ);
	}

}

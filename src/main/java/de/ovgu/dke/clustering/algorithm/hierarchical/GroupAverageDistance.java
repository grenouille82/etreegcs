package de.ovgu.dke.clustering.algorithm.hierarchical;

public class GroupAverageDistance implements ClusterDistanceMeasure 
{
	public double calculateUnionDistance(double distanceXZ, double distanceYZ, double distanceXY, 
										 int sizeX, int sizeY, int sizeZ) 
	{
		return (distanceXZ*sizeX+distanceYZ*sizeY)/((double) sizeX+sizeY);
	}

}

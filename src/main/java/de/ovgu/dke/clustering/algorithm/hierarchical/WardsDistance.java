package de.ovgu.dke.clustering.algorithm.hierarchical;


public class WardsDistance implements ClusterDistanceMeasure 
{

	public double calculateUnionDistance(double distanceXZ, double distanceYZ, double distanceXY, 
										 int sizeX, int sizeY, int sizeZ) 
	{
		return (distanceXZ*(sizeX+sizeZ)+distanceYZ*(sizeY+sizeZ)-distanceXY*sizeZ)/((double) sizeX+sizeY+sizeZ);
	}

}

package de.ovgu.dke.clustering.algorithm.hierarchical;

import de.ovgu.dke.util.MathUtil;

public class CentroidDistance implements ClusterDistanceMeasure 
{	
	public double calculateUnionDistance(double distanceXZ, double distanceYZ, double distanceXY, 
			 							 int sizeX, int sizeY, int sizeZ) 
 
	{
		return (distanceXZ*sizeX+distanceYZ*sizeY)/((double) sizeX+sizeY) +
		       (-sizeX*sizeY*distanceXY)/(MathUtil.square(sizeX+sizeY));
	}
}

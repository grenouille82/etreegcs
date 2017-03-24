package de.ovgu.dke.clustering.algorithm.hierarchical;


public interface ClusterDistanceMeasure 
{
	/**
	 * Calculate the new distance between the two joint cluster (x,y) and the cluster z. This
	 * computation is based on the lance-williams formula. 
	 * 
	 * @param distanceXZ
	 * @param distanceYZ
	 * @param distanceXY
	 * @param sizeX
	 * @param sizeY
	 * @param sizeZ
	 * @return
	 */
	public double calculateUnionDistance(double distanceXZ, double distanceYZ, double distanceXY, 
										 int sizeX, int sizeY, int sizeZ);

}

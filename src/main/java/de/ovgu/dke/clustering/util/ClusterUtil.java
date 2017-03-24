package de.ovgu.dke.clustering.util;

import java.util.Arrays;

import de.ovgu.dke.clustering.model.PrototypeCluster;

public final class ClusterUtil 
{
	private ClusterUtil() {}
	
	public static int[] bestDimensions(PrototypeCluster cluster, int n)
	{
		int dimensions[] = new int[n];
		Arrays.fill(dimensions, Integer.MIN_VALUE);
		
		double minScore = Integer.MIN_VALUE;
		int minIdx 	 = 0;
		
		double centroid[] = cluster.getCentroid();
		for(int i=0; i<centroid.length; i++)
		{
			if(centroid[i] > minScore) {
				dimensions[minIdx] = i;
				minScore = centroid[i];
				
				//find minimum value and index 
				for(int j=0; j<n; j++)
				{
					if(dimensions[j] == Integer.MIN_VALUE) {
						minScore = Integer.MIN_VALUE;
						minIdx = j;
						break;
					} 
					
					int idx = dimensions[j];
					if(centroid[idx] < minScore) {
						minScore = centroid[idx];
						minIdx = j;
					}
				}
			}
		}
		return dimensions;
	}

}

package de.ovgu.dke.clustering.algorithm.hierarchical;

public enum ClusterDistanceMethod 
{
	SingleLink,
	CompleteLink,
	GroupAverage,
	WardsMethod,
	CentroidMethod;
	
	public static ClusterDistanceMeasure createClusterDistanceMeasure(ClusterDistanceMethod method)
	{
		if(method == null)
			throw new NullPointerException();
		
		switch(method)
		{
		case SingleLink:
			return new SingleLinkDistance();
		case CompleteLink:
			return new CompleteLinkDistance();
		case GroupAverage:
			return new GroupAverageDistance();
		case WardsMethod:
			return new WardsDistance();
		case CentroidMethod:
			return new CentroidDistance();
		default:
			return new CompleteLinkDistance();
		}
	}
}

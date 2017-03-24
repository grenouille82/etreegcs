package de.ovgu.dke.clustering.algorithm.hierarchical;

import de.ovgu.dke.util.Settings;

public enum ClusterExtractorType 
{
	KForwardClusterIndex;
	
	public static ClusterHierarchyExtractor createExtractor(ClusterExtractorType type)
	{
		return createExtractor(type, null);
	}
	
	public static ClusterHierarchyExtractor createExtractor(ClusterExtractorType type, Settings setting)
	{
		if(type == null)
			throw new NullPointerException();
		
		ClusterHierarchyExtractor retVal = null;
		switch(type)
		{
		case KForwardClusterIndex:
			retVal = new KForwardClusterIndexExtractor();
			break;
		default:
			throw new IllegalArgumentException();
		}
		retVal.applySettings(setting);
		return retVal;
	}
}

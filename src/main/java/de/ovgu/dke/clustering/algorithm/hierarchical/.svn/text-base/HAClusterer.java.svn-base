package de.ovgu.dke.clustering.algorithm.hierarchical;



import de.ovgu.dke.clustering.util.ProximityMatrix;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.Parameter;
import de.ovgu.dke.util.Settings;

public class HAClusterer implements HierarchicalClusterer<HAClusterModel> 
{
	private static final DistanceMeasure DEFAULT_METRIC = DistanceMeasure.EUCLIDEAN;
	
	private static final ClusterDistanceMethod DEFAULT_DISTANCE_METHOD = ClusterDistanceMethod.CentroidMethod;
	
	private static final ClusterExtractorType DEFAULT_CLUSTER_EXTRACTOR_TYPE = ClusterExtractorType.KForwardClusterIndex;
	private static final Settings DEFAULT_CLUSTER_EXTRACTOR_SETTINGS = null;
	
	private static final int DEFAULT_MIN_CLUSTERS = 1;
	
	private DistanceMeasure metric;
	
	private ClusterHierarchyExtractor clusterExtractor;
	private ClusterExtractorType extractorType;
	private Settings extractorSettings;
	
	private ClusterDistanceMeasure clusterMeasure;
	private ClusterDistanceMethod distanceMethod;

	private int minClusters;
	
	public HAClusterer()
	{
		applyDefaultSettings();
	}
	
	public HAClusterModel computeClusterModel(ObjectSet dataset) 
	{
		if(dataset == null)
			throw new NullPointerException();
		
		final int n = dataset.size();
		
		HAClusterModel model = new HAClusterModel(dataset);
		HAClusterNode[] clusters = new HAClusterNode[n];
		
		//initialize proximity matrix
		ProximityMatrix proxMatrix = ProximityMatrix.createProximityMatrix(dataset, metric);
		
		//initialize clusters (leaf clusters of the hierarchy)
		for(int i=0; i<n; i++)
		{
			clusters[i] = new HAClusterNode();
			clusters[i].assignData(i);
		}
		
		//hierarchical clustering process (main-loop)
		int lastInsert = -1;
		for(int numClusters=n; numClusters>minClusters; numClusters--)
		{
			double minDist = Double.POSITIVE_INFINITY;
			int x = -1, y =-1;
			//find two clusters i,j with minimum distance
			for(int i=0; i<n; i++)
				if(clusters[i] != null) {
					for(int j=i+1; j<n; j++)
					{
						if(clusters[j] != null && proxMatrix.getValue(i, j) < minDist) {
							minDist = proxMatrix.getValue(i, j);
							x = i;
							y = j;
						}
							
					}
				}
			//System.out.println(numClusters + "\t" + minDist+ "\t" + x + "\t"+y);
			double distXZ, distYZ,newDist;
			double distXY = minDist;
			int sizeX = clusters[x].numberOfDataInSubTree();
			int sizeY = clusters[y].numberOfDataInSubTree();
			int sizeZ;
			//update proximity matrix
			for(int z=0; z<n; z++)
			{
				if(clusters[z] != null) {
					sizeZ = clusters[z].size();
				//	System.out.println(x +"\t" +z);
					distXZ = proxMatrix.getValue(x, z);
					distYZ = proxMatrix.getValue(y, z);
					newDist = clusterMeasure.calculateUnionDistance(distXZ, distYZ, distXY, sizeX, sizeY, sizeZ);
					proxMatrix.setValue(x, z, newDist);
					proxMatrix.setValue(y, z, Double.POSITIVE_INFINITY);
				}
			}
			
			//merge the two closest clusters
			HAClusterNode newCluster = new HAClusterNode(minDist);
			newCluster.addSubCluster(clusters[x]);
			newCluster.addSubCluster(clusters[y]);
			clusters[x] = newCluster;
			clusters[y] = null;
			
			lastInsert = x;
		}
		
		HAClusterNode root = null;
		if(minClusters==1) {
			root = clusters[lastInsert];
		} else {
			root = new HAClusterNode();
			for(int i=0; i<n; i++)
			{
				if(clusters[i] != null)
					root.addSubCluster(clusters[i]);
			}
		}
		
		//aggregate
		model.setDistanceMetric(metric);
		model.setRootCluster(root);
		if(clusterExtractor != null)
			model = clusterExtractor.extract(model);
		return model;
	}

	public void applyDefaultSettings() 
	{
		metric = DEFAULT_METRIC;
		minClusters = DEFAULT_MIN_CLUSTERS;
		setClusterDistanceMethod(DEFAULT_DISTANCE_METHOD);
		setClusterExtractionType(DEFAULT_CLUSTER_EXTRACTOR_TYPE, DEFAULT_CLUSTER_EXTRACTOR_SETTINGS);
	}

	public void applySettings(Settings settings) 
	{
		if(settings != null) {
			Parameter p = settings.getParameter("minClusters");
			if(p!=null)
				setMinNumberOfClusters(Integer.valueOf(p.getValue()));
			
			p = settings.getParameter("clusterExtractor");
			if(p!=null) 
				setClusterExtractionType(ClusterExtractorType.valueOf(p.getValue()), p.getSettings());
			
			p = settings.getParameter("distanceMethod");
			if(p!=null)
				setClusterDistanceMethod(ClusterDistanceMethod.valueOf(p.getValue()));
			
		}
	}

	public Settings getDefaultSettings() 
	{
		Settings setting = new Settings();
		setting.setParameter(new Parameter("minClusters", Integer.toString(DEFAULT_MIN_CLUSTERS)));
		setting.setParameter(new Parameter("distanceMethod", DEFAULT_DISTANCE_METHOD.name()));
		if(extractorType != null) {
			Parameter p = new Parameter("clusterExtractor", DEFAULT_CLUSTER_EXTRACTOR_TYPE.name());
			p.setSettings(clusterExtractor.getSettings());
			setting.setParameter(p);
		}
		return setting;
	}

	public Settings getSettings() 
	{
		Settings setting = new Settings();
		setting.setParameter(new Parameter("minClusters", Integer.toString(minClusters)));
		setting.setParameter(new Parameter("distanceMethod", distanceMethod.name()));
		if(extractorType != null) {
			Parameter p = new Parameter("clusterExtractor", extractorType.name());
			p.setSettings(clusterExtractor.getSettings());
			setting.setParameter(p);
		}
		return setting;
	}
	
	public DistanceMeasure getMetric()
	{
		return metric;
	}
	
	public void setMetric(DistanceMeasure metric)
	{
		if(metric == null)
			throw new NullPointerException();
		this.metric = metric;
	}
	
	public int getMinNumberOfClusters()
	{
		return minClusters;
	}
	
	public void setMinNumberOfClusters(int n)
	{
		if(n<1)
			throw new IllegalArgumentException();
		minClusters = n;
	}
	
	public ClusterExtractorType getClusterExtractorType()
	{
		return extractorType;
	}
	
	public Settings getClusterExtractorSettings()
	{
		return extractorSettings;
	}
	
	public void setClusterExtractionType(ClusterExtractorType type, Settings settings)
	{
		if(type == null) {
			clusterExtractor 	= null;
			extractorSettings 	= null;
			extractorType 		= null;
		} else {
			clusterExtractor 	= ClusterExtractorType.createExtractor(type, settings);
			extractorSettings 	= settings;
			extractorType		= type;
		}
	}
	
	public ClusterDistanceMethod getClusterDistanceMethod()
	{
		return distanceMethod;
	}
	
	public void setClusterDistanceMethod(ClusterDistanceMethod method)
	{
		if(method == null)
			throw new NullPointerException();
		distanceMethod = method;
		clusterMeasure = ClusterDistanceMethod.createClusterDistanceMeasure(method);
	}

}

package de.ovgu.dke.clustering.msctest;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import de.ovgu.dke.clustering.algorithm.flat.FlatClusterer;
import de.ovgu.dke.clustering.evaluation.BCubedFMeasure;
import de.ovgu.dke.clustering.evaluation.ClusterConfusionMatrix;
import de.ovgu.dke.clustering.evaluation.DaviesBouldinIndex;
import de.ovgu.dke.clustering.evaluation.Entropy;
import de.ovgu.dke.clustering.evaluation.MeanSquaredError;
import de.ovgu.dke.clustering.evaluation.Purity;
import de.ovgu.dke.clustering.evaluation.SilhouetteCoefficient;
import de.ovgu.dke.clustering.evaluation.StandardFMeasure;
import de.ovgu.dke.clustering.evaluation.SumSquaredError;
import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.util.CSVObjectSet;
import de.ovgu.dke.util.ObjectSet;

public class FlatClusterEvaluator 
{
	//TODO: make evaluation measures more generic
	private final StandardFMeasure<FlatClusterModel> standardFMeasure = new StandardFMeasure<FlatClusterModel>();
	private final BCubedFMeasure bcFMeasure = new BCubedFMeasure();
	private final Entropy entropy = new Entropy();
	private final Purity purity = new Purity();
	private final SilhouetteCoefficient sCoefficient = new SilhouetteCoefficient();
	private final DaviesBouldinIndex dbIndex = new DaviesBouldinIndex();
	private final MeanSquaredError mse = new MeanSquaredError();
	private final SumSquaredError sse = new SumSquaredError();
	
	private ObjectSet dataset;
	private FlatClusterer<? extends FlatClusterModel> clusterer;
	
	//results
	private FlatClusterModel model;
	private ClusterConfusionMatrix confMatrix;
	
	public void evaluate()
	{
		if(dataset == null)
			throw new IllegalStateException("no dataset applied");
		if(clusterer == null)
			throw new IllegalStateException("no clusterer applied");
		
		//build cluster model and compute the confusion matrix
		model = clusterer.computeClusterModel(dataset);
		confMatrix = ClusterConfusionMatrix.createClusterConfusionMatrix(model);
		
		entropy.compute(model, confMatrix);
		bcFMeasure.compute(model, confMatrix);
		standardFMeasure.compute(model, confMatrix);
		purity.compute(model, confMatrix);
		sCoefficient.compute(model);
		dbIndex.compute(model);
		mse.compute(model);
		sse.compute(model);	
	}
	
	public void applyClusterer(FlatClusterer<? extends FlatClusterModel> clusterer)
	{
		if(clusterer == null)
			throw new NullPointerException();
		this.clusterer = clusterer;
	}
	
	public void applyDataset(ObjectSet dataset)
	{
		if(dataset == null)
			throw new NullPointerException();
		this.dataset = dataset;
	}
	
	public void printResult()
	{
		System.out.println(createResultString());
	}
	
	public void writeResultToFile(String pathname)
	throws FileNotFoundException, IOException
	{
		FileWriter writer = new FileWriter(pathname);
		writer.write(createResultString());
		writer.flush();
		writer.close();
	}
	
	public void writeDataAssignmentToFile(String pathname)
	throws FileNotFoundException, IOException
	{
		assignData(dataset, model);
		((CSVObjectSet) dataset).writeToFile(pathname);
	}
	
	private final String createResultString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Settings:\n");
		builder.append(clusterer.getSettings().toString());
		builder.append("\n\nConfusion Matrix:\n");
		builder.append(confMatrix.toString());
		builder.append("\n\nCluster-based Measures:\n");
		builder.append(createClusterBasedMeasureString());
		builder.append("\n\nSupervised Measures:\n");
		builder.append(createClassBasedMeasureString());
		return builder.toString();
	}
	
	private final String createClusterBasedMeasureString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Cluster\tSSE\tMSE\tDavies Bouldin Index\tSilhouette Coefficient\tPurity\tEntropy\n");
		Collection<? extends Cluster> clusters = model.getClusters();
		for(Cluster cluster : clusters)
		{
			builder.append(cluster.toString());
			builder.append("\t");
			builder.append(sse.getValue(cluster.getId()));
			builder.append("\t");
			builder.append(mse.getValue(cluster.getId()));
			builder.append("\t");
			builder.append(dbIndex.getValue(cluster.getId()));
			builder.append("\t");
			builder.append(sCoefficient.getValue(cluster.getId()));
			builder.append("\t");
			builder.append(purity.getValue(cluster.getId()));
			builder.append("\t");
			builder.append(entropy.getValue(cluster.getId()));
			builder.append("\n");
		}
		builder.append("Overall:\t");
		builder.append(sse.getOverallValue());
		builder.append("\t");
		builder.append(mse.getOverallValue());
		builder.append("\t");
		builder.append(dbIndex.getOverallValue());
		builder.append("\t");
		builder.append(sCoefficient.getOverallValue());
		builder.append("\t");
		builder.append(purity.getOverallValue());
		builder.append("\t");
		builder.append(entropy.getOverallValue());
		builder.append("\n");
		
		return builder.toString();
	}
	
	private final String createClassBasedMeasureString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Class\tStandard FMeasure\tBCubed FMeasure\n");
		Collection<String> classLabels = confMatrix.getClassLabels();
		for(String label : classLabels)
		{
			builder.append(label);
			builder.append("\t");
			builder.append(standardFMeasure.getValue(label));
			builder.append("\t");
			builder.append(bcFMeasure.getValue(label));
			builder.append("\n");
		}
		builder.append("Overall:\t");
		builder.append(standardFMeasure.getOverallValue());
		builder.append("\t");
		builder.append(bcFMeasure.getOverallValue());
		builder.append("\n");
		
		return builder.toString();
	}
	
	private final void assignData(ObjectSet dataset, FlatClusterModel model)
	{
		for(Cluster cluster : model.getClusters())
		{
			Collection<Integer> data = cluster.getData();
			for(Integer id : data)
				dataset.get(id).setLabel(cluster.toString());
		}
	}
}

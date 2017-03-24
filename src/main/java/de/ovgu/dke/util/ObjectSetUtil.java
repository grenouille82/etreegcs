package de.ovgu.dke.util;

public final class ObjectSetUtil 
{
	private ObjectSetUtil() {}
	
	public static void tfidfWeighting(ObjectSet dataset)
	{
		if(dataset != null) {
			int n = dataset.size();
			int m = dataset.getObjectSize();
			
			double maxFrequencies[] = new double[n];
			int attributeOccurences[] = new int[m];
			
			for(int i=0; i<n; i++)
			{
				ObjectWrapper wrapper = dataset.get(i);
				double[] vector = wrapper.getRepresentation();
				
				for(int j=0; j<m; j++)
				{
					if(vector[j] != 0.0) {
						attributeOccurences[j]++;
						if(maxFrequencies[i] < vector[j])
							maxFrequencies[i] = vector[j];
					}
				}
			}
			
			for(int i=0; i<n; i++)
			{
				ObjectWrapper wrapper = dataset.get(i);
				double[] vector = wrapper.getRepresentation();
			
				for(int j=0; j<m; j++)
				{
					double nf	= vector[j]/maxFrequencies[i];
					double iaf	= MathUtil.log2((double)n/attributeOccurences[j]);
					vector[j] 	= nf*iaf;
					if(Double.isNaN(vector[j]))
						vector[j] = 0.0d;
				}
			}
			
		}
	}
}

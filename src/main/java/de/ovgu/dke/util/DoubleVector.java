package de.ovgu.dke.util;

import java.util.Arrays;
import java.util.Random;

public class DoubleVector {
	
	static Random r = new Random(1);
	
	public static double[] asDoubleVector(float[] f) {
		double[] d = new double[f.length];
		for (int i = 0; i < f.length; i++)
			d[i] = f[i];
//		System.arraycopy(f, 0, d, 0, f.length);
		return d;
	}

    public static double[] copy(double[] d) {
    	double[] d2 = new double[d.length];        
        System.arraycopy(d, 0, d2, 0, d.length);
        return d2;
    }        
    
    /**
     * True if all vector elements are zero.
     * 
     * @return 
     */
    public static boolean isZero(double[] d) {
        for (int i = 0; i < d.length; i++) 
        	if (d[i] != 0) 
        		return false;
        return true;
    }
    
    public static double innerProduct(double[] v1, double[] v2) {
    	double sum = 0;
    	for (int i = v1.length; --i >= 0; ) 
    		sum += v1[i] * v2[i];
    	return sum;
    }
    
    public static double weightedInnerProduct(double[] v1, double[] v2,	double[] w) {
    	if (w == null)
    		return innerProduct(v1, v2);
    	double sum = 0;
    	for (int i = v1.length; --i >= 0; ) 
    		sum += w[i] * v1[i] * v2[i];
    	return sum;
	}
    
    public static double vectLength(double[] v) {
    	return Math.sqrt(innerProduct(v, v));
    }
    
    public static double elementSum(double[] v) {
    	double sum = 0;
    	for (int i = v.length; --i >= 0; ) 
    		sum += v[i];
    	return sum;
    }

//    /**
//     * Set all vector elements to zero.
//     * 
//     * @return 
//     */
//    public ObjDesc setZero() {
//        //for(int i=0; i<desc.length; i++) desc[i]=0.f;
//        Arrays.fill(desc, 0.f);
//        return this;
//    }
//
//    /**
//     * Set all vector elements to one.
//     * 
//     * @return 
//     */
//    public ObjDesc setOne() {
//        //for(int i=0; i<desc.length; i++) desc[i]=1.f;
//        Arrays.fill(desc, 1.f);
//        return this;
//    }
    
    /** Subtract element-wise, Compute difference vector (objects are not changed). */	
    public static double[] diffVect(double[] v1, double[] v2) {
    	return diffVect(v1, v2, new double[v1.length]);
    }
        
    public static double[] diffVect(double[] v1, double[] v2, double[] target) {
        for(int i = v1.length; --i >= 0; ) 
            target[i] = v1[i] - v2[i];
        return target;
    }    
    
//    /** Subtract element-wise */
//    public static double[] subVect(double[] d) {
//      for(int i=desc.length; --i>=0; ) desc[i]-=d.desc[i];
//    }
    
    /** Add element-wise */
    public static double[] addVect(double[] v1, double[] v2) {
    	return addVect(v1, v2, new double[v1.length]);
    }
        
    public static double[] addVect(double[] v1, double[] v2, double[] target) {
        for(int i = v1.length; --i >= 0; )
            target[i] = v1[i] + v2[i];
        return target;
    }    
 

    /** Multiply elementwise */
    public static double[] multElementwise(double[] v1, double[] v2) {
    	return multElementwise(v1, v2, new double[v1.length]);
    }
        
    public static double[] multElementwise(double[] v1, double[] v2, double[] target) {
        for(int i = v1.length; --i >= 0; )
            target[i] = v1[i] * v2[i];
        return target;
    }
    

    /** Multiply all elements with the scalar s */
    public static double[] multScalar(double[] v, double s) {
    	return multScalar(v, s, new double[v.length]);
    }
        
    public static double[] multScalar(double[] v, double s, double[] target) {
        for (int i = v.length; --i >= 0; )
            target[i] = s * v[i];
        return target;
    }
    
    
    /** Assign absolute values to all elements */
    public static double[] abs(double[] v) {
    	return abs(v, new double[v.length]);
    }
        
    public static double[] abs(double[] v, double[] target) {
        for (int i = v.length; --i >= 0; )
            target[i] = (v[i] < 0) ? -v[i] : v[i];
        return target;
    }        
    
    /** Normalize the vector to length 1.0 */
    public static double[] normalize(double[] v) {
    	return normalize(v, new double[v.length]);
    }    
        
    public static double[] normalize(double[] v, double[] target) {
    	double len = vectLength(v);
    	if (len<=0) 
    		return target; // FIXME: make sure it's all zeros    	
    	len = 1.0 / len; //Math.sqrt(len);    	        
        return multScalar(v, len, target);
    }   

    /** Normalize as weighting vector (sum = #elements) */
    public static double[] normWeightingVec(double[] v) {
    	return normWeightingVec(v, new double[v.length]);
    }
    
    public static double[] normWeightingVec(double[] v, double[] target) {
    	double sum = elementSum(v);
    	if (sum<=0) 
    		return target; // FIXME: make sure it's all zeros    	
    	sum = 1.0 / sum; //v.length / sum; //1.0 / Math.sqrt(len);    	        
        return multScalar(v, sum, v);
    }
    
    /** Compute linear interpolation of vectors using difference:
     * @param d vector used for extension
     * this = this + m*(d - this)
     * target = v1 + s*(v2 - v1)
     */        
    public static double[] linInterpolVect(double[] v1, double[] v2, double s) {
    	return linInterpolVect(v1, v2, s, new double[v1.length]);
    }
    
    public static double[] linInterpolVect(double[] v1, double[] v2, double s, double[] target) {
        for (int i = v1.length; --i >= 0; ) {
            target[i] = v1[i] + s * (v2[i] - v1[i]);
        }
        return target;
    }
    
    /** Compute extension of vector using difference:
     * @param d vector used for extension
     * this = this + d1 + (d1 - d2)
     * target = v + v1 + (v1 - v2)
     * @deprecated do we really need such special stuff?
     */
//    public static double[] extendVect(double[] d1, double[] d2, double fact) {
//    	for (int i = desc.length; --i >= 0; ) 
//    		desc[i] += d1.desc[i] + fact * (d1.desc[i] - d2.desc[i]);
//    }            
    public static double[] extendVect(double[] v, double[] v1, double[] v2, double s) {
    	return extendVect(v, v1, v2, s, new double[v1.length]);
    }
    
    /**
     * @deprecated do we really need such special stuff?
     */
    public static double[] extendVect(double[] v, double[] v1, double[] v2, double s, double[] target) {
        for(int i = v1.length; --i >= 0; )
            target[i] = v[i] + v1[i] + s * (v1[i] - v2[i]);
        return target;
    }
    
	public static double[] fill(int len, double value) {
    	return fill(new double[len], value);
    }
    
    public static double[] fill(double[] v, double value) {
    	Arrays.fill(v, value);  	
    	return v;
    }  

	public static double[] zeros(int len) {
    	return zeros(new double[len]);
    }
    
    public static double[] zeros(double[] v) {
    	Arrays.fill(v, 0);  	
    	return v;
    }
    
    public static double[] ones(int len) {
    	return ones(new double[len]);
    }
    
    public static double[] ones(double[] v) {
    	Arrays.fill(v, 1);  	
    	return v;
    }
    
	public static double[] uniformWeightVector(int len) {
		return uniformWeightVector(new double[len]);
	}
	
	public static double[] uniformWeightVector(double[] v) {
		Arrays.fill(v, 1.0 / (double) v.length);  	
		return v;
	}
    
    /** Create a random vector (uses java.util.Random to create elements). */
    public static double[] random(int len) {
    	return random(new double[len]);
    }
    
    public static double[] random(double[] v) {
    	for (int i = 0; i < v.length; i++)
    		v[i] = r.nextDouble();    	
    	return v;
    }

    /** Create a random signed vector ([-1,+1); uses java.util.Random to create elements). */
    public static double[] randomSigned(int len) {
    	return randomSigned(new double[len]);
    }
    
    public static double[] randomSigned(double[] v) {
    	for (int i = 0; i < v.length; i++)
    		v[i] = 2*r.nextDouble()-1;    	
    	return v;
    }

    /** Create a random binary vector (uses java.util.Random to create elements). */
    public static double[] randomBinary(int len) {
    	return randomBinary(new double[len]);
    }
    
    public static double[] randomBinary(double[] v) {
    	for (int i = 0; i < v.length; i++) 
    		v[i] = (r.nextDouble() >=0.5f) ? 1 : 0;    	
    	return v;
    }	

    /** Create a random weight vector (uses java.util.Random to create elements). */
    public static double[] randomWeights(int len) {
    	return randomWeights(new double[len]);
    }
    
    public static double[] randomWeights(double[] v) {
    	double sum = 0;
    	for (int i = 0; i < v.length; i++) {
    		v[i] = r.nextDouble();
    		sum += v[i];
    	}
    	if (sum == 0)
    		return ones(v);
    	
    	//sum = 1.0 / sum; // v.length / sum; // 1.0 / Math.sqrt(len);     	
    	return multScalar(v, 1.0 / sum, v);
    }
    
    /**********/
    
    /**
     * Compute Euclidean distance (object is not changed).
     * 
     * @param d
     * 
     * @return 
     */
    public static double euclDist(double[] v1, double[] v2) {
      return Math.sqrt(euclDistSQR(v1, v2, Double.MAX_VALUE));
    }
    /**
     * 
     * @param d
     * @param thres
     * 
     * @return 
     */
    public static double euclDistSQR(double[] v1, double[] v2, double thres) {
        double dist=0, diff;
        for (int i = v1.length; --i >= 0 && dist <= thres; ) {
          diff = v1[i] - v2[i];
          dist += diff * diff;
        }
        return dist;
    }

    /** Compute weighted Euclidean distance (object is not changed). */
    public static double weightedEuclDist(double[] v1, double[] v2, double[] w) {      
      return (double)Math.sqrt(weightedEuclDistSQR(v1, v2, w, Double.MAX_VALUE));
    }
    
    public static double weightedEuclDistSQR(double[] v1, double[] v2, double[] w, double thres) {
    	if (w == null)
    		return euclDistSQR(v1, v2, thres);
        double dist=0, diff;
        for (int i = v1.length; --i >= 0 && dist <= thres; ) {
          diff = v1[i] - v2[i];
          dist += w[i] * diff * diff;
        }
        return dist;
    }

    /** Compute weighted inner product (object is not changed). */
    public static double weightedCosine(double[] v1, double[] v2, double[] w) {
    	if (w == null)
    		return cosine(v1, v2);
        double cos = 0, len1 = 0, len2 = 0, p1, p2;
        for(int i = 0; i < v1.length; i++) {
            p1 = v1[i] * w[i];
            p2 = v2[i]; 				//*w.desc[i]; mod stober 2007-04-26
            len1 += p1 * p1;
            len2 += p2 * p2;
            cos += p1 * p2;
        }
        cos = cos / (Math.sqrt(len1) * Math.sqrt(len2));
        return cos;
    }
    
    /** Compute weighted inner product (object is not changed). */
    public static double cosine(double[] v1, double[] v2) {
    	if (v1.length != v2.length)
    		throw new IllegalArgumentException("vectors must have same number of elements");
        double cos = 0, len1 = 0, len2 = 0;
        for(int i = 0; i < v1.length; i++) {
            len1 += v1[i] * v1[i];
            len2 += v2[i] * v2[i];
            cos += v1[i] * v2[i];
        }
        double normFactor = (Math.sqrt(len1 * len2));
        if(normFactor != 0d)
        	cos /= normFactor;  // cosine normalization
        return cos;
    }


    
    



}

package de.ovgu.dke.util;

import de.ovgu.dke.aucoma.db.ResourceHandle;

/**
 * A basic object, stores its description, its label, etc...
 * @author  klose
 * @version
 */
public abstract class ObjectWrapper {

    /**
     * object properties used for clustering
     */
	public final double[] representation;
    
    /**
     * reference to the real object and its meta-data
     */
    public final Object linkedObject;
   
    /**
     * for classified data
     */
    protected String  classLabel;
    protected String  label;

    
    public ObjectWrapper(Object object, double[] representation) {
    	this.representation = representation;
    	this.linkedObject = object;
    }
    
//    public double  score;
    
	public String getClassLabel() {
		return classLabel;
	}
	public void setClassLabel(String clazz) {
		this.classLabel = clazz;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
//	public void setCurrentPos(GridPos currentpos) {
//		this.currentPos = currentpos;
//	}
	
	public double[] getRepresentation() {
		return representation;
	}

	/** to be overwritten */
	public ResourceHandle getIconHandle() {
		return null;
	}
	
	/** to be overwritten */
	public String getShortDescription() {
		return "ObjectWrapper";
	}
	
     
    
    
}

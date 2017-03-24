package de.ovgu.dke.clustering.util;

import de.ovgu.dke.util.Parametrizable;

/**
 *
 * @author hermkes
 *
 */
public interface PrototypeInitializer extends Parametrizable
{
	public double[] nextVector();
	
	public double[][] getNVectors(int n);
}

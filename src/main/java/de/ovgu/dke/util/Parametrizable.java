package de.ovgu.dke.util;

/**
 * 
 * @author mhermkes
 *
 */
public interface Parametrizable 
{
	/**
	 * 
	 * @param settings
	 */
	public void applySettings(Settings settings);
	
	/**
	 * 
	 *
	 */
	public void applyDefaultSettings();
	
	/**
	 * 
	 * @return
	 */
	public Settings getSettings();
	
	/**
	 * 
	 * @return
	 */
	public Settings getDefaultSettings();
	
}

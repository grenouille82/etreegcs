package de.ovgu.dke.clustering.algorithm.competitive;

import de.ovgu.dke.util.Settings;


public enum WinnerSearchMethodType 
{
	NaiveSearch,
	GreedySearch,
	LocalSearch,
	GlobalSearch;
	
	public static WinnerSearchMethod createWinnerSearchMethod(WinnerSearchMethodType type)
	{
		if(type == null)
			throw new NullPointerException();
		
		WinnerSearchMethod retVal = null;
		switch(type)
		{
		case LocalSearch:
			retVal = new LocalSearchMethod();
			break;
		case GreedySearch:
			retVal = new GreedySearchMethod();
			break;
		case GlobalSearch:
			retVal = new GlobalSearchMethod();
			break;
		case NaiveSearch:
		default:
			retVal = new NaiveSearchMethod();
			break;
			
		}
		return retVal;
	}
	
	public static WinnerSearchMethod createWinnerSearchMethod(WinnerSearchMethodType type, Settings setting)
	{	
		WinnerSearchMethod retVal = createWinnerSearchMethod(type);
		retVal.applySettings(setting);
		return retVal;
	}

}

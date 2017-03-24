package de.ovgu.dke.util;

public class SimpleAttribute implements Attribute 
{
	private String name;
	
	public SimpleAttribute(String name)
	{
		if(name == null)
			throw new NullPointerException("name");
		this.name = name;
	}

	@Override
	public String getName() 
	{
		return name;
	}

}

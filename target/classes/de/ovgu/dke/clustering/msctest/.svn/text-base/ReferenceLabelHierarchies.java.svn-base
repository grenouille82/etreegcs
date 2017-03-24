package de.ovgu.dke.clustering.msctest;

import de.ovgu.dke.tree.LinkedTree;

public final class ReferenceLabelHierarchies 
{
	private ReferenceLabelHierarchies() {}
	
	private static LinkedTree<String> banksearchReferenceHierarchy;
	private static LinkedTree<String> reuters1ReferenceHierarchy;
	private static LinkedTree<String> reuters2ReferenceHierarchy;
	
	public static LinkedTree<String> getBanksearchHierarchy()
	{
		if(banksearchReferenceHierarchy == null) {
			banksearchReferenceHierarchy = new LinkedTree<String>();
			banksearchReferenceHierarchy.setRoot("root");
			banksearchReferenceHierarchy.appendElement("root", "Banking");
			banksearchReferenceHierarchy.appendElement("Banking", "Commercial Banks");
			banksearchReferenceHierarchy.appendElement("Banking", "Insurance Agencies");
			banksearchReferenceHierarchy.appendElement("Banking", "Building Societies");
			banksearchReferenceHierarchy.appendElement("root", "Programming");
			banksearchReferenceHierarchy.appendElement("Programming", "Java");
			banksearchReferenceHierarchy.appendElement("Programming", "C");
			banksearchReferenceHierarchy.appendElement("Programming", "Visual Basic");
			banksearchReferenceHierarchy.appendElement("root", "Science");
			banksearchReferenceHierarchy.appendElement("Science", "Biology");
			banksearchReferenceHierarchy.appendElement("Science", "Astronomy");
			banksearchReferenceHierarchy.appendElement("root", "Sport");
			banksearchReferenceHierarchy.appendElement("Sport", "Soccer");
			banksearchReferenceHierarchy.appendElement("Sport", "Motor Racing");
		}
		return banksearchReferenceHierarchy;
	}
	
	public static LinkedTree<String> getReuters1Hierarchy()
	{
		if(reuters1ReferenceHierarchy == null) {
			reuters1ReferenceHierarchy = new LinkedTree<String>();
			reuters1ReferenceHierarchy.setRoot("root");
			reuters1ReferenceHierarchy.appendElement("root", "CORPORATE/INDUSTRIAL");
			reuters1ReferenceHierarchy.appendElement("CORPORATE/INDUSTRIAL", "STRATEGY/PLANS");
			reuters1ReferenceHierarchy.appendElement("CORPORATE/INDUSTRIAL", "RESEARCH/DEVELOPMENT");
			reuters1ReferenceHierarchy.appendElement("CORPORATE/INDUSTRIAL", "ADVERTISING/PROMOTION");
			reuters1ReferenceHierarchy.appendElement("root", "ECONOMICS");
			reuters1ReferenceHierarchy.appendElement("ECONOMICS", "ECONOMIC PERFORMANCE");
			reuters1ReferenceHierarchy.appendElement("ECONOMICS", "GOVERNMENT BORROWING");
			reuters1ReferenceHierarchy.appendElement("root", "GOVERNMENT/SOCIAL");
			reuters1ReferenceHierarchy.appendElement("GOVERNMENT/SOCIAL", "DISASTERS AND ACCIDENTS");
			reuters1ReferenceHierarchy.appendElement("GOVERNMENT/SOCIAL", "HEALTH");
			reuters1ReferenceHierarchy.appendElement("GOVERNMENT/SOCIAL", "WEATHER");
		}
		return reuters1ReferenceHierarchy;
	}
	
	public static LinkedTree<String> getReuters2Hierarchy()
	{
		if(reuters2ReferenceHierarchy == null) {
			reuters2ReferenceHierarchy = new LinkedTree<String>();
			reuters2ReferenceHierarchy.setRoot("root");
			reuters2ReferenceHierarchy.appendElement("root", "EQUITY MARKETS");
			reuters2ReferenceHierarchy.appendElement("root", "BOND MARKETS");
			reuters2ReferenceHierarchy.appendElement("root", "MONEY MARKETS");
			reuters2ReferenceHierarchy.appendElement("MONEY MARKETS", "INTERBANK MARKETS");
			reuters2ReferenceHierarchy.appendElement("MONEY MARKETS", "FOREX MARKETS");
			reuters2ReferenceHierarchy.appendElement("root", "COMMODITY MARKETS");
			reuters2ReferenceHierarchy.appendElement("COMMODITY MARKETS", "SOFT COMMODITIES");
			reuters2ReferenceHierarchy.appendElement("COMMODITY MARKETS", "METALS TRADING");
			reuters2ReferenceHierarchy.appendElement("COMMODITY MARKETS", "ENERGY MARKETS");
		}
		return reuters2ReferenceHierarchy;
	}
}

package com.castsoftware.gorgon.servers;

public class GorgonManager {
	private static GorgonList gorgonList;
	
	static
	{
		gorgonList = new GorgonList();
		//TODO Fix this mess!
		gorgonList.setGorgonFileName("C:/Program Files/Apache Software Foundation/Tomcat 7.0/webapps/restapi/WEB-INF/classes/GorgonServers.json");
		gorgonList.load();
	}
	
	public static GorgonList getGorgonList() {
	    return gorgonList;
	  }
}

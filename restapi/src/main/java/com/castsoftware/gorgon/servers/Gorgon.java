package com.castsoftware.gorgon.servers;

public class Gorgon implements Comparable<Gorgon>, Cloneable {
	private Integer id;
	private String name;
	private String url;
	private Integer port;
	private String database;
	private String login;
	private String password;
	private String schema;
	
	public Gorgon()
	{
		super();
	}
	
	public Gorgon(Integer id, String name, String url, Integer port, String database, 
			String login, String password, String schema) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.port = port;
		this.database = database;
		this.login = login;
		this.password = password;
		this.schema = schema;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
	        return false;
	    }
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
	    
	    final Gorgon other = (Gorgon) obj;  
	    	    	        
	    return url.equals(other.url) && (port == other.port) && 
	    		database.equals(database) && schema.equals(schema);
	}

	@Override
	public int compareTo(Gorgon g) {	
		return name.compareTo(g.name);
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException{
		return (Gorgon) super.clone();
	}
}

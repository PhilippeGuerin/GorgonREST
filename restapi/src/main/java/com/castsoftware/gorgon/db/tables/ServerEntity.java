package com.castsoftware.gorgon.db.tables;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

@Entity
@Table(name = "servers")
public class ServerEntity implements java.io.Serializable {

	private static final long serialVersionUID = 4255728837862167645L;
	
	@Id
	@Column(name = "server_id", unique = true, nullable = false)
	private Integer serverId;

	@Column(name = "server_name", nullable = false, length = 255)
	private String serverName;
	
	@Column(name = "connection_string", nullable = true)
	private String connectionString;
	
	@Column(name = "active", nullable = true)
	private Boolean active;
	
	@Column(name = "last_computed", nullable = true)
	private Date lastComputed;
	
	public ServerEntity()
	{
		super();
	}

	public ServerEntity(Integer serverId, String serverName,
			String connectionString, Boolean active) {
		super();
		this.serverId = serverId;
		this.serverName = serverName;
		this.connectionString = connectionString;
		this.active = active;
	}
	
	@Override 
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof ServerEntity))
			return false;
		ServerEntity me = (ServerEntity) o;
		
		return Objects.equals(this.serverId, me.serverId);
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(serverId, serverName,
    			connectionString, active);
    }
	
	public static Integer getNextId(Session session)
	{
		Criteria cr = session.createCriteria(ServerEntity.class);
		cr.setProjection(Projections.projectionList()
	            .add(Projections.max("serverId")));
		
		@SuppressWarnings("unchecked")
		List<Integer> results = cr.list();
		
		if (results.size() == 0)
			return 1;
		else
			return results.get(0).intValue() + 1;
	}
	
	@Override
	public String toString()
	{
		return String.format("#%d %s (%s) - %s", 
				serverId, serverName, active ? "active" : "inactive", connectionString);
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getLastComputed() {
		return lastComputed;
	}

	public void setLastComputed(Date lastComputed) {
		this.lastComputed = lastComputed;
	}

}

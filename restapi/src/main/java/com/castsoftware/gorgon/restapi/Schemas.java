package com.castsoftware.gorgon.restapi;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import com.castsoftware.gorgon.db.tables.SchemaEntity;
import com.castsoftware.gorgon.db.util.SchemaEntityTool;
import com.castsoftware.gorgon.servers.GorgonList;
import com.castsoftware.gorgon.servers.GorgonManager;

@Path("/gorgons/{gorgonid}/servers/{serverid}/schemas")
public class Schemas {
	private static final Logger logger = Logger.getLogger(Schemas.class);
	private static final GorgonList gorgonList = GorgonManager.getGorgonList();
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SchemaEntity> getAll(@PathParam("gorgonid") int gorgonServerId,
    		@PathParam("serverid") int serverId) 
    {
		logger.debug(String.format("[%d][%d]getAll", gorgonServerId, serverId));
		return SchemaEntityTool.getAll(gorgonList.getGorgon(gorgonServerId), serverId);
    }
	
	@GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SchemaEntity getOne(@PathParam("gorgonid") int gorgonServerId,
    		@PathParam("serverid") int serverId, 
    		@PathParam("id") int schemaId)
    {
    	logger.debug(String.format("[%d][%d]getOne(%d)", gorgonServerId, serverId, schemaId));		
		return SchemaEntityTool.getOne(gorgonList.getGorgon(gorgonServerId), serverId, schemaId);
    }
	
	@OPTIONS
    @Produces(MediaType.APPLICATION_JSON)
    public String getSupportedOperations()
    {
    	logger.debug(String.format("getSupportedOperations"));
    	return "{\"operations\":\"GET\"}";
    }    
}

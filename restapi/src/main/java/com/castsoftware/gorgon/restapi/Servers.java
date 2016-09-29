package com.castsoftware.gorgon.restapi;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;

import com.castsoftware.gorgon.db.tables.ServerEntity;
import com.castsoftware.gorgon.db.util.CommonTool;
import com.castsoftware.gorgon.db.util.ServerEntityTool;
import com.castsoftware.gorgon.servers.GorgonList;
import com.castsoftware.gorgon.servers.GorgonManager;

@Path("/gorgons/{gorgonid}/servers")
public class Servers {
	private static final Logger logger = Logger.getLogger(Servers.class);
	private static final GorgonList gorgonList = GorgonManager.getGorgonList();
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServerEntity> getAll(@PathParam("gorgonid") int gorgonServerId) 
    {
    	logger.debug(String.format("[%d]getAll", gorgonServerId));		
    	return ServerEntityTool.getAll(gorgonList.getGorgon(gorgonServerId));
    }
	
	@GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ServerEntity getOne(@PathParam("gorgonid") int gorgonServerId, 
    		@PathParam("id") int serverId)
    {
    	logger.debug(String.format("[%d]getOne(%d)", gorgonServerId, serverId));		
    	return ServerEntityTool.getOne(gorgonList.getGorgon(gorgonServerId), serverId);
    }
	
	@GET
	@Path("/serverTime")
    @Produces(MediaType.APPLICATION_JSON)
    public Date getServerTime(@PathParam("gorgonid") int gorgonServerId) 
    {
    	logger.debug(String.format("[%d]getServerTime", gorgonServerId));
	    try {
	        return CommonTool.getServerTime(gorgonList.getGorgon(gorgonServerId));
	    } catch (ParseException e) {
	        logger.error(e);
	        return null;
	    }
    }	
	
    @PUT
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(@PathParam("gorgonid") int gorgonServerId, ServerEntity s) 
    {
    	logger.debug(String.format("[%d]add - %d %s", gorgonServerId, s.getServerId(), s.getServerName()));
    	
		try { 
			ServerEntityTool.add(gorgonList.getGorgon(gorgonServerId), s);
			return Response.status(201).build();
		} catch (ConstraintViolationException e) {
        	logger.error(e);
			return Response.status(500).build();
		}			
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("gorgonid") int gorgonServerId, ServerEntity s) 
    {
    	logger.debug(String.format("[%d]update - %d %s", gorgonServerId, s.getServerId(), s.getServerName()));
    	
		try { 
			ServerEntityTool.update(gorgonList.getGorgon(gorgonServerId), s);
			return Response.status(200).build();
		} catch (StaleStateException e) {
        	logger.error(e);
        	return Response.status(404).build();
		}
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("gorgonid") int gorgonServerId, @PathParam("id") int serverId)
    {
    	logger.debug(String.format("[%d]update - %d", gorgonServerId, serverId));
    	
    	if (ServerEntityTool.delete(gorgonList.getGorgon(gorgonServerId), serverId))
    		return Response.status(204).build();
    	else
    		return Response.status(400).build();		
    }

    @OPTIONS
    @Produces(MediaType.APPLICATION_JSON)
    public String getSupportedOperations()
    {
    	logger.debug(String.format("getSupportedOperations"));
    	return "{\"operations\":\"GET, PUT, POST, DELETE\"}";
    }    
}

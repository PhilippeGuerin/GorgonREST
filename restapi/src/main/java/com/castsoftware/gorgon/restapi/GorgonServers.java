package com.castsoftware.gorgon.restapi;

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

import com.castsoftware.gorgon.servers.Gorgon;
import com.castsoftware.gorgon.servers.GorgonList;
import com.castsoftware.gorgon.servers.GorgonManager;

@Path("/gorgons")
public class GorgonServers {
	private static final Logger logger = Logger.getLogger(GorgonServers.class);
	private static final GorgonList gorgonList = GorgonManager.getGorgonList();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Gorgon> getAll() {
    	logger.debug("getAll");
		return gorgonList.getList();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Gorgon getOne(@PathParam("id") int gorgonServerId)
    {
    	logger.debug(String.format("getOne - %d", gorgonServerId));
    	return gorgonList.getGorgon(gorgonServerId);
    }
    
    @PUT
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Gorgon g) 
    {
    	logger.info(String.format("add - [%d] %s", g.getId(), g.getName()));
    	if (gorgonList.addGorgon(g))
			return Response.status(201).build();
		else
			return Response.status(500).build(); 	
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Gorgon g) 
    {
    	logger.info(String.format("update - [%d] %s", g.getId(), g.getName()));
    	if (gorgonList.updateGorgon(g))
			return Response.status(200).build();
		else
			return Response.status(404).build();
    }
	
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int gorgonServerId)
    {
    	logger.info(String.format("delete - %d", gorgonServerId));
    	if (gorgonList.deleteGorgon(gorgonServerId))
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

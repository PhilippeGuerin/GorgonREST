package com.castsoftware.gorgon.restapi;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;

import com.castsoftware.gorgon.db.tables.MetricEntity;
import com.castsoftware.gorgon.db.util.MetricEntityTool;
import com.castsoftware.gorgon.servers.GorgonList;
import com.castsoftware.gorgon.servers.GorgonManager;

@Path("/gorgons/{gorgonid}/metrics")
public class Metrics {
	private static final Logger logger = Logger.getLogger(Metrics.class);
	private static final GorgonList gorgonList = GorgonManager.getGorgonList();
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MetricEntity> getAll(@PathParam("gorgonid") int gorgonServerId,
    		@DefaultValue("-1") @QueryParam("schemaId") int schemaId) 
    {
    	logger.debug(String.format("[%d]getAll (%d)", gorgonServerId, schemaId));
    	List<MetricEntity> list;
    	if (schemaId == -1)
    		list = MetricEntityTool.getAll(gorgonList.getGorgon(gorgonServerId));
    	else
    		list = MetricEntityTool.getAllForSchema(gorgonList.getGorgon(gorgonServerId), schemaId);
    	logger.debug(String.format("[%d]getAll (%d) - %d rows", gorgonServerId, schemaId, list.size()));
    	return list;
    }
	
	@GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MetricEntity getOne(@PathParam("gorgonid") int gorgonServerId, @PathParam("id") int metricId)
    {
    	logger.debug(String.format("[%d]getOne", gorgonServerId));
    	return MetricEntityTool.getOne(gorgonList.getGorgon(gorgonServerId), metricId);
    }
	
    @PUT
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(@PathParam("gorgonid") int gorgonServerId, MetricEntity m) 
    {
    	logger.debug(String.format("[%d]add - %d %s", gorgonServerId, m.getMetricId(), m.getMetricName()));

		try { 
			MetricEntityTool.add(gorgonList.getGorgon(gorgonServerId), m);
			return Response.status(201).build();
		} catch (ConstraintViolationException e) {
        	logger.error(e);
			return Response.status(500).build();
		}
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("gorgonid") int gorgonServerId, MetricEntity m) 
    {
    	logger.debug(String.format("[%d]update - %d %s", gorgonServerId, m.getMetricId(), m.getMetricName()));

		try { 
			MetricEntityTool.update(gorgonList.getGorgon(gorgonServerId), m);
			return Response.status(200).build();
		} catch (StaleStateException e) {
        	logger.error(e);
        	return Response.status(404).build();
		}
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("gorgonid") int gorgonServerId, @PathParam("id") int metricId)
    {
    	logger.debug(String.format("[%d]update - %d", gorgonServerId, metricId));
    	if (MetricEntityTool.delete(gorgonList.getGorgon(gorgonServerId), metricId))
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

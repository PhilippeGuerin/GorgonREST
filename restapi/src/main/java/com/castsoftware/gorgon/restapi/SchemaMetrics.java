package com.castsoftware.gorgon.restapi;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.castsoftware.gorgon.db.resultsets.MetricData;
import com.castsoftware.gorgon.db.tables.SchemaMetricEntity;
import com.castsoftware.gorgon.db.util.SchemaMetricEntityTool;
import com.castsoftware.gorgon.servers.GorgonList;
import com.castsoftware.gorgon.servers.GorgonManager;

@Path("/gorgons/{gorgonid}/schemas/{schemaid}/metrics")
public class SchemaMetrics {
	private static final Logger logger = Logger.getLogger(SchemaMetrics.class);
	private static final GorgonList gorgonList = GorgonManager.getGorgonList();
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MetricData> getAll(@PathParam("gorgonid") int gorgonServerId,
    		@PathParam("schemaid") int schemaId) 
    {
    	logger.debug(String.format("[%d][%d]getAll", gorgonServerId, schemaId));
		return SchemaMetricEntityTool.getAllMetrics(gorgonList.getGorgon(gorgonServerId), schemaId);
    }
	
	@GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SchemaMetricEntity> getOne(@PathParam("gorgonid") int gorgonServerId,
    		@PathParam("schemaid") int schemaId, 
    		@PathParam("id") int metricId)
    {
    	logger.debug(String.format("[%d][%d]getOne(%d)", gorgonServerId, schemaId, metricId));
    	return SchemaMetricEntityTool.getOneMetric(gorgonList.getGorgon(gorgonServerId), schemaId, metricId);
    }

    @OPTIONS
    @Produces(MediaType.APPLICATION_JSON)
    public String getSupportedOperations()
    {
    	logger.debug(String.format("getSupportedOperations"));
    	return "{\"operations\":\"GET\"}";
    }  	
}

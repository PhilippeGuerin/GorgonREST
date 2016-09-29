package com.castsoftware.gorgon.restapi;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.castsoftware.gorgon.processes.ProcessDetails;
import com.castsoftware.gorgon.processes.ProcessTaskList;
import com.castsoftware.gorgon.processes.ProcessTaskManager;
import com.castsoftware.gorgon.servers.Gorgon;
import com.castsoftware.gorgon.servers.GorgonList;
import com.castsoftware.gorgon.servers.GorgonManager;

@Path("/gorgons/{gorgonid}/processes")
public class Processes {
	private static final Logger logger = Logger.getLogger(Processes.class);
	private static final GorgonList gorgonList = GorgonManager.getGorgonList();
	private static final ProcessTaskList taskList = ProcessTaskManager.getTaskList();
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProcessDetails> getAll(@PathParam("gorgonid") int gorgonServerId) 
    {
    	logger.debug(String.format("[%d]getAll", gorgonServerId));
    	
    	Gorgon g = gorgonList.getGorgon(gorgonServerId);
    	return taskList.getProcessTasks(g);
    }
	
	@GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProcessDetails getOne(@PathParam("id") int processId)
    {
    	logger.debug(String.format("getOne %d", processId));
    	
    	return taskList.getProcessTask(processId);
    }
	
	@PUT
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(@PathParam("gorgonid") int gorgonServerId, ProcessDetails p) 
    {
    	logger.debug(String.format("[%d]add - %d %s", gorgonServerId, p.getId(), p.getName()));
    	Gorgon g = gorgonList.getGorgon(gorgonServerId);
    	
    	if (taskList.addProcessTask(g, p))
    		return Response.status(201).build();
    	else
    		return Response.status(500).build();
    }
	
	@DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int processId)
    {
    	logger.debug(String.format("delete - %d", processId));
    	
    	if (taskList.deletePendingProcessTask(processId))
    		return Response.status(204).build();
    	else
    		return Response.status(400).build();
    }
	
	@OPTIONS
    @Produces(MediaType.APPLICATION_JSON)
    public String getSupportedOperations()
    {
    	logger.debug(String.format("getSupportedOperations"));
    	return "{\"operations\":\"GET, PUT, DELETE\"}";
    }    
}

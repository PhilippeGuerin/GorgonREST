package com.castsoftware.gorgon.processes;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.postgresql.PGNotification;

import com.castsoftware.gorgon.db.DBHelper;
import com.castsoftware.gorgon.db.PostgresNotificationFeedback;
import com.castsoftware.gorgon.db.PostgresNotificationListener;
import com.castsoftware.gorgon.servers.Gorgon;
import com.castsoftware.util.db.Constants;
import com.castsoftware.util.taskengine.ICallBack;
import com.castsoftware.util.taskengine.Task;

public class ProcessTask extends Task implements PostgresNotificationFeedback {
	
	private Gorgon gorgon;
	private ProcessDetails processDetails;
	
	private PostgresNotificationListener listener;
	
	private static final String RUN_ALL="process_servers()";
	private static final String RUN_ONE="process_server(%d)";

	public ProcessTask(int taskId, ICallBack callback, Gorgon gorgon, ProcessDetails processDetails) {
		super(taskId, callback);
		this.gorgon = gorgon;
		this.processDetails = processDetails;
		this.processDetails.setId(taskId);
		this.processDetails.setState(0);
		processDetails.setStarted(new Date());
	}
	
	public ProcessDetails getProcessDetails()
	{
		//update current duration		
		processDetails.setDuration(TimeUnit.SECONDS.convert(this.getDuration(), TimeUnit.NANOSECONDS));
		processDetails.setOutput(this.getOutput());
		return processDetails;
	}
	
	public Integer getGorgonId()
	{
		return gorgon.getId();
	}
	
	private void startNotificationListener()
	{
		logger.info("startNotificationListener");
		try {
			listener = new PostgresNotificationListener(gorgon.getUrl(), gorgon.getPort(), gorgon.getDatabase(),
					gorgon.getLogin(), gorgon.getPassword(), this);
			listener.start();
			TimeUnit.SECONDS.sleep(1);
		} catch (ClassNotFoundException | SQLException | InterruptedException e) {
			logger.error(e);
		}
	}
	
	private void stopNotificationListener(){
		logger.info("stopNotificationListener");
		try {
			TimeUnit.SECONDS.sleep(2);
			listener.terminate();
			listener.join();
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}
	
	@Override
	public void reportNotification(PGNotification notification) {
		logger.info("reportNotification:" + notification.getParameter());
		log(notification.getParameter());	
	}
	
	protected void runLogic()
	{
		processDetails.setState(1);
		processDetails.setStarted(new Date());
		log(String.format("About to run analysis for server Id %d", processDetails.getServerId()));
		logger.debug(String.format("About to run analysis for server Id %d", processDetails.getServerId()));
		
		String url = DBHelper.buildUrl(gorgon.getUrl(), gorgon.getPort(), gorgon.getDatabase());
		
		String proc = "";;
		try
		{
			if (processDetails.getServerId() != -1) //Run All
				proc = String.format(RUN_ONE, processDetails.getServerId());
			else
				proc = RUN_ALL;
			
			logger.info(String.format("Executing proc '%s' on server '%s' and schema '%s' ", 
					proc, url, gorgon.getSchema()));

			startNotificationListener();
			
			DBHelper.runProc(Constants.DbType.Postgres, url, gorgon.getLogin(), gorgon.getPassword(), 
					gorgon.getSchema(), proc);
			
			stopNotificationListener();
			processDetails.setState(2);
			log(String.format("Analysis completed successfully for server Id %d", processDetails.getServerId()));
			
		} catch(Exception e) {
			logger.error(String.format("Task %d failure: ", getTaskId()), e);
			log(String.format("Analysis failed for server Id %d", processDetails.getServerId()));
			processDetails.setState(-1);
		}
	}
	
	@Override
	public boolean canRunInParallel(Task t) {
		//can't run if another task is running on the same gorgon		
		return !gorgon.equals(((ProcessTask) t).gorgon);
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
	    
	    final ProcessTask other = (ProcessTask) obj;  
	    	    	        
	    return gorgon.equals(other.gorgon) && (processDetails.getServerId() == other.processDetails.getServerId());
	}
}

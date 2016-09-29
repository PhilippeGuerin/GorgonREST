package com.castsoftware.gorgon.processes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.castsoftware.gorgon.servers.Gorgon;
import com.castsoftware.util.taskengine.Task;
import com.castsoftware.util.taskengine.TaskList;

public class ProcessTaskList extends TaskList {
	
	
	public List<ProcessDetails> getProcessTasks(Gorgon gorgon){
		List<ProcessDetails> list = new ArrayList<ProcessDetails>();
		ProcessTask pt;
		try
		{
			lock.tryLock(5, TimeUnit.SECONDS);	
			//
			cleanupCompletedTasks();		

			for(Task t : runningTasks)
				if (ProcessTask.class.isInstance(t))
				{
					pt = (ProcessTask)t;
					if (pt.getGorgonId() == gorgon.getId())
						list.add((ProcessDetails)pt.getProcessDetails().clone());
					
				}

			for(Task t : queuedTasks)
				if (ProcessTask.class.isInstance(t))
				{
					pt = (ProcessTask)t;
					if (pt.getGorgonId() == gorgon.getId())
						list.add((ProcessDetails)pt.getProcessDetails().clone());
				}

			for(Task t : completedTasks)
				if (ProcessTask.class.isInstance(t))
				{
					pt = (ProcessTask)t;
					if (pt.getGorgonId() == gorgon.getId())
						list.add((ProcessDetails)pt.getProcessDetails().clone());
				}
			
			Collections.sort(list, Collections.reverseOrder());
			return list;			
		} catch (CloneNotSupportedException | InterruptedException e) {
			logger.error(e);
			return new ArrayList<ProcessDetails>();
		} 
		finally
		{
			lock.unlock();
		}		
	}
	
	public ProcessDetails getProcessTask(Integer taskId){
		try
		{
			
			lock.tryLock(5, TimeUnit.SECONDS);
				
			for(Task t : runningTasks)
			{
				if ((ProcessTask.class.isInstance(t)) && (t.getTaskId() == taskId))
					return (ProcessDetails)((ProcessTask)t).getProcessDetails().clone();
			}
			
			for(Task t : queuedTasks)
			{
				if ((ProcessTask.class.isInstance(t)) && (t.getTaskId() == taskId))
					return (ProcessDetails)((ProcessTask)t).getProcessDetails().clone();
			}
			
			return null;
		} catch (CloneNotSupportedException | InterruptedException e) {
			logger.error(e);
			return null;
		} 
		finally
		{
			lock.unlock();
		}
	}
	
	public Boolean deletePendingProcessTask(Integer taskId){
		try
		{			
			lock.tryLock(5, TimeUnit.SECONDS);
			
			for(Task t : queuedTasks)
				if ((ProcessTask.class.isInstance(t)) && (t.getTaskId() == taskId))
					return queuedTasks.remove(t);
			
			return false;
		} catch (InterruptedException e) {
			logger.error(e);
			return false;
		} 
		finally
		{
			lock.unlock();
		}
	}
	
	public Boolean addProcessTask(Gorgon gorgon, ProcessDetails processDetails){
		try {
			return submitTask( new ProcessTask(this.getNextId(), this, gorgon, processDetails));
		} catch (InterruptedException e) {
			logger.error(e);
			return false;
		}
	}
}

package com.castsoftware.util.taskengine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.castsoftware.util.taskengine.ICallBack;
import com.castsoftware.util.CastUtil;

public class TaskList implements ICallBack{
	protected static final Logger logger = Logger.getLogger(TaskList.class);
	
	private volatile boolean running = true;
	
	protected final Lock lock = new ReentrantLock();
	
	protected final List<Task> runningTasks = new ArrayList<Task>();
	protected final List<Task> queuedTasks = new ArrayList<Task>();
	protected final List<Task> completedTasks = new ArrayList<Task>();
	
	//private static final ErrorMessageList errorMessageList = ErrorMessageManager.getErrorMessageList();
		
	private int maxConcurrentTasks = 10;
	private Boolean noConcurrentTasksLimit = false;
	private long startTime = 0;
	private long totalRunTime = 0;
	private int completedTaskExpiration = 10; //Minutes
	
	public void terminate() 
	{
		running = false;
		try
		{			
			lock.tryLock(5, TimeUnit.SECONDS);
			
			if (queuedTasks.size() > 0)
				logger.warn(String.format("%d task(s) in the queue will be lost!", queuedTasks.size()));
			
		} catch (InterruptedException e) {
			logger.error(String.format("[%d] InterruptedException", Thread.currentThread().getId()), e);
		} 
		finally
		{
			lock.unlock();
		}	
    }
	
	public boolean stillRunning()
	{
		boolean stillRunning = true;
		try
		{			
			lock.tryLock(5, TimeUnit.SECONDS);
			
			stillRunning = (runningTasks.size() > 0);
			
		} catch (InterruptedException e) {
			logger.error(String.format("[%d] InterruptedException", Thread.currentThread().getId()), e);
		} 
		finally
		{
			lock.unlock();
		}		
		return stillRunning;
	}
	
	public void waitForTask(int taskId) throws InterruptedException
	{
		Task task = null;
		try
		{			
			lock.tryLock(5, TimeUnit.SECONDS);
			
			for(Task t : runningTasks)	
			{
				if (t.getTaskId() == taskId)
				{
					task = t;
					break;
				}
			}
			
		} catch (InterruptedException e) {
			logger.error(String.format("[%d] InterruptedException", Thread.currentThread().getId()), e);
		} 
		finally
		{
			lock.unlock();
		}	
		if (task != null)
			task.join();
	}
	
	public void taskCompleted(int taskId) {
		logger.debug(String.format("[%d] Completed Task %d", Thread.currentThread().getId(), taskId));		

		try
		{			
			lock.tryLock(5, TimeUnit.SECONDS);
			
			for(Task t : runningTasks)
			{
				if (t.getTaskId() == taskId)
				{
					logger.debug(String.format("[%d] Duration: %s", Thread.currentThread().getId(), CastUtil.formatNanoTime(t.getDuration())));
					logger.debug(String.format("[%d] ExitVal: %d", Thread.currentThread().getId(), t.getExitVal()));					
					if (t.getExitVal() != 0)
					{
						logger.error(String.format("[%d] %s", Thread.currentThread().getId(),  t.getOutput()));
					}
					else
					{
						logger.debug(String.format("[%d] Command Output:\n%s", Thread.currentThread().getId(), t.getOutput()));
					}
					
					runningTasks.remove(t);
					completedTasks.add(t);
					break;
				}				
			}	
			
			kickOffNextTasks();
			cleanupCompletedTasks();
			
			if (runningTasks.size() == 0)
			{
				totalRunTime += System.nanoTime() - startTime;
				startTime = 0;
				logger.info(String.format("Total Runtime: %s", CastUtil.formatNanoTime(totalRunTime)));
			}
		} catch (InterruptedException e) {
			logger.error(String.format("[%d] InterruptedException", Thread.currentThread().getId()), e);
		} 
		finally
		{
			lock.unlock();
		}		
	}

	public int getNextId() throws InterruptedException
	{
		int nextId = 1;
		try
		{
			
			lock.tryLock(5, TimeUnit.SECONDS);
			//Running Tasks
			for(Task t : runningTasks)
				nextId = Math.max(nextId, t.getTaskId());
			//Completed Tasks
			for(Task t : completedTasks)
				nextId = Math.max(nextId, t.getTaskId());
			//Completed Tasks
			for(Task t : queuedTasks)
				nextId = Math.max(nextId, t.getTaskId());
			return nextId+1;
		} 
		finally
		{
			lock.unlock();
		}				
	}
	
	public boolean submitTask(Task task) throws InterruptedException
	{
		try
		{			
			lock.tryLock(5, TimeUnit.SECONDS);
			//
			cleanupCompletedTasks();
			//check if task is a duplicate
			for (Task t : queuedTasks)
				if (t.equals(task))
					return false;
			for (Task t : runningTasks)
				if (t.equals(task))
					return false;
			
			queuedTasks.add(task);
			kickOffNextTasks();
			return true;
		} 
		finally
		{
			lock.unlock();
		}
	}	
	
	//not thread safe and not meant to be because it's only called from within thread safe methods
	private void kickOffNextTasks()
	{	
		//Task List has been asked to stop
		if (!running)
			return;
		
		if ((runningTasks.size() == 0) && (queuedTasks.size() > 0))
		{
			if (startTime == 0)
				startTime = System.nanoTime();
		}
		
		while ((queuedTasks.size() > 0) && (canRunNewTask()))
		{
			Task task = getNextTaskToRun();
			if (task == null)
				break; //We have tasks in the queue but they can't run because of dependencies with running tasks.
			runningTasks.add(task);
			logger.debug(String.format("Running Task %d", task.getTaskId()));
			task.start();
		}
	}
	
	//not thread safe and not meant to be because it's only called from within thread safe methods
	protected void cleanupCompletedTasks()
	{
		Task task;
		int listSize = completedTasks.size();
		Calendar c = Calendar.getInstance();
		
		for(int i=listSize-1;i>=0;i--)
		{
			task = completedTasks.get(i);
			c.setTime(task.getEndStamp());
			c.add(Calendar.MINUTE, completedTaskExpiration);
			//logger.debug(c);
			//logger.debug(new Date());
			if (c.getTime().before(new Date()))
			{
				completedTasks.remove(i);		
				logger.debug(String.format("Removed Completed Task %d", task.getTaskId()));
			}
		}
	}
	
	//not thread safe and not meant to be because it's only called from within thread safe methods
	private Boolean canRunNewTask()
	{
		if (noConcurrentTasksLimit)
			return true;
		else
			return runningTasks.size() < maxConcurrentTasks;
	}
	
	private Task getNextTaskToRun()
	{
		int i = 0;
		boolean canRun = true;
		for(Task qt : queuedTasks)
		{
			for(Task rt : runningTasks)
				canRun = canRun && qt.canRunInParallel(rt);
			if (canRun)
				return queuedTasks.remove(i);
			i++;
		}
		return null;
	}	
	
	public boolean isTaskRunning(int taskId) throws InterruptedException
	{
		try
		{
			
			lock.tryLock(5, TimeUnit.SECONDS);
			
			for(Task t : runningTasks)
			{
				if (t.getTaskId() == taskId)
					return t.isAlive();
			}
			return false;
		} 
		finally
		{
			lock.unlock();
		}
	}
	
	public int getTaskExitValue(int taskId) throws InterruptedException
	{
		try
		{
			
			lock.tryLock(5, TimeUnit.SECONDS);
				
			for(Task t : completedTasks)
			{
				if (t.getTaskId() == taskId)
					return t.getExitVal();
			}
			
			return -1;
		} 
		finally
		{
			lock.unlock();
		}
	}
	
	public List<String> getTaskOutput(int taskId, int index) throws InterruptedException
	{
		try
		{
			
			lock.tryLock(5, TimeUnit.SECONDS);
			
			for(Task t : runningTasks)
			{
				if (t.getTaskId() == taskId)
					return t.getOutput(index);
			}
			
			for(Task t : completedTasks)
			{
				if (t.getTaskId() == taskId)
					return t.getOutput(index);
			}
				
			return null;
		} 
		finally
		{
			lock.unlock();
		}
	}
	
	public int getMaxConcurrentTasks() throws InterruptedException {
		try
		{			
			lock.tryLock(5, TimeUnit.SECONDS);
			return maxConcurrentTasks;
		} 
		finally
		{
			lock.unlock();
		}
	}
	
	public void setNoConcurrentTasksLimit(Boolean noConcurrentTasksLimit) throws InterruptedException {
		try
		{			
			lock.tryLock(5, TimeUnit.SECONDS);
			this.noConcurrentTasksLimit = noConcurrentTasksLimit;
			kickOffNextTasks();
		} 
		finally
		{
			lock.unlock();
		}		
	}

	public Boolean getNoConcurrentTasksLimit() throws InterruptedException {
		try
		{			
			lock.tryLock(5, TimeUnit.SECONDS);
			return noConcurrentTasksLimit;
		} 
		finally
		{
			lock.unlock();
		}
	}
	
	public void setMaxConcurrentTasks(int maxConcurrentTasks) throws InterruptedException {
		try
		{			
			lock.tryLock(5, TimeUnit.SECONDS);
			this.maxConcurrentTasks = maxConcurrentTasks;
			kickOffNextTasks();
		} 
		finally
		{
			lock.unlock();
		}		
	}	
}


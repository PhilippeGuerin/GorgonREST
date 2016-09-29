package com.castsoftware.util.taskengine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.castsoftware.util.taskengine.ICallBack;

public class Task extends Thread implements IRunInParallel  {
	protected static final Logger logger = Logger.getLogger(Task.class);
	private final ICallBack callback;
	private final int taskId;
	protected long duration = 0;
	private volatile List<String> output = new ArrayList<String>();
	protected int exitVal = -1;
	private Date endStamp;
	private long startTime;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
	
	protected void log(String s){
		synchronized (this){
			output.add(String.format("%s %s", format.format(new Date()), s));
		}
	}
	
	public int getTaskId()
	{
		return taskId;
	}
	
	public long getDuration()
	{
		if (duration == 0)
			return System.nanoTime() - startTime;
		else
			return duration;
	}
	
	public Date getEndStamp()
	{
		return endStamp;
	}
	
	public List<String> getOutput(int index)
	{		
		synchronized (this){
			return  output.subList(index, output.size());
		}
	}
	
	public String getOutput()
	{		
		String returnString = "";
		synchronized (this){
			for (String s : output)
				returnString += s + System.lineSeparator();
			}
		return  returnString;		
	}
	
	public int getExitVal()
	{
		return exitVal;
	}
	
	public Task(int taskId, ICallBack callback)
	{
		this.taskId = taskId;
		this.callback = callback;
	}
	
	public void run() 
	{
		startTime = System.nanoTime();	
		
		try {
			logger.info(String.format("Task %s started", getTaskName()));
			runLogic();
			logger.info(String.format("Task %s completed", getTaskName()));

		} finally {
			endStamp = new Date();
			duration = System.nanoTime() - startTime;
			callback.taskCompleted(taskId);
		}
	}	
	
	//Override this method to display your custom name
	protected String getTaskName()
	{
		return String.format("Task %d", taskId);
	}	
	
	//override this method and include your task's logic
	protected void runLogic()
	{
		return;
	}
	
	//override this method if special conditions prevent some of your tasks to run in //
	public boolean canRunInParallel(Task t) {
		return true;
	}
	
	//override this method to prevent duplicate tasks to be added to the task list
	@Override
	public boolean equals(Object obj)
	{
		return false;
	}
}


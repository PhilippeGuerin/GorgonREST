package com.castsoftware.util.taskengine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ErrorMessageList {
	private final Lock lock = new ReentrantLock();
	private final List< ErrorMessage> list = new ArrayList< ErrorMessage>();
	
	private int getNextId() throws InterruptedException
	{
		ErrorMessage errorMessage;
		int nextId = 1;
		try
		{
			
			lock.tryLock(5, TimeUnit.SECONDS);
			int listSize = list.size();
			for(int i=0;i<listSize;i++)
			{
				errorMessage = list.get(i);
				nextId = Math.max(nextId, errorMessage.getId());
			}
			return nextId+1;
		} 
		finally
		{
			lock.unlock();
		}				
	}
	
	public int storeMessage(String message) throws InterruptedException
	{
		int newId = getNextId();
		
		try
		{
			
			lock.tryLock(5, TimeUnit.SECONDS);
			list.add(new ErrorMessage(newId, message));
			return newId;
		} 
		finally
		{
			lock.unlock();
		}
	}
	
	public String retrieveMessage(int id) throws InterruptedException
	{
		ErrorMessage errorMessage;
		String message = String.format("No Message found for id %d",id);
		try
		{
			
			lock.tryLock(5, TimeUnit.SECONDS);
			
			int listSize = list.size();
			for(int i=0;i<listSize;i++)
			{
				errorMessage = list.get(i);
				if (errorMessage.getId() == id)
				{
					message = errorMessage.getMessage();
					list.remove(i);
					break;
				}
			}			
			return message;
		} 
		finally
		{
			lock.unlock();
		}
	}
}

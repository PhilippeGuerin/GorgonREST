package com.castsoftware.util.taskengine;

public class ErrorMessage {

	private final int id;
	private final String message;
	
	public ErrorMessage(int id, String message)
	{
		this.id = id;
		this.message = message;
	}
	
	public int getId()
	{
		return id;
	}
	
	
	public String getMessage()
	{
		return message;
	}
}

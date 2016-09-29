package com.castsoftware.util.taskengine;

public class ErrorMessageManager {

	private static ErrorMessageList errorMessageList;
	
	static
	{
		errorMessageList = new ErrorMessageList();		
	}
	
	public static ErrorMessageList getErrorMessageList() {
	    return errorMessageList;
	  }
}

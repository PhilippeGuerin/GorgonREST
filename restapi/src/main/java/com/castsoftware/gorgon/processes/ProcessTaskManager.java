package com.castsoftware.gorgon.processes;

public class ProcessTaskManager {
		private static ProcessTaskList taskList;
			
		public static ProcessTaskList getTaskList() {
		    if (taskList == null)
		    	taskList = new ProcessTaskList();	
			
			return taskList;
		  }
}

package com.castsoftware.util.taskengine;

public interface IRunInParallel {
	//return true if both tasks can run in //
	//return false otherwise
	public boolean canRunInParallel(Task t);
}

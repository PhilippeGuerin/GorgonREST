package com.castsoftware.gorgon.processes;

import java.util.Date;

public class ProcessDetails implements Cloneable, Comparable<ProcessDetails> {
	private Integer id;
	private String name;
	private Integer serverId;
	private Date started;
	private long duration;
	private Integer state; //0 queue, 1 running, 2 success, -1 failed
	private String output;
	
	public ProcessDetails() {
		super();
	}
	public ProcessDetails(Integer serverId, long duration) {
		super();
		this.serverId = serverId;
		this.duration = duration;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getServerId() {
		return serverId;
	}
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	public Date getStarted() {
		return started;
	}
	public void setStarted(Date started) {
		this.started = started;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException{
		return (ProcessDetails) super.clone();
	}
	@Override
	public int compareTo(ProcessDetails pd) {
		return this.started.compareTo(pd.started);
	}
}

package com.castsoftware.util;

public class VersionInfo {
	private String version;
	private int build;
	
	public VersionInfo()
	{
		
	}
	
	public VersionInfo(String version, int build)
	{
		this.version = version;
		this.build = build;
	}
	
	public String getVersion() {
		return version;
	}

	public int getBuild() {
		return build;
	}

	@Override
	public String toString()
	{
		return String.format("%s build %d", version, build);
	}
}

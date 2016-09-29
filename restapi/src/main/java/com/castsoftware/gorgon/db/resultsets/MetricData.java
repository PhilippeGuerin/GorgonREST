package com.castsoftware.gorgon.db.resultsets;

import java.util.ArrayList;
import java.util.List;

import com.castsoftware.gorgon.db.tables.MetricEntity;
import com.castsoftware.gorgon.db.tables.SchemaMetricEntity;

public class MetricData implements java.io.Serializable {
	private static final long serialVersionUID = -1420894452903229999L;
	
	private MetricEntity metric;
	private List<SchemaMetricEntity> data = new ArrayList<SchemaMetricEntity>();
	private Integer count;
	
	public MetricData()
	{
		super();
	}
	
	public MetricData(MetricEntity metric, List<SchemaMetricEntity> data,
			Integer count) {
		super();
		this.metric = metric;
		this.data = data;
		this.count = count;
	}

	public MetricEntity getMetric() {
		return metric;
	}

	public void setMetric(MetricEntity metric) {
		this.metric = metric;
	}

	public List<SchemaMetricEntity> getData() {
		return data;
	}

	public void setData(List<SchemaMetricEntity> data) {
		this.data = data;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}

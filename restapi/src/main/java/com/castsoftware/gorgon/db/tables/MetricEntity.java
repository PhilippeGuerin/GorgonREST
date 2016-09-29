package com.castsoftware.gorgon.db.tables;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

@Entity
@Table(name = "metrics")
public class MetricEntity implements java.io.Serializable {

	private static final long serialVersionUID = -3961683715308046117L;
	
	@Id
	@Column(name = "metric_id", unique = true, nullable = false)
	private Integer metricId;

	@Column(name = "metric_name", nullable = false, length = 255)
	private String metricName;
	
	@Column(name = "metric_type", nullable = false, length = 1)
	private String metricType;

	@Column(name = "metric_scope", nullable = false, length = 1)
	private String metricScope;

	@Column(name = "metric_value_index", nullable = false)
	private Integer metricValueIndex;

	@Column(name = "list_sql", nullable = true)
	private String listSql;

	@Column(name = "active", nullable = true)
	private Boolean active;

	@Column(name = "level", nullable = true, length = 1)
	private String level;

	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "category", nullable = true, length = 255)
	private String category;

	@Column(name = "mode", nullable = true, length = 20)
	private String mode;

	@Column(name = "fixing_metric_id", nullable = true)
	private Integer fixingMetricId;
	
	public MetricEntity()
	{
		super();
	}

	public MetricEntity(Integer metricId, String metricName, String metricType,
			String metricScope, Integer metricValueIndex, String listSql,
			Boolean active, String level, String description, String category,
			String mode, Integer fixingMetricId) {
		super();
		this.metricId = metricId;
		this.metricName = metricName;
		this.metricType = metricType;
		this.metricScope = metricScope;
		this.metricValueIndex = metricValueIndex;
		this.listSql = listSql;
		this.active = active;
		this.level = level;
		this.description = description;
		this.category = category;
		this.mode = mode;
		this.fixingMetricId = fixingMetricId;
	}
	
	@Override 
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof MetricEntity))
			return false;
		MetricEntity me = (MetricEntity) o;
		
		return Objects.equals(this.metricId, me.metricId);
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(metricId, metricName, metricType,
    			metricScope, metricValueIndex, listSql,
    			active, level, description, category,
    			mode, fixingMetricId);
    }
	
	public static Integer getNextId(Session session)
	{
		Criteria cr = session.createCriteria(ServerEntity.class);
		cr.setProjection(Projections.projectionList()
	            .add(Projections.max("metricId")));
		
		@SuppressWarnings("unchecked")
		List<Integer> results = cr.list();
		
		if (results.size() == 0)
			return 1;
		else
			return results.get(0).intValue() + 1;
	}

	public Integer getMetricId() {
		return metricId;
	}

	public void setMetricId(Integer metricId) {
		this.metricId = metricId;
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public String getMetricType() {
		return metricType;
	}

	public void setMetricType(String metricType) {
		this.metricType = metricType;
	}

	public String getMetricScope() {
		return metricScope;
	}

	public void setMetricScope(String metricScope) {
		this.metricScope = metricScope;
	}

	public Integer getMetricValueIndex() {
		return metricValueIndex;
	}

	public void setMetricValueIndex(Integer metricValueIndex) {
		this.metricValueIndex = metricValueIndex;
	}

	public String getListSql() {
		return listSql;
	}

	public void setListSql(String listSql) {
		this.listSql = listSql;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getFixingMetricId() {
		return fixingMetricId;
	}

	public void setFixingMetricId(Integer fixingMetricId) {
		this.fixingMetricId = fixingMetricId;
	}

}

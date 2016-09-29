package com.castsoftware.gorgon.db.tables;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "schema_metrics")
public class SchemaMetricEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 2919558940508516344L;

	@Id
	@OneToOne()
	@JoinColumn(name = "schema_id", nullable = false)
	private SchemaEntity schema;
	
	@Id
	@OneToOne()
	@JoinColumn(name = "metric_id", nullable = false)
	private MetricEntity metric;
	
	@Id
	@Column(name = "idx", nullable = false)
	private Integer idx;
	
	@Column(name = "num_value", nullable = true)
	private BigDecimal numValue;
	
	@Column(name = "num_value2", nullable = true)
	private BigDecimal numValue2;
	
	@Column(name = "num_value3", nullable = true)
	private BigDecimal numValue3;
	
	@Column(name = "char_value", nullable = true)
	private String charValue;
	
	@Column(name = "char_value2", nullable = true)
	private String charValue2;
	
	@Column(name = "char_value3", nullable = true)
	private String charValue3;
	
	public SchemaMetricEntity()
	{
		super();
	}

	public SchemaMetricEntity(SchemaEntity schema, MetricEntity metric, Integer idx,
			BigDecimal numValue, BigDecimal numValue2, BigDecimal numValue3,
			String charValue, String charValue2, String charValue3) {
		super();
		this.schema = schema;
		this.metric = metric;
		this.idx = idx;
		this.numValue = numValue;
		this.numValue2 = numValue2;
		this.numValue3 = numValue3;
		this.charValue = charValue;
		this.charValue2 = charValue2;
		this.charValue3 = charValue3;
	}
	
	@Override public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof SchemaMetricEntity))
			return false;
		SchemaMetricEntity sme = (SchemaMetricEntity) o;
		
		return Objects.equals(this.schema, sme.schema) && 
				Objects.equals(this.metric, sme.metric) && 
				Objects.equals(this.idx, sme.idx);
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(schema, metric, idx,
    			numValue, numValue2, numValue3,
    			charValue, charValue2, charValue3);
    }

	public SchemaEntity getSchemaId() {
		return schema;
	}

	public void setSchema(SchemaEntity schema) {
		this.schema = schema;
	}

	public MetricEntity getMetric() {
		return metric;
	}

	public void setMetricId(MetricEntity metric) {
		this.metric = metric;
	}

	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public BigDecimal getNumValue() {
		return numValue;
	}

	public void setNumValue(BigDecimal numValue) {
		this.numValue = numValue;
	}

	public BigDecimal getNumValue2() {
		return numValue2;
	}

	public void setNumValue2(BigDecimal numValue2) {
		this.numValue2 = numValue2;
	}

	public BigDecimal getNumValue3() {
		return numValue3;
	}

	public void setNumValue3(BigDecimal numValue3) {
		this.numValue3 = numValue3;
	}

	public String getCharValue() {
		return charValue;
	}

	public void setCharValue(String charValue) {
		this.charValue = charValue;
	}

	public String getCharValue2() {
		return charValue2;
	}

	public void setCharValue2(String charValue2) {
		this.charValue2 = charValue2;
	}

	public String getCharValue3() {
		return charValue3;
	}

	public void setCharValue3(String charValue3) {
		this.charValue3 = charValue3;
	}	
}

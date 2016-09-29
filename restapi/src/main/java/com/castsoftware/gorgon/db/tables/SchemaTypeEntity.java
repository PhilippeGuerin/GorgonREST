package com.castsoftware.gorgon.db.tables;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "schema_types")
public class SchemaTypeEntity implements java.io.Serializable {

	private static final long serialVersionUID = -7911528603746606769L;

	@Id
	@Column(name = "schema_type_id", unique = true, nullable = false)
	private Integer schemaTypeId;

	@Column(name = "name", nullable = false, length = 255)
	private String name;
	
	@Column(name = "description", nullable = false, length = 255)
	private String description;

	public SchemaTypeEntity()
	{
		super();
	}
	
	public SchemaTypeEntity(Integer schemaTypeId, String name,
			String description) {
		super();
		this.schemaTypeId = schemaTypeId;
		this.name = name;
		this.description = description;
	}
	
	@Override 
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof SchemaTypeEntity))
			return false;
		SchemaTypeEntity me = (SchemaTypeEntity) o;
		
		return Objects.equals(this.schemaTypeId, me.schemaTypeId);
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(schemaTypeId, name, description);
    }

	public Integer getSchemaTypeId() {
		return schemaTypeId;
	}

	public void setSchemaTypeId(Integer schemaTypeId) {
		this.schemaTypeId = schemaTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
}

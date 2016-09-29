package com.castsoftware.gorgon.db.tables;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "schemas")
public class SchemaEntity implements java.io.Serializable {

	private static final long serialVersionUID = 4299602569222957417L;

	@Id
	@Column(name = "schema_id", nullable = false)
	private Integer schemaId;
	
	@OneToOne()
	@JoinColumn(name = "server_id", nullable = false)
	private ServerEntity server;
	
	@OneToOne()
	@JoinColumn(name = "schema_type_id", nullable = false)
	private SchemaTypeEntity schemaType;
	
	@Column(name = "name", nullable = false, length = 255)
	private String name;

	public SchemaEntity()
	{
		super();
	}
	
	public SchemaEntity(Integer schemaId, ServerEntity server, SchemaTypeEntity schemaType, String name) {
		super();
		this.schemaId = schemaId;
		this.server = server;
		this.schemaType = schemaType;
		this.name = name;
	}
	
	@Override 
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof SchemaEntity))
			return false;
		SchemaEntity se = (SchemaEntity) o;
		
		return Objects.equals(this.schemaId, se.schemaId);
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(schemaId, server, schemaType, name);
    }

	public Integer getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(Integer schemaId) {
		this.schemaId = schemaId;
	}

	public ServerEntity getServerId() {
		return server;
	}

	public void setServerId(ServerEntity server) {
		this.server = server;
	}

	public SchemaTypeEntity getSchemaTypeId() {
		return schemaType;
	}

	public void setSchemaTypeId(SchemaTypeEntity schemaType) {
		this.schemaType = schemaType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	
}

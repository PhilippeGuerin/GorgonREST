package com.castsoftware.util.db;

import java.util.EnumMap;

public class Constants {	
	
	public static enum DbType {Unknown, Postgres, Oracle, SqlServer};
	
	private static String[] DbTypeStr = {"", "postgres", "oracle", "sqlserver"};
	
	private static EnumMap<DbType, String> DbConnectionString = new EnumMap<DbType, String>(DbType.class);
	
	private static EnumMap<DbType, String> DbDriverType = new EnumMap<DbType, String>(DbType.class);
	
	private static EnumMap<DbType, String> DbDialect = new EnumMap<DbType, String>(DbType.class);
	
	static
	{
		DbDriverType.put(DbType.Unknown, "");
		DbDriverType.put(DbType.Postgres, "org.postgresql.Driver");
		DbDriverType.put(DbType.Oracle, "oracle.jdbc.OracleDriver");
		DbDriverType.put(DbType.SqlServer, "net.sourceforge.jtds.jdbc.Driver");
		
		DbDialect.put(DbType.Unknown, "");
		DbDialect.put(DbType.Postgres, "org.hibernate.dialect.PostgreSQLDialect");
		DbDialect.put(DbType.Oracle, "org.hibernate.dialect.Oracle10gDialect");
		DbDialect.put(DbType.SqlServer, "org.hibernate.dialect.SQLServerDialect");
		
		DbConnectionString.put(DbType.Unknown, "");
		DbConnectionString.put(DbType.Postgres, "jdbc:postgresql://");
		DbConnectionString.put(DbType.Oracle, "jdbc:oracle:thin:@");
		DbConnectionString.put(DbType.SqlServer, "jdbc:jtds:sqlserver://");
	}
	
	public static DbType getDbType(String Value)
	{
		for (int i=0; i < DbTypeStr.length; i++)
		{
			if (DbTypeStr[i].equals(Value))
				return DbType.values()[i];
		}
		return DbType.Unknown;
	}
	
	public static String getDriverType(DbType dbType)
	{
		return DbDriverType.get(dbType);
	}
	
	public static String getDialect(DbType dbType)
	{
		return DbDialect.get(dbType);
	}
	
	public static String getConnectionString(DbType dbType)
	{
		return DbConnectionString.get(dbType);
	}	
}

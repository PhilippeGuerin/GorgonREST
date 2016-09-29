package com.castsoftware.util.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static StandardServiceRegistry serviceRegistry; 
	private static SessionFactory sessionFactory; 
	 
	public static void buildSessionFactory(Constants.DbType dbType, 
			String url, String userName, String password, String schema) {
		if (sessionFactory != null)
			return ;
		
		// Create the SessionFactory from hibernate.cfg.xml
		Configuration configuration = new Configuration().configure();
		//
		configuration.setProperty("hibernate.connection.driver_class", Constants.getDriverType(dbType));
		configuration.setProperty("hibernate.connection.url", Constants.getConnectionString(dbType) + url);
		configuration.setProperty("hibernate.connection.username", userName);
		configuration.setProperty("hibernate.connection.password", password);
		configuration.setProperty("hibernate.default_schema", schema);
		configuration.setProperty("hibernate.dialect", Constants.getDialect(dbType));
		
		//http://stackoverflow.com/questions/10075081/hibernate-slow-to-acquire-postgres-connection
		configuration.setProperty("hibernate.temp.use_jdbc_metadata_defaults","false");
		
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		sessionFactory = configuration.configure().buildSessionFactory(serviceRegistry); 
	}
 
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
 
	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}
}

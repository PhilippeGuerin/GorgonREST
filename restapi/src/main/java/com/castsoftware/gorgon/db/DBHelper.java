package com.castsoftware.gorgon.db;

import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;

import com.castsoftware.util.db.Constants;
import com.castsoftware.util.db.HibernateUtil;

public class DBHelper {	
	public static String buildUrl(String url, Integer port, String database)
	{
		return String.format("%s:%d/%s", url, port, database);
	}
	
	public static Object runProc(Constants.DbType dbType, String url, 
			String login, String password, String schema, String proc) throws SQLGrammarException
	{
		
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, login, password, schema);
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try
        {
            session.beginTransaction();
            
            //Ugly work around so these procedures are executed in the correct schema context
            session.createSQLQuery(String.format("set search_path to %s", schema)).executeUpdate();
            return session.createSQLQuery(String.format("SELECT %s;", proc)).uniqueResult();
		    //return session.createSQLQuery(String.format("SELECT count(*) from %s;", proc)).uniqueResult();
		    
        } finally {
        	session.getTransaction().commit();
        	session.close();
        }
	}
}

package com.castsoftware.gorgon.db.util;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.castsoftware.gorgon.db.tables.SchemaEntity;
import com.castsoftware.gorgon.servers.Gorgon;
import com.castsoftware.util.db.Constants;
import com.castsoftware.util.db.HibernateUtil;

public class SchemaEntityTool {
	public static SchemaEntity getOne(Gorgon g, int serverId, int schemaId)
	{
		//Get Server
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
            @SuppressWarnings("unchecked")
			List<SchemaEntity> list = session.createCriteria(SchemaEntity.class)
					.add(Restrictions.eq("server.serverId", serverId))
					.add(Restrictions.eq("schemaId", schemaId))
					.list();
	        if (list.size() == 1)
	        	return list.iterator().next();
	        else
	        	return null;
        } finally {
        	session.getTransaction().commit();
        	session.close();
		        }
	}
	
	@SuppressWarnings("unchecked")
	//Hiding the warning due to hibernate's method 'list()'
	public static List<SchemaEntity> getAll(Gorgon g, int serverId)
	{
		//Get Server List
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
			return session.createCriteria(SchemaEntity.class)
					.add(Restrictions.eq("server.serverId", serverId))
					.addOrder(Order.asc("name"))
					.list(); 	            
        } finally {
        	session.getTransaction().commit();
        	session.close();
        }
	}
}

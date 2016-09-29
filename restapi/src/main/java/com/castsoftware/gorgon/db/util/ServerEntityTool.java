package com.castsoftware.gorgon.db.util;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.castsoftware.gorgon.db.tables.ServerEntity;
import com.castsoftware.gorgon.servers.Gorgon;
import com.castsoftware.util.db.Constants;
import com.castsoftware.util.db.HibernateUtil;

public class ServerEntityTool {
	public static ServerEntity getOne(Gorgon g, int serverId)
	{
		//Get Server
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
            @SuppressWarnings("unchecked")
			List<ServerEntity> list = session.createCriteria(ServerEntity.class)
					.add(Restrictions.eq("serverId", serverId))
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
	public static List<ServerEntity> getAll(Gorgon g)
	{
		//Get Server List
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
			return session.createCriteria(ServerEntity.class)
					.add(Restrictions.gt("serverId", 0))
					.addOrder(Order.asc("serverName"))
					.list(); 	            
        } finally {
        	session.getTransaction().commit();
        	session.close();
        }
	}
	
	public static void add(Gorgon g, ServerEntity s)
	{
		//Create Server
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
            //Get Next Id
			s.setServerId(ServerEntity.getNextId(session));
			session.save(s);
			session.getTransaction().commit();		
        } finally {
        	session.close();
        }
	}
	
	public static void update(Gorgon g, ServerEntity s)
	{
    	//Update Server
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
            session.update(s); 
			session.getTransaction().commit();

        } finally {
        	session.close();
        }
	}
	
	public static boolean delete(Gorgon g, int serverId)
	{
    	//Delete Server
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
            @SuppressWarnings("unchecked")
			List<ServerEntity> list = session.createCriteria(ServerEntity.class).add(Restrictions.eq("serverId", serverId)).list();
	        if (list.size() == 1)
	        {
	        	session.delete(list.iterator().next());
	        	session.getTransaction().commit();
	        	return true;
	        }
	        else
	        	return false;
        } finally {
        	session.close();
    	}		
	}
}

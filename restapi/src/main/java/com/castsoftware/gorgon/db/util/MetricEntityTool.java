package com.castsoftware.gorgon.db.util;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.castsoftware.gorgon.db.tables.MetricEntity;
import com.castsoftware.gorgon.db.tables.SchemaMetricEntity;
import com.castsoftware.gorgon.servers.Gorgon;
import com.castsoftware.util.db.Constants;
import com.castsoftware.util.db.HibernateUtil;

public class MetricEntityTool {
	
	@SuppressWarnings("unchecked")
    public static List<MetricEntity> getAll(Gorgon g) 
    {
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
			return session.createCriteria(MetricEntity.class)
					.addOrder(Order.asc("metricName"))
					.list(); 	            
        } finally {
        	session.getTransaction().commit();
        	session.close();
        }
    }
	
    public static List<MetricEntity> getAllForSchema(Gorgon g, int schemaId) 
    {
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
        	session.beginTransaction();
        	
        	return getAllForSchema(session, schemaId);            
        } finally {
        	session.getTransaction().commit();
        	session.close();
        }
    }
	
	@SuppressWarnings("unchecked")
    public static List<MetricEntity> getAllForSchema(Session session, int schemaId) 
    {
        Criteria metrics = session.createCriteria(MetricEntity.class, "m");
        DetachedCriteria metricsWithData = DetachedCriteria.forClass(SchemaMetricEntity.class, "me")
        		.add(Restrictions.eq("me.schema.schemaId", schemaId))
        		.add(Property.forName("me.metric.metricId").eqProperty("m.metricId"))
        		.setProjection(Projections.property("idx"));
        
		return metrics.add(Subqueries.exists(metricsWithData)).list(); 	            
    }
	
    public static MetricEntity getOne(Gorgon g, int metricId)
    {
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
            @SuppressWarnings("unchecked")
			List<MetricEntity> list = session.createCriteria(MetricEntity.class)
					.add(Restrictions.eq("metricId", metricId))
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
	
    public static void add(Gorgon g, MetricEntity m) 
    {
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
            //Get Next Id
            if (m.getMetricId() == -1)
            	m.setMetricId(MetricEntity.getNextId(session));
			session.save(m);
			session.getTransaction().commit();		
        } finally {
        	session.close();
        }
    }

    public static void update(Gorgon g, MetricEntity m) 
    {
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
            session.update(m); 
			session.getTransaction().commit();
        } finally {
        	session.close();
        }
    }
    
    public static boolean delete(Gorgon g, int metricId)
    {
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
            @SuppressWarnings("unchecked")
			List<MetricEntity> list = session.createCriteria(MetricEntity.class).add(Restrictions.eq("metricId", metricId)).list();
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

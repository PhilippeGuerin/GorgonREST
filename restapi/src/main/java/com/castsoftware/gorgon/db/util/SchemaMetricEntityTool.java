package com.castsoftware.gorgon.db.util;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.castsoftware.gorgon.db.resultsets.MetricData;
import com.castsoftware.gorgon.db.tables.MetricEntity;
import com.castsoftware.gorgon.db.tables.SchemaMetricEntity;
import com.castsoftware.gorgon.servers.Gorgon;
import com.castsoftware.util.db.Constants;
import com.castsoftware.util.db.HibernateUtil;

public class SchemaMetricEntityTool {

    @SuppressWarnings("unchecked")
	public static List<MetricData> getAllMetrics(Gorgon g, int schemaId) 
    {    	    	
    	List<MetricData> list = new ArrayList<MetricData>();
    	//Get Server List
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            
    		//Get Metric List with Data
        	for(MetricEntity m : MetricEntityTool.getAllForSchema(session, schemaId))
        	{
        		List<SchemaMetricEntity> data = new ArrayList<SchemaMetricEntity>();
        		Integer count = 0;
        		if (m.getMetricType().equals("L") && 
        				(m.getLevel().equals("INFO") || m.getLevel().equals("RECOMMENDATION")))
        		{
        			data = session.createCriteria(SchemaMetricEntity.class)
        					.add(Restrictions.eq("schema.schemaId", schemaId))
        					.add(Restrictions.eq("metric.metricId", m.getMetricId()))
        					.addOrder(Order.asc("idx"))
        					.list();
        			count = data.size();
        		} else if (m.getMetricType().equals("L") && m.getLevel().equals("ERROR"))
        		{
        			count = session.createCriteria(SchemaMetricEntity.class)
        					.add(Restrictions.eq("schema.schemaId", schemaId))
        					.add(Restrictions.eq("metric.metricId", m.getMetricId()))
        					.addOrder(Order.asc("idx"))
        					.list()
        					.size();
        		}
        		list.add(new MetricData(m, data, count));
        	}            
			return list; 	            
        } finally {
        	session.getTransaction().commit();
        	session.close();
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<SchemaMetricEntity> getOneMetric(Gorgon g, int schemaId, int metricId)
    {
		//Get Server List
		String url = String.format("%s:%d/%s", g.getUrl(), g.getPort(), g.getDatabase());
		HibernateUtil.buildSessionFactory(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), g.getSchema());
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();            

            return session.createCriteria(SchemaMetricEntity.class)
					.add(Restrictions.eq("schema.schemaId", schemaId))
					.add(Restrictions.eq("metric.metricId", metricId))
					.addOrder(Order.asc("idx"))
					.list();
        } finally {
        	session.getTransaction().commit();
        	session.close();
        }
    }
}

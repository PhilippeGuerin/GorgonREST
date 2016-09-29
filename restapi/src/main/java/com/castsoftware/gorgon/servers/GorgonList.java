package com.castsoftware.gorgon.servers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GorgonList {
	private static final Logger logger = Logger.getLogger(GorgonList.class);
	private String gorgonFileName;
	private final Lock lock = new ReentrantLock();
	private ArrayList<Gorgon> list = new ArrayList<Gorgon>();
	
	public String getGorgonFileName() throws InterruptedException {
		try
		{
			lock.tryLock(10, TimeUnit.SECONDS);
			return gorgonFileName;
		} 
		finally
		{
			lock.unlock();
		}
	}

	public void setGorgonFileName(String gorgonFileName) {
		try
		{
			lock.tryLock(10, TimeUnit.SECONDS);
			this.gorgonFileName = gorgonFileName;
		} catch (InterruptedException e) {
			logger.error(e);
		} 
		finally
		{
			lock.unlock();
		}
	}
	
	public Boolean load()
	{
		Boolean loaded = false;
		try
		{
			lock.tryLock(10, TimeUnit.SECONDS);
			
			File file = new File(gorgonFileName);
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int)file.length()];
			fis.read(data);
		    fis.close();
		    
		    String jsonString = new String(data, "UTF-8");

		    Type collectionType = new TypeToken<Collection<Gorgon>>(){}.getType();
			Collection<Gorgon> tmp;
			Gson gson = new Gson();
			
	        tmp = gson.fromJson(jsonString, collectionType);
	        List<Gorgon> newList = new ArrayList<Gorgon>(tmp);
	        list.addAll(newList);
		    
		    loaded = true;
		} catch (InterruptedException e) {
			logger.error(e);
		} catch (FileNotFoundException e) {
			logger.error(String.format("File '%s' not found. Unable to load Gorgon List!", gorgonFileName));
		} catch (IOException e) {
			logger.error(String.format("IO Excetpion while reading '%s'. Unable to load Gorgon List!", gorgonFileName));
		}
		finally
		{
			lock.unlock();
		}
		return loaded;
	}
	
	public Boolean save()
	{
		Boolean saved = false;
		try
		{
			lock.tryLock(10, TimeUnit.SECONDS);
			
			Files.write(Paths.get(gorgonFileName), 
					getList(true).getBytes("UTF-8"), 
					StandardOpenOption.CREATE, 
					StandardOpenOption.TRUNCATE_EXISTING);
			saved = true;
		} catch (InterruptedException e) {
			logger.error(e);
		} catch (UnsupportedEncodingException e) {
			logger.error(String.format("Encoding error in file '%s'. Unable to save Gorgon List!", gorgonFileName));
		} catch (IOException e) {
			logger.error(String.format("IO Excetpion while writing '%s'. Unable to save Gorgon List!", gorgonFileName));
		}
		finally
		{
			lock.unlock();
		}
		return saved;
	}
		
	public Gorgon getGorgon(Integer id)
	{
		try
		{
			lock.tryLock(10, TimeUnit.SECONDS);
			
			for(Gorgon g : list)
				if (g.getId().equals(id))
					return (Gorgon) g.clone();
			return null;
		} catch (InterruptedException | CloneNotSupportedException e) {
			logger.error(e);
			return null;
		} finally
		{
			lock.unlock();
		}
	}
	
	private Boolean isNameUnique(String name)
	{
		for(Gorgon g : list)
			if (g.getName().equals(name))
				return false;
		return true;
	}
	
	private Integer getNextId()
	{
		Integer newId = 0;
		for(Gorgon g : list)
			newId = Math.max(newId, g.getId());
		return newId + 1;
	}
	
	public static List<Gorgon> cloneList(List<Gorgon> list) throws CloneNotSupportedException {
	    List<Gorgon> clone = new ArrayList<Gorgon>(list.size());
	    for(Gorgon g: list) 
	    	clone.add((Gorgon) g.clone());
	    return clone;
	}
	
	public List<Gorgon> getList()
	{
		try
		{
			lock.tryLock(10, TimeUnit.SECONDS);
			
			return cloneList(list);
		} catch (InterruptedException | CloneNotSupportedException e) {
			logger.error(e);
			return new ArrayList<Gorgon>();
		} finally
		{
			lock.unlock();
		}	
	}
	
	private String getList(Boolean pretty) throws InterruptedException 
	{
		try
		{
			lock.tryLock(10, TimeUnit.SECONDS);
			
			Gson gson;
			if (pretty)
				gson = new GsonBuilder().setPrettyPrinting().create();
			else
				gson = new Gson();
			
			return gson.toJson(list);
		}
		finally
		{
			lock.unlock();
		}				
	}
	
	public Boolean addGorgon(Gorgon g) 
	{
		try
		{
			lock.tryLock(10, TimeUnit.SECONDS);
			
			while (!isNameUnique(g.getName()))
				g.setName(g.getName() + "#");
			
			//assign new Id
			g.setId(getNextId());
			
			list.add(g);
			
			Collections.sort(list);
        	save();
        	return true;
		}  catch (InterruptedException e) {
			logger.error(e);
			return false;
		} finally
		{
			lock.unlock();
		}
	}
	
	public Boolean updateGorgon(Gorgon g)
	{
		try
		{
			lock.tryLock(10, TimeUnit.SECONDS);
			
			Gorgon originalGorgon = getGorgon(g.getId());
			
			//Not found!
			if (originalGorgon == null)
				return false;
			
			list.remove(originalGorgon);
			//Make sure name is unique!
			while (!isNameUnique(g.getName()))
				g.setName(g.getName() + "#");
			
			list.add(g);
			
			Collections.sort(list);
        	save();
        	return true;
		}  catch (InterruptedException e) {
			logger.error(e);
			return false;
		} finally
		{
			lock.unlock();
		}
	}
	
	public Boolean deleteGorgon(Integer gorgonServerId)
	{
		try
		{
			lock.tryLock(10, TimeUnit.SECONDS);
			
			Gorgon originalGorgon = getGorgon(gorgonServerId);
			
			//Not found!
			if (originalGorgon == null)
				return false;
			
			list.remove(originalGorgon);
        	save();
        	return true;
		}  catch (InterruptedException e) {
			logger.error(e);
			return false;
		} finally
		{
			lock.unlock();
		}
	}
}

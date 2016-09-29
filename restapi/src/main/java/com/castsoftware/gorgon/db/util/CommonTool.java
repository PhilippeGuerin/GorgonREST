package com.castsoftware.gorgon.db.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.castsoftware.gorgon.db.DBHelper;
import com.castsoftware.gorgon.servers.Gorgon;
import com.castsoftware.util.db.Constants;

public class CommonTool {
	private static final String GET_SERVER_TIME = "util_get_server_time()";
	
	public static Date getServerTime(Gorgon g) throws ParseException
	{
		String url = DBHelper.buildUrl(g.getUrl(), g.getPort(), g.getDatabase());
		
		String serverTime = (String)DBHelper.runProc(Constants.DbType.Postgres, url, g.getLogin(), g.getPassword(), 
				g.getSchema(), GET_SERVER_TIME);
		
		//Expected format yyyy-MM-DD HH24:MI:SS
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(serverTime);
	}
}

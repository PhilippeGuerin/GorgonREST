package com.castsoftware.gorgon.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

public class PostgresNotificationListener extends Thread {
	private static final Logger logger = Logger.getLogger(PostgresNotificationListener.class);
	
	private Connection conn;
	private PostgresNotificationFeedback feedback;
	
	private volatile boolean running = true;

    public void terminate() {
        running = false;
    }
	
	public PostgresNotificationListener(String server, Integer port, String database, 
			String login, String password, PostgresNotificationFeedback feedback) 
					throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		String url = String.format("jdbc:postgresql://%s:%d/%s", server, port, database);
		conn = DriverManager.getConnection(url, login, password);;
		this.feedback = feedback;
		Statement stmt = conn.createStatement();
		try {
			stmt.execute("LISTEN gorgon");
			logger.info("Listening to gorgon");
		} finally {
			stmt.close();
		}
	}
	
	public void run() {
		try {
			logger.info("Gorgon Listening thread running...");
			PGConnection pgconn = (PGConnection) conn;
			while (running) {
				try {
					// issue a dummy query to contact the backend
					// and receive any pending notifications.
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT 1");
					rs.close();
					stmt.close();
	
					PGNotification notifications[] = pgconn.getNotifications();
					if (notifications != null) {
						for (int i=0; i<notifications.length; i++) {
							synchronized (this)
							{
								logger.info(notifications[i].getParameter());
								feedback.reportNotification(notifications[i]);
							}
						}
					}
	
					// wait a while before checking again for new
					// notifications
					Thread.sleep(500);
				} catch (InterruptedException | SQLException e) {
					logger.error(e);
				}
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}
}

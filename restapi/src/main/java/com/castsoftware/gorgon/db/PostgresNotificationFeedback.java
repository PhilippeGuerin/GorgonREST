package com.castsoftware.gorgon.db;

import org.postgresql.PGNotification;

public interface PostgresNotificationFeedback {
	public void reportNotification(PGNotification notification);
}

package org.example.util;

import org.apache.maven.plugin.logging.Log;
import org.example.model.enums.Driver;
import org.example.schema.ConnectionManager;

public abstract class CurrentContext {
	private CurrentContext() {}
	public static Log log = null;
	public static ConnectionManager connectionManager = null;

	public static Driver getDriver() {
		if (connectionManager == null) return null;
		else return connectionManager.getDriver();
	}
}

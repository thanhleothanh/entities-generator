package org.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.example.schema.ConnectionManager;

public abstract class AbstractGeneratorContext extends AbstractMojo {
	public static Log log;
	public static ConnectionManager connectionManager;

	@Override
	public void execute() {
		initContext();
		doExecute();
	}

	private void initContext() {
		log = getLog();
		initConnectionManager();
	}

	public abstract void doExecute();
	public abstract void initConnectionManager();

}

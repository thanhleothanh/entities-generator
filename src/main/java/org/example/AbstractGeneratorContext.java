package org.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.example.schema.ConnectionManager;

public abstract class AbstractGeneratorContext extends AbstractMojo {
	@Parameter(property = "jdbcUrl", required = true)
	protected String jdbcUrl;
	@Parameter(property = "jdbcUser", required = true)
	protected String jdbcUser;
	@Parameter(property = "jdbcPassword", required = true)
	protected String jdbcPassword;
	@Parameter(property = "toPath", required = true)
	protected String toPath;
	public static String packageName;
	public static Log log;
	public static ConnectionManager connectionManager;

	@Override
	public void execute() {
		initContext();
		doExecute();
	}

	private void initContext() {
		packageName = toPath;
		log = getLog();
		connectionManager = new ConnectionManager(jdbcUrl, jdbcUser, jdbcPassword);
	}

	public abstract void doExecute();

}

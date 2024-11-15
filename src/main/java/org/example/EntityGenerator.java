package org.example;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.example.schema.AbstractSchemaScanner;
import org.example.schema.ConnectionManager;
import org.example.schema.SchemaScannerFactory;
import org.example.util.LogUtil;

@Mojo(name = "entities-generate")
public final class EntityGenerator extends AbstractMojo {
	{
		LogUtil.log = getLog();
	}

	@Parameter(property = "jdbcUrl", required = true)
	private String jdbcUrl;
	@Parameter(property = "jdbcUser", required = true)
	private String jdbcUser;
	@Parameter(property = "jdbcPassword", required = true)
	private String jdbcPassword;


	@Override
	public void execute() {
		getLog().info("Generating entities!");
		getLog().info(jdbcUrl);
		getLog().info(jdbcUser);
		getLog().info(jdbcPassword);

		ConnectionManager connectionManager = new ConnectionManager(jdbcUrl, jdbcUser, jdbcPassword);
		AbstractSchemaScanner abstractSchemaScanner = SchemaScannerFactory.of(connectionManager);
		abstractSchemaScanner.scanTablePrimaryKeys();
		abstractSchemaScanner.scanTableColumns();
		abstractSchemaScanner.scanTableForeignKeys();
		abstractSchemaScanner.scanViews();
	}
}

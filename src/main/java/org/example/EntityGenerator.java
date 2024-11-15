package org.example;


import java.util.List;
import java.util.Map;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.example.model.Column;
import org.example.model.Constraint;
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
		LogUtil.log.info("Generating entities!");
		LogUtil.log.info(jdbcUrl);
		LogUtil.log.info(jdbcUser);
		LogUtil.log.info(jdbcPassword);

		ConnectionManager connectionManager = new ConnectionManager(jdbcUrl, jdbcUser, jdbcPassword);
		AbstractSchemaScanner scanner = SchemaScannerFactory.of(connectionManager);
		Map<String, List<Constraint>> primaryKeys = scanner.scanTablePrimaryKeys();
		Map<String, List<Column>> columns = scanner.scanTableColumns();
		scanner.scanTableForeignKeys();
		scanner.scanViews();
	}
}

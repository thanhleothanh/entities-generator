package org.example;


import java.util.List;
import java.util.Map;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.example.model.entity.Entity;
import org.example.model.entity.Field;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;
import org.example.schema.AbstractSchemaScanner;
import org.example.schema.ConnectionManager;
import org.example.schema.SchemaScannerFactory;
import org.example.service.FtlTemplateService;
import org.example.service.TemplateService;
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
		LogUtil.log.info("Scanning schema!");
		AbstractSchemaScanner scanner = SchemaScannerFactory.of(new ConnectionManager(jdbcUrl, jdbcUser, jdbcPassword));
		Map<String, List<Constraint>> primaryKeys = scanner.scanTablePrimaryKeys();
		Map<String, List<Column>> columns = scanner.scanTableColumns();
		Map<String, List<Constraint>> foreignKeys = scanner.scanTableForeignKeys();
		scanner.scanViews();

		LogUtil.log.info("Generating Java files!");
		Entity entity = new Entity(
				"DCustomer",
				"d_customers",
				List.of(
						new Field("id", "id", "Long", true),
						new Field("name", "name", "String", false)));
		TemplateService service = new FtlTemplateService();
		service.process(List.of(entity));
	}
}

package org.example;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.text.CaseUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.example.model.entity.Entity;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;
import org.example.schema.AbstractSchemaScanner;
import org.example.schema.ConnectionManager;
import org.example.schema.SchemaScannerFactory;
import org.example.service.FtlTemplateService;
import org.example.service.TemplateService;
import org.example.util.CurrentContext;

@Mojo(name = "entities-generate")
public final class EntityGenerator extends AbstractMojo {
	{
		CurrentContext.log = getLog();
	}

	@Parameter(property = "jdbcUrl", required = true)
	private String jdbcUrl;
	@Parameter(property = "jdbcUser", required = true)
	private String jdbcUser;
	@Parameter(property = "jdbcPassword", required = true)
	private String jdbcPassword;


	@Override
	public void execute() {
		CurrentContext.log.info("Scanning schema!");
		ConnectionManager connectionManager = new ConnectionManager(jdbcUrl, jdbcUser, jdbcPassword);
		CurrentContext.connectionManager = connectionManager;
		AbstractSchemaScanner scanner = SchemaScannerFactory.of(connectionManager);
		Map<String, List<Constraint>> primaryKeys = scanner.scanTablePrimaryKeys();
		Map<String, List<Column>> columns = scanner.scanTableColumns();
		Map<String, List<Constraint>> foreignKeys = scanner.scanTableForeignKeys();
		scanner.scanViews();

		CurrentContext.log.info("Generating Java files!");
		TemplateService service = new FtlTemplateService();
		service.process(normalizeSchemaData(primaryKeys, columns, foreignKeys));
	}

	private List<Entity> normalizeSchemaData(
			Map<String, List<Constraint>> primaryKeys,
			Map<String, List<Column>> columns,
			Map<String, List<Constraint>> foreignKeys
	) {
		return Stream.of(primaryKeys.keySet(), columns.keySet(), foreignKeys.keySet())
				.flatMap(Set::stream)
				.collect(Collectors.toSet())
				.parallelStream()
				.map(tableName -> buildEntityFromSchema(
						tableName,
						primaryKeys.getOrDefault(tableName, Collections.emptyList()),
						columns.getOrDefault(tableName, Collections.emptyList()),
						foreignKeys.getOrDefault(tableName, Collections.emptyList())))
				.toList();
	}

	private Entity buildEntityFromSchema(
			String table,
			List<Constraint> primaryKeys,
			List<Column> columns,
			List<Constraint> foreignKeys
	) {
		Map<String, Constraint> mapPrimaryKeyByColumnName = primaryKeys.stream().collect(Collectors.toMap(Constraint::columnName, Function.identity(), (l, r) -> l));
		Map<String, Constraint> mapForeignKeyByColumnName = foreignKeys.stream().collect(Collectors.toMap(Constraint::columnName, Function.identity(), (l, r) -> l));

		Entity entity = new Entity().toBuilder()
				.name(CaseUtils.toCamelCase(table, true, '_'))
				.tableName(table)
				.build();
		columns.stream()
				.filter(col -> mapPrimaryKeyByColumnName.containsKey(col.columnName()))
				.forEach(col -> entity.addId(col, mapForeignKeyByColumnName.get(col.columnName())));
		columns.stream()
				.filter(col -> !mapPrimaryKeyByColumnName.containsKey(col.columnName()))
				.forEach(col -> entity.addField(col, mapForeignKeyByColumnName.get(col.columnName())));
		return entity;
	}

}

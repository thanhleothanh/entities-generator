package org.example.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.example.AbstractGeneratorContext;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;

public final class PostgresSchemaScanner extends AbstractSchemaScanner {
	private static final String QUERY_PRIMARY_KEY_CONSTRAINTS = """
				SELECT istc.constraint_type AS constraint_type,
				       istc.constraint_name AS constraint_name,
				       istc.table_name      AS table_name,
				       kcu.column_name      AS column_name
				  FROM information_schema.table_constraints istc
				  JOIN information_schema.key_column_usage kcu ON istc.constraint_name = kcu.constraint_name
				 WHERE istc.constraint_type = 'PRIMARY KEY'
				   AND istc.table_schema NOT IN ('pg_catalog', 'information_schema')""";

	private static final String QUERY_TABLE_COLUMNS = """
				SELECT isc.table_name        AS table_name,
				       isc.column_name       AS column_name,
				       isc.data_type         AS data_type
				  FROM information_schema.columns isc
				  JOIN information_schema.tables ist ON isc.table_name = ist.table_name
				 WHERE isc.table_schema NOT IN ('pg_catalog', 'information_schema')
				   AND ist.table_type = 'BASE TABLE'
				 ORDER BY table_name, ordinal_position""";

	private static final String QUERY_FOREIGN_KEY_CONSTRAINTS = """
				SELECT tc.constraint_type AS constraint_type,
				       tc.constraint_name AS constraint_name,
				       kcu.table_name     AS table_name,
				       kcu.column_name    AS column_name,
				       fkcu.table_name    AS f_table_name,
				       fkcu.column_name   AS f_column_name
				  FROM information_schema.table_constraints tc
				  INNER JOIN (SELECT DISTINCT * FROM information_schema.referential_constraints rc) rc ON tc.constraint_name = rc.constraint_name
				  INNER JOIN information_schema.key_column_usage kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_name = kcu.table_name
				  INNER JOIN information_schema.key_column_usage fkcu ON fkcu.ordinal_position = kcu.position_in_unique_constraint AND fkcu.constraint_name = rc.unique_constraint_name
				 WHERE tc.constraint_type = 'FOREIGN KEY'
				   AND tc.table_schema NOT IN ('pg_catalog', 'information_schema')""";

	private static final String QUERY_VIEW_COLUMNS = """
				SELECT isc.table_name        AS table_name,
				       isc.column_name       AS column_name,
				       isc.data_type         AS data_type
				  FROM information_schema.columns isc
				  JOIN information_schema.tables ist ON isc.table_name = ist.table_name
				 WHERE isc.table_schema NOT IN ('pg_catalog', 'information_schema')
				   AND ist.table_type = 'VIEW'
				 ORDER BY table_name, ordinal_position""";

	public PostgresSchemaScanner(ConnectionManager connectionManager) {
		super(connectionManager);
	}

	@Override
	public Map<String, List<Constraint>> scanTablePrimaryKeys() {
		AbstractGeneratorContext.log.info("\t\t\t\t\tScanning all table primary keys");
		try (Connection connection = connectionManager.get(); PreparedStatement st = connection.prepareStatement(QUERY_PRIMARY_KEY_CONSTRAINTS)) {
			return normalizePrimaryKeys(st.executeQuery());
		} catch (SQLException e) {
			AbstractGeneratorContext.log.error(String.format("Failed scanning all table primary keys - %s", e.getMessage()));
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, List<Column>> scanTableColumns() {
		AbstractGeneratorContext.log.info("\t\t\t\t\tScanning all table columns");
		try (Connection connection = connectionManager.get(); PreparedStatement st = connection.prepareStatement(QUERY_TABLE_COLUMNS)) {
			return normalizeColumns(st.executeQuery());
		} catch (SQLException e) {
			AbstractGeneratorContext.log.error(String.format("Failed scanning all table columns - %s", e.getMessage()));
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, List<Constraint>> scanTableForeignKeys() {
		AbstractGeneratorContext.log.info("\t\t\t\t\tScanning all table foreign keys");
		try (Connection connection = connectionManager.get(); PreparedStatement st = connection.prepareStatement(QUERY_FOREIGN_KEY_CONSTRAINTS)) {
			return normalizeForeignKeys(st.executeQuery());
		} catch (SQLException e) {
			AbstractGeneratorContext.log.error(String.format("Failed scanning all table foreign keys - %s", e.getMessage()));
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, List<Column>> scanViews() {
		AbstractGeneratorContext.log.info("\t\t\t\t\tScanning all view columns");
		try (Connection connection = connectionManager.get(); PreparedStatement st = connection.prepareStatement(QUERY_VIEW_COLUMNS)) {
			return normalizeColumns(st.executeQuery());
		} catch (SQLException e) {
			AbstractGeneratorContext.log.error(String.format("Failed scanning all view columns - %s", e.getMessage()));
			throw new RuntimeException(e);
		}
	}
}

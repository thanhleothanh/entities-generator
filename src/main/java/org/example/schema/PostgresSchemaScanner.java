package org.example.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;
import org.example.util.CurrentContext;

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

	private static final String QUERY_COLUMNS = """
				SELECT isc.table_name        AS table_name,
				       isc.column_name       AS column_name,
				       isc.data_type         AS data_type,
				       isc.is_nullable::bool AS is_nullable
				  FROM information_schema.columns isc
				 WHERE isc.table_schema NOT IN ('pg_catalog', 'information_schema')
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

	public PostgresSchemaScanner(ConnectionManager connectionManager) {
		super(connectionManager);
	}

	@Override
	public Map<String, List<Constraint>> scanTablePrimaryKeys() {
		CurrentContext.log.info("Scanning all table primary keys");
		try (Connection connection = connectionManager.get(); PreparedStatement st = connection.prepareStatement(QUERY_PRIMARY_KEY_CONSTRAINTS)) {
			return normalizePrimaryKeys(st.executeQuery());
		} catch (SQLException e) {
			CurrentContext.log.error(String.format("Failed scanning all table primary keys - %s", e.getMessage()));
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, List<Column>> scanTableColumns() {
		CurrentContext.log.info("Scanning all table columns");
		try (Connection connection = connectionManager.get(); PreparedStatement st = connection.prepareStatement(QUERY_COLUMNS)) {
			return normalizeColumns(st.executeQuery());
		} catch (SQLException e) {
			CurrentContext.log.error(String.format("Failed scanning all table columns - %s", e.getMessage()));
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, List<Constraint>> scanTableForeignKeys() {
		CurrentContext.log.info("Scanning all table foreign keys");
		try (Connection connection = connectionManager.get(); PreparedStatement st = connection.prepareStatement(QUERY_FOREIGN_KEY_CONSTRAINTS)) {
			return normalizeForeignKeys(st.executeQuery());
		} catch (SQLException e) {
			CurrentContext.log.error(String.format("Failed scanning all table foreign keys - %s", e.getMessage()));
			throw new RuntimeException(e);
		}
	}

	@Override
	public void scanViews() {
		try (Connection connection = connectionManager.get(); Statement st = connection.createStatement()) {
			ResultSet rs = st.executeQuery("SELECT * FROM information_schema.tables WHERE table_type = 'VIEW'");
			rs.next();
			int colCount = rs.getMetaData().getColumnCount();
			StringBuilder str = new StringBuilder();
			for (int i = 1; i <= colCount; i++) str.append(rs.getString(i)).append(" ");
			CurrentContext.log.info(str);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

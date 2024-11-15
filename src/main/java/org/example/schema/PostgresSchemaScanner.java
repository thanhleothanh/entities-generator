package org.example.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import org.example.model.Constraint;
import org.example.util.LogUtil;

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

	public PostgresSchemaScanner(ConnectionManager connectionManager) {
		super(connectionManager);
	}

	@Override
	public Map<String, List<Constraint>> scanTablePrimaryKeys() {
		LogUtil.log.info("Scanning all table primary keys");
		try (Connection connection = connectionManager.get(); PreparedStatement st = connection.prepareStatement(QUERY_PRIMARY_KEY_CONSTRAINTS)) {
			return normalizePrimaryKeys(st.executeQuery());
		} catch (SQLException e) {
			LogUtil.log.error(String.format("Failed scanning all table primary keys - %s", e.getMessage()));
			throw new RuntimeException(e);
		}
	}

	@Override
	public void scanTableColumns() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void scanTableForeignKeys() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void scanViews() {
		try (Connection connection = connectionManager.get(); Statement st = connection.createStatement()) {
			ResultSet rs = st.executeQuery("SELECT * FROM information_schema.tables WHERE table_type = 'VIEW'");
			rs.next();
			int colCount = rs.getMetaData().getColumnCount();
			StringBuilder str = new StringBuilder();
			for (int i = 1; i <= colCount; i++) str.append(rs.getString(i)).append(" ");
			LogUtil.log.info(str);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
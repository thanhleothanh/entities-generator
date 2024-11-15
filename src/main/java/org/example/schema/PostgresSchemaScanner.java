package org.example.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.example.util.LogUtil;

public final class PostgresSchemaScanner extends AbstractSchemaScanner {
	public PostgresSchemaScanner(ConnectionManager connectionManager) {
		super(connectionManager);
	}

	@Override
	public void scanTablePrimaryKeys() {
		throw new UnsupportedOperationException();
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

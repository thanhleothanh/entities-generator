package org.example.schema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.example.model.Column;
import org.example.model.Constraint;
import org.example.model.enums.ConstraintType;
import org.example.util.LogUtil;

public abstract class AbstractSchemaScanner {
	protected final ConnectionManager connectionManager;

	protected AbstractSchemaScanner(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public abstract Map<String, List<Constraint>> scanTablePrimaryKeys();
	public abstract Map<String, List<Column>> scanTableColumns();
	public abstract void scanTableForeignKeys();
	public abstract void scanViews();

	protected Map<String, List<Constraint>> normalizePrimaryKeys(ResultSet rs) throws SQLException {
		List<Constraint> constraints = new ArrayList<>();
		while (rs.next()) {
			Constraint constraint = new Constraint(
					ConstraintType.of(this.connectionManager.getDriver(), rs.getString("constraint_type")),
					rs.getString("constraint_name"),
					rs.getString("table_name"),
					rs.getString("column_name"),
					null,
					null);
			constraints.add(constraint);
			LogUtil.log.info(String.format("Found in table '%s', primary key '%s'", constraint.tableName(), constraint.columnName()));
		}
		return constraints.stream().collect(Collectors.groupingBy(Constraint::constraintName));
	}

	protected Map<String, List<Column>> normalizeColumns(ResultSet rs) throws SQLException {
		List<Column> columns = new ArrayList<>();
		while (rs.next()) {
			Column column = new Column(
					rs.getString("table_name"),
					rs.getString("column_name"),
					rs.getString("data_type"),
					rs.getBoolean("is_nullable"));
			columns.add(column);
			LogUtil.log.info(String.format("Found in table '%s', column '%s', data type '%s'", column.tableName(), column.columnName(), column.dataType()));
		}
		return columns.stream().collect(Collectors.groupingBy(Column::tableName));
	}

	protected void normalizeForeignKeys() {

	}

	protected void normalizeViews() {

	}
}

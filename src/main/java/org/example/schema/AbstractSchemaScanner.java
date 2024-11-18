package org.example.schema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.example.AbstractGeneratorContext;
import org.example.model.enums.ConstraintType;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;

public abstract class AbstractSchemaScanner {
	protected final ConnectionManager connectionManager;

	protected AbstractSchemaScanner(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public abstract Map<String, List<Constraint>> scanTablePrimaryKeys();
	public abstract Map<String, List<Column>> scanTableColumns();
	public abstract Map<String, List<Constraint>> scanTableForeignKeys();
	public abstract Map<String, List<Column>> scanViews();

	protected Map<String, List<Constraint>> normalizePrimaryKeys(ResultSet rs) throws SQLException {
		List<Constraint> constraints = new ArrayList<>();
		while (rs.next()) {
			Constraint constraint = new Constraint(
					ConstraintType.of(this.connectionManager.getDriver(), rs.getString("constraint_type")),
					rs.getString("constraint_name"),
					rs.getString("table_name"),
					rs.getString("column_name"),
					rs.getString("table_name"),
					rs.getString("column_name"));
			constraints.add(constraint);
			AbstractGeneratorContext.log.info(String.format("\t\tFound in table (%s), primary key (%s)", constraint.tableName(), constraint.columnName()));
		}
		return constraints.stream().collect(Collectors.groupingBy(Constraint::tableName, TreeMap::new, Collectors.toList()));
	}

	protected Map<String, List<Column>> normalizeColumns(ResultSet rs) throws SQLException {
		List<Column> columns = new ArrayList<>();
		while (rs.next()) {
			Column column = new Column(
					rs.getString("table_name"),
					rs.getString("column_name"),
					rs.getString("data_type"));
			columns.add(column);
			AbstractGeneratorContext.log.info(String.format("\t\tFound in table (%s), column (%s), data type (%s)", column.tableName(), column.columnName(), column.dataType()));
		}
		return columns.stream().collect(Collectors.groupingBy(Column::tableName, TreeMap::new, Collectors.toList()));
	}

	protected Map<String, List<Constraint>> normalizeForeignKeys(ResultSet rs) throws SQLException {
		List<Constraint> constraints = new ArrayList<>();
		while (rs.next()) {
			Constraint constraint = new Constraint(
					ConstraintType.of(this.connectionManager.getDriver(), rs.getString("constraint_type")),
					rs.getString("constraint_name"),
					rs.getString("table_name"),
					rs.getString("column_name"),
					rs.getString("fk_table_name"),
					rs.getString("fk_column_name"));
			constraints.add(constraint);
			AbstractGeneratorContext.log.info(String.format("\t\tFound foreign key, %s (%s) -> %s (%s)", constraint.tableName(), constraint.columnName(), constraint.fkTableName(), constraint.fkColumnName()));
		}
		return constraints.stream().collect(Collectors.groupingBy(Constraint::tableName, TreeMap::new, Collectors.toList()));
	}
}

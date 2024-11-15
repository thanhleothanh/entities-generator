package org.example.schema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.example.model.Constraint;
import org.example.model.enums.ConstraintType;
import org.example.util.LogUtil;

public abstract class AbstractSchemaScanner {
	protected final ConnectionManager connectionManager;

	protected AbstractSchemaScanner(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public abstract Map<String, List<Constraint>> scanTablePrimaryKeys();
	public abstract void scanTableColumns();
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
			LogUtil.log.info(String.format("Found primary key '%s' for table '%s'", constraint.columnName(), constraint.tableName()));
		}
		return constraints.stream().collect(Collectors.groupingBy(Constraint::constraintName));
	}

	protected void normalizeColumns() {

	}

	protected void normalizeForeignKeys() {

	}

	protected void normalizeViews() {

	}
}

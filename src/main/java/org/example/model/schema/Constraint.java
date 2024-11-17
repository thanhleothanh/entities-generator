package org.example.model.schema;

import org.example.model.enums.ConstraintType;

public record Constraint(
		ConstraintType constraintType,
		String constraintName,
		String tableName,
		String columnName,
		String fkTableName,
		String fkColumnName
) {
}

package org.example.model;

public record Column(
		String tableName,
		String columnName,
		String dataType,
		Boolean isNullable
) {
}

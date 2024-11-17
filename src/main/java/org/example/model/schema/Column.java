package org.example.model.schema;

public record Column(
		String tableName,
		String columnName,
		String dataType
) {
}

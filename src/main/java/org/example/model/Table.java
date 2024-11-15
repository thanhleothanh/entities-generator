package org.example.model;

public record Table(
		String tableName,
		String columnName,
		String dataType
) {
}
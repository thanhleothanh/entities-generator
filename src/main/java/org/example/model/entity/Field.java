package org.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.text.CaseUtils;
import org.example.AbstractGeneratorContext;
import org.example.model.enums.DataType;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;

@Getter
@AllArgsConstructor
public class Field {
	private final String name;
	private final String columnName;
	private final String javaClass;

	public static Field of(Column col) {
		DataType dataType = DataType.of(AbstractGeneratorContext.connectionManager.getDriver(), col.dataType());
		return new Field(
				CaseUtils.toCamelCase(col.columnName(), false, '_'),
				col.columnName(),
				dataType.getJavaClass());
	}

	public static Field of(Constraint fk) {
		return new Field(
				CaseUtils.toCamelCase(fk.fColumnName(), false, '_'),
				fk.fColumnName(),
				CaseUtils.toCamelCase(fk.fTableName(), true, '_'));
	}
}

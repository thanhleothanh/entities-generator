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

	public static Field ofReferencingField(Constraint fk) {
		return new Field(
				CaseUtils.toCamelCase(fk.columnName(), false, '_'),
				fk.columnName(),
				CaseUtils.toCamelCase(fk.fkTableName(), true, '_'));
	}

	public static Field ofReferencedField(Constraint fk) {
		return new Field(
				CaseUtils.toCamelCase(fk.fkColumnName(), false, '_'),
				fk.fkColumnName(),
				null);
	}
}

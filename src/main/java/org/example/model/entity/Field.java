package org.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.AbstractGeneratorContext;
import org.example.model.enums.DataType;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;
import org.example.util.TextUtil;

@Getter
@AllArgsConstructor
public class Field {
	@Setter
	private String name;
	private final String columnName;
	private final String javaClass;

	public static Field of(Column col) {
		DataType dataType = DataType.of(AbstractGeneratorContext.connectionManager.getDriver(), col.dataType());
		return new Field(
				TextUtil.toCamelCase(col.columnName(), false),
				col.columnName(),
				dataType.getJavaClass());
	}

	public static Field ofReferencingField(Constraint fk) {
		return new Field(
				TextUtil.toCamelCase(fk.columnName(), false),
				fk.columnName(),
				TextUtil.toCamelCase(fk.tableName(), true));
	}

	public static Field ofReferencedField(Constraint fk) {
		return new Field(
				TextUtil.toCamelCase(fk.fkColumnName(), false),
				fk.fkColumnName(),
				TextUtil.toCamelCase(fk.fkTableName(), true));
	}
}

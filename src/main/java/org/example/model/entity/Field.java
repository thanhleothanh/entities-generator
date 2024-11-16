package org.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.text.CaseUtils;
import org.example.AbstractGeneratorContext;
import org.example.model.enums.DataType;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Field {
	private String name;
	private String columnName;
	private String javaClass;

	public static Field of(Column col, Constraint fk) {
		DataType dataType = DataType.of(AbstractGeneratorContext.connectionManager.getDriver(), col.dataType());
		return new Field().toBuilder()
				.name(CaseUtils.toCamelCase(col.columnName(), false, '_'))
				.columnName(col.columnName())
				.javaClass(dataType.getJavaClass())
				.build();
	}
}

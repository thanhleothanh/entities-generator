package org.example.model.entity;

import lombok.Getter;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;

@Getter
public class ReferencingField extends Field {

	private final Field toField;

	public ReferencingField(Field field, Field toField) {
		super(field.getName(), field.getColumnName(), field.getJavaClass());
		this.toField = toField;
	}

	public static ReferencingField of(Column col, Constraint fk) {
		return new ReferencingField(Field.of(col), Field.of(fk));
	}
}

package org.example.model.entity;

import lombok.Getter;
import org.example.model.schema.Constraint;

@Getter
public class ReferencingField extends Field {

	private final Field referencedField;

	public ReferencingField(Field referencingField, Field referencedField) {
		super(referencingField.getName(), referencingField.getTableName(), referencingField.getColumnName(), referencingField.getJavaClass());
		this.referencedField = referencedField;
	}

	public static ReferencingField of(Constraint fk) {
		return new ReferencingField(Field.ofReferencingField(fk), Field.ofReferencedField(fk));
	}
}

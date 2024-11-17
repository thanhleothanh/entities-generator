package org.example.model.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.example.AbstractGeneratorContext;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;

@Getter
@Setter
public class Entity {
	private final String packageName;
	private final String name;
	private final String tableName;
	private final List<String> imports = new ArrayList<>();
	private final List<Field> ids = new ArrayList<>();
	private final List<Field> fields = new ArrayList<>();
	private final List<ReferencingField> referencingFields = new ArrayList<>();

	public Entity(String packageName, String name, String tableName) {
		this.packageName = packageName;
		this.name = name;
		this.tableName = tableName;
	}

	public void addId(Column col) {
		this.ids.add(Field.of(col));
	}

	public void addField(Column col) {
		this.fields.add(Field.of(col));
	}

	public void addReferencedField(Column col, Constraint fk) {
		ReferencingField rf = ReferencingField.of(col, fk);
		this.referencingFields.add(rf);
		this.imports.add(String.format("%s.%s", AbstractGeneratorContext.packageName, rf.getToField().getJavaClass()));
	}
}

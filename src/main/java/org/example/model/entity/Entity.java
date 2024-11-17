package org.example.model.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.example.AbstractGeneratorContext;
import org.example.model.schema.Column;

@Getter
@Setter
public class Entity {
	private final String packageName;
	private final String name;
	private final String tableName;
	private final List<String> imports = new ArrayList<>();
	private final List<Field> ids = new ArrayList<>();
	private final List<Field> fields = new ArrayList<>();
	private final List<Relationship> relationships = new ArrayList<>();
	private final List<Relationship> compRelationships = new ArrayList<>();

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

	public void addRelationship(List<ReferencingField> fields) {
		if (CollectionUtils.isEmpty(fields)) {
			return;
		}
		this.imports.add(String.format("%s.%s", AbstractGeneratorContext.packageName, fields.get(0).getJavaClass()));
		Relationship relationship = new Relationship(fields);
		if (fields.size() > 1) this.compRelationships.add(relationship);
		else this.relationships.add(relationship);

	}
}

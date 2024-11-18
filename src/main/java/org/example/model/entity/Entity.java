package org.example.model.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.example.model.schema.Column;
import org.example.util.TextUtil;

@Getter
public class Entity {
	private final String packageName;
	private final String name;
	private final String tableName;
	private final List<Field> ids = new ArrayList<>();
	private final List<Field> fields = new ArrayList<>();
	private final List<Relationship> relationships = new ArrayList<>();

	/* keeping track of and making sure of unique variable names */
	private final Set<String> variableNames = new HashSet<>();

	public Entity(String packageName, String name, String tableName) {
		this.packageName = packageName;
		this.name = name;
		this.tableName = tableName;
	}

	public void addId(Column col) {
		Field id = Field.of(col);
		keepVariableNameUnique(id);
		ids.add(id);
	}

	public void addField(Column col) {
		Field field = Field.of(col);
		keepVariableNameUnique(field);
		fields.add(field);
	}

	public void addRelationship(List<ReferencingField> fields) {
		if (CollectionUtils.isEmpty(fields)) {
			return;
		}
		if (fields.size() > 1) {
			String javaClass = fields.get(0).getReferencedField().getJavaClass();
			String variableName = getUniqueVariableName(TextUtil.toCamelCase(javaClass, false));
			variableNames.add(variableName);
			relationships.add(new Relationship(fields, javaClass, variableName));
		} else {
			String javaClass = fields.get(0).getReferencedField().getJavaClass();
			String variableName = getUniqueVariableName(fields.get(0).getName());
			variableNames.add(variableName);
			relationships.add(new Relationship(fields, javaClass, variableName));
		}
	}

	private void keepVariableNameUnique(Field field) {
		if (variableNames.contains(field.getName())) {
			field.setName(field.getName() + "_");
			keepVariableNameUnique(field);
		}
		variableNames.add(field.getName());
	}

	private String getUniqueVariableName(String variableName) {
		if (variableNames.contains(variableName)) {
			return getUniqueVariableName(variableName + "_");
		}
		return variableName;
	}
}

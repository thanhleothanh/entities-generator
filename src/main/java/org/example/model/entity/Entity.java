package org.example.model.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Entity {
	private String name;
	private String tableName;
	private List<Field> ids;
	private List<Field> fields;

	public void addId(Column col, Constraint fk) {
		if (this.ids == null) {
			this.ids = new ArrayList<>();
		}
		this.ids.add(Field.of(col, fk));
	}

	public void addField(Column col, Constraint fk) {
		if (this.fields == null) {
			this.fields = new ArrayList<>();
		}
		this.fields.add(Field.of(col, fk));
	}
}

package org.example.service;

import java.util.List;
import java.util.Map;
import org.example.model.entity.Entity;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;

public interface EntityNormalizerService {

	default List<Entity> normalizeEntities(
			Map<String, List<Constraint>> primaryKeys,
			Map<String, List<Column>> columns,
			Map<String, List<Constraint>> foreignKeys,
			Map<String, List<Column>> viewColumns
	) {
		beforeNormalizeEntities(primaryKeys, columns, foreignKeys, viewColumns);
		List<Entity> entities = doNormalizeEntities(primaryKeys, columns, foreignKeys, viewColumns);
		afterNormalizeEntities(entities);
		return entities;
	}

	default void beforeNormalizeEntities(
			Map<String, List<Constraint>> primaryKeys,
			Map<String, List<Column>> columns,
			Map<String, List<Constraint>> foreignKeys,
			Map<String, List<Column>> viewColumns
	) {}

	List<Entity> doNormalizeEntities(
			Map<String, List<Constraint>> primaryKeys,
			Map<String, List<Column>> columns,
			Map<String, List<Constraint>> foreignKeys,
			Map<String, List<Column>> viewColumns
	);

	default void afterNormalizeEntities(List<Entity> entities) {}
}

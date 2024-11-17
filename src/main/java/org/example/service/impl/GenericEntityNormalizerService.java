package org.example.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.example.AbstractGeneratorContext;
import org.example.model.entity.Entity;
import org.example.model.entity.ReferencingField;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;
import org.example.service.EntityNormalizerService;
import org.example.util.TextUtil;

public class GenericEntityNormalizerService implements EntityNormalizerService {

	@Override
	public List<Entity> doNormalizeEntities(
			Map<String, List<Constraint>> primaryKeys, Map<String, List<Column>> columns,
			Map<String, List<Constraint>> foreignKeys, Map<String, List<Column>> viewColumns
	) {
		return Stream.concat(
						Stream.of(primaryKeys.keySet(), columns.keySet(), foreignKeys.keySet())
								.flatMap(Set::stream)
								.collect(Collectors.toSet())
								.parallelStream()
								.map(tableName -> buildTableEntity(
										tableName,
										primaryKeys.getOrDefault(tableName, Collections.emptyList()),
										columns.getOrDefault(tableName, Collections.emptyList()),
										foreignKeys.getOrDefault(tableName, Collections.emptyList())))
								.filter(Optional::isPresent)
								.map(Optional::get),
						viewColumns.entrySet().stream()
								.map(e -> buildViewEntity(e.getKey(), e.getValue()))
								.filter(Optional::isPresent)
								.map(Optional::get))
				.toList();
	}

	private Optional<Entity> buildTableEntity(
			String tableName, List<Constraint> primaryKeys,
			List<Column> columns, List<Constraint> foreignKeys
	) {
		if (CollectionUtils.isEmpty(columns)) {
			return Optional.empty();
		}

		Map<String, Constraint> mapPrimaryKeyByColumnName = primaryKeys.stream()
				.collect(Collectors.toMap(Constraint::columnName, Function.identity(), (l, r) -> l));
		Map<String, Constraint> mapForeignKeyByColumnName = foreignKeys.stream()
				.collect(Collectors.toMap(Constraint::columnName, Function.identity(), (l, r) -> l));
		Map<String, List<Constraint>> mapForeignKeyByConstraintName = foreignKeys.stream()
				.collect(Collectors.groupingBy(Constraint::constraintName));

		Entity entity = new Entity(
				AbstractGeneratorContext.packageName,
				TextUtil.toCamelCase(tableName, true),
				tableName);
		columns.stream()
				.filter(col -> mapPrimaryKeyByColumnName.containsKey(col.columnName()))
				.forEach(entity::addId);
		columns.stream()
				.filter(col -> !mapPrimaryKeyByColumnName.containsKey(col.columnName()) && !mapForeignKeyByColumnName.containsKey(col.columnName()))
				.forEach(entity::addField);
		mapForeignKeyByConstraintName.values().stream()
				.map(constraints -> constraints.stream().map(ReferencingField::of).toList())
				.toList()
				.forEach(entity::addRelationship);
		return Optional.of(entity);
	}

	private Optional<Entity> buildViewEntity(String tableName, List<Column> columns) {
		if (CollectionUtils.isEmpty(columns)) {
			return Optional.empty();
		}

		Entity entity = new Entity(
				AbstractGeneratorContext.packageName,
				TextUtil.toCamelCase(tableName, true),
				tableName);
		entity.addId(columns.get(0));
		for (int i = 1; i < columns.size(); i++) entity.addField(columns.get(i));
		return Optional.of(entity);
	}

}

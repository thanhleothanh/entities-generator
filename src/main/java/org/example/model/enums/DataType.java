package org.example.model.enums;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@Getter
public enum DataType {
	STRING("String"),
	LONG("Long"),
	INTEGER("Integer"),
	BIG_DECIMAL("BigDecimal"),
	DATE("Date"),
	TIMESTAMP("Timestamp"),
	BOOLEAN("Boolean"),
	BYTE_ARRAY("byte[]"),
	OBJECT("Object");

	private final String javaClass;

	public static final Map<DataType, List<String>> postgresqlMapper = Map.ofEntries(
			Map.entry(DataType.STRING, List.of("character varying", "text", "character", "json", "jsonb")),
			Map.entry(DataType.LONG, List.of("bigint", "bigserial")),
			Map.entry(DataType.INTEGER, List.of("integer", "serial", "smallserial", "smallint")),
			Map.entry(DataType.BIG_DECIMAL, List.of("numeric", "real", "double precision", "decimal")),
			Map.entry(DataType.DATE, List.of("date")),
			Map.entry(DataType.TIMESTAMP, List.of("timestamp without time zone", "timestamp with time zone")),
			Map.entry(DataType.BOOLEAN, List.of("boolean")),
			Map.entry(DataType.BYTE_ARRAY, List.of("bytea"))
	);

	public static DataType of(Driver driver, String dbDataType) {
		if (StringUtils.isBlank(dbDataType)) {
			throw new IllegalArgumentException("dbDataType must not be blank");
		}
		if (Objects.isNull(driver)) {
			throw new IllegalArgumentException("driver must not be null");
		}

		if (Driver.POSTGRESQL.equals(driver)) return find(dbDataType, postgresqlMapper);
		else throw new IllegalArgumentException(String.format("driver (%s) does not specify supported data types", driver));
	}

	private static DataType find(String dbDataType, Map<DataType, List<String>> mapper) {
		Map<String, DataType> reversedMapper = mapper.entrySet().stream()
				.flatMap(entry -> entry.getValue().stream() .map(value -> Map.entry(value, entry.getKey())))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return reversedMapper.getOrDefault(dbDataType, OBJECT);
	}

}

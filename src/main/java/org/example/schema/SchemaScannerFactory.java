package org.example.schema;

import java.util.Objects;

public class SchemaScannerFactory {
	private SchemaScannerFactory() {}

	public static AbstractSchemaScanner of(ConnectionManager connectionManager) {
		if (Objects.isNull(connectionManager)) {
			throw new IllegalArgumentException("connectionManager must not be null");
		}

		return switch (connectionManager.getDriver()) {
			case POSTGRESQL -> new PostgresSchemaScanner(connectionManager);
			default -> throw new IllegalArgumentException("connectionManager driver is not supported");
		};
	}
}

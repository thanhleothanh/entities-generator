package org.example.schema;

public abstract class AbstractSchemaScanner {
	protected final ConnectionManager connectionManager;

	protected AbstractSchemaScanner(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public abstract void scanTablePrimaryKeys();
	public abstract void scanTableColumns();
	public abstract void scanTableForeignKeys();
	public abstract void scanViews();

	protected void normalizePrimaryKeys() {

	}

	protected void normalizeColumns() {

	}

	protected void normalizeForeignKeys() {

	}

	protected void normalizeViews() {

	}
}

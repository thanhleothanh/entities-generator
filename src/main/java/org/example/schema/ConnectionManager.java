package org.example.schema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.Getter;
import org.example.model.enums.Driver;

@Getter
public final class ConnectionManager {
	private final String jdbcUrl;
	private final String jdbcUser;
	private final String jdbcPassword;
	private final Driver driver;

	public ConnectionManager(String jdbcUrl, String jdbcUser, String jdbcPassword) {
		this.driver = Driver.of(jdbcUrl);
		this.jdbcUrl = jdbcUrl;
		this.jdbcUser = jdbcUser;
		this.jdbcPassword = jdbcPassword;
	}

	public Connection get() throws SQLException {
		return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
	}
}



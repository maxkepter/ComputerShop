package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataSourceProvider {
	private static DataSource dataSource;

	static {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/computer-shop");
		} catch (NamingException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to initialize DataSource", e);
		}
	}

	private DataSourceProvider() {

	}

	public static DataSource getDataSource() {
		return dataSource;
	}

	public static void close(Connection connection, Statement statement, ResultSet resultSet) {
		try {
			if (connection != null) {
				connection.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException sQLException) {
			sQLException.printStackTrace();
		}

	}
}

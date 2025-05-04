package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import Model.Type;
import utils.DataSourceProvider;

public class TypeDao {
	private DataSource dataSource;

	public TypeDao(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	// pagination from data (1 page max 100)
	public List<Type> getListType(int numPage) {
		int from = numPage * 100 + 1;
		int to = (numPage + 1) * 100;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Type> typeList = new ArrayList<>();

		String sql = "SELECT * FROM (" + "  SELECT *, ROW_NUMBER() OVER (ORDER BY [TypeID]) AS rn "
				+ "  FROM [dbo].[Type]" + ") AS temp " + "WHERE rn BETWEEN ? AND ?";
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, Integer.toString(from));
			preparedStatement.setString(2, Integer.toString(to));
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("TypeID");
				String name = resultSet.getString("TypeName");
				typeList.add(new Type(id, name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);
		}
		return typeList;
	}

// get all type
	public List<Type> getType() {
		List<Type> typeList = new ArrayList<>();
		String sql = "SELECT TypeID, TypeName FROM Type";
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("TypeID");
				String name = resultSet.getString("TypeName");
				typeList.add(new Type(id, name));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);
		}		

		return typeList;
	}

	public int countType() {
		String sql = "select count(*) as numType from Type";
		int numType = 0;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				numType = resultSet.getInt("numType");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, statement, resultSet);
		}
		return numType;
	}

	public void createType(String typeName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO Type (TypeName) VALUES (?)";

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, typeName);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, null);
		}
	}

	public void deleteType(int id) {
		String sql = "DELETE FROM Type WHERE TypeID = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, null);
		}
	}

	public void updateType(int id, String newName) {
		String sql = "UPDATE Type SET TypeName = ? WHERE TypeID = ?";
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newName);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, null);
		}
	}
}

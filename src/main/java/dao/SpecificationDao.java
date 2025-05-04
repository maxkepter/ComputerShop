package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import Model.Specification;
import utils.DataSourceProvider;

public class SpecificationDao {
	private DataSource dataSource;

	public SpecificationDao(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

// pagination from data (1 page max 100)
	public List<Specification> getListSpecification(int numPage) {
		int from = numPage * 100 + 1;
		int to = (numPage + 1) * 100;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Specification> specList = new ArrayList<>();

		String sql = "SELECT * FROM (" + "  SELECT *, ROW_NUMBER() OVER (ORDER BY [SpecID]) AS rn "
				+ "  FROM [dbo].[Specifications]" + ") AS temp " + "WHERE rn BETWEEN ? AND ?";
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, Integer.toString(from));
			preparedStatement.setString(2, Integer.toString(to));
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("SpecID");
				String name = resultSet.getString("SpecName");
				specList.add(new Specification(id, name));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);
		}
		return specList;
	}

// get all spec
	public List<Specification> getSpecification() {
		List<Specification> specificationList = new ArrayList<>();
		String sql = "SELECT SpecID, SpecName FROM Specifications";

		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				int id = resultSet.getInt("SpecID");
				String name = resultSet.getString("SpecName");
				specificationList.add(new Specification(id, name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return specificationList;
	}

// count number spec in database
	public int countSpec() {
		String sql = "SELECT COUNT(*) AS numSpec FROM Specifications";
		int numSpec = 0;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				numSpec = resultSet.getInt("numSpec");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, statement, resultSet);
		}
		return numSpec;
	}

	public void createSpec(String specName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "insert into Specifications (SpecName) values (?)";

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, specName);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, null);
		}
	}

	public void deleteSpec(int id) {
		String sql = "DELETE FROM Specifications WHERE SpecID = ?";
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

	public void updateSpec(int id, String newName) {
		String sql = "UPDATE Specifications SET SpecName = ? WHERE SpecID = ?";
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

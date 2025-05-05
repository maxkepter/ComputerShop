package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Specification;

public class SpecificationDao {
	private Connection connection;

	public SpecificationDao(Connection connection) {
		this.connection = connection;
	}

	// Pagination: mỗi trang tối đa 100 specification
	public List<Specification> getListSpecification(int numPage) {
		int from = numPage * 100 + 1;
		int to = (numPage + 1) * 100;
		List<Specification> specList = new ArrayList<>();

		String sql = "SELECT * FROM (" +
				"  SELECT *, ROW_NUMBER() OVER (ORDER BY [SpecID]) AS rn " +
				"  FROM [dbo].[Specifications]" +
				") AS temp WHERE rn BETWEEN ? AND ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, from);
			preparedStatement.setInt(2, to);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					int id = resultSet.getInt("SpecID");
					String name = resultSet.getString("SpecName");
					specList.add(new Specification(id, name));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return specList;
	}

	// Lấy toàn bộ specification
	public List<Specification> getSpecification() {
		List<Specification> specificationList = new ArrayList<>();
		String sql = "SELECT SpecID, SpecName FROM Specifications";

		try (PreparedStatement statement = connection.prepareStatement(sql);
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

	// Đếm số specification
	public int countSpec() {
		String sql = "SELECT COUNT(*) AS numSpec FROM Specifications";
		int numSpec = 0;

		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(sql)) {

			if (resultSet.next()) {
				numSpec = resultSet.getInt("numSpec");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return numSpec;
	}

	// Thêm mới specification
	public void createSpec(String specName) {
		String sql = "INSERT INTO Specifications (SpecName) VALUES (?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, specName);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Xóa specification
	public void deleteSpec(int id) {
		String sql = "DELETE FROM Specifications WHERE SpecID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Cập nhật tên specification
	public void updateSpec(int id, String newName) {
		String sql = "UPDATE Specifications SET SpecName = ? WHERE SpecID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, newName);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

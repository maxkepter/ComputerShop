package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Type;

public class TypeDao {
	private Connection connection;

	public TypeDao(Connection connection) {
		this.connection = connection;
	}

	// Pagination (mỗi trang tối đa 100 bản ghi)
	public List<Type> getListType(int numPage) {
		int from = numPage * 100 + 1;
		int to = (numPage + 1) * 100;
		List<Type> typeList = new ArrayList<>();

		String sql = "SELECT * FROM (" +
				"  SELECT *, ROW_NUMBER() OVER (ORDER BY [TypeID]) AS rn " +
				"  FROM [dbo].[Type]" +
				") AS temp WHERE rn BETWEEN ? AND ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, from);
			preparedStatement.setInt(2, to);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					int id = resultSet.getInt("TypeID");
					String name = resultSet.getString("TypeName");
					typeList.add(new Type(id, name));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return typeList;
	}

	// Lấy toàn bộ loại sản phẩm
	public List<Type> getType() {
		List<Type> typeList = new ArrayList<>();
		String sql = "SELECT TypeID, TypeName FROM Type";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
			 ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				int id = resultSet.getInt("TypeID");
				String name = resultSet.getString("TypeName");
				typeList.add(new Type(id, name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return typeList;
	}

	// Đếm số lượng loại sản phẩm
	public int countType() {
		String sql = "SELECT COUNT(*) AS numType FROM Type";
		int numType = 0;

		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(sql)) {

			if (resultSet.next()) {
				numType = resultSet.getInt("numType");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numType;
	}

	// Tạo mới một loại
	public void createType(String typeName) {
		String sql = "INSERT INTO Type (TypeName) VALUES (?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, typeName);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Xoá loại theo ID
	public void deleteType(int id) {
		String sql = "DELETE FROM Type WHERE TypeID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Cập nhật tên loại
	public void updateType(int id, String newName) {
		String sql = "UPDATE Type SET TypeName = ? WHERE TypeID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, newName);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

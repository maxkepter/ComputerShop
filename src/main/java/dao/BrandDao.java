package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Brand;

public class BrandDao {
	private Connection connection;

	public BrandDao(Connection connection) {
		this.connection = connection;
	}

	// Pagination: mỗi trang lấy tối đa 100 brand
	public List<Brand> getListBrand(int numPage) {
		int from = numPage * 100 + 1;
		int to = (numPage + 1) * 100;
		List<Brand> brandList = new ArrayList<>();

		String sql = "SELECT * FROM (" +
					 "  SELECT *, ROW_NUMBER() OVER (ORDER BY [BrandID]) AS rn " +
					 "  FROM [dbo].[Brand]" +
					 ") AS temp WHERE rn BETWEEN ? AND ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, from);
			preparedStatement.setInt(2, to);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					int id = resultSet.getInt("BrandID");
					String name = resultSet.getString("BrandName");
					brandList.add(new Brand(id, name));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return brandList;
	}

	// Lấy toàn bộ brand
	public List<Brand> getBrand() {
		List<Brand> brandList = new ArrayList<>();
		String sql = "SELECT BrandID, BrandName FROM Brand";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
			 ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				int id = resultSet.getInt("BrandID");
				String name = resultSet.getString("BrandName");
				brandList.add(new Brand(id, name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return brandList;
	}

	// Lấy tên brand theo ID
	public String getBrandById(int brandId) {
		String sql = "SELECT BrandName FROM Brand WHERE BrandID=?";
		String brandName = "";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, brandId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					brandName = resultSet.getString("BrandName");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return brandName;
	}

	// Đếm số lượng brand trong database
	public int countBrand() {
		String sql = "SELECT COUNT(*) AS numBrand FROM Brand";
		int numBrand = 0;

		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(sql)) {

			if (resultSet.next()) {
				numBrand = resultSet.getInt("numBrand");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return numBrand;
	}

	// Thêm brand mới
	public void createBrand(String brandName) {
		String sql = "INSERT INTO Brand (BrandName) VALUES (?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, brandName);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Xóa brand theo ID
	public void deleteBrand(int id) {
		String sql = "DELETE FROM Brand WHERE BrandID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Cập nhật tên brand
	public void updateBrand(int id, String newName) {
		String sql = "UPDATE Brand SET BrandName = ? WHERE BrandID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, newName);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

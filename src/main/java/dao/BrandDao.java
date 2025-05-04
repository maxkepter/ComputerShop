// BrandDao.java
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import Model.Brand;
import utils.DataSourceProvider;

public class BrandDao {
	private DataSource dataSource;

	public BrandDao(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

//	pagination from data base(1 page max 100 brand)
	public List<Brand> getListBrand(int numPage) {
		int from = numPage * 100 + 1;
		int to = (numPage + 1) * 100;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Brand> brandList = new ArrayList<>();

		String sql = "SELECT * FROM (" + "  SELECT *, ROW_NUMBER() OVER (ORDER BY [BrandID]) AS rn "
				+ "  FROM [dbo].[Brand]" + ") AS temp " + "WHERE rn BETWEEN ? AND ?";
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, Integer.toString(from));
			preparedStatement.setString(2, Integer.toString(to));
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("BrandID");
				String name = resultSet.getString("BrandName");
				brandList.add(new Brand(id, name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);
		}
		return brandList;
	}

//get all brand
	public List<Brand> getBrand() {
		List<Brand> brandList = new ArrayList<>();
		String sql = "SELECT BrandID, BrandName FROM Brand";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("BrandID");
				String name = resultSet.getString("BrandName");
				brandList.add(new Brand(id, name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);
		}

		return brandList;
	}


	public String getBrandById(int brandId) {
		String sql = "SELECT BrandName FROM Brand where BrandID=?";
		String brandName="";
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			connection=dataSource.getConnection();
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setInt(1, brandId);
			resultSet=preparedStatement.executeQuery();
			if(resultSet.next()) {
				brandName=resultSet.getString("BrandName");
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);
		}
		return brandName;
		
	}
	
//count number of brand in database
	public int countBrand() {
		String sql = "select count(*) as numBrand from Brand";
		int numBrand = 0;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				numBrand = resultSet.getInt("numBrand");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, statement, resultSet);
		}
		return numBrand;
	}

	public void createBrand(String brandName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO Brand (BrandName) VALUES (?)";

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, brandName);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, null);
		}
	}

	public void deleteBrand(int id) {
		String sql = "DELETE FROM Brand WHERE BrandID = ?";
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

	public void updateBrand(int id, String newName) {
		String sql = "UPDATE Brand SET BrandName = ? WHERE BrandID = ?";
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

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import Model.Rating;
import utils.DataSourceProvider;

public class RatingDao {
	private DataSource dataSource;

	public RatingDao(DataSource dataSource) {
		super();
		this.setDataSource(dataSource);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// get rating of a product and paging
	// page 0-index
	// a Rating have userId,userName,rating,comment
	public List<Rating> getRating(int productId, int page, int ratingPerPage) {
	    List<Rating> ratingList = new ArrayList<>();
	    String sql = "SELECT * FROM ( " +
	                 "  SELECT r.UserID, u.UserName, r.Rate, r.Comment, r.CreatedAt, " +
	                 "         ROW_NUMBER() OVER (ORDER BY r.CreatedAt DESC) AS rn " +
	                 "  FROM Rating AS r " +
	                 "  JOIN [User] AS u ON r.UserID = u.UserID " +
	                 "  WHERE r.ProductID = ? " +
	                 ") AS temp " +
	                 "WHERE rn BETWEEN ? AND ?";

	    int from = (page - 1) * ratingPerPage + 1;
	    int to = page * ratingPerPage;

	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = dataSource.getConnection();
	        preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setInt(1, productId);
	        preparedStatement.setInt(2, from);
	        preparedStatement.setInt(3, to);

	        resultSet = preparedStatement.executeQuery();
	        while (resultSet.next()) {
	            int userId = resultSet.getInt("UserID");
	            String userName = resultSet.getString("UserName");
	            int rate = resultSet.getInt("Rate");
	            String comment = resultSet.getString("Comment");
	            Timestamp createdAt = resultSet.getTimestamp("CreatedAt");

	            Rating rating = new Rating(userId, userName, rate, comment, createdAt);
	            ratingList.add(rating);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DataSourceProvider.close(connection, preparedStatement, resultSet);
	    }

	    return ratingList;
	}


	public void createRating(int userId,int productId, double rate, String comment) {
		String sql="insert into Rating (UserID,ProductID,Rate,Comment) values (?,?,?,?)";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection=dataSource.getConnection();
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, productId);
			preparedStatement.setDouble(3, rate);
			preparedStatement.setString(4, comment);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DataSourceProvider.close(connection, preparedStatement, null);
		}
	}
	
	public void deleteRating(int userId, int productId) {
	    String sql = "DELETE FROM Rating WHERE UserID = ? AND ProductID = ?";
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    try {
	        connection = dataSource.getConnection();
	        preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setInt(1, userId);
	        preparedStatement.setInt(2, productId);
	        preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace(); 
	    } finally {
	        DataSourceProvider.close(connection, preparedStatement, null);
	    }
	}
	
	public static Rating getRatingByUserId(List<Rating> ratingList,int userId) {
		Rating rating=null;
		for (int i=0;i<ratingList.size();i++) {
			if(ratingList.get(i).getUserId()==userId) {
				rating=ratingList.get(i);
				ratingList.remove(i);
			}
		}
		return rating;
	}


}

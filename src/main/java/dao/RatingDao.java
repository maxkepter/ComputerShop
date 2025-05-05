package dao;

import Model.Rating;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RatingDao {
	private Connection connection;

	public RatingDao(Connection connection) {
		this.connection = connection;
	}

	// Get ratings of a product with pagination
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

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, productId);
			preparedStatement.setInt(2, from);
			preparedStatement.setInt(3, to);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					int userId = resultSet.getInt("UserID");
					String userName = resultSet.getString("UserName");
					int rate = resultSet.getInt("Rate");
					String comment = resultSet.getString("Comment");
					Timestamp createdAt = resultSet.getTimestamp("CreatedAt");

					Rating rating = new Rating(userId, userName, rate, comment, createdAt);
					ratingList.add(rating);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ratingList;
	}

	public void createRating(int userId, int productId, double rate, String comment) {
		String sql = "INSERT INTO Rating (UserID, ProductID, Rate, Comment) VALUES (?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, productId);
			preparedStatement.setDouble(3, rate);
			preparedStatement.setString(4, comment);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteRating(int userId, int productId) {
		String sql = "DELETE FROM Rating WHERE UserID = ? AND ProductID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, productId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Rating getRatingByUserId(List<Rating> ratingList, int userId) {
		Rating rating = null;
		for (int i = 0; i < ratingList.size(); i++) {
			if (ratingList.get(i).getUserId() == userId) {
				rating = ratingList.get(i);
				ratingList.remove(i);
				break;
			}
		}
		return rating;
	}
}

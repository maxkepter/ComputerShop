package dao;

import Model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDao {
	private Connection connection;

	public CartDao(Connection connection) {
		this.connection = connection;
	}

	// get all products in the cart for a user by userId
	public List<Product> getCartByUserId(int userId) {
		String sql = "SELECT c.ProductID, p.ProductName, p.ProductQuantity, p.Price " +
				"FROM CartItem AS c " +
				"JOIN Product AS p ON c.ProductID = p.ProductID " +
				"WHERE c.UserId = ?";
		List<Product> cartItems = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, userId);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					int productId = resultSet.getInt("ProductID");
					String productName = resultSet.getString("ProductName");
					int productQuantity = resultSet.getInt("ProductQuantity");
					double price = resultSet.getDouble("Price");

					Product product = new Product(productId, productName, productQuantity, price);
					cartItems.add(product);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cartItems;
	}

	// add a product to the cart if it does not already exist
	public void addToCart(int userId, int productId) {
		String sql = "IF NOT EXISTS (SELECT 1 FROM CartItem WHERE UserID = ? AND ProductID = ?) " +
				"BEGIN INSERT INTO CartItem (UserID, ProductID) VALUES (?, ?) END";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, userId);
			statement.setInt(2, productId);
			statement.setInt(3, userId);
			statement.setInt(4, productId);
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// remove a product from the cart
	public void removeFromCart(int userId, int productId) {
		String sql = "DELETE FROM CartItem WHERE UserID = ? AND ProductID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, userId);
			statement.setInt(2, productId);
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

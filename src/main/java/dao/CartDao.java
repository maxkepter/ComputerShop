package dao;

import Model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class CartDao {
	DataSource dataSource;

	public CartDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// get all products in the cart for a user by userId
	public List<Product> getCartByUserId(int userId) {
		String sql = "select c.ProductID, p.ProductName,p.ProductQuantity,p.Price from CartItem as c join Product as p on c.ProductID=p.ProductID where c.UserId=?";
		List<Product> cartItems = new ArrayList<>();
		try (Connection connection = dataSource.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, userId);
				statement.executeQuery();
				try (ResultSet resultSet = statement.getResultSet()) {
					while (resultSet.next()) {
						int productId = resultSet.getInt("ProductID");
						String productName = resultSet.getString("ProductName");
						int productQuantity = resultSet.getInt("ProductQuantity");
						double price = resultSet.getDouble("Price");

						Product product = new Product(productId, productName, productQuantity, price);
						cartItems.add(product);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cartItems;
	}

	// add a product to the cart if it does not already exist
	// if it exists, do nothing
	public void addToCart(int userId, int productId) {
		String sql = "IF NOT EXISTS (SELECT 1 FROM CartItem WHERE UserID = ? AND ProductID = ?) "
				+ "BEGIN INSERT INTO CartItem (UserID, ProductID) VALUES (?, ?) END";
		try (Connection connection = dataSource.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, userId);
				statement.setInt(2, productId);
				statement.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

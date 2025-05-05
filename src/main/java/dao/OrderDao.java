package dao;

import Model.Order;
import Model.OrderDetail;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderDao {
	private Connection connection;

	public OrderDao(Connection connection) {
		this.connection = connection;
	}

	// product list map<productId, quantity>
	public void createOrder(int userId, List<Map<Integer, Integer>> productList) {
		String insertOrderSql = "INSERT INTO [Order] (OrderDate, Status, UserID) VALUES (?, ?, ?)";
		String insertDetailSql = "INSERT INTO OrderDetail (OrderID, ProductID, OrderQuantity) VALUES (?, ?, ?)";
		String updateProductSql = "UPDATE Product SET ProductQuantity = ProductQuantity - ? WHERE ProductID = ?";
		String checkStockSql = "SELECT ProductQuantity FROM Product WHERE ProductID = ?";

		try {
			connection.setAutoCommit(false);

			// 1. Create order
			int orderId;
			try (PreparedStatement stmt = connection.prepareStatement(insertOrderSql,
					Statement.RETURN_GENERATED_KEYS)) {
				stmt.setDate(1, new Date(System.currentTimeMillis()));
				stmt.setInt(2, 1); // Status mặc định
				stmt.setInt(3, userId);
				stmt.executeUpdate();

				try (ResultSet rs = stmt.getGeneratedKeys()) {
					if (rs.next()) {
						orderId = rs.getInt(1);
					} else {
						connection.rollback();
						throw new SQLException("Không lấy được OrderID vừa tạo.");
					}
				}
			}

			// 2. Check stock
			try (PreparedStatement checkStmt = connection.prepareStatement(checkStockSql)) {
				for (Map<Integer, Integer> item : productList) {
					for (Map.Entry<Integer, Integer> entry : item.entrySet()) {
						int productId = entry.getKey();
						int quantity = entry.getValue();

						checkStmt.setInt(1, productId);
						try (ResultSet rs = checkStmt.executeQuery()) {
							if (rs.next()) {
								int stock = rs.getInt("ProductQuantity");
								if (stock < quantity) {
									connection.rollback();
									throw new SQLException(
											"ProductId " + productId + " không đủ hàng. Còn lại: " + stock);
								}
							} else {
								connection.rollback();
								throw new SQLException("Không tìm thấy ProductId: " + productId);
							}
						}
					}
				}
			}

			// 3. Insert order detail & update product quantity
			try (PreparedStatement stmt = connection.prepareStatement(insertDetailSql);
					PreparedStatement updateStmt = connection.prepareStatement(updateProductSql)) {
				for (Map<Integer, Integer> item : productList) {
					for (Map.Entry<Integer, Integer> entry : item.entrySet()) {
						int productId = entry.getKey();
						int quantity = entry.getValue();

						// Thêm vào order detail
						stmt.setInt(1, orderId);
						stmt.setInt(2, productId);
						stmt.setInt(3, quantity);
						stmt.addBatch();

						// Cập nhật tồn kho
						updateStmt.setInt(1, quantity);
						updateStmt.setInt(2, productId);
						updateStmt.addBatch();
					}
				}
				stmt.executeBatch();
				updateStmt.executeBatch();
			}

			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public List<Order> getOrder(int userId, int numPage, int orderPerPage) {
		Map<Integer, Order> orderMap = new LinkedHashMap<>();
		String sql = "SELECT o.OrderID, o.OrderDate, o.Status, o.UserID, " +
				"od.ProductID, od.OrderQuantity, (od.OrderQuantity * p.Price) AS Amount " +
				"FROM [Order] o " +
				"JOIN OrderDetail od ON o.OrderID = od.OrderID " +
				"JOIN Product p ON od.ProductID = p.ProductID " +
				"WHERE o.UserID = ? " +
				"ORDER BY o.OrderDate DESC, o.OrderID";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, userId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					int orderId = resultSet.getInt("OrderID");
					Order order = orderMap.get(orderId);
					if (order == null) {
						Date orderDate = resultSet.getDate("OrderDate");
						int status = resultSet.getInt("Status");
						order = new Order(orderId, orderDate, status, userId, new ArrayList<>());
						orderMap.put(orderId, order);
					}

					int productId = resultSet.getInt("ProductID");
					int quantity = resultSet.getInt("OrderQuantity");
					double amount = resultSet.getDouble("Amount");

					order.getOrderDetails().add(new OrderDetail(productId, quantity, amount));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>(orderMap.values());
	}

	public boolean hasUserPurchasedProduct(int userId, int productId) {
		String sql = "SELECT COUNT(*) AS count " +
				"FROM OrderDetail AS od " +
				"JOIN [Order] AS o ON od.OrderID = o.OrderID " +
				"WHERE od.ProductID = ? AND o.UserID = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, productId);
			stmt.setInt(2, userId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt("count");
					return count > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}

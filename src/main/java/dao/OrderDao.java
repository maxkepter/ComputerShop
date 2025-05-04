package dao;

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

import javax.sql.DataSource;

import Model.Order;
import Model.OrderDetail;
import utils.DataSourceProvider;

public class OrderDao {
	private DataSource dataSource;

	public OrderDao(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	// product list map<productId,quantity>
	public void createOrder(int userId, List<Map<Integer, Integer>> productList) {
		String insertOrderSql = "INSERT INTO [Order] (OrderDate, Status, UserID) VALUES (?, ?, ?)";
		String insertDetailSql = "INSERT INTO OrderDetail (OrderID, ProductID, OrderQuantity) VALUES (?, ?, ?)";
		String updateProductSql = "UPDATE Product SET ProductQuantity = ProductQuantity - ? WHERE ProductID = ?";
		String checkStockSql = "SELECT ProductQuantity FROM Product WHERE ProductID = ?";

		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);

			// 1. Create order
			int orderId;
			try (PreparedStatement stmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
				stmt.setDate(1, new Date(System.currentTimeMillis())); // Current day
				stmt.setInt(2, 1);
				stmt.setInt(3, userId);
				stmt.executeUpdate();

				try (ResultSet rs = stmt.getGeneratedKeys()) {
					if (rs.next()) {
						orderId = rs.getInt(1);
					} else {
						conn.rollback();
						throw new SQLException("Không lấy được OrderID vừa tạo.");
					}
				}
			}
			
			//2. check stock
		    try (PreparedStatement checkStmt = conn.prepareStatement(checkStockSql)) {
	            for (Map<Integer, Integer> item : productList) {
	                for (Map.Entry<Integer, Integer> entry : item.entrySet()) {
	                    int productId = entry.getKey();
	                    int quantity = entry.getValue();

	                    checkStmt.setInt(1, productId);
	                    try (ResultSet rs = checkStmt.executeQuery()) {
	                        if (rs.next()) {
	                            int stock = rs.getInt("ProductQuantity");
	                            if (stock < quantity) {
	                                conn.rollback();
	                                throw new SQLException("ProudctId " + productId + " out of stock. Remain: " + stock);
	                            }
	                        } else {
	                            conn.rollback();
	                            throw new SQLException("ProductId not found: " + productId);
	                        }
	                    }
	                }
	            }
	        }

			// 3. insert each order detail - update quantity
			try (PreparedStatement stmt = conn.prepareStatement(insertDetailSql);
					PreparedStatement updateStmt = conn.prepareStatement(updateProductSql)) {
				for (Map<Integer, Integer> item : productList) {
					for (Map.Entry<Integer, Integer> entry : item.entrySet()) {
						int productId = entry.getKey();
						int quantity = entry.getValue();

						//insert into order detail
						stmt.setInt(1, orderId);
						stmt.setInt(2, productId);
						stmt.setInt(3, quantity);
						stmt.addBatch();
						
						//update product quantity
						updateStmt.setInt(1, quantity);
						updateStmt.setInt(2, productId);
						updateStmt.addBatch();
					}
				}
				stmt.executeBatch();
				updateStmt.executeBatch();
			}

			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Order> getOrder(int userId, int numPage, int orderPerPage) {
		Map<Integer, Order> orderMap = new LinkedHashMap<>();
		String sql = "SELECT o.OrderID, o.OrderDate, o.Status, o.UserID, "
				+ "od.ProductID, od.OrderQuantity, (od.OrderQuantity * p.Price) AS Amount " + "FROM Orders o "
				+ "JOIN OrderDetail od ON o.OrderID = od.OrderID " + "JOIN Product p ON od.ProductID = p.ProductID "
				+ "WHERE o.UserID = ? " + "ORDER BY o.OrderDate DESC, o.OrderID";

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int orderId = resultSet.getInt("OrderID");
				Order order = orderMap.get(orderId);
				if (order == null) {
					Date orderDate = resultSet.getDate("OrderDate");
					int status = resultSet.getInt("Status");
					int uid = resultSet.getInt("UserID");
					order = new Order(orderId, orderDate, status, userId, new ArrayList<OrderDetail>());
					orderMap.put(orderId, order);

				}

				int productId = resultSet.getInt("ProductID");
				int quantity = resultSet.getInt("OrderQuantity");
				double amount = resultSet.getDouble("Amount");

				order.getOrderDetails().add(new OrderDetail(productId, quantity, amount));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);
		}
		return new ArrayList<>(orderMap.values());
	}
	
	public boolean hasUserPurchasedProduct(int userId, int productId) {
	    String sql = "SELECT COUNT(*) AS count " +
	                 "FROM OrderDetail AS od " +
	                 "JOIN Orders AS o ON od.OrderID = o.OrderID " +
	                 "WHERE od.ProductID = ? AND o.UserID = ?";

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

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

package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Product;
import Model.SpecProduct;

public class ProductDao {
	private final Connection connection;

	public ProductDao(Connection connection) {
		this.connection = connection;
	}

	public int countProduct() {
		String sql = "SELECT COUNT(*) AS countProduct FROM Product";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {
			if (resultSet.next()) {
				return resultSet.getInt("countProduct");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Product> getProduct() {
		List<Product> list = new ArrayList<>();
		String sql = "SELECT ProductID, ProductName, ProductQuantity, Description, Price, BrandID FROM Product";

		try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				int productId = rs.getInt("ProductID");
				String name = rs.getString("ProductName");
				int quantity = rs.getInt("ProductQuantity");
				String description = rs.getString("Description");
				double price = rs.getDouble("Price");
				int brandId = rs.getInt("BrandID");

				List<String> productType = getProductType(productId);
				List<SpecProduct> specs = getSpecProduct(productId);
				List<String> images = getImageProduct(productId);
				double avgRate = getAvgRate(productId);
				int sold = getSold(productId);

				list.add(new Product(productId, name, quantity, description, price, brandId, avgRate, sold, productType,
						specs, images));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Product getProductById(int productId) {
		String sql = "SELECT ProductID, ProductName, ProductQuantity, Description, Price, BrandID FROM Product WHERE ProductID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, productId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String name = rs.getString("ProductName");
					int quantity = rs.getInt("ProductQuantity");
					String description = rs.getString("Description");
					double price = rs.getDouble("Price");
					int brandId = rs.getInt("BrandID");

					List<String> productType = getProductType(productId);
					List<SpecProduct> specs = getSpecProduct(productId);
					List<String> images = getImageProduct(productId);
					double avgRate = getAvgRate(productId);
					int sold = getSold(productId);

					return new Product(productId, name, quantity, description, price, brandId, avgRate, sold,
							productType, specs, images);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Product> getProductByPage(int page, int pageSize) {
		List<Product> list = new ArrayList<>();
		int from = page * pageSize + 1;
		int to = (page + 1) * pageSize;

		String sql = """
				    SELECT * FROM (
				        SELECT ProductID, ProductName, ProductQuantity, Description, Price, BrandID,
				               ROW_NUMBER() OVER (ORDER BY ProductID) AS rn
				        FROM Product
				    ) AS temp
				    WHERE rn BETWEEN ? AND ?
				""";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, from);
			ps.setInt(2, to);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("ProductID");
					String name = rs.getString("ProductName");
					int qty = rs.getInt("ProductQuantity");
					String desc = rs.getString("Description");
					double price = rs.getDouble("Price");
					int brandId = rs.getInt("BrandID");

					List<String> productType = getProductType(id);
					List<SpecProduct> specs = getSpecProduct(id);
					List<String> images = getImageProduct(id);
					double avgRate = getAvgRate(id);
					int sold = getSold(id);

					list.add(new Product(id, name, qty, desc, price, brandId, avgRate, sold, productType, specs,
							images));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void createProduct(Product product, List<Integer> typeIds) {
		String sql = "INSERT INTO Product (ProductName, ProductQuantity, Description, Price, BrandID) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			connection.setAutoCommit(false);

			ps.setString(1, product.getProductName());
			ps.setInt(2, product.getProductQuantity());
			ps.setString(3, product.getDescription());
			ps.setDouble(4, product.getPrice());
			ps.setInt(5, product.getBrandID());
			ps.executeUpdate();

			int productId;
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					productId = keys.getInt(1);
				} else {
					throw new SQLException("Failed to retrieve product ID.");
				}
			}

			if (typeIds != null) {
				for (int typeId : typeIds) {
					createProductType(productId, typeId);
				}
			}

			if (product.getSpecProducts() != null) {
				for (SpecProduct spec : product.getSpecProducts()) {
					createProductSpec(productId, spec);
				}
			}

			if (product.getImageProduct() != null) {
				for (String url : product.getImageProduct()) {
					createProductImage(productId, url);
				}
			}

			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
		}
	}

	public void createProductType(int productId, int typeId) throws SQLException {
		String sql = "INSERT INTO ProductType (ProductID, TypeID) VALUES (?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, productId);
			ps.setInt(2, typeId);
			ps.executeUpdate();
		}
	}

	public void createProductSpec(int productId, SpecProduct spec) throws SQLException {
		String sql = "INSERT INTO Has (SpecProduct, SpecProductValue, SpecID, ProductID) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, spec.getSpecProduct() == null ? "" : spec.getSpecProduct());
			ps.setDouble(2, spec.getSpecValue() == null ? 0 : spec.getSpecValue());
			ps.setInt(3, spec.getSpecId());
			ps.setInt(4, productId);
			ps.executeUpdate();
		}
	}

	public void createProductImage(int productId, String url) throws SQLException {
		String sql = "INSERT INTO Image (URL, ProductID) VALUES (?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, url);
			ps.setInt(2, productId);
			ps.executeUpdate();
		}
	}

	public void deleteProduct(int productId) {
		String sql = "DELETE FROM Product WHERE ProductID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, productId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<String> getProductType(int productId) {
		List<String> list = new ArrayList<>();
		String sql = "SELECT Type.TypeName FROM Type JOIN ProductType ON Type.TypeID = ProductType.TypeID WHERE ProductID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, productId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(rs.getString("TypeName"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<String> getImageProduct(int productId) {
		List<String> list = new ArrayList<>();
		String sql = "SELECT URL FROM Image WHERE ProductID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, productId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(rs.getString("URL"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<SpecProduct> getSpecProduct(int productId) {
		List<SpecProduct> list = new ArrayList<>();
		String sql = """
				    SELECT h.SpecProduct, h.SpecProductValue, s.SpecName, h.SpecID
				    FROM Has h
				    JOIN Specifications s ON h.SpecID = s.SpecID
				    WHERE h.ProductID = ?
				""";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, productId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String name = rs.getString("SpecName");
					String spec = rs.getString("SpecProduct");
					double value = rs.getDouble("SpecProductValue");
					list.add(new SpecProduct(name, spec, value));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public double getAvgRate(int productId) {
		String sql = "SELECT AVG(CAST(Rate AS FLOAT)) AS AvgRate FROM Rating WHERE ProductID = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, productId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getDouble("AvgRate");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getSold(int productId) {
		String sql = """
				    SELECT COALESCE(SUM(od.OrderQuantity), 0) AS TotalSold
				    FROM OrderDetail od
				    JOIN [Order] o ON od.OrderID = o.OrderID
				    WHERE od.ProductID = ? AND o.Status = 1
				""";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, productId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("TotalSold");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import Model.Product;
import Model.SpecProduct;
import utils.DataSourceProvider;

public class ProductDao {
	private DataSource dataSource;

	public ProductDao(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public int countProduct() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sql = "SELECT COUNT(*) AS countProduct FROM Product";
		int numProduct = 0;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				numProduct = resultSet.getInt("countProduct");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);
		}

		return numProduct;
	}

	// lấy toàn bộ thông tin của đối tượng project
	public List<Product> getProduct() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Product> list = new ArrayList<>();
		String sql = "SELECT [ProductID],[ProductName],[ProductQuantity],[Description],[Price],[BrandID] FROM [dbo].[Product]";

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int productId = resultSet.getInt("ProductID");
				String productName = resultSet.getString("ProductName");
				int productQuantity = resultSet.getInt("ProductQuantity");
				String description = resultSet.getString("Description");
				double price = resultSet.getDouble("Price");
				int brandId = resultSet.getInt("BrandID");

				// Lấy các thông tin liên quan đến Product
				List<String> productType = getProductType(connection, productId);
				List<SpecProduct> specProducts = getSpecProduct(connection, productId);
				List<String> imageProduct = getImageProduct(connection, productId);
				double avgRate = getAvgRate(connection, productId);
				int quantitySold = getSold(connection, productId);

				Product newProduct = new Product(productId, productName, productQuantity, description, price, brandId,
						avgRate, quantitySold, productType, specProducts, imageProduct);
				list.add(newProduct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);
		}
		return list;
	}
	
	public Product getProductById(int productId) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Product product = null;

	    String sql = "SELECT [ProductID], [ProductName], [ProductQuantity], [Description], [Price], [BrandID] "
	               + "FROM [dbo].[Product] WHERE [ProductID] = ?";

	    try {
	        connection = dataSource.getConnection();
	        preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setInt(1, productId);
	        resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            String productName = resultSet.getString("ProductName");
	            int productQuantity = resultSet.getInt("ProductQuantity");
	            String description = resultSet.getString("Description");
	            double price = resultSet.getDouble("Price");
	            int brandId = resultSet.getInt("BrandID");

	            // Lấy các thông tin liên quan đến Product
	            List<String> productType = getProductType(connection, productId);
	            List<SpecProduct> specProducts = getSpecProduct(connection, productId);
	            List<String> imageProduct = getImageProduct(connection, productId);
	            double avgRate = getAvgRate(connection, productId);
	            int quantitySold = getSold(connection, productId);

	            product = new Product(productId, productName, productQuantity, description, price, brandId,
	                    avgRate, quantitySold, productType, specProducts, imageProduct);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DataSourceProvider.close(connection, preparedStatement, resultSet);
	    }

	    return product;
	}


	public List<Product> getProductByPage(int numPage, int productPerPage) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Product> list = new ArrayList<>();
		String sql = "SELECT * FROM ("
				+ "  SELECT [ProductID], [ProductName], [ProductQuantity], [Description], [Price], [BrandID], "
				+ "         ROW_NUMBER() OVER (ORDER BY [ProductID]) AS rn " + "  FROM [dbo].[Product]" + ") AS temp "
				+ "WHERE rn BETWEEN ? AND ?";
		int from = numPage * productPerPage + 1;
		int to = (numPage + 1) * productPerPage;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, from);
			preparedStatement.setInt(2, to);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int productId = resultSet.getInt("ProductID");
				String productName = resultSet.getString("ProductName");
				int productQuantity = resultSet.getInt("ProductQuantity");
				String description = resultSet.getString("Description");
				double price = resultSet.getDouble("Price");
				int brandId = resultSet.getInt("BrandID");

				// Lấy các thông tin liên quan đến Product
				List<String> productType = getProductType(connection, productId);
				List<SpecProduct> specProducts = getSpecProduct(connection, productId);
				List<String> imageProduct = getImageProduct(connection, productId);
				double avgRate = getAvgRate(connection, productId);
				int quantitySold = getSold(connection, productId);

				Product product = new Product(productId, productName, productQuantity, description, price, brandId,
						avgRate, quantitySold, productType, specProducts, imageProduct);
				list.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);
		}

		return list;
	}

	public List<String> getProductType(Connection connection, int productId) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> typeList = new ArrayList<>();
		String sql = "SELECT Type.TypeName FROM Type JOIN ProductType ON Type.TypeID = ProductType.TypeID WHERE ProductID = ?";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, productId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				typeList.add(resultSet.getString("TypeName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(null, preparedStatement, resultSet);
		}
		return typeList;
	}

	public List<String> getImageProduct(Connection connection, int productId) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<String> imageList = new ArrayList<>();
		String sql = "SELECT URL FROM Image WHERE ProductID = ?";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, productId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				imageList.add(resultSet.getString("URL"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(null, preparedStatement, resultSet);
		}
		return imageList;
	}

	public List<SpecProduct> getSpecProduct(Connection connection, int productId) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<SpecProduct> specProductList = new ArrayList<>();
		String sql = """
				    SELECT h.SpecProduct, h.SpecProductValue, s.SpecName
				    FROM Has h
				    JOIN Specifications s ON h.SpecID = s.SpecID
				    WHERE h.ProductID = ?
				""";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, productId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String specName = resultSet.getString("SpecName");
				String specProduct = resultSet.getString("SpecProduct");
				double specValue = resultSet.getDouble("SpecProductValue");

				SpecProduct spec = new SpecProduct(specName, specProduct, specValue);
				specProductList.add(spec);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(null, preparedStatement, resultSet);
		}

		return specProductList;
	}

	public double getAvgRate(Connection connection, int productId) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		double avgRate = 0;
		String sql = "SELECT AVG(CAST(Rate AS FLOAT)) AS AvgRate FROM Rating WHERE ProductID = ?";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, productId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				avgRate = resultSet.getDouble("AvgRate");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(null, preparedStatement, resultSet);
		}

		return avgRate;
	}

	public int getSold(Connection connection, int productId) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int quantitySold = 0;

		String sql = """
				    SELECT COALESCE(SUM(od.OrderQuantity), 0) AS TotalSold
				    FROM OrderDetail od
				    JOIN [Order] o ON od.OrderID = o.OrderID
				    WHERE od.ProductID = ? AND o.Status = 1
				""";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, productId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				quantitySold = resultSet.getInt("TotalSold"); // Sẽ luôn trả về số, không phải null
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(null, preparedStatement, resultSet);
		}

		return quantitySold;
	}

	public void createProduct(Product product, List<Integer> typeIds) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		String sql = "INSERT INTO Product (ProductName, ProductQuantity, Description, Price, BrandID) VALUES (?, ?, ?, ?, ?)";

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false); // Bắt đầu transaction

			// 1. Thêm vào bảng Product
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, product.getProductName());
			preparedStatement.setInt(2, product.getProductQuantity());
			preparedStatement.setString(3, product.getDescription());
			preparedStatement.setDouble(4, product.getPrice());
			preparedStatement.setInt(5, product.getBrandID());
			preparedStatement.executeUpdate();

			// 2. Lấy ProductID vừa được sinh ra
			generatedKeys = preparedStatement.getGeneratedKeys();
			int productId = -1;
			if (generatedKeys.next()) {
				productId = generatedKeys.getInt(1);
			} else {
				throw new SQLException("Không thể lấy ProductID sau khi insert.");
			}

			// 3. Gọi các hàm thêm phụ (truyền connection vào các hàm phụ)
			if (typeIds != null) {
				for (int typeId : typeIds) {
					createProductType(connection, productId, typeId);
				}
			}

			if (product.getSpecProducts() != null) {
				for (SpecProduct specProduct : product.getSpecProducts()) {
					createProductSpec(connection, productId, specProduct);
				}
			}

			if (product.getImageProduct() != null) {
				for (String url : product.getImageProduct()) {
					createProductImage(connection, productId, url);
				}
			}

			connection.commit(); // Commit transaction
		} catch (SQLException e) {
			e.printStackTrace();
			if (connection != null) {
				try {
					connection.rollback(); // Rollback nếu có lỗi
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		} finally {
			DataSourceProvider.close(connection, preparedStatement, generatedKeys);
		}
	}

	public void createProductType(Connection connection, int productId, int typeId) {
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO ProductType (ProductID, TypeID) VALUES (?, ?)";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, productId);
			preparedStatement.setInt(2, typeId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(null, preparedStatement, null);
		}
	}

	public void createProductSpec(Connection connection, int productId, SpecProduct specProduct) {
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO Has (SpecProduct, SpecProductValue, SpecID, ProductID) VALUES (?, ?, ?, ?)";
		if(specProduct.getSpecProduct()==null) {
			specProduct.setSpecProduct("");
		}
		if(specProduct.getSpecValue()==null) {
			specProduct.setSpecValue((double) 0);
		}
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, specProduct.getSpecProduct());
			preparedStatement.setDouble(2, specProduct.getSpecValue());
			preparedStatement.setInt(3, specProduct.getSpecId());
			preparedStatement.setInt(4, productId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(null, preparedStatement, null);
		}
	}

	public void createProductImage(Connection connection, int productId, String url) {
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO Image (URL, ProductID) VALUES (?, ?)";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, url);
			preparedStatement.setInt(2, productId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(null, preparedStatement, null);
		}
	}

	public void deleteProduct(int productId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "DELETE FROM Product WHERE ProductID = ?";

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, productId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, null);
		}
	}

}

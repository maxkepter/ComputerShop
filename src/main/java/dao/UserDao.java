package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import Model.User;
import utils.DataSourceProvider;

public class UserDao {
	private DataSource dataSource;

	public UserDao(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public List<User> getUserList() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String sql = "SELECT * FROM [dbo].[User]";
		List<User> list = new ArrayList<>();

		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				int userID = resultSet.getInt("UserID");
				int userRole = resultSet.getInt("UserRole");
				String userName = resultSet.getString("UserName");
				String email = resultSet.getString("Email");
				String phoneNumber = resultSet.getString("PhoneNumber");
				String address = resultSet.getString("Address");
				String firstName = resultSet.getString("FirstName");
				String lastName = resultSet.getString("LastName");

				User user = new User(userID, userRole, userName, email, phoneNumber, address, firstName, lastName);
				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, statement, resultSet);
		}

		return list;
	}

	public User getUser(String userName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User user = null;
		String sql = "SELECT * FROM [dbo].[User] where UserName= ?";
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userName);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int userID = resultSet.getInt("UserID");
				int userRole = resultSet.getInt("UserRole");
				String email = resultSet.getString("Email");
				String phoneNumber = resultSet.getString("PhoneNumber");
				String address = resultSet.getString("Address");
				String firstName = resultSet.getString("FirstName");
				String lastName = resultSet.getString("LastName");
				user = new User(userID, userRole, userName, email, phoneNumber, address, firstName, lastName);
			}
		} catch (Exception e) {

		} finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);
		}
		return user;
	}

	public int checkLoginUser(String userName, String password) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sql = "SELECT Password FROM [dbo].[User] WHERE UserName = ?";

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userName);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				String storedPassword = resultSet.getString("Password");

				if (storedPassword.equals(password)) {
					return 0; // username tồn tại và mật khẩu đúng
				} else {
					return 1; // username tồn tại nhưng mật khẩu sai
				}
			} else {
				return 2; // Không tìm thấy username
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, resultSet);

		}

		return 2;
	}

	public void createUser(User user, String password) {
		String sql = "INSERT INTO [User] (UserRole, UserName, Email, PhoneNumber, Address, Password, FirstName, LastName) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, user.getUserRole());
			preparedStatement.setString(2, user.getUserName());
			preparedStatement.setString(3, user.getEmail());
			preparedStatement.setString(4, user.getPhoneNumber());
			preparedStatement.setString(5, user.getAddress());
			preparedStatement.setString(6, password);
			preparedStatement.setString(7, user.getFirstName());
			preparedStatement.setString(8, user.getLastName());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(connection, preparedStatement, null);
		}

	}

	public boolean checkDuplicateUserName(String userName) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isDuplicate = false;

		try {
			conn = dataSource.getConnection();

			String sql = "SELECT 1 FROM [User] WHERE UserName = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				isDuplicate = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceProvider.close(conn, pstmt, rs);
		}

		return isDuplicate;
	}
	
	public void updateUser() {
		
	}

}

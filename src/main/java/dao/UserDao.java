package dao;

import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
	private Connection connection;

	public UserDao(Connection connection) {
		this.connection = connection;
	}

	public List<User> getUserList() {
		String sql = "SELECT * FROM [dbo].[User]";
		List<User> list = new ArrayList<>();

		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {

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
		}

		return list;
	}

	public User getUser(String userName) {
		String sql = "SELECT * FROM [dbo].[User] WHERE UserName = ?";
		User user = null;

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, userName);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	public int checkLoginUser(String userName, String password) {
		String sql = "SELECT Password FROM [dbo].[User] WHERE UserName = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, userName);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					String storedPassword = resultSet.getString("Password");
					if (storedPassword.equals(password)) {
						return 0; // đúng
					} else {
						return 1; // sai mật khẩu
					}
				} else {
					return 2; // không có username
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 2;
	}

	public void createUser(User user, String password) {
		String sql = "INSERT INTO [User] (UserRole, UserName, Email, PhoneNumber, Address, Password, FirstName, LastName) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
		}
	}

	public boolean checkDuplicateUserName(String userName) {
		String sql = "SELECT 1 FROM [User] WHERE UserName = ?";
		boolean isDuplicate = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, userName);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					isDuplicate = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isDuplicate;
	}

	public void updateUser(int userId, String firstName, String lastName, String email, String phoneNumber,
			String address) {
		String sql = "UPDATE [User] SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Address = ? WHERE UserID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.setString(3, email);
			statement.setString(4, phoneNumber);
			statement.setString(5, address);
			statement.setInt(6, userId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updatePassword(int userId, String password) {
		String sql = "UPDATE [User] SET Password = ? WHERE UserID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, password);
			statement.setInt(2, userId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import Model.User;
import dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DBConnection
 */
@WebServlet("/DBConnection")
public class DBConnection extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource dataSource = DataSourceProvider.getDataSource();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DBConnection() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String message = "";
		List<User> list = null;
		try {
			Connection connection = dataSource.getConnection();
			if (connection != null) {
				UserDao userDao = new UserDao(connection);

				list = userDao.getUserList();
				connection.close();
				message = "Ket noi thanh cong den co so du lieu!";
			} else {
				message = "Khong the ket noi den co so du lieu.";
			}

		} catch (SQLException e) {
			message = "Loi khi ket noi: " + e.getMessage();
		}
		request.setAttribute("userList", list);
		request.setAttribute("message", message);
		request.getRequestDispatcher("testConnection.jsp").forward(request, response);
	}

}

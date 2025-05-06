package controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;
import utils.ResponseUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import Model.User;
import dao.UserDao;

/**
 * Servlet implementation class ChangePassword
 */
@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangePassword() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = SessionUtils.getUser(request.getSession());
		if (user != null) {
			request.getRequestDispatcher("change_password.jsp").forward(request, response);
		} else {
			ResponseUtils.evict(response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String newPassword = request.getParameter("newPassword");
		User user = SessionUtils.getUser(request.getSession());
		if (user != null) {
			try (Connection connection = DataSourceProvider.getDataSource().getConnection();) {
				UserDao userDao = new UserDao(connection);
				userDao.updatePassword(user.getUserID(), newPassword);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				response.sendRedirect("UserProfile");
			}
		} else {
			ResponseUtils.evict(response);
		}

	}

}

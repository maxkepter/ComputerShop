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
 * Servlet implementation class UserProfile
 */
@WebServlet("/UserProfile")
public class UserProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserProfile() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) SessionUtils.getUser(request.getSession());
		if (user != null) {
			request.getRequestDispatcher("user_profile.jsp").forward(request, response);
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
		User user = (User) SessionUtils.getUser(request.getSession());
		if (user != null) {
			String command = request.getParameter("command");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String address = request.getParameter("address");
			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				UserDao userDao = new UserDao(connection);
				switch (command) {
				case "update":

					userDao.updateUser(user.getUserID(), firstName, lastName, email, phone, address);
					break;

				default:
					break;
				}

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

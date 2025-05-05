package controller.login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import Model.User;
import dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.DataSourceProvider;
import utils.Validate;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/Register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("register.jsp").forward(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection=DataSourceProvider.getDataSource().getConnection()){
			UserDao userDao = new UserDao(connection);

			// Take data from form
			String userName = request.getParameter("userName");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String address = request.getParameter("address");
			String password = request.getParameter("password");
			if (Validate.checkPhoneNumber(phone)) {
				try {
					if (userDao.checkDuplicateUserName(userName)) {
						// If userName duplicate
						int registerStatus = 1; // 1 = duplicate userName
						request.setAttribute("registerStatus", registerStatus);
						request.setAttribute("registerFirstName", firstName);
						request.setAttribute("registerLastName", lastName);
						request.setAttribute("registerEmail", email);
						request.setAttribute("registerPhone", phone);
						request.setAttribute("registerAddress", address);
						request.getRequestDispatcher("register.jsp").forward(request, response);
					} else {
						User user = new User(0, userName, email, phone, address, firstName, lastName);
						userDao.createUser(user, password);
						HttpSession session = request.getSession();
						session.setAttribute("isLogged", true);// user is logged
						session.setAttribute("user", user);
						response.sendRedirect("Home");
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new ServletException(e);
				}
			} else {
				response.sendRedirect("Register");
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}

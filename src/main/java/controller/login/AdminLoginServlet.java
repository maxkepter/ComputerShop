package controller.login;

import java.io.IOException;
import java.sql.Connection;

import Model.User;
import dao.UserDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.DataSourceProvider;
import utils.ResponseUtils;

/**
 * Servlet implementation class AdminLoginServlet
 */
@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminLoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		try(Connection connection=DataSourceProvider.getDataSource().getConnection()) {
			UserDao userDao = new UserDao(connection);
			int status = userDao.checkLoginUser(userName, password);
			if (status == 0) {
				User admin = userDao.getUser(userName);
				HttpSession session = request.getSession();
				session.setAttribute("isLogged", true);// user is logged
				session.setAttribute("admin", admin);
				response.sendRedirect("AdminHome");
			} else {
				// fail to login
				ResponseUtils.evict(response);
			}} catch (Exception e) {
				// TODO: handle exception
			}
		}	
		

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dispatcher = req.getRequestDispatcher("/admin_login.jsp");
		dispatcher.forward(req, resp);
	}

}

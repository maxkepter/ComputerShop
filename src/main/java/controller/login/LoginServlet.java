package controller.login;

import java.io.IOException;

import javax.sql.DataSource;

import Model.User;
import dao.UserDao;
import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name = "jdbc/computer-shop")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("login.jsp").forward(request, response);
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
		UserDao userDao = new UserDao(dataSource);
		RequestDispatcher dispatcher = null;
		int status = userDao.checkLoginUser(userName, password);
		switch (status) {
		case 0:// login success
			User user = userDao.getUser(userName);
			HttpSession session = request.getSession();
			session.setAttribute("isLogged", true);// user is logged
			session.setAttribute("user", user);
			session.setMaxInactiveInterval(60*60);//second
			response.sendRedirect("Home");
			break;
		case 1:// wrong password66
			request.setAttribute("loginStatus", 1);
			request.setAttribute("userName", userName);
			dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
			break;
		case 2:// userName not found
			request.setAttribute("loginStatus", 2);
			request.setAttribute("userName", userName);
			dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
			break;

		default:

			break;
		}
	}

}

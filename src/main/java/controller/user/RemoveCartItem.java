package controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;
import utils.ResponseUtils;
import utils.SessionUtils;
import utils.Validate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import Model.User;
import dao.CartDao;

/**
 * Servlet implementation class RemoveCartItem
 */
@WebServlet("/RemoveCartItem")
public class RemoveCartItem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RemoveCartItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String command = request.getParameter("command");
		User user = SessionUtils.getUser(request.getSession());
		if (user != null) {
			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				CartDao cartDao = new CartDao(connection);
				switch (command) {
				case "removeCartItem":
					String productId = request.getParameter("productId");
					if (Validate.checkInt(productId)) {
						cartDao.removeFromCart(user.getUserID(), Integer.parseInt(productId));
					}
					break;

				default:
					break;
				}
			} catch (SQLException e) {
				// TODO: handle exception
			} finally {
				response.sendRedirect("Cart");
			}
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

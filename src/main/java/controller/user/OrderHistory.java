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
import java.util.List;

import Model.Order;
import Model.User;
import dao.OrderDao;

/**
 * Servlet implementation class OrderHistory
 */
@WebServlet("/OrderHistory")
public class OrderHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OrderHistory() {
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
			try (Connection connection=DataSourceProvider.getDataSource().getConnection()){
				OrderDao orderDao=new OrderDao(connection);
				List<Order> orderList=orderDao.getOrder(user.getUserID());
				request.setAttribute("orderList", orderList);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				request.getRequestDispatcher("order_history.jsp").forward(request, response);
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
		User user = (User) SessionUtils.getUser(request.getSession());
		if (user != null) {

		} else {
			ResponseUtils.evict(response);
		}
	}

}

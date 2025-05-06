package controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;
import utils.ResponseUtils;
import utils.SessionUtils;
import utils.StringFilter;
import utils.Validate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Model.Product;
import Model.User;
import dao.CartDao;
import dao.OrderDao;

/**
 * Servlet implementation class Cart
 */
@WebServlet("/Cart")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Cart() {
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
			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				CartDao cartDao = new CartDao(connection);
				List<Product> cartItems = cartDao.getCartByUserId(user.getUserID());

				request.setAttribute("cartItems", cartItems);

				request.getRequestDispatcher("cart.jsp").forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
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
		String command = request.getParameter("command");
		User user = SessionUtils.getUser(request.getSession());
		if (user != null) {
			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				OrderDao orderDao = new OrderDao(connection);
				switch (command) {
				case "order":
					String[] selectedItems = request.getParameterValues("selectedItems");

					if (selectedItems != null && Validate.checkInt(selectedItems)) {
						List<Integer> productIds = StringFilter.toListInt(selectedItems);
						List<Integer> quantities = new ArrayList<>();

						for (String productIdStr : selectedItems) {
							String quantityParam = request.getParameter("quantity_" + productIdStr);
							int quantity = Integer.parseInt(quantityParam);
							quantities.add(quantity);
						}

						// Tạo đơn hàng
						orderDao.createOrder(user.getUserID(), Product.getProductQuantityList(productIds, quantities));
					}

					break;
				default:
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				response.sendRedirect("Cart");
			}
		} else {
			ResponseUtils.evict(response);
		}
	}

	private void print(String[] s) {
		for (String string : s) {
			System.out.println(s);
		}
	}

}

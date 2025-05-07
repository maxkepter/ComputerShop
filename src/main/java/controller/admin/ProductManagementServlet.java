package controller.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Model.Product;
import Model.User;
import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;
import utils.ResponseUtils;
import utils.SessionUtils;
import utils.Validate;

/**
 * Servlet implementation class ProductManagement
 */
@WebServlet("/ProductManagement")
public class ProductManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int PAGE_SIZE = 36;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductManagementServlet() {
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
		if (SessionUtils.isAdmin(request.getSession())) {
			ResponseUtils.evict(response);
			return;
		} else {
			String numPageString = request.getParameter("numPage");
			int numPage = 0;
			if (numPageString == null || !Validate.checkInt(numPageString)) {
				numPage = 0;
			} else {
				numPage = Integer.parseInt(numPageString);
			}
			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				ProductDao productDao = new ProductDao(connection);
				int totalProduct = productDao.countProduct();
				int maxPage = (int) Math.ceil(totalProduct / PAGE_SIZE); // 36 product per page
				List<Product> products = productDao.getProductByPage(numPage, 36);

				request.setAttribute("numPage", numPage);
				request.setAttribute("maxNumPage", maxPage);
				request.setAttribute("listProduct", products);
				// foward request to jsp file
				request.getRequestDispatcher("admin/admin_product.jsp").forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not load product list.");
			}
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (SessionUtils.isAdmin(request.getSession())) {
			ResponseUtils.evict(response);
			return;
		} else {
			String command = request.getParameter("command");
			String productIdString = request.getParameter("productId");
			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				ProductDao productDao = new ProductDao(connection);
				switch (command) {
				case "delete":
					if (Validate.checkInt(productIdString)) {
						int productId = Integer.parseInt(productIdString);
						productDao.deleteProduct(productId);
					}
					break;

				default:
					break;
				}
				response.sendRedirect("ProductManagement");
			} catch (SQLException e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not load product list.");
			}
		}

	}

}

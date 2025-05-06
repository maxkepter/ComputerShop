package View;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Model.Product;
import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;

/**
 * Servlet implementation class Home
 */
@WebServlet("/Home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Home() {
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
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			ProductDao productDao = new ProductDao(connection);
			List<Product> listProduct1 = productDao.searchProduct("", 1, 0, 0, 5);
			List<Product> listProduct2 = productDao.searchProduct("", 2, 0, 0, 5);
			List<Product> listProduct3 = productDao.searchProduct("", 3, 0, 0, 5);
			List<Product> listProduct4 = productDao.searchProduct("", 0, 1, 0, 5);
			List<Product> listProduct5 = productDao.searchProduct("", 0, 2, 0, 5);
			List<Product> listProduct6 = productDao.searchProduct("", 0, 3, 0, 5);
			
			request.setAttribute("listProduct1", listProduct1);
			request.setAttribute("listProduct2", listProduct2);
			request.setAttribute("listProduct3", listProduct3);
			request.setAttribute("listProduct4", listProduct4);
			request.setAttribute("listProduct5", listProduct5);
			request.setAttribute("listProduct6", listProduct6);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.getRequestDispatcher("/index.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

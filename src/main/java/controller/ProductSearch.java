package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;
import utils.SessionUtils;
import utils.Validate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Model.Brand;
import Model.Product;
import Model.Type;
import Model.User;
import dao.BrandDao;
import dao.CartDao;
import dao.ProductDao;
import dao.TypeDao;

/**
 * Servlet implementation class ProductSearch
 */
@WebServlet("/ProductSearch")
public class ProductSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductSearch() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String typeIdString = request.getParameter("productType");
		String brandIdString = request.getParameter("productBrand");
		String search=request.getParameter("search");
		String sortBy = request.getParameter("sort");

		String numPageString = request.getParameter("numPage");
		int typeId=0;
		int brandId=0;
		int numPage = 0;
		if (Validate.checkInt(numPageString)) {
			numPage = Integer.parseInt(numPageString);
		}

		if(search==null) {
			search="";
		}
		if(Validate.checkInt(typeIdString)) {
			typeId=Integer.parseInt(typeIdString);
		}
		
		if(Validate.checkInt(brandIdString)) {
			brandId=Integer.parseInt(brandIdString);
		}

		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			ProductDao productDao = new ProductDao(connection);
			List<Product> productList = productDao.searchProduct(numPageString, brandId, typeId, numPage, 40);

			TypeDao typeDao = new TypeDao(connection);
			List<Type> typeList = typeDao.getType();

			BrandDao brandDao = new BrandDao(connection);
			List<Brand> brandList = brandDao.getBrand();

			request.setAttribute("productList", productList);
			request.setAttribute("typeList", typeList);
			request.setAttribute("brandList", brandList);

			request.getRequestDispatcher("product_search.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
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
		String productId = request.getParameter("productId");
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			CartDao cartDao = new CartDao(connection);
			switch (command) {
			case "addToCart":
				if (user != null && Validate.checkInt(productId)) {
					cartDao.addToCart(user.getUserID(), Integer.parseInt(productId));

				}
				break;

			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		response.sendRedirect("ProductSearch");

	}

}

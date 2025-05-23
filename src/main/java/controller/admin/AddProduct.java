package controller.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Model.Brand;
import Model.Product;
import Model.SpecProduct;
import Model.Specification;
import Model.Type;
import dao.BrandDao;
import dao.ProductDao;
import dao.SpecificationDao;
import dao.TypeDao;
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

/**
 * Servlet implementation class AddProduct
 */
@WebServlet("/AddProduct")
public class AddProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddProduct() {
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
		}
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			BrandDao brandDao = new BrandDao(connection);
			TypeDao typeDao = new TypeDao(connection);
			SpecificationDao specificationDao = new SpecificationDao(connection);

			// send list of prarameter to form
			List<Brand> brandList = brandDao.getBrand();
			List<Type> typeList = typeDao.getType();
			List<Specification> specificationList = specificationDao.getSpecification();

			request.setAttribute("brandList", brandList);
			request.setAttribute("typeList", typeList);
			request.setAttribute("specificationList", specificationList);
			request.getRequestDispatcher("admin/add_product.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not load brand list.");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			ProductDao productDao = new ProductDao(connection);

			// take parameters from form
			String productName = request.getParameter("productName");
			String productQuantity = request.getParameter("productQuantity");
			String productDescription = request.getParameter("productDescription");
			String productPrice = request.getParameter("productPrice");
			String productBrand = request.getParameter("productBrand");
			String[] typeId = request.getParameterValues("productType");
			String[] productSpecificationId = request.getParameterValues("productSpecification");
			String[] specProduct = request.getParameterValues("specProduct");
			String[] specValue = request.getParameterValues("specValue");
			String[] productImage = request.getParameterValues("productImage");

			// check data type number, prevent exception
			if (Validate.checkInt(productPrice) && Validate.checkDouble(productPrice) && Validate.checkInt(productBrand)
					&& Validate.checkInt(typeId) && Validate.checkDouble(specValue)) {
				// convert array to list
				List<Integer> typeList = StringFilter.toListInt(typeId);
				List<SpecProduct> specProductList = StringFilter.toListSpecProduct(productSpecificationId, specProduct,
						specValue);
				List<String> imageList = StringFilter.toListString(productImage);
				Product newProduct = new Product(productName.trim(), Integer.parseInt(productQuantity),
						productDescription.trim(), Double.parseDouble(productPrice), Integer.parseInt(productBrand),
						specProductList, imageList);
				productDao.createProduct(newProduct, typeList);
				response.sendRedirect("ProductManagement");
			} else {
				
				request.setAttribute("error", "Invalid data!");
				request.getRequestDispatcher("admin/add_product.jsp").forward(request, response);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}

package View;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Product;
import Model.Rating;
import Model.User;
import dao.BrandDao;
import dao.CartDao;
import dao.OrderDao;
import dao.ProductDao;
import dao.RatingDao;
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
 * Servlet implementation class ViewProduct
 */
@WebServlet("/ViewProduct")
public class ViewProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewProduct() {
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
		String productIdString = request.getParameter("productId");
		String ratingPageString = request.getParameter("numPage");
		User user = SessionUtils.getUser(request.getSession());
		int ratingPage = 1; // Mặc định trang 1 nếu không có hoặc truyền giá trị không hợp lệ

		// Kiểm tra và gán giá trị ratingPage nếu hợp lệ
		if (Validate.checkInt(ratingPageString)) {
			int parsedPage = Integer.parseInt(ratingPageString);
			if (parsedPage >= 1) {
				ratingPage = parsedPage;
			}
		}

		if (Validate.checkInt(productIdString)) {
			int productId = Integer.parseInt(productIdString);

			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				ProductDao productDao = new ProductDao(connection);
				RatingDao ratingDao = new RatingDao(connection);
				OrderDao orderDao = new OrderDao(connection);
				BrandDao brandDao = new BrandDao(connection);

				// Lấy thông tin sản phẩm
				Product product = productDao.getProductById(productId);
				// Lấy danh sách đánh giá theo phân trang
				List<Rating> ratingList = ratingDao.getRating(productId, ratingPage, 20);
				// Lấy tên thương hiệu
				String brand = brandDao.getBrandById(product.getBrandID());
				// Kiểm tra xem user đã mua sản phẩm này chưa
				boolean hasUserPurchased =false;
				if (user != null) {
					hasUserPurchased = orderDao.hasUserPurchasedProduct(user.getUserID(), productId);					
				}
				// Đặt các thuộc tính cần thiết vào request
				request.setAttribute("product", product);
				request.setAttribute("ratingList", ratingList);
				request.setAttribute("brandName", brand);
				request.setAttribute("hasUserPurchased", hasUserPurchased);

				// Forward request đến view
				request.getRequestDispatcher("view_product.jsp").forward(request, response);
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else {
			// Nếu productId không hợp lệ, trả về lỗi hoặc redirect
			ResponseUtils.evict(response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userId = Integer.parseInt(request.getParameter("userId"));
		int productId = Integer.parseInt(request.getParameter("productId"));
		RatingDao ratingDao = null;
		OrderDao orderDao = null;
		String command = request.getParameter("command");
		switch (command) {
		case "addRating":
			double rate = Double.parseDouble(request.getParameter("rating"));
			String comment = request.getParameter("comment");

			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				ratingDao = new RatingDao(connection);
				ratingDao.createRating(userId, productId, rate, comment);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			break;
		case "deleteRating":
			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				ratingDao = new RatingDao(connection);
				ratingDao.deleteRating(userId, productId);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			break;
		case "buyProduct":

			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				orderDao = new OrderDao(connection);
				String quantity = request.getParameter("quantity");
				List<Map<Integer, Integer>> productList = new ArrayList<Map<Integer, Integer>>();

				if (Validate.checkInt(quantity)) {
					Map<Integer, Integer> productinfo = new HashMap<Integer, Integer>();
					productinfo.put(productId, Integer.parseInt(quantity));
					productList.add(productinfo);
					orderDao.createOrder(userId, productList);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			break;
		case "cart":
			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				CartDao cartDao = new CartDao(connection);
				cartDao.addToCart(userId, productId);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
		response.sendRedirect("ViewProduct?productId=" + productId);
	}

}

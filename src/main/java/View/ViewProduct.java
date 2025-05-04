package View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import Model.Order;
import Model.Product;
import Model.Rating;
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
	        ProductDao productDao = new ProductDao(DataSourceProvider.getDataSource());
	        RatingDao ratingDao = new RatingDao(DataSourceProvider.getDataSource());
	        BrandDao brandDao = new BrandDao(DataSourceProvider.getDataSource());

	        // Lấy thông tin sản phẩm
	        Product product = productDao.getProductById(productId);
	        // Lấy danh sách đánh giá theo phân trang
	        List<Rating> ratingList = ratingDao.getRating(productId, ratingPage, 20);
	        // Lấy tên thương hiệu
	        String brand = brandDao.getBrandById(product.getBrandID());

	        // Đặt các thuộc tính cần thiết vào request
	        request.setAttribute("product", product);
	        request.setAttribute("ratingList", ratingList);
	        request.setAttribute("brandName", brand);

	        // Forward request đến view
	        request.getRequestDispatcher("view_product.jsp").forward(request, response);
	        
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
		RatingDao ratingDao=null;
		OrderDao orderDao=null;
		String command = request.getParameter("command");
		switch (command) {
		case "addRating":
			double rate = Double.parseDouble(request.getParameter("rating"));
			String comment = request.getParameter("comment");

			ratingDao = new RatingDao(DataSourceProvider.getDataSource());
			ratingDao.createRating(userId, productId, rate, comment);

			// Sau khi thêm đánh giá xong, redirect lại trang ViewProduct để hiển thị lại
			// sản phẩm với đánh giá mới
			response.sendRedirect("ViewProduct?productId=" + productId);
			break;
		case "deleteRating":
			ratingDao=new RatingDao(DataSourceProvider.getDataSource());			
			ratingDao.deleteRating(userId, productId);  			
			break;
		case "buyProduct":
			orderDao =new OrderDao(DataSourceProvider.getDataSource());
			String quantity=request.getParameter("quantity");
			List<Map<Integer, Integer>> productList=new ArrayList<Map<Integer,Integer>>();
				
			if(Validate.checkInt(quantity)) {
				Map<Integer, Integer> productinfo=new HashMap<Integer, Integer>();
				productinfo.put(productId, Integer.parseInt(quantity));
				productList.add(productinfo);
				orderDao.createOrder(userId, productList);
			}			
			break;
		case "cart":
			CartDao cartDao=new CartDao(DataSourceProvider.getDataSource());
			cartDao.addToCart(userId, productId);			
			break;
		default:
			break;
		}
		response.sendRedirect("ViewProduct?productId=" + productId);
	}

}

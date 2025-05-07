package controller.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Model.Brand;
import dao.BrandDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;
import utils.ResponseUtils;
import utils.SessionUtils;
import utils.Validate;

@WebServlet("/BrandManagement")
public class BrandManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int PAGE_SIZE = 100;

	public BrandManagementServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!SessionUtils.isAdmin(request.getSession())) {
			ResponseUtils.evict(response);
			return;
		}
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			String num = request.getParameter("numPage");

			BrandDao brandDao = new BrandDao(connection);
			int totalBrand = brandDao.countBrand();
			int maxNumPage = (int) Math.ceil(totalBrand / (double) PAGE_SIZE);

			int numPage = 0;
			if (Validate.checkInt(num)) {
				numPage = Integer.parseInt(num);
				if (numPage < 0)
					numPage = 0;
				else if (numPage > maxNumPage)
					numPage = maxNumPage;
			}

			List<Brand> brandList = brandDao.getListBrand(numPage);
			request.setAttribute("brandList", brandList);
			request.setAttribute("numPage", numPage);
			request.setAttribute("maxNumPage", maxNumPage);
			request.getRequestDispatcher("/admin/brand_management.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not load brand list.");
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (!SessionUtils.isAdmin(request.getSession())) {
			ResponseUtils.evict(response);
			return;
		}
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			String command = request.getParameter("command");
			BrandDao brandDao = new BrandDao(connection);
			String brandIdString = request.getParameter("brandId");
			String brandName = request.getParameter("brandName");

			if (command == null) {
				request.setAttribute("error", "Lệnh không hợp lệ.");
				request.getRequestDispatcher("/admin/brand_management.jsp").forward(request, response);
				return;
			}

			switch (command) {
			case "create":
				if (!Validate.checkString(brandName)) {
					request.setAttribute("error", "Tên thương hiệu không được để trống.");
					request.getRequestDispatcher("/admin/brand_management.jsp").forward(request, response);
					return;
				}
				brandDao.createBrand(brandName.trim());
				break;

			case "delete":
				if (!Validate.checkInt(brandIdString)) {
					request.setAttribute("error", "ID thương hiệu không hợp lệ.");
					request.getRequestDispatcher("/admin/brand_management.jsp").forward(request, response);
					return;
				}
				brandDao.deleteBrand(Integer.parseInt(brandIdString));
				break;

			case "update":
				if (!Validate.checkInt(brandIdString) || !Validate.checkString(brandName)) {
					request.setAttribute("error", "Thông tin cập nhật không hợp lệ.");
					request.getRequestDispatcher("/admin/brand_management.jsp").forward(request, response);
					return;
				}
				brandDao.updateBrand(Integer.parseInt(brandIdString), brandName.trim());
				break;

			default:
				request.setAttribute("error", "Lệnh không xác định.");
				request.getRequestDispatcher("/admin/brand_management.jsp").forward(request, response);
				return;
			}
			response.sendRedirect(request.getContextPath() + "/BrandManagement");
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not load brand list.");
		}

	}
}
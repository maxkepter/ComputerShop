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

@WebServlet("/BrandManagement")
public class BrandManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BrandManagement() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			String num = request.getParameter("numPage");
			BrandDao brandDao = new BrandDao(connection);
			int maxNumPage = brandDao.countBrand() / 100;
			if (num == null) {
				num = "0";
			}
			int numPage = Integer.parseInt(num);
			List<Brand> brandList = brandDao.getListBrand(numPage);
			request.setAttribute("brandList", brandList);
			request.setAttribute("numPage", numPage);
			request.setAttribute("maxNumPage", maxNumPage);
			request.getRequestDispatcher("/admin/brand_management.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			String command = request.getParameter("command");
			BrandDao brandDao = new BrandDao(connection);

			if (command != null) {
				switch (command) {
				case "create":
					String brandName = request.getParameter("brandName");
					if (brandName != null) {
						brandDao.createBrand(brandName);
					}
					break;
				case "delete":
					if (request.getParameter("brandId") != null) {
						int brandId = Integer.parseInt(request.getParameter("brandId"));
						brandDao.deleteBrand(brandId);
					}
					break;
				case "update":
					String brandNameUpdate = request.getParameter("brandName");
					String brandIdUpdate = request.getParameter("brandId");
					if (brandIdUpdate != null && brandNameUpdate != null) {
						brandDao.updateBrand(Integer.parseInt(brandIdUpdate), brandNameUpdate);
					}
					break;
				default:
					break;
				}
			}
			response.sendRedirect("BrandManagement");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
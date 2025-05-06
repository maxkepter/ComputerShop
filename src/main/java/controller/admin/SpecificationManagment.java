package controller.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Model.Specification;
import Model.User;
import dao.SpecificationDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;
import utils.ResponseUtils;
import utils.SessionUtils;

/**
 * Servlet implementation class SpecificationManagment
 */
@WebServlet("/SpecificationManagment")
public class SpecificationManagment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SpecificationManagment() {
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
		User user = SessionUtils.getUser(request.getSession());
		if (user == null || user.getUserRole() != 1) {
			ResponseUtils.evict(response);
		} else {
			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				SpecificationDao specificationDao = new SpecificationDao(connection);
				String num = request.getParameter("numPage");
				int maxNumPage = specificationDao.countSpec() / 100;
				if (num == null) {
					num = "0";
				}
				int numPage = Integer.parseInt(num);

				List<Specification> sepcList = specificationDao.getListSpecification(numPage);
				request.setAttribute("specList", sepcList);
				request.setAttribute("maxNumPage", maxNumPage);
				request.setAttribute("numPage", numPage);
				request.getRequestDispatcher("/admin/specifications_managment.jsp").forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
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
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			String command = request.getParameter("command");
			SpecificationDao specificationDao = new SpecificationDao(connection);

			if (command != null) {
				switch (command) {
				case "create":
					String specName = request.getParameter("specName");
					if (specName != null) {
						specificationDao.createSpec(specName);
					}
					break;
				case "delete":
					if (request.getParameter("specId") != null) {
						int specId = Integer.parseInt(request.getParameter("specId"));
						specificationDao.deleteSpec(specId);
					}
					break;
				case "update":
					String specNameUpdate = request.getParameter("specName");
					String specIdUpdate = request.getParameter("specId");
					if (specIdUpdate != null && specNameUpdate != null) {
						specificationDao.updateSpec(Integer.parseInt(specIdUpdate), specNameUpdate);
					}
					break;
				default:
					break;
				}

			}
			response.sendRedirect("SpecificationManagment");
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}

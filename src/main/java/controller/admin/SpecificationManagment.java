package controller.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Model.Specification;
import dao.SpecificationDao;
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
 * Servlet implementation class SpecificationManagment
 */
@WebServlet("/SpecificationManagment")
public class SpecificationManagment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int PAGE_SIZE = 100;

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
		if (!SessionUtils.isAdmin(request.getSession())) {
			ResponseUtils.evict(response);
			return;
		}
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			SpecificationDao specificationDao = new SpecificationDao(connection);
			String num = request.getParameter("numPage");

			int totalSpec = specificationDao.countSpec();
			int maxNumPage = (int) Math.ceil(totalSpec / (double) PAGE_SIZE);

			int numPage = 0;
			if (Validate.checkInt(num)) {
				numPage = Integer.parseInt(num);
				if (numPage < 0)
					numPage = 0;
				else if (numPage > maxNumPage)
					numPage = maxNumPage;
			}

			List<Specification> specList = specificationDao.getListSpecification(numPage);
			request.setAttribute("specList", specList);
			request.setAttribute("maxNumPage", maxNumPage);
			request.setAttribute("numPage", numPage);
			request.getRequestDispatcher("/admin/specifications_managment.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not load speccification list.");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!SessionUtils.isAdmin(request.getSession())) {
			ResponseUtils.evict(response);
			return;
		}

		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			String command = request.getParameter("command");
			String specName = request.getParameter("specName");
			String specIdString = request.getParameter("specId");
			SpecificationDao specificationDao = new SpecificationDao(connection);

			if (command != null) {
				switch (command) {
				case "create":
					if (!Validate.checkString(specName)) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Specification name cannot be empty!");
						return;
					}
					specificationDao.createSpec(specName);
					break;
				case "delete":
					if (!Validate.checkInt(specIdString)) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID!");
						return;
					}

					// check exist id
					specificationDao.deleteSpec(Integer.parseInt(specIdString));
					break;
				case "update":
					if (!Validate.checkString(specName) || !Validate.checkString(specIdString)) {
						response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid data input!");
						return;
					}
					specificationDao.updateSpec(Integer.parseInt(specIdString), specName);

					break;
				default:
					request.setAttribute("error", "Undefined command");
					request.getRequestDispatcher("/admin/specifications_managment.jsp").forward(request, response);
					return;
				}

			}
			response.sendRedirect(request.getContextPath() + "/SpecificationManagment");
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid data input!");
		}

	}

}

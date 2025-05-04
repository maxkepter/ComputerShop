package controller.admin;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import Model.Specification;
import dao.SpecificationDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;

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
		DataSource dataSource = DataSourceProvider.getDataSource();
		SpecificationDao specificationDao = new SpecificationDao(dataSource);
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

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String command = request.getParameter("command");
		SpecificationDao specificationDao = new SpecificationDao(DataSourceProvider.getDataSource());

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

	}

}

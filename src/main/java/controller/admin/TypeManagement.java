package controller.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Model.Type;
import dao.TypeDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;
import utils.ResponseUtils;
import utils.SessionUtils;
import utils.Validate;

@WebServlet("/TypeManagement")
public class TypeManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final double PAGE_SIZE = 100;

	public TypeManagement() {
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
			TypeDao typeDao = new TypeDao(connection);
			int totalType = typeDao.countType();
			int maxNumPage = (int) Math.ceil(totalType /(double) PAGE_SIZE);
			int numPage = 0;
			if (Validate.checkInt(num)) {
				numPage = Integer.parseInt(num);
				if (numPage < 0)
					numPage = 0;
				else if (numPage > maxNumPage)
					numPage = maxNumPage;
			}

			List<Type> typeList = typeDao.getListType(numPage);
			request.setAttribute("typeList", typeList);
			request.setAttribute("numPage", numPage);
			request.setAttribute("maxNumPage", maxNumPage);
			request.getRequestDispatcher("/admin/type_managment.jsp").forward(request, response);

		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not load type list.");
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
			TypeDao typeDao = new TypeDao(connection);
			String typeName = request.getParameter("typeName");
			String typeIdString = request.getParameter("typeId");
			if (command != null) {
				switch (command) {
				case "create":

					if (!Validate.checkString(typeName)) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Type name cannot be empty!");
						return;
					}
					typeDao.createType(typeName.trim());
					break;
				case "delete":
					if (!Validate.checkInt(typeIdString)) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID!");
						return;

					}
					// check id is exist
					typeDao.deleteType(Integer.parseInt(typeIdString));
					break;
				case "update":

					if (!Validate.checkString(typeName) || !Validate.checkInt(typeIdString)) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid data input!");
						return;
					}
					typeDao.updateType(Integer.parseInt(typeIdString), typeName);
					break;
				default:
					request.setAttribute("error", "Undefined command");
					request.getRequestDispatcher("/admin/type_managment.jsp").forward(request, response);
					return;
				}
			}
			response.sendRedirect(request.getContextPath() + "/TypeManagement");
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connect error!");
		}

	}
}

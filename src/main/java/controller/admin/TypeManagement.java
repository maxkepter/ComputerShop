package controller.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Model.Type;
import Model.User;
import dao.TypeDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;
import utils.ResponseUtils;
import utils.SessionUtils;

@WebServlet("/TypeManagement")
public class TypeManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TypeManagement() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = SessionUtils.getUser(request.getSession());
		if (user == null || user.getUserRole() != 1) {
			ResponseUtils.evict(response);
		} else {
			try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
				String num = request.getParameter("numPage");
				TypeDao typeDao = new TypeDao(connection);
				int maxNumPage = typeDao.countType() / 100;
				if (num == null) {
					num = "0";
				}
				int numPage = Integer.parseInt(num);
				List<Type> typeList = typeDao.getListType(numPage);
				request.setAttribute("typeList", typeList);
				request.setAttribute("numPage", numPage);
				request.setAttribute("maxNumPage", maxNumPage);
				request.getRequestDispatcher("/admin/type_managment.jsp").forward(request, response);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
			String command = request.getParameter("command");
			TypeDao typeDao = new TypeDao(connection);

			if (command != null) {
				switch (command) {
				case "create":
					String typeName = request.getParameter("typeName");
					if (typeName != null) {
						typeDao.createType(typeName);
					}
					break;
				case "delete":
					if (request.getParameter("typeId") != null) {
						int typeId = Integer.parseInt(request.getParameter("typeId"));
						typeDao.deleteType(typeId);
					}
					break;
				case "update":
					String typeNameUpdate = request.getParameter("typeName");
					String typeIdUpdate = request.getParameter("typeId");
					if (typeIdUpdate != null && typeNameUpdate != null) {
						typeDao.updateType(Integer.parseInt(typeIdUpdate), typeNameUpdate);
					}
					break;
				default:
					break;
				}
			}
			response.sendRedirect("TypeManagement");
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}

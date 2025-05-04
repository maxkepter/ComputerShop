package controller.admin;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import Model.Type;
import dao.TypeDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataSourceProvider;

@WebServlet("/TypeManagement")
public class TypeManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TypeManagement() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String num = request.getParameter("numPage");
		DataSource dataSource = DataSourceProvider.getDataSource();
		TypeDao typeDao = new TypeDao(dataSource);
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
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String command = request.getParameter("command");
		TypeDao typeDao = new TypeDao(DataSourceProvider.getDataSource());

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
	}
}

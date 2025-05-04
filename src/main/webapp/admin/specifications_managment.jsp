<%@page import="dao.SpecificationDao"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Specifications Management</title>
<%
Integer numPage = (Integer) request.getAttribute("numPage");
Integer maxNumPage = (Integer) request.getAttribute("maxNumPage");
if (numPage == null) {
	numPage = 0;
}
if (maxNumPage == null) {
	maxNumPage = 0;
}
%>


</head>
<body>
	<f:view>
		<h1>Specifications Management</h1>
		<h2>Add specifications</h2>
		<form action="SpecificationManagment" method="post">
			<!--check not null -->
			<input type="hidden" name="command" value="create" /> <label>Specifications</label>
			<input type="text" name="specName" /> <input type="submit"
				value="Add" required />
		</form>

		<h2>Specifications Info</h2>
		<table border="1">
			<tr>
				<th>Id</th>
				<th>Name</th>
				<th>Delete</th>
				<th>Update</th>
			</tr>

			<!-- Duyệt danh sách các specification từ backend -->
			<c:forEach var="spec" items="${specList}">
				<tr>
					<td>${spec.specID}</td>
					<td>${spec.specName}</td>
					<td>
						<!-- Delete link -->
						<form action="<c:url value='SpecificationManagment' />"
							method="post" style="display: inline;">
							<input type="hidden" name="command" value="delete" /> <input
								type="hidden" name="specId" value="${spec.specID}" />
							<button type="submit" class="btn btn-danger"
								onclick="return confirm('Delete this spec?')">Delete</button>
						</form>
					</td>
					<td>
						<!-- Update link -->
						<form action="<c:url value='SpecificationManagment' />"
							method="post" style="display: inline;">
							<input type="hidden" name="command" value="update" /> <input
								type="hidden" name="specId" value="${spec.specID}" /> <input
								type="text" name="specName" value="${spec.specName}" required />
							<button type="submit"
								onclick="return confirm('Update this spec?')">Update</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</table>

		<!-- Pagination buttons -->
		<form action="SpecificationManagment" method="get">
			<c:if test="${numPage > 0}">
				<button type="submit" name="numPage" value="${numPage - 1}">Previous</button>
			</c:if>

			<button type="submit" name="numPage" value="${numPage}">${numPage+1}</button>

			<c:if test="${numPage < maxNumPage}">
				<button type="submit" name="numPage" value="${numPage + 1}">Next</button>
			</c:if>
		</form>


		<!--paging  -->
		<form action="SpecificationManagment" method="get"
			onsubmit="return adjustPageNumber()">
			<label for="numPage">Page:</label> <input type="number" id="numPage"
				name="numPage" value="<%=numPage + 1%>" min="1"
				max="<%=maxNumPage + 1%>" required />
			<button type="submit">Go</button>
		</form>
		<!--paging -->
		<script>
			function adjustPageNumber() {
				const input = document.getElementById('numPage');
				input.value = parseInt(input.value) - 1;
				return true
			}
		</script>


	</f:view>
</body>
</html>

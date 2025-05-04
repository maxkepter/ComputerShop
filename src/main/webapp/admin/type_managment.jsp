<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Type Management</title>
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

	<h1>Type Management</h1>
	<h2>Add type</h2>
	<form action="TypeManagement" method="post">
		<input type="hidden" name="command" value="create"> <input
			type="text" name="typeName" placeholder="Enter new type name"
			required>
		<button type="submit">Add Type</button>
	</form>
	<h2>Type Info</h2>
	<table border="1">
		<thead>
			<tr>
				<th>TypeID</th>
				<th>TypeName</th>
				<th>Delete</th>
				<th>Actions</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="type" items="${typeList}">
				<tr>
					<td>${type.typeID}</td>
					<td>${type.typeName}</td>
					<td>
						<form action="TypeManagement" method="post"
							style="display: inline;">
							<input type="hidden" name="command" value="delete"> <input
								type="hidden" name="typeId" value="${type.typeID}">
							<button type="submit" class="btn btn-danger"
								onclick="return confirm('Delete this type?')">Delete</button>
						</form>


					</td>
					<td><form action="TypeManagement" method="post"
							style="display: inline;">
							<input type="hidden" name="command" value="update"> <input
								type="hidden" name="typeId" value="${type.typeID}"> <input
								type="text" name="typeName" value="${type.typeName}" required>
							<button type="submit"
								onclick="return confirm('Update this type?')">Update</button>
						</form></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<!-- Pagination buttons -->
	<form action="TypeManagement" method="get">
		<c:if test="${numPage > 0}">
			<button type="submit" name="numPage" value="${numPage - 1}">Previous</button>
		</c:if>

		<button type="submit" name="numPage" value="${numPage}">${numPage+1}</button>

		<c:if test="${numPage < maxNumPage}">
			<button type="submit" name="numPage" value="${numPage + 1}">Next</button>
		</c:if>
	</form>

	<form action="TypeManagement" method="get"
		onsubmit="return adjustPageNumber()">
		<label for="numPage">Page:</label> <input type="number" id="numPage"
			name="numPage" value="<%=numPage + 1%>" min="1"
			max="<%=maxNumPage + 1%>" required />
		<button type="submit">Go</button>
	</form>

	<script>
		function adjustPageNumber() {
			const input = document.getElementById('numPage');
			input.value = parseInt(input.value) - 1;
			return true
		}
	</script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Brand Management</title>
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

	<h1>Brand Management</h1>
	<h2>Add brand</h2>
	<form action="BrandManagement" method="post">
		<input type="hidden" name="command" value="create"> <input
			type="text" name="brandName" placeholder="Enter new brand name"
			required>
		<button type="submit">Add Brand</button>
	</form>

	<h2>Brand Info</h2>
	<table border="1">
		<thead>
			<tr>
				<th>BrandID</th>
				<th>BrandName</th>
				<th>Delete</th>
				<th>Actions</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="brand" items="${brandList}">
				<tr>
					<td>${brand.brandID}</td>
					<td>${brand.brandName}</td>
					<td>
						<form action="BrandManagement" method="post"
							style="display: inline;">
							<input type="hidden" name="command" value="delete"> <input
								type="hidden" name="brandId" value="${brand.brandID}">
							<button type="submit"
								onclick="return confirm('Delete this brand?')">Delete</button>
						</form>
					</td>
					<td>
						<form action="BrandManagement" method="post"
							style="display: inline;">
							<input type="hidden" name="command" value="update"> <input
								type="hidden" name="brandId" value="${brand.brandID}"> <input
								type="text" name="brandName" value="${brand.brandName}" required>
							<button type="submit"
								onclick="return confirm('Update this brand?')">Update</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<!-- Pagination buttons -->
	<form action="BrandManagement" method="get">
		<c:if test="${numPage > 0}">
			<button type="submit" name="numPage" value="${numPage - 1}">Previous</button>
		</c:if>

		<button type="submit" name="numPage" value="${numPage}">${numPage + 1}</button>

		<c:if test="${numPage < maxNumPage}">
			<button type="submit" name="numPage" value="${numPage + 1}">Next</button>
		</c:if>
	</form>
	
	<form action="BrandManagement" method="get"
		onsubmit="return adjustPageNumber()">
		<label for="numPage">Page:</label> <input type="number" id="numPage"
			name="numPage" value="<%=numPage + 1%>" min="1" max="<%=maxNumPage+1%>"
			required />
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

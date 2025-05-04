<%@page import="Model.Product"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Product Management</title>
<link rel="stylesheet" href="../css/add_product.css" />
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
<style>
body {
	font-family: Arial, sans-serif;
	margin: 20px;
}

h1 {
	color: red;
}

.product-list {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 20px;
	margin-top: 20px;
}

.product-item {
	border: 1px solid #ccc;
	padding: 15px;
	border-radius: 8px;
	text-align: center;
	background-color: #f9f9f9;
	transition: box-shadow 0.3s ease;
}

.product-item:hover {
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);
}

.product-item a {
	text-decoration: none; /* Xóa gạch chân */
	color: inherit; /* Giữ màu chữ gốc */
	display: block; /* Cho toàn bộ thẻ a bao hết nội dung */
}

.product-item a:hover {
	color: #007bff; /* Đổi màu chữ khi hover */
}

.product-item img {
	max-width: 100%; /* Không vượt quá khung */
	max-height: 150px; /* Giới hạn chiều cao */
	object-fit: contain; /* Giữ tỉ lệ ảnh */
	margin-bottom: 10px;
}

.product-item p {
	margin: 5px 0;
	font-size: 14px;
}

.product-item form {
	margin-top: 5px;
}

.product-item button {
	padding: 5px 10px;
	margin: 2px;
	border: none;
	border-radius: 4px;
	background-color: #007bff;
	color: white;
	cursor: pointer;
}

.product-item button:hover {
	background-color: #0056b3;
}
</style>
</head>
<body>
	<h1>Product management</h1>
	<a href="AddProduct">Add product</a>
	<div class="product-management">
		<div class="search-bar">
			<h2>Product search</h2>
			<form action="ProductManagement" method="get">
				<input type="text" name="search" placeholder="Search product..." />
				<button type="submit">Search</button>
			</form>
			<div class="product-filter">
				<form action="ProductManagement" method="get">
					<label for="productType">Type</label> <select id="productType"
						name="productType" required>
						<c:forEach var="type" items="${typeList}">
							<option value="${type.typeID}">${type.typeName}</option>
						</c:forEach>
					</select>
				</form>
				<form action="ProductManagement" method="get">
					<label for="productBrand">Brand</label> <select id="productBrand"
						name="productBrand" required>
						<c:forEach var="brand" items="${brandList}">
							<option value="${brand.brandID}">${brand.brandName}</option>
						</c:forEach>
					</select>
				</form>
				<form action="ProductManagement" method="get">
					<label for="sort">Sort by:</label> <select name="sort" id="sort">
						<option value="">Default</option>
						<option value="priceAsc">Price: Low to High</option>
						<option value="priceDesc">Price: High to Low</option>
						<option value="quantitySold">Quantity Sold</option>
					</select>
					<button type="submit">Sort</button>
			</div>
		</div>
		<h2>Product Info</h2>

		<div class="product-list">
			<%
			List<Product> productList = (List<Product>) request.getAttribute("listProduct");
			if (productList != null) {
				for (Product product : productList) {
			%>
			<c:url var="viewLink" value="ViewProduct">
				<c:param name="productId"
					value="<%=String.valueOf(product.getProductID())%>" />
			</c:url>
			<div class="product-item">
				<a href="${viewLink}"> <%
 String imageSrc = "https://www.svgrepo.com/show/20617/desktop-computer.svg"; // default image if can not not load image
 if (product.getImageProduct() != null && !product.getImageProduct().isEmpty()) {
 	imageSrc = product.getImageProduct().get(0);
 }
 %> <img src="<%=imageSrc%>"
					onerror="this.onerror=null; this.src='https://www.svgrepo.com/show/20617/desktop-computer.svg';"
					alt="Image product" />
					<p><%=product.getProductName()%></p>
					<p>
						Price:<%=(int) product.getPrice()%>
						đ
					</p>
					<p>
						Sold:<%=product.getQuantitySold()%></p>
					<p>
						Quantity:<%=product.getProductQuantity()%></p>
					<p>
						Rate:<%=product.getAvgRate()%></p>
				</a>
				<form action="ProductManagement" method="post"
					style="display: inline;">
					<input type="hidden" name="command" value="delete" /> <input
						type="hidden" name="productId" value="<%=product.getProductID()%>" />
					<button type="submit"
						onclick="return confirm('Delete this product?')">Delete</button>
				</form>
				<form action="UpdateProduct" method="get" style="display: inline;">
					<input type="hidden" name="productId"
						value="<%=product.getProductID()%>" />
					<button type="submit">Update</button>
				</form>
			</div>
			<%
			}
			}
			%>
		</div>
	</div>

	<form action="ProductManagement" method="get">
		<c:if test="${numPage > 0}">
			<button type="submit" name="numPage" value="${numPage - 1}">
				Previous</button>
		</c:if>

		<button type="submit" name="numPage" value="${numPage}">
			${numPage + 1}</button>

		<c:if test="${numPage < maxNumPage}">
			<button type="submit" name="numPage" value="${numPage + 1}">
				Next</button>
		</c:if>
	</form>
	<form action="ProductManagement" method="get">
		<label for="numPage">Page:</label> <input type="number" id="numPage"
			name="numPage" value="<%=numPage%>" min="1" max="<%=maxNumPage + 1%>"
			required />
		<button type="submit">Go</button>
	</form>
</body>
</html>

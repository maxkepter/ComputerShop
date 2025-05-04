<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="Model.User"%>
<%@page import="Model.Product"%>
<%@page import="Model.Brand"%>
<%@page import="Model.Type"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Search Product</title>
<%
Integer numPage = (Integer) request.getAttribute("numPage");
Integer maxNumPage = (Integer) request.getAttribute("maxNumPage");
if (numPage == null) {
	numPage = 0;
}
if (maxNumPage == null) {
	maxNumPage = 0;
}

List<Type> typeList = (List<Type>) request.getAttribute("typeList");
List<Brand> brandList = (List<Brand>) request.getAttribute("brandList");
List<Product> productList = (List<Product>) request.getAttribute("productList");
%>
<link rel="stylesheet" href="./css/navbar.css">
	<link rel="stylesheet" href="./css/filter_bar.css">
		<link rel="stylesheet" href="./css/search_product.css">
			<link rel="stylesheet" href="./css/footer.css">
				<link rel="stylesheet" href="./css/paging.css">
</head>
<body>
	<f:view>
		<div class="header">
			<%
			Boolean isLogged = (Boolean) session.getAttribute("isLogged");
			User user = (User) session.getAttribute("user");
			%>
			<a href="Home"><img id="home-logo"
				src="https://www.svgrepo.com/show/22031/home-icon-silhouette.svg"
				alt="Home"></a>
			<form action="ProductSearch" method="get">
				<input type="text" name="search" placeholder="Search product..." />
				<button type="submit">Search</button>
			</form>

			<div class="right-header">
				<div class="cart">
					<a href="Cart"><img
						src="https://www.svgrepo.com/show/34974/shopping-cart.svg" alt="">Cart</a>
				</div>
				<div class="login">
					<%
					if (isLogged != null && isLogged) {
					%>
					<a href="UserProfile" class="user-profile"> <img
						src="https://serg.al/files/image/meme/wojak/stupid/1554519396936.png"
						alt="">
							<p><%=user.getUserName()%></p></a>

					<%
					} else {
					%>
					<a href="Login">Login</a> <a href="Register">Register</a>
					<%
					}
					%>
				</div>



			</div>
		</div>
		<div class="body-content">
			<div class="filter-bar">
				<h2>Product Filter</h2>
				<div class="product-filter">
					<form action="ProductSearch" method="get">
						<div class="filter-item">
							<label for="productType">Type: </label> <select id="productType"
								name="productType">
								<%
								if (typeList != null) {
									for (Type type : typeList) {
								%>
								<option value="<%=type.getTypeID()%>"><%=type.getTypeName()%></option>
								<%
								}
								}
								%>
							</select>
						</div>

						<div class="filter-item">
							<label for="productBrand">Brand: </label> <select
								id="productBrand" name="productBrand" required>
								<%
								if (brandList != null) {
									for (Brand brand : brandList) {
								%>
								<option value="<%=brand.getBrandID()%>"><%=brand.getBrandName()%></option>
								<%
								}
								}
								%>
							</select>
						</div>

						<div class="filter-item">
							<label for="sort">Sort by:</label> <select name="sort" id="sort">
								<option value="">Default</option>
								<option value="priceAsc">Price: Low to High</option>
								<option value="priceDesc">Price: High to Low</option>
								<option value="bestSeller">Best seller</option>
							</select>
						</div>

						<div class="filter-apply">
							<button type="submit">Apply Filters</button>
						</div>
					</form>
				</div>
			</div>
			<div class="content">
				<div class="product-list">
					<%
					if (productList != null) {
						for (Product product : productList) {
					%>
					<c:url var="viewLink" value="ViewProduct">
						<c:param name="productId"
							value="<%=String.valueOf(product.getProductID())%>" />
					</c:url>
					<div class="product-item">
						<a href="${viewLink}"> <%
 String imageSrc = "https://www.svgrepo.com/show/20617/desktop-computer.svg";
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
								Quantity:<%=product.getProductQuantity()%></p> <%
 if (product.getAvgRate() >= 1) {
 %>
							<p>
								Rate:<%=product.getAvgRate()%></p> <%
 }
 %>
						</a>
						<form action="AddToCart" method="post" style="display: inline;">
					<input type="hidden" name="productId" value="<%=product.getProductID()%>" />
					<button type="submit">Add to Cart</button>
				</form>
				<form action="BuyProduct" method="post" style="display: inline;">
					<input type="hidden" name="productId" value="<%=product.getProductID()%>" />
					<button type="submit">Buy Now</button>
				</form>
					</div>
					<%
					}
					}
					%>
					<c:if test="${productList == null || productList.isEmpty()}">
						<p>No products found.</p>
					</c:if>
				</div>

			</div>

		</div>

		<div class="paging-container">

			<!-- Pagination buttons -->
			<form action="ProductSearch" method="get">
				<c:if test="${numPage > 0}">
					<button type="submit" name="numPage" value="${numPage - 1}">Previous</button>
				</c:if>

				<button type="submit" name="numPage" value="${numPage}">${numPage+1}</button>

				<c:if test="${numPage < maxNumPage}">
					<button type="submit" name="numPage" value="${numPage + 1}">Next</button>
				</c:if>
			</form>

			<!--paging  -->
			<form action="ProductSearch" method="get"
				onsubmit="return adjustPageNumber()">
				<label for="numPage">Page:</label> <input type="number" id="numPage"
					name="numPage" value="<%=numPage + 1%>" min="1"
					max="<%=maxNumPage + 1%>" required />
				<button type="submit">Go</button>
			</form>
			<!--format data-->
			<script>
				function adjustPageNumber() {
					const input = document.getElementById('numPage');
					input.value = parseInt(input.value) - 1;
					return true
				}
			</script>
		</div>


		<div class="footer">
			<div class="footer-content">
				<p>&copy; 2023 Your Company. All rights reserved.</p>
				<p>Contact:
			</div>
		</div>
	</f:view>
</body>
</html>
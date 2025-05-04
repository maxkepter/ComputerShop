<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="utils.Validate"%>
<%@page import="Model.SpecProduct"%>
<%@page import="Model.Rating"%>
<%@page import="java.util.List"%>
<%@page import="Model.User"%>
<%@page import="Model.Product"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%
Product product = (Product) request.getAttribute("product");
String brand = (String) request.getAttribute("brandName");
List<Rating> ratingList = (List<Rating>) request.getAttribute("ratingList");
List<SpecProduct> specificationList = product.getSpecProducts();
Boolean isLogged = (Boolean) session.getAttribute("isLogged");
User user = (User) session.getAttribute("user");
%>
<title><%=product.getProductName()%></title>
<link rel="stylesheet" href="./css/navbar.css" />
<link rel="stylesheet" href="./css/footer.css">
<link rel="stylesheet" href="./css/view_product.css">
<style>.rating-item {
	display: flex;
	margin-bottom: 10px;
	width: 100%;
	background-color: #8f8888;
	padding: 10px;
	border-radius: 5px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	margin-bottom: 10px;
  }
  .user-info img {
	width: 50px;
	height: 50px;
	border-radius: 50%;
	margin-right: 10px;
  }
  </style>
		<script>
  // Chờ toàn bộ DOM được load
  document.addEventListener("DOMContentLoaded", function () {
    const mainImage = document.getElementById("mainImage");
    const imageItems = document.querySelectorAll(".image-item img");

    imageItems.forEach(img => {
      img.addEventListener("mouseover", function () {
        mainImage.src = this.src;
      });
    });
  });
</script>
</head>
<body>
	<div class="header">
		<a href="Home"><img id="home-logo"
			src="https://www.svgrepo.com/show/22031/home-icon-silhouette.svg"
			alt="Home" /></a>
		<form action="ProductSearch" method="get">
			<input type="text" name="search" placeholder="Search product..." />
			<button type="submit">Search</button>
		</form>

		<div class="right-header">
			<div class="cart">
				<a href="Cart"><img
					src="https://www.svgrepo.com/show/34974/shopping-cart.svg" alt="" />Cart</a>
			</div>
			<div class="login">
				<%
				if (isLogged != null && isLogged) {
				%>
				<a href="UserProfile" class="user-profile"> <img
					src="https://serg.al/files/image/meme/wojak/stupid/1554519396936.png"
					alt="" />
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

	<div class="content">
		<div class="product-detail">
			<div class="product-image">
				<div class="image-list">
					<%
					List<String> listImage = product.getImageProduct();
					if (listImage != null && !listImage.isEmpty()) {
						for (String image : listImage) {
					%>
					<div class="image-item">
						<img src="<%=image%>" alt="" />
					</div>
					<%
					}
					}
					%>
				</div>
				<div class="main-image">
					<img id="mainImage" src="<%=listImage.get(0)%>" alt="Main Image" />
				</div>
			</div>
			<div class="product-detail-info">
				<h1><%=product.getProductName()%></h1>
				<div class="info">
					<p>
						Brand:
						<%=brand%></p>
					<p>
						Price:
						<%=(int) product.getPrice()%>
						đ
					</p>
					<p>
						Quantity:<%=product.getProductQuantity()%></p>
					<p>
						Sold:<%=product.getQuantitySold()%></p>
					<%
					if (product.getAvgRate() < 1) {
					%>
					<p>
						Rate:<%=product.getAvgRate()%></p>
					<%
					}
					%>
				</div>
				<%
				if (isLogged != null) {
				%>
				<div class="order">
					<button>Buy</button>
					<button>Add to Cart</button>
				</div>
				<%
				} else {
				%>
				<h2 style="color: red;">Login to buy product</h2>

				<%
				}
				%>

			</div>
		</div>

		<div class="product-info">
			<div class="product-description">
				<h2>Product Description</h2>
				<div class="description">
					<p><%=product.getDescription()%></p>
				</div>

			</div>
			<div class="product-specifications">
				<h2>Product Specifications</h2>
				<div class="specifications-detail">
					<%
					if (specificationList != null && !specificationList.isEmpty()) {
						for (SpecProduct specProduct : specificationList) {
					%>
					<div class="specproduct-info">
						<h4><%=specProduct.getSpecName()%>
							:
						</h4>
						<p>
							<%
							Double value = specProduct.getSpecValue();
							String valueString = String.valueOf(value);
							if (value.doubleValue() == value.intValue()) {
								valueString = Integer.toString(value.intValue());
							}
							%>
							<%=valueString%></p>
						<p>
							<%=specProduct.getSpecProduct()%></p>
					</div>
					<%
					}
					}
					%>


				</div>
			</div>
		</div>

		<div class="rating">
			...
			<h2>Rating</h2>

			<%
			if (isLogged != null) {
			%>

			<div class="rating-form">
				<form action="ViewProduct" method="post">
					<input type="hidden" name="command" value="addRating" /> <input
						type="hidden" name="userId" value="<%=user.getUserID()%>" /> <input
						type="hidden" name="productId" value="<%=product.getProductID()%>" />
					<div class="rate">
						Rate this product <input type="number" name="rating" min="1"
							max="5" required />
					</div>
					<textarea name="comment" placeholder="Leave a comment..."></textarea>
					<button type="submit">Submit</button>
				</form>

			</div>
			<%
			} else {
			%>
			<h2 style="color: red;">Login to rate this product</h2>
			<%
			}
			%>



			<div class="rating-list">
				<h3>Review</h3>
				<div class="rating-items">
					<div class="rating-info">
						<%
						if (ratingList != null && !ratingList.isEmpty()) {
							for (Rating rating : ratingList) {
						%>
						<div class="rating-item">
							<div class="user-info">
								<img
									src="https://serg.al/files/image/meme/wojak/stupid/1554519396936.png"
									alt="" />
								<p><%=rating.getUserName()%></p>
							</div>
							<div class="rating-info">
								<p>
									<strong>Rate:</strong>
									<%=rating.getRating()%></p>
								<p>
									<strong>Comment:</strong>
									<%=rating.getComment()%></p>
							</div>
						</div>
						<%
						}
						} else {
						out.print("<h1>No comment </h1>");
						}
						%>
						<%

						%>
					</div>
				</div>

			</div>
		</div>

		<div class="related-products">
			<h2>Related Products</h2>
			Display related products here
		</div>
	</div>

	<div class="footer">
		<div class="footer-content">
			<p>&copy; 2023 Your Company. All rights reserved.</p>
			<p>Contact:
		</div>
</body>
</html>

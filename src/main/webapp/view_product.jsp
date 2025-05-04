<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="utils.DataSourceProvider"%>
<%@page import="dao.OrderDao"%>
<%@page import="dao.RatingDao"%>
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
OrderDao orderDao = new OrderDao(DataSourceProvider.getDataSource());
Rating userRating = null;
boolean hasUserPurchased = false;
if (isLogged != null && isLogged && user != null) {
	userRating = RatingDao.getRatingByUserId(ratingList, user.getUserID());
	hasUserPurchased = orderDao.hasUserPurchasedProduct(user.getUserID(), product.getProductID());
}
%>
<title><%=product.getProductName()%></title>
<link rel="stylesheet" href="./css/navbar.css" />
<link rel="stylesheet" href="./css/footer.css">
	<link rel="stylesheet" href="./css/viewProduct.css">
		<style>

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

	<%
	if (isLogged != null) {
	%>
	<div class="model-overlay" id="buyModel" style="display: none;">
		<div class="modal-content">
			<button class="delete-btn" onclick="closePopup()">x</button>
			<div class="modal-body">
				<h2>Buy Product</h2>
				<p>
					Product Name:
					<%=product.getProductName()%></p>
				<p>
					Price:
					<%=product.getPrice()%>
					đ
				</p>
				<p>Quantity:</p>

				<form action="ViewProduct" method="post">
					<input type="hidden" name="command" value="buyProduct" /> <input
						id="quantityInput" type="number" name="quantity" min="1"
						max="<%=product.getProductQuantity()%>" /> <input type="hidden"
						name="userId" value="<%=user.getUserID()%>" /> <input
						type="hidden" name="productId" value="<%=product.getProductID()%>" />
					<div>
						<strong>Total: </strong>
						<p id="total-amount" style="display: inline;"></p>
					</div>
					<button type="submit">Confirm</button>
				</form>

			</div>
		</div>
	</div>
	<%
	}
	%>

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
					<p>
						Rate:
						<%=product.getAvgRate() > 0 ? product.getAvgRate() : "No ratings yet"%></p>
				</div>
				<%
				if (isLogged != null) {
					if (product.getProductQuantity() <= 0) {
				%>
				<h2 style="color: red;">Product sold off</h2>
				<%
				} else {
				%>
				<div class="order">
					<button onclick="openPopup()">Buy</button>
					<form action="ViewProduct">
						<button type="submit">Add to Cart</button>
					</form>
				</div>
				<%
				}
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

			<h2>Rating</h2>

			<%
			if (isLogged != null) {
				if (userRating != null) {
			%>
			<div class="user-rating">
				<form action="ViewProduct" method="post" style="position: relative;">
					<div class="rating-item">
						<input type="hidden" name="command" value="deleteRating" /> <input
							type="hidden" name="userId" value="<%=user.getUserID()%>" /> <input
							type="hidden" name="productId"
							value="<%=product.getProductID()%>" />
						<button type="submit" class="delete-btn">x</button>

						<div class="user-info">
							<img
								src="https://serg.al/files/image/meme/wojak/stupid/1554519396936.png"
								alt="" /> <strong><%=userRating.getUserName()%></strong>
						</div>

						<div class="rating-info">
							<p>
								<strong>Rate:</strong>
								<%=userRating.getRating()%></p>
							<p>
								<strong>Comment:</strong>
								<%=userRating.getComment()%></p>
						</div>

					</div>
				</form>
			</div>

			<%
			} else if (hasUserPurchased) {
			%>
			<div class="rating-form">
				<form action="ViewProduct" method="post">
					<input type="hidden" name="command" value="addRating" /> <input
						type="hidden" name="userId" value="<%=user.getUserID()%>" /> <input
						type="hidden" name="productId" value="<%=product.getProductID()%>" />
					<div class="rate">
						Rate this product: <input type="number" name="rating" min="1"
							max="5" required />
					</div>
					<textarea name="comment" placeholder="Leave a comment..."></textarea>
					<button type="submit">Submit</button>
				</form>
			</div>
			<%
			} else {
			%>
			<h2 style="color: red;">Buy product to rate</h2>
			<%
			}
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
									alt="" /> <strong><%=rating.getUserName()%></strong>
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

		<script>
const inputQuantity = document.getElementById("quantityInput");
inputQuantity.addEventListener("input", function () {
	const totalAmountElement = document.getElementById("total-amount");
	const price = <%=product.getPrice()%>;
	const value = parseInt(inputQuantity.value);
	totalAmountElement.innerText = value * price +"đ";
  });

  function openPopup() {
    document.getElementById("buyModel").style.display = "block";
  }

  function closePopup() {
    document.getElementById("buyModel").style.display = "none";
  }
</script>
</body>
</html>

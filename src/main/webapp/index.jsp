<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="Model.Product"%>
<%@page import="java.util.List"%>
<%@page import="Model.User"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ page language="java" contentType="text/html;
charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Home</title>
<link rel="stylesheet" href="./css/navbar.css" />
<link rel="stylesheet" href="./css/footer.css">
<link rel="stylesheet" href="./css/home.css" />
<style>.product-list {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
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
}</style>
	<%
	List<Product> listProduct1 = (List<Product>) request.getAttribute("listProduct1");
	List<Product> listProduct2 = (List<Product>) request.getAttribute("listProduct2");
	List<Product> listProduct3 = (List<Product>) request.getAttribute("listProduct3");
	List<Product> listProduct4 = (List<Product>) request.getAttribute("listProduct4");
	List<Product> listProduct5 = (List<Product>) request.getAttribute("listProduct5");
	List<Product> listProduct6 = (List<Product>) request.getAttribute("listProduct6");
	%>
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
		<div class="header-content">
			<div id="sidebar">
				<div class="menu-item" data-value="Laptop">
					<a href="ProductSearch?productBrand=1">Laptop</a>
				</div>
				<div class="menu-item" data-value="Sản phẩm Apple">
					<a href="ProductSearch?productBrand=2">Sản phẩm Apple</a>
				</div>
				<div class="menu-item" data-value="Điện máy">
					<a href="ProductSearch?productBrand=3">Điện máy</a>
				</div>
				<div class="menu-item" data-value="Điện gia dụng">
					<a href="ProductSearch?productBrand=4">Điện gia dụng</a>
				</div>
				<div class="menu-item" data-value="PC - Máy tính bàn">
					<a href="ProductSearch?productBrand=5">PC - Máy tính bàn</a>
				</div>
				<div class="menu-item" data-value="Màn hình máy tính">
					<a href="ProductSearch?productBrand=6">Màn hình máy tính</a>
				</div>
				<div class="menu-item" data-value="Linh kiện máy tính">
					<a href="ProductSearch?productBrand=7">Linh kiện máy tính</a>
				</div>
				<div class="menu-item" data-value="Phụ kiện máy tính">
					<a href="ProductSearch?productBrand=8">Phụ kiện máy tính</a>
				</div>
				<div class="menu-item" data-value="Gaming Gear">
					<a href="ProductSearch?productBrand=9">Gaming Gear</a>
				</div>
				<div class="menu-item" data-value="Điện thoại & Phụ kiện">
					<a href="ProductSearch?productBrand=10">Điện thoại & Phụ kiện</a>
				</div>
			</div>
			<img
				src="https://thumbs.dreamstime.com/z/computer-part-vector-banner-design-concept-flat-style-thin-line-art-icons-white-background-88310754.jpg?ct=jpeg"
				alt="banner" .class="banner"
				style="width: 100%; height: auto; margin-top: 20px;">
		</div>

		<div class="main-content">
			<div>
				<h2>PC</h2>
				<div class="product-list">
					<%
					if (listProduct1 != null) {
						for (Product product : listProduct1) {
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

						<%
						if (product.getProductQuantity() > 0) {
						%>
						<form action="ProductSearch" method="post"
							style="display: inline;">
							<input type="hidden" name="command" value="addToCart" /> <input
								type="hidden" name="productId"
								value="<%=product.getProductID()%>" />

							<button type="submit" class="requires-login">Add to
								Cart</button>
						</form>
						<%
						} else {
						%>
						<strong style="color: red;">Product sold off</strong>
						<%
						}
						%>
					</div>
					<%
					}
					}
					%>
					<c:if test="${listProduct1 == null || listProduct1.isEmpty()}">
						<strong>No products found.</strong>
					</c:if>
				</div>
			</div>
			<div>
				<h2>Laptop</h2>
				<div class="product-list">
					<%
					if (listProduct2 != null) {
						for (Product product : listProduct2) {
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

						<%
						if (product.getProductQuantity() > 0) {
						%>
						<form action="ProductSearch" method="post"
							style="display: inline;">
							<input type="hidden" name="command" value="addToCart" /> <input
								type="hidden" name="productId"
								value="<%=product.getProductID()%>" />

							<button type="submit" class="requires-login">Add to
								Cart</button>
						</form>
						<%
						} else {
						%>
						<strong style="color: red;">Product sold off</strong>
						<%
						}
						%>
					</div>
					<%
					}
					}
					%>
					<c:if test="${listProduct2 == null || listProduct2.isEmpty()}">
						<strong>No products found.</strong>
					</c:if>
				</div>
			</div>
			<div>
				<h2>Apple</h2>
				<div class="product-list">
					<%
					if (listProduct3 != null) {
						for (Product product : listProduct3) {
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

						<%
						if (product.getProductQuantity() > 0) {
						%>
						<form action="ProductSearch" method="post"
							style="display: inline;">
							<input type="hidden" name="command" value="addToCart" /> <input
								type="hidden" name="productId"
								value="<%=product.getProductID()%>" />

							<button type="submit" class="requires-login">Add to
								Cart</button>
						</form>
						<%
						} else {
						%>
						<strong style="color: red;">Product sold off</strong>
						<%
						}
						%>
					</div>
					<%
					}
					}
					%>
					<c:if test="${listProduct3 == null || listProduct3.isEmpty()}">
						<strong>No products found.</strong>
					</c:if>
				</div>
			</div>
			<div>
				<h2>Linh kiện</h2>
				<div class="product-list">
					<%
					if (listProduct4 != null) {
						for (Product product : listProduct4) {
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

						<%
						if (product.getProductQuantity() > 0) {
						%>
						<form action="ProductSearch" method="post"
							style="display: inline;">
							<input type="hidden" name="command" value="addToCart" /> <input
								type="hidden" name="productId"
								value="<%=product.getProductID()%>" />

							<button type="submit" class="requires-login">Add to
								Cart</button>
						</form>
						<%
						} else {
						%>
						<strong style="color: red;">Product sold off</strong>
						<%
						}
						%>
					</div>
					<%
					}
					}
					%>
					<c:if test="${listProduct4 == null || listProduct4.isEmpty()}">
						<strong>No products found.</strong>
					</c:if>
				</div>
			</div>
			<div>
				<h2>Màn hình</h2>
				<div class="product-list">
					<%
					if (listProduct5 != null) {
						for (Product product : listProduct5) {
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

						<%
						if (product.getProductQuantity() > 0) {
						%>
						<form action="ProductSearch" method="post"
							style="display: inline;">
							<input type="hidden" name="command" value="addToCart" /> <input
								type="hidden" name="productId"
								value="<%=product.getProductID()%>" />

							<button type="submit" class="requires-login">Add to
								Cart</button>
						</form>
						<%
						} else {
						%>
						<strong style="color: red;">Product sold off</strong>
						<%
						}
						%>
					</div>
					<%
					}
					}
					%>
					<c:if test="${listProduct5 == null || listProduct5.isEmpty()}">
						<strong>No products found.</strong>
					</c:if>
				</div>
			</div>
			<div>
				<h2>Gaming gear</h2>
				<div class="product-list">
					<%
					if (listProduct6 != null) {
						for (Product product : listProduct6) {
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

						<%
						if (product.getProductQuantity() > 0) {
						%>
						<form action="ProductSearch" method="post"
							style="display: inline;">
							<input type="hidden" name="command" value="addToCart" /> <input
								type="hidden" name="productId"
								value="<%=product.getProductID()%>" />

							<button type="submit" class="requires-login">Add to
								Cart</button>
						</form>
						<%
						} else {
						%>
						<strong style="color: red;">Product sold off</strong>
						<%
						}
						%>
					</div>
					<%
					}
					}
					%>
					<c:if test="${listProduct6 == null || listProduct6.isEmpty()}">
						<strong>No products found.</strong>
					</c:if>
				</div>
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

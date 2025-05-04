<?xml version="1.0" encoding="UTF-8" ?>
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
					<a href="#">Laptop</a>
				</div>
				<div class="menu-item" data-value="Sản phẩm Apple">
					<a href="#">Sản phẩm Apple</a>
				</div>
				<div class="menu-item" data-value="Điện máy">
					<a href="#">Điện máy</a>
				</div>
				<div class="menu-item" data-value="Điện gia dụng">
					<a href="#">Điện gia dụng</a>
				</div>
				<div class="menu-item" data-value="PC - Máy tính bàn">
					<a href="#">PC - Máy tính bàn</a>
				</div>
				<div class="menu-item" data-value="Màn hình máy tính">
					<a href="#">Màn hình máy tính</a>
				</div>
				<div class="menu-item" data-value="Linh kiện máy tính">
					<a href="#">Linh kiện máy tính</a>
				</div>
				<div class="menu-item" data-value="Phụ kiện máy tính">
					<a href="#">Phụ kiện máy tính</a>
				</div>
				<div class="menu-item" data-value="Gaming Gear">
					<a href="#">Gaming Gear</a>
				</div>
				<div class="menu-item" data-value="Điện thoại & Phụ kiện">
					<a href="#">Điện thoại & Phụ kiện</a>
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
			</div>
			<div>
				<h2>Laptop</h2>
			</div>
			<div>
				<h2>Apple</h2>
			</div>
			<div>
				<h2>Linh kiện</h2>
			</div>
			<div>
				<h2>Màn hình</h2>
			</div>
			<div>
				<h2>Gaming gear</h2>
			</div>
			<div>
				<h2>Top seller</h2>
			</div>
			<p>This is home</p>
			<a href="DBConnection">Test kết nối DB</a>
			<div class="footer">
				<div class="footer-content">
					<p>&copy; 2023 Your Company. All rights reserved.</p>
					<p>Contact:
				</div>
			</div>
	</f:view>
</body>
</html>

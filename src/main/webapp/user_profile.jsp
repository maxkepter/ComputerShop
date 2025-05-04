<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="utils.ResponseUtils"%>
<%@page import="Model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>User Profile</title>
<%
Boolean isLogged = (Boolean) session.getAttribute("isLogged");
User user = (User) session.getAttribute("user");
%>
<link rel="stylesheet" href="./css/navbar.css">
<link rel="stylesheet" href="./css/user_profile.css">
	<link rel="stylesheet" href="./css/footer.css">
</head>
<body>
	<f:view>
		<%
		if (isLogged == null || !isLogged) {
			ResponseUtils.evict(response);
		} else {
		%>

		<div class="header">
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
		<div class="main-content">
			<div class="sidebar">
				<a href="">User Profile</a> <a href="">View Order History</a> <a
					href="">Change Password</a> <a href="Logout">Logout</a>
			</div>
			<div class="content">
				<div class="view-profile"><h1>User Profile</h1>
					<p>
						<strong>Username:</strong>
						<%=user.getUserName()%></p>
						<p><strong>First name:</strong>
						<%=user.getFirstName() %>
						</p>
						<p><strong>Last name:</strong><%=user.getLastName()%>
						</p>
					<p>
						<strong>Email:</strong>
						<%=user.getEmail()%></p>
					<p>
						<strong>Phone:</strong>
						<%=user.getPhoneNumber()%></p>
					<p>
						<strong>Address:</strong>
						<%=user.getAddress()%></p></div>
				
			<div class="update-profile">
				<h1>Update Profile</h1>
					<form action="UpdateProfile" method="post">
						<div class="form-group">
							<label for="firstName">First Name:</label>
							<input type="text" id="firstName" name="firstName" value="<%=user.getFirstName()%>" />
						</div>
						<div class="form-group">
							<label for="lastName">Last Name:</label>
							<input type="text" id="lastName" name="lastName" value="<%=user.getLastName()%>" />
						</div>
						<div class="form-group">
							<label for="email">Email:</label>
							<input type="text" id="email" name="email" value="<%=user.getEmail()%>" />
						</div>
						<div class="form-group">
							<label for="phone">Phone:</label>
							<input type="text" id="phone" name="phone" value="<%=user.getPhoneNumber()%>" />
						</div>
						<div class="form-group">
							<label for="address">Address:</label>
							<input type="text" id="address" name="address" value="<%=user.getAddress()%>" />
						</div>
						<button type="submit">Update</button>
					</form>
				</div>
			</div>
			</div>
			</div>
		
		<div class="footer">
			<div class="footer-content">
				<p>&copy; 2023 Your Company. All rights reserved.</p>
				<p>Contact:
			</div>
		</div>
		<%
		}
		%>
	</f:view>
</body>
</html>
<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="Model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Change Password</title>
<%
Boolean isLogged = (Boolean) session.getAttribute("isLogged");
User user = (User) session.getAttribute("user");
%>
<link rel="stylesheet" href="./css/navbar.css">
<link rel="stylesheet" href="./css/change_password.css">
	<link rel="stylesheet" href="./css/footer.css">
</head>
<body>
	<f:view>
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
		<div class="content">
			<div class="sidebar">
				<a href="UserProfile">User Profile</a> <a href="OrderHistory">View
					Order History</a> <a href="ChangePassword">Change Password</a> <a
					href="Logout">Logout</a>
			</div>
			<div class="main-content">
				<h1>Change Password</h1>
				<form action="ChangePassword" method="post">
					<div class="form-group">
						<label for="newPassword">New Password:</label>
						<input type="password" id="newPassword" name="newPassword" required />
					</div>
					<div class="form-group">
						<label for="confirmPassword">Confirm New Password:</label>
						<input type="password" id="confirmPassword" name="confirmPassword" required />
					</div>
					<div class="form-group">
						<button type="submit" id="submitChange">Update</button>
					</div>
				</form>

			</div>
		</div>
		</div>

		<div class="footer">
			<div class="footer-content">
				<p>&copy; 2023 Your Company. All rights reserved.</p>
				<p>Contact:
			</div>
		</div>

		<script>
const newPassword = document.getElementById("newPassword");
const confirmPassword = document.getElementById("confirmPassword");
const submitChange = document.getElementById("submitChange");
submitChange.addEventListener("click", function(event) {
	if (newPassword.value !== confirmPassword.value) {
		event.preventDefault(); // Prevent form submission
		alert("Passwords do not match!");
	}});


		</script>
	</f:view>
</body>
</html>
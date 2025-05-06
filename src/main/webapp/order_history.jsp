<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="Model.OrderDetail"%>
<%@page import="Model.Order"%>
<%@page import="java.util.List"%>
<%@page import="Model.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Order History</title>
<%
Boolean isLogged = (Boolean) session.getAttribute("isLogged");
User user = (User) session.getAttribute("user");
List<Order> orderlist = (List<Order>) request.getAttribute("orderList");
%>
<link rel="stylesheet" href="./css/navbar.css">
<link rel="stylesheet" href="./css/order_history.css">
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
		<div class="main-content">
			<div class="sidebar">
				<a href="UserProfile">User Profile</a> <a href="OrderHistory">View
					Order History</a> <a href="ChangePassword">Change Password</a> <a
					href="Logout">Logout</a>
			</div>
			<div class="content">
				<div class="order-list">


					<h2>Order History</h2>
					<%
					if (orderlist != null && !orderlist.isEmpty()) {
						for (Order order : orderlist) {
							List<OrderDetail> orderDetails = order.getOrderDetails();
					%>

					<div class="order-item">
						<p>
							Order ID:
							<%=order.getOrderID()%></p>
						<p>
							Order Date:
							<%=order.getOrderDate()%></p>
						<p>
							Total Amount:
							<%=order.getTotalAmount()%></p>
						<details> <summary>
							<p>Order Details</p>	
						</summary>
						<div class="orderDetail-list">
							<%
							for (OrderDetail orderDetail : orderDetails) {
							%>
							<div class="orderDetail-item">
								<c:url var="viewLink" value="ViewProduct">
									<c:param name="productId"
										value="<%=String.valueOf(orderDetail.getProductID())%>" />
								</c:url>
								<a href="${viewLink}">
									Product Name:
									<%=orderDetail.getProductName()%></a>
								
								<p>
									Quantity:
									<%=orderDetail.getOrderQuantity()%></p>
								<p>
									Price:
									<%=(int)orderDetail.getAmount()%> đ</p>
							</div>
							<%
							}
							%>				
						</div>
						
						 </details>

					</div>

					<%
					}
					}
					%>
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
	</f:view>
</body>
</html>
<%@page import="Model.Product"%>
<%@page import="dao.CartDao"%>
<%@page import="controller.user.Cart"%>
<%@page import="java.util.List"%>
<%@page import="utils.ResponseUtils"%>
<%@page import="Model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cart</title>
<%
Boolean isLogged = (Boolean) session.getAttribute("isLogged");
User user = (User) session.getAttribute("user");
String message = (String) request.getAttribute("message");
List<Product> cartItems = (List<Product>) request.getAttribute("cartItems");
%>
<link rel="stylesheet" href="./css/navbar.css">
<link rel="stylesheet" href="./css/cart.css">
<link rel="stylesheet" href="./css/footer.css">
</head>
<body>
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
					<p><%=user.getUserName()%></p>
				</a>
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
		<div class="cart-list">
			<h2>Your Cart</h2>
			<%
			if (message != null) {
			%>
			<p style="color: green;"><%=message%></p>
			<%
			}
			%>

			<%
			if (cartItems.isEmpty()) {
			%>
			<p>Your cart is empty.</p>
			<%
			} else {
			%>
			<form action="Cart" method="post" id="cartForm">
				<label><input type="checkbox" id="selectAll" /> Select All</label><br />
				<%
				for (Product item : cartItems) {
					boolean outOfStock = item.getProductQuantity() == 0;
				%>
				<div class="cart-item">
					<input type="checkbox" class="item-checkbox"
						data-price="<%=item.getPrice()%>"
						data-id="<%=item.getProductID()%>" name="selectedItems"
						value="<%=item.getProductID()%>"
						<%=outOfStock ? "disabled" : ""%> /> <a
						href="ViewProduct?productId=<%=item.getProductID()%>"
						class="product-link" style="display: flex; gap: 10px;"> <img
						src="<%=item.getImageProduct().get(0)%>" alt="product Image">
						<div class="product-info">
							<p><%=item.getProductName()%></p>
							<p>
								Price:
								<%=(int) item.getPrice()%></p>
							<p>
								Quantity:
								<%=item.getProductQuantity()%></p>
						</div>
					</a> <input type="hidden" name="productId_<%=item.getProductID()%>"
						value="<%=item.getProductID()%>"> <label>Quantity:</label>
					<%
					if (!outOfStock) {
					%>
					<input type="number" class="quantity-input"
						name="quantity_<%=item.getProductID()%>" min="1"
						max="<%=item.getProductQuantity()%>"
						value="1"
						data-id="<%=item.getProductID()%>" required>
					<%
					} else {
					%>
					<span style="color: red; font-weight: bold;">Out of stock</span>
					<%
					}
					%>

					<a
						href="RemoveCartItem?productId=<%=item.getProductID()%>&command=removeCartItem">Remove</a>
				</div>
				<%
				}
				%>

				<input type="hidden" name="command" value="order" />
				<button type="submit"
					onclick="return confirm('Are you sure you want to place this order?')" class="order-btn">Order</button>
			</form>
			<%
			}
			%>
		</div>

		<div class="cart-summary">
			<h2>Cart Summary</h2>
			<p>
				Total Price: <span id="totalPrice">0</span>
			</p>
		</div>
	</div>

	<div class="footer">
		<div class="footer-content">
			<p>&copy; 2023 Your Company. All rights reserved.</p>
			<p>Contact: ...</p>
		</div>
	</div>

	<script>
		// Tính tổng tiền
		function calculateTotal() {
			let checkboxes = document.querySelectorAll(".item-checkbox");
			let total = 0;
			checkboxes.forEach(function (cb) {
				if (cb.checked) {
					let price = parseFloat(cb.getAttribute("data-price"));
					let id = cb.getAttribute("data-id");
					let quantityInput = document.querySelector('input[name="quantity_' + id + '"]');
					let quantity = parseInt(quantityInput.value);
					total += price * quantity;
				}
			});
			document.getElementById("totalPrice").innerText = Math.floor(total);
		}

		// Gắn sự kiện
		document.querySelectorAll(".item-checkbox").forEach(cb => {
			cb.addEventListener("change", calculateTotal);
		});
		document.querySelectorAll(".quantity-input").forEach(input => {
			input.addEventListener("input", calculateTotal);
		});
		document.getElementById("selectAll").addEventListener("change", function () {
			let checked = this.checked;
			document.querySelectorAll(".item-checkbox").forEach(cb => {
				cb.checked = checked;
			});
			calculateTotal();
		});

		// Gọi lần đầu khi trang load
		window.addEventListener("load", calculateTotal);
	</script>
	<%
	}
	%>
</body>
</html>

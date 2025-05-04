<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="./css/login.css" />
<title>Login | Register</title>
<%
Integer loginStatus = (Integer) request.getAttribute("loginStatus");
String loginInfo = "";
if (loginStatus != null) {
	if (loginStatus == 1) {
		loginInfo = "Wrong password";
	} else if (loginStatus == 2) {
		loginInfo = "Username not found";
	}
}
%>
<%
String userName = (String) request.getAttribute("userName");
%>
<%
String registerFirstName = (String) request.getAttribute("registerFirstName");
String registerLastName = (String) request.getAttribute("registerLastName");
String registerEmail = (String) request.getAttribute("registerEmail");
String registerPhone = (String) request.getAttribute("registerPhone");
String registerAddress = (String) request.getAttribute("registerAddress");
Integer registerStatus = (Integer) request.getAttribute("registerStatus");
%>
</head>
<body>
	<div class="container" id="container">
		<!-- Login Form -->
		<div class="form-container sign-in-container">
			<form action="Login" method="post">
				<h1>Sign In</h1>
				<input type="hidden" name="command" value="login" />
				<div class="infield">
					<label>User Name</label> <input type="text" name="userName"
						value="<%=userName != null ? userName : ""%>" required /> <i
						class="fas fa-user"></i>
				</div>
				<div class="infield">
					<label>Password</label> <input type="password" name="password"
						required /> <i class="fas fa-lock"></i>
				</div>
				<input type="submit" value="Sign In" class="save" />
				<%
				if (loginStatus != null) {
				%>
				<p class="error"><%=loginInfo%></p>
				<%
				}
				%>
			</form>
		</div>
		<!-- Register Form -->
		<div class="form-container sign-up-container">
			<form action="Register" method="post">
				<h1>Create Account</h1>
				<div class="social-container">
					<a href="#" class="social"><i class="fab fa-facebook-f"></i></a> <a
						href="#" class="social"><i class="fab fa-google-plus-g"></i></a> <a
						href="#" class="social"><i class="fab fa-linkedin-in"></i></a>
				</div>
				<span>Or use your email for registration</span>
				<div class="infield">
					<label>User Name</label> <input type="text" name="userName"
						value="" required /> <i class="fas fa-user"></i>
				</div>
				<div class="infield">
					<label>First Name</label> <input type="text" name="firstName"
						value="<%=registerFirstName != null ? registerFirstName : ""%>"
						required /> <i class="fas fa-user"></i>
				</div>
				<div class="infield">
					<label>Last Name</label> <input type="text" name="lastName"
						value="<%=registerLastName != null ? registerLastName : ""%>"
						required /> <i class="fas fa-user"></i>
				</div>
				<div class="infield">
					<label>Email</label> <input type="email" name="email"
						value="<%=registerEmail != null ? registerEmail : ""%>" required />
					<i class="fas fa-envelope"></i>
				</div>
				<div class="infield">
					<label>Phone</label> <input type="text" name="phone"
						value="<%=registerPhone != null ? registerPhone : ""%>"
						pattern="^[0-9]{10}$" title="Phone number must be 10 digits"
						required /> <i class="fas fa-phone"></i>
				</div>
				<div class="infield">
					<label>Address</label> <input type="text" name="address"
						value="<%=registerAddress != null ? registerAddress : ""%>"
						required /> <i class="fas fa-map-marker-alt"></i>
				</div>
				<div class="infield">
					<label>Password</label> <input type="password" name="password"
						required /> <i class="fas fa-lock"></i>
				</div>
				<div class="infield">
					<label>Confirm Password</label> <input type="password"
						name="confirmPassword" required /> <i class="fas fa-lock"></i>
				</div>
				<input type="submit" value="Sign Up" />
				<%
				if (registerStatus != null) {
				%>
				<p class="error">User name is duplicated</p>
				<%
				}
				%>
			</form>
		</div>

		<!-- Overlay Panel -->
		<div class="overlay-container" id="overlayCon">
			<div class="overlay">
				<div class="overlay-panel overlay-left">
					<h1>Welcome Back!</h1>
					<p>To keep connected with us please login with your personal
						info</p>
					<button id="signIn">Sign In</button>
				</div>
				<div class="overlay-panel overlay-right">
					<h1>Hello, Friend!</h1>
					<p>Enter your personal details and start journey with us</p>
					<button id="signUp">Sign Up</button>
				</div>
			</div>
		</div>
	</div>

	<script>
      // Slider toggle functionality
      const container = document.querySelector("#container");
      const signInButton = document.querySelector("#signIn");
      const signUpButton = document.querySelector("#signUp");

      signUpButton.addEventListener("click", () => {
        container.classList.add("right-panel-active");
      });

      signInButton.addEventListener("click", () => {
        container.classList.remove("right-panel-active");
      });

      // Register form validation
      document
        .querySelector('form[action="RegisterServlet"]')
        .addEventListener("submit", function (e) {
          e.preventDefault();
          const userName = this.querySelector(
            'input[name="userName"]'
          ).value.trim();
          const firstName = this.querySelector(
            'input[name="firstName"]'
          ).value.trim();
          const lastName = this.querySelector(
            'input[name="lastName"]'
          ).value.trim();
          const email = this.querySelector('input[name="email"]').value.trim();
          const phone = this.querySelector('input[name="phone"]').value.trim();
          const address = this.querySelector(
            'input[name="address"]'
          ).value.trim();
          const password = this.querySelector('input[name="password"]').value;
          const confirmPassword = this.querySelector(
            'input[name="confirmPassword"]'
          ).value;

          let errors = [];

          if (!userName) errors.push("Username is required");
          if (!firstName) errors.push("First Name is required");
          if (!lastName) errors.push("Last Name is required");
          if (!email) errors.push("Email is required");
          if (!phone) errors.push("Phone is required");
          if (!address) errors.push("Address is required");
          if (!password) errors.push("Password is required");
          if (!confirmPassword) errors.push("Confirm Password is required");

          const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
          if (email && !emailRegex.test(email)) {
            errors.push("Invalid email format");
          }

          const phoneRegex = /^\d{10}$/;
          if (phone && !phoneRegex.test(phone)) {
            errors.push("Phone must be a 10-digit number");
          }

          if (password !== confirmPassword) {
            errors.push("Passwords do not match");
          }

          const existingError = this.querySelector(".error");
          if (existingError) existingError.remove();

          if (errors.length > 0) {
            const errorP = document.createElement("p");
            errorP.className = "error";
            errorP.textContent = errors[0];
            this.appendChild(errorP);
          } else {
            this.submit();
          }
        });
    </script>
</body>
</html>

<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Register</title>
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
	<f:view>
		<p>Register side</p>
		<!--kiem tra nhap day du thong tin bang javascript -->
		<!--se kien tra trung user name bang servlet  -->
		<!--Email,phone kiem tra format-->
		<form action="RegisterServlet" method="post">
			<label>User Name</label> <input type="text" name="userName" value=""
				required /> <label>First Name</label> <input type="text"
				name="firstName"
				value="<%=registerFirstName != null ? registerFirstName : ""%>"
				required /> <label>Last Name</label> <input type="text"
				name="lastName"
				value="<%=registerLastName != null ? registerLastName : ""%>"
				required /> <label>Email</label> <input type="text" name="email"
				value="<%=registerEmail != null ? registerEmail : ""%>" required />

			<label>Phone</label> <input type="text" name="phone"
				value="<%=registerPhone != null ? registerPhone : ""%>" required />

			<label>Address</label> <input type="text" name="address"
				value="<%=registerAddress != null ? registerAddress : ""%>" required />
			<label>Password</label> <input type="password" name="password"
				required /> <label>Confirm Password</label> <input type="password"
				name="confirmPassword" required /> <input type="submit"
				value="Register" />
		</form>
		<%
		if (registerStatus != null) {
		%>
		<p>User name is duplicated</p>
		<%
		}
		%>
	</f:view>
</body>
</html>
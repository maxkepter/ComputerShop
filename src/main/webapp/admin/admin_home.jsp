<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Admin Home</title>
<style>
h1 {
	font-size: 1.5em;
	color: #c22727;
	margin: 0;
	padding: 0;
	text-align: center;
}

.content {
	margin: 0 auto;
	padding: 20px;
	display: flex;
	flex-direction: column;
	gap: 20px;
}

.content a {
	text-decoration: none;
	color: #c22727;
	font-size: 1.2em;
	font-weight: bold;
	padding: 10px;
	border-radius: 5px;
	background-color: #f0f0f0;
	transition: background-color 0.3s, color 0.3s;
}

.content a:hover {
	background-color: #7c3a3a;
	color: #fff;
}
</style>
</head>
<body>
	<f:view>
		<h1>Admin Side</h1>
		<div class="content">
			<a href="Home">User Home</a> <a href="ProductManagement">Product
				Managment</a> <a href="BrandManagement">Brand Managment</a> <a
				href="SpecificationManagment">Specification Managment</a> <a
				href="TypeManagement">Type Managment</a> <a href="">Statistics </a>
		</div>
	</f:view>
</body>
</html>

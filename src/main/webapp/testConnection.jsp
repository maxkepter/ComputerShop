<%@page import="java.util.ArrayList"%>
<%@page import="Model.User"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<title>Test Ket Noi DB</title>
</head>
<body>
	<h2>Ket qua kiem tra ket noi:</h2>
	<p
		style="color: <%= request.getAttribute("message").toString().contains("thanh cong") ? "green" : "red" %>;">
		<%= request.getAttribute("message") %>
	</p>
	<a href="DBConnection">Thu lai</a>
	<%ArrayList<User> list=(ArrayList<User>)request.getAttribute("userList"); 
    for(User user:list){
    	out.print(user.toString());
    	out.print("<br>");
    }
    %>
</body>
</html>

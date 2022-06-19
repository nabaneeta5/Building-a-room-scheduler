<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList;"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Home Page</title>
</head>
<link rel="stylesheet"
	href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>

<script>
	$(document).ready(function() {
		$("#datepicker").datepicker();
	});
</script>
<body>

	<c:choose>
		<c:when test="${user != null }">
		<br>
			<form action="/" method="post">
				<h4>Enter Name of Added Room:</h4>
				<input type="text" autocomplete="off" name="new_room_name" /> 
				<input type="hidden" name="hidenParam" value="add_room_name" /> 
				<input type="submit" value="ADD ROOM" />
			</form>

			<br>
					
				&nbsp; &nbsp;<a href="${signout}"><b>Sign out</b></a>
			<br>
			
		</c:when>
		<c:otherwise>
			<br>
			
				&nbsp; &nbsp; <a href="${signin}"><b>Sign in</b></a>

		</c:otherwise>
	</c:choose>
</body>
</html>
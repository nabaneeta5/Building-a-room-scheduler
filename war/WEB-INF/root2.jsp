<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Booking Page</title>
</head>
<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>

<script>
  $(document).ready(function() {
    $("#datepicker1").datepicker();
    $("#datepicker2").datepicker();
  });
  </script>
<body>

	<c:choose>
		<c:when test="${user != null }">
		
<h4>Add Booking Information for ${room} :</h4>

<form action="/" method="post">
	<input type="hidden" name="hidenParam" value="add_book_info"/>
	<input type="hidden" name="room" value="${room}"/>
				<b>Enter Name of the Booking Information:</b>
				<br>
				<input type="text" autocomplete="off" name="newbook"/>
				<br>
				<b>From:</b>
				<br>
				Date: <input id="datepicker1" autocomplete="off" name="input_from" />
				&nbsp; &nbsp; &nbsp;Time: Hr:<select name="t1_hh">
				<option value="select">select</option>
				<% for(int i=0;i<=11;i++){
					out.println("<option value="+i+">"+i+"</option>");
				}
					%>
				
				</select>
				
				 &nbsp; &nbsp; &nbsp;Min: <select name="t1_mm">
				<option value="select">select</option>
				<% for(int i=0;i<=59;i++){
					out.println("<option value="+i+">"+i+"</option>");
				}
					%>
				
				</select>
				 &nbsp; &nbsp; &nbsp;AM/PM: <select name="t1_fmt">
				<option value="select">select</option>
				<option value="AM">AM</option>
				<option value="PM">PM</option>
				</select>
				<br>
				
				<b>to:</b>
				<br>
				Date: <input id="datepicker2" autocomplete="off" name="input_to" />
				&nbsp; &nbsp; &nbsp;Time: Hr: <select name="t2_hh">
				<option value="select">select</option>
				<% for(int i=0;i<=11;i++){
					out.println("<option value="+i+">"+i+"</option>");
				}
					%>
				
				</select>
				
				&nbsp; &nbsp; &nbsp; Min: <select name="t2_mm">
				<option value="select">select</option>
				<% for(int i=0;i<=59;i++){
					out.println("<option value="+i+">"+i+"</option>");
				}
					%>
				
				</select>
				&nbsp; &nbsp; &nbsp; AM/PM: <select name="t2_fmt">
				<option value="select">select</option>
				<option value="AM">AM</option>
				<option value="PM">PM</option>
				</select>
				<br>
				
				<input type="submit" value="Add"/>
</form>
<br>

			&nbsp; &nbsp;<a href="/"><b>Home Page</b></a>
				
		</c:when>
		<c:otherwise>
		<br>
			
				&nbsp; &nbsp; <a href="${signin}"><b>Sign in</b></a>

			
		</c:otherwise>
	</c:choose>
</body>
</html>
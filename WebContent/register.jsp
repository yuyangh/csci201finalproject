<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Register</title>
<script>
var socket;
function connectToServer() {
	socket = new WebSocket("ws://localhost:8080/csci201finalproject/ws");
}
function sendMessage() {
	socket.send(document.form.fname.value + " is registered.");
	return false;
}
</script>
</head>
<body onload="connectToServer()">
	<form name="form" onsubmit="return sendMessage();">
		<input type="text" name="fname" value="Name" /><br />
		<input type="submit" name="submit" value="Register" /><br />
	</form>
</body>
</html>
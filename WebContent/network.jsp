<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Client</title>
<script>
	var socket;
	function connectToServer() {
		socket = new WebSocket("ws://localhost:8080/csci201finalprojectnew/ws");
		socket.onopen = function(event) {
			document.getElementById("test").innerHTML += "Connected!";
		}
		socket.onmessage = function(event) {
			if (event.data = "James") {
				document.getElementById("test").innerHTML += "<img src=\"fblogin.png\" />" + event.data + "<br />";
			}
		}
		socket.onclose = function(event) {
			document.getElementById("test").innerHTML += "Disconnected!";
		}
	}
</script>
</head>
<body onload="connectToServer()">
	<div id="test"></div>
</body>
</html>
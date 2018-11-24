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
		socket = new WebSocket("ws://localhost:8080/csci201finalproject/ws");
		socket.onopen = function(event) {
			document.getElementById("test").innerHTML += "Connected!";
		}
		socket.onmessage = function(event) {
			document.getElementById("test").innerHTML += event.data + "<br />";
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
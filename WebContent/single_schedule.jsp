<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>ScheduleMe (Single User)</title>
		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
	</head>
	<body>
		<script>
			(function(d, s, id){
				var js, fjs = d.getElementsByTagName(s)[0];
			    if (d.getElementById(id)) {return;}
			    js = d.createElement(s); js.id = id;
			    js.src = "https://connect.facebook.net/en_US/sdk.js";
			    fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));

		  
			window.fbAsyncInit = function() {
			    FB.init({
			      appId: '562234917553248',
			      cookie: true,
			      xfbml: true,
			      version: 'v3.2'
			    });
			    FB.AppEvents.logPageView(); 
		    	FB.getLoginStatus(function(response) {
		    		  if (response.status === 'connected') {
		    		    successfulLogin();
		    		  }
		    	});
			};
		  
			function fbLogin() {
		      FB.login(function (response) {
		          if (response.authResponse) {
		              successfulLogin();
		          } else {
		              //User cancelled login or did not fully authorize
		          }
		      }, {scope: 'email, user_friends'});
		  	}
		  
			// Fetch the user profile data from facebook
		  	function successfulLogin(){
		    	FB.api('/me', {locale: 'en_US', fields: 'id,first_name,last_name,email,link,gender,locale,picture'}, function (response) {
		        	document.getElementById('fbButton').setAttribute("onclick","fbLogout()");
		        	document.getElementById('fbButton').innerHTML = '<img src="fblogout.png"/>';
		        	var userID = response.id;
	    	    	sessionStorage.setItem("userID", userID);
	    	    	//check if sessionUserID == null -> if so -> send to DB
					//TODO: uncomment line below to add user to database 
					//addUniqueUser(userID);
		   		});
		   	}
		  
		  	function fbLogout() {
		    	FB.logout(function() {
		          	document.getElementById('fbButton').setAttribute("onclick","fbLogin()");
		          	document.getElementById('fbButton').innerHTML = '<img src="fblogin.png"/>';
		          	//document.getElementById('userData').innerHTML = '';
		      	});
		   	}
		  	
		    function addUniqueUser(userID) { 
				var xhttp = new XMLHttpRequest();
			   	xhttp.open("POST", "AddUniqueUserServlet?userID="+userID, true);
			   	xhttp.onreadystatechange = function() { 
			   		//console.log(this.responseText);
			   	}
			   	xhttp.send();
		     }
		</script>
		

		
		<div class="container-fluid">
			<div class="row" style="background-color: #E9E9E9; padding: 15px; height:10%">
				<div class="col-md-3">
					<button type="button" class="btn btn-info btn-block" style="height: 90%; margin:auto;">
						Generate Schedules <i class="fas fa-cogs"></i>
					</button>
				</div>
				<div class="col-md-3">
					<button type="button" class="btn btn-primary btn-block" style="height: 90%; margin:auto;">
						Schedule with Friends <i class="fas fa-arrow-right"></i>
					</button>
				</div>
				<div class="col-md-3">
				</div>
				<div class="col-md-2">
					<!-- facebook button -->
					<div style="height:90%"><a href="javascript:void(0);" onclick="fbLogin()" id="fbButton"><img src="fblogin.png" style="border-radius:5px"/></a></div>
				</div>
				<div class="col-md-1">
				</div>
			</div>
			<div class="row" style="background-color: green; height:90%">
				<div class="col-md-6" style="border-right: solid black 2px;">
					next row <br />
					next row
				</div>
				<div class="col-md-6">
					next row <br />
					next row
				</div>
			</div>
			</div>
		</div>
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Login</title>
		<link rel="stylesheet" type="text/css" href="login.css" />
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

		  	//282325632395045 //real appID
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
					//TODO: uncomment line below to add user to database 
					// addUniqueUser(userID);
	    	    	
	    		    //TODO: uncomment line below for normal functionality
	    		    //window.location.href = 'single_schedule.jsp';
		          	//document.getElementById('userData').innerHTML = '<p><b>FB ID:</b> '+response.id+'</p><p><b>Name:</b> '+response.first_name+' '+response.last_name+'</p><p><b>Email:</b> '+response.email+'</p><p><b>Gender:</b> '+response.gender+'</p><p><b>Locale:</b> '+response.locale+'</p><p><b>Picture:</b> <img src="'+response.picture.data.url+'"/></p><p><b>FB Profile:</b> <a target="_blank" href="'+response.link+'">click to view profile</a></p>';
	    	    	//TODO: after app submission
		          	FB.api(
	    	    		    "/me/friends",
	    	    		    function (response) {
	    	    		      if (response && !response.error) {
	    	    		        /* handle the result */
	    	    		      }
	    	    		      console.log("response obj: " + response.data.length);
	    	    		    }
	    	    		);
		    	});
		   	}
		  
		  	function fbLogout() {
		    	FB.logout(function() {
		          	document.getElementById('fbButton').setAttribute("onclick","fbLogin()");
		          	document.getElementById('fbButton').innerHTML = '<img src="fblogin.png"/>';
		          	//document.getElementById('userData').innerHTML = '';
		      	});
		   	}
		  	
		  	function goToNextPage() {
		  		window.location.href = 'single_schedule.jsp';
		  	}
		  	
		  	// DELEGATE: LUZ AddUniqueUserServlet
		  	// - get string userID, add this userID IFF the ID is unique
		  	// - no response needed
		    function addUniqueUser(userID) { 
				var xhttp = new XMLHttpRequest();
			   	xhttp.open("POST", "AddUniqueUserServlet?userID="+userID, true);
			   	xhttp.onreadystatechange = function() { 
			   		//console.log(this.responseText);
			   	}
			   	xhttp.send();
		     }
		</script>
	
	 	<div class="headerBar"></div>
	 	
	 	<div id="title"> ScheduleMe</div>
	 	
	 	<div id="buttons">
	 		<div><button type="button" onclick="goToNextPage();" id="guestButton">Enter as guest!</button></div>
	 		<div><a href="javascript:void(0);" onclick="fbLogin()" id="fbButton"><img src="fblogin.png"/></a></div> 
	 	</div>
	 	
	 	<div class="footer"></div>
	</body>
</html>
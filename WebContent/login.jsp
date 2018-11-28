<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Login</title>
		<link rel="stylesheet" type="text/css" href="login.css" />
		<link href='https://fonts.googleapis.com/css?family=Euphoria Script' rel='stylesheet'>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
	</head>
	
	<body>
		<script>
		//session variables
		//sessionStorage.getItem("userID");
		//sessionStorage.getItem("userName"); 
		//sessionStorage.getItem("userEmail"); 
		//sessionStorage.getItem("userPicURL"); 
		//sessionStorage.getItem("friendList"); //must use below function to get properties: name, id
		//copy and paste to get friendList -> var fl = []; function getFriendListIntoArrayVar(fl)
		
			(function(d, s, id){
				var js, fjs = d.getElementsByTagName(s)[0];
			    if (d.getElementById(id)) {return;}
			    js = d.createElement(s); js.id = id;
			    js.src = "https://connect.facebook.net/en_US/sdk.js";
			    fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));

			window.fbAsyncInit = function() {
			    FB.init({
			      appId: '2169604589968142',
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
		    	FB.api('/me', {locale: 'en_US', fields: 'id, name, first_name,last_name,email,link,gender,locale,picture'}, function (response) {
		        	document.getElementById('fbButton').setAttribute("onclick","fbLogout()");
		        	document.getElementById('fbButton').innerHTML = '<img src="fblogout.png" class="fb-button"/>';
	    	    	sessionStorage.setItem("userID", response.id); 
	    	    	sessionStorage.setItem("userName", response.name);
	    	    	sessionStorage.setItem("userEmail", response.email);
	    	    	sessionStorage.setItem("userPicURL", response.picture.data.url);
	    	    	console.log("login  check: " + response.id + " " + response.name + " " + response.email + " " + response.picture.data.url);
					//TODO: uncomment line below to add user to database 
					addUniqueUser(response.id, response.name, response.email, response.picture.data.url);
	    	    	
	    		    //TODO: uncomment line below for normal functionality
	    		    window.location.href = 'friend_schedule.jsp';
		          	FB.api("/me/friends", function (response) {
	    	    		if (response && !response.error) { //on success
	    	    			storeFriendsInStorage(response);
	    	    		} else { // on error 
	    	    		}
	    	    	});
		    	});
		   	}
			
			//var fl = []
			function getFriendListIntoArrayVar(fl) {
    			var tfl = JSON.parse(sessionStorage.getItem("friendList"));
    			for(i = 0; i < tfl.length; i++) {
    				fl.push(JSON.parse(tfl[i]));
    			}
			}
			
			function storeFriendsInStorage(response) {
				var fl = [];
 				for(i = 0; i < response.data.length; i++) {
 					fl.push(JSON.stringify(response.data[i]));
 				}
    			sessionStorage.setItem("friendList", JSON.stringify(fl));
			}
			
			function printObjectKeyValPairs(obj) {
				Object.getOwnPropertyNames(obj).forEach(
  					  function (val, idx, array) {
  					  	console.log(val + ' -> ' + obj[val]);
  					  }
  				);
			}
		  
		  	function fbLogout() {
		    	FB.logout(function() {
		          	document.getElementById('fbButton').setAttribute("onclick","fbLogin()");
		          	document.getElementById('fbButton').innerHTML = '<img src="fblogin.png" class="fb-button"/>';
		      	});
		   	}
		  	
		  	function goToNextPage() {
		  		window.location.href = 'single_schedule.jsp';
		  	}
		  	
		  	// DELEGATE: LUZ AddUniqueUserServlet
		  	// - get string userID, IFF the ID is unique -> add it + userName, userEmail, userPicURL
		  	// - no response needed
		    function addUniqueUser(userID, userName, userEmail, userPicURL) { 
				var xhttp = new XMLHttpRequest();
			   	xhttp.open("POST", "AddUniqueUserServlet?userID="+userID + "&userName="+userName + "&userEmail="+userEmail + "&userPicURL="+userPicURL, true);
			   	xhttp.onreadystatechange = function() { 
			   		console.log(this.responseText);
			   	}
			   	xhttp.send();
		     }
		</script>
	
	 	<p class="headerBar"></p>
	 	<div class="middle">
		 	<div id="title">ScheduleMe</div>
		 	
		 	<div id="buttons">
		 		<div><button type="button" onclick="goToNextPage();" id="guestButton">Enter as guest</button></div>
		 		<div><a href="javascript:void(0);" onclick="fbLogin()" id="fbButton"><img src="fblogin.png" class="fb-button" /></a></div> 
		 	</div>
		 </div>
	 	
	 	<p class="footer"></p>
	</body>
</html>
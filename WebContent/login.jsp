<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Login</title>
		<link rel="stylesheet" type="text/css" href="login.css" />
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
		var friendsListGlobalVar = [];
    	var userIDGlobalVar;
    	var userNameGlobalVar;
    	var userEmailGlobalVar;
    	var userPicURLGlobalVar;

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
		    	FB.api('/me', {locale: 'en_US', fields: 'id,name,first_name,last_name,email,link,gender,locale,picture'}, function (response) {
		        	document.getElementById('fbButton').setAttribute("onclick","fbLogout()");
		        	document.getElementById('fbButton').innerHTML = '<img src="fblogout.png"/>';
	    	    	sessionStorage.setItem("userID", response.id); 
	    	    	sessionStorage.setItem("userName", response.name);
	    	    	sessionStorage.setItem("userEmail", response.email);
	    	    	sessionStorage.setItem("userPicURL", response.picture.data.url);
	    	    	userIDGlobalVar = response.id;
	    	    	userNameGlobalVar = response.name;	    	    	
	    	    	userEmailGlobalVar = response.email;
	    	    	userPicURLGlobalVar = response.picture.data.url;
	    	    	
					//TODO: uncomment line below to add user to database 
					// addUniqueUser(response.id, response.name, response.email, response.picture.data.url);
	    	    	
	    		    //TODO: uncomment line below for normal functionality
	    		    //window.location.href = 'single_schedule.jsp';
		          	FB.api("/me/friends", function (response) {
	    	    		if (response && !response.error) { //on success
	    	    			storeFriendsInStorage(response);
	    	    			friendsListGlobalVar = response;
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
		          	document.getElementById('fbButton').innerHTML = '<img src="fblogin.png"/>';
		      	});
		   	}
		  	
		  	function goToNextPage() {
		  		window.location.href = 'single_schedule.jsp';
		  	}
		  	
		    function addUniqueUser(userID, userName, userEmail, userPicURL) { 
				var xhttp = new XMLHttpRequest();
			   	xhttp.open("POST", "AddUniqueUserServlet?userID="+userID + "&userName="+userName + "&userEmail="+userEmail + "&userPicURL="+userPicURL, true);
			   	xhttp.onreadystatechange = function() { 
			   		//console.log(this.responseText);
			   	}
			   	xhttp.send();
		     }
		  	
		  	//js code for modal
		  	function deleteTableRows() {
		  		var table = document.getElementById("tableFriendsList");
		  		while(table.rows.length > 0) {
		  		  table.deleteRow(0);
		  		}
		  	}
		  	
		  	function fillHeaderForTable() {
		  		var headerNode = document.getElementById("headerFriendsListTable");
		  		var text = sessionStorage.getItem("userName") + "'s friends in this class:";
		  		headerNode.innerHTML = text;
		  	}
		  	
		  	function populateFriendsListTable(classID) {
				var xhttp = new XMLHttpRequest();
			   	xhttp.open("POST", "GetUsersTakingClassServlet?classID="+classID, true);
			   	xhttp.onreadystatechange = function() { 
			   		//console.log(this.responseText);
			   		//iterate through response (for loop)...nested our friends list...if they match...add to table
			   		if(this.readyState == 4 && this.status == 200) {
			   			var classEnrollmentList = JSON.parse(this.responseText);
			   			var length = classEnrollmentList.length;
			   			if(length == 0 || classEnrollmentList == '[ ]' || classEnrollmentList == null) {
			   				//TODO: have table display no friends in class
				   			return;
			   			}
			   			
			   			//if users in response match our current friends then add them to the table
			   			for (var i = 0; i < length; i++) {
		    				var usernameStudent = classEnrollmentList[i].username;
		    				var userPicURLStudent = classEnrollmentList[i].userPicURL;
		    				for(var j = 0; j < friendsListGlobalVar.data.length; j++) {
		    					var friendName = friendsListGlobalVar.data[j].name;
		    					if(friendName.localeCompare(usernameStudent)) {
		    				  		var table = document.getElementById("tableFriendsList");
		    						var row = table.insertRow(0);
		    						var imageCell = row.insertCell(0);
		    						var nameCell = row.insertCell(1);
		    						var img = document.createElement('img');
		    					    img.src = userPicURLStudent; 
		    						imageCell.appendChild(img);
		    						nameCell.innerHTML = userPicURLStudent;
		    					}
		    				} //end of inner for loop
						} // end of outer for loop
			   		}
			   	} //end of async call
			   	xhttp.send();		  		
			} //end of function call
		  	
			function modalClicked() {
				var modal = document.getElementById('myModal');
			    modal.style.display = "block";
			    fillHeaderForTable();
			    populateFriendsListTable(); //TODO: need to pass in classID into this function...
			    // get it from the dynamic button that was created...when creating that button - give it an id
			    
		    	window.addEventListener("click", function(event){
					var modal = document.getElementById('myModal');
				    if (event.target == modal) {
				        modal.style.display = "none";
				        deleteTableRows();
				    }
		    	});
			}
			
			function spanClicked() {
				var modal = document.getElementById('myModal');
				modal.style.display = "none";
				deleteTableRows();
			}
			//end of js code for modal
		</script>
	
	 	<div class="headerBar"></div>
	 	
	 	<div id="title"> ScheduleMe</div>
	 	
	 	<div id="buttons">
	 		<div><button type="button" onclick="goToNextPage();" id="guestButton">Enter as guest!</button></div>
	 		<div><a href="javascript:void(0);" onclick="fbLogin()" id="fbButton"><img src="fblogin.png"/></a></div> 
	 		<!-- TODO: get rid of the temp button below-->
	 		<div><button id="myBtn" onclick="modalClicked();">View Friends</button></div>
	 	</div>
	 	
	 	<!-- html code for modal -->
	 	<!-- The Modal -->
		<div id="myModal" class="modal">
		  <!-- Modal content -->
		  <div class="modal-content">
		  
		    <div class="modal-header">
		      <span class="close" onclick="spanClicked();">&times;</span>
		      <h2 id="headerFriendsListTable"></h2>
		    </div>
		    
		    <div class="modal-body">
		      <table id="tableFriendsList"></table>
		    </div>
		    
		  </div> <!-- end of modal content -->
		  
		</div> <!-- end of modal -->
	 	<!-- end of html code for modal -->
	 	
	 	
	 	<div class="footer"></div>
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Friend Scheduling</title>
		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
		<link rel="stylesheet" href="single_schedule.css">
		<link href='https://fonts.googleapis.com/css?family=Euphoria Script' rel='stylesheet'>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
  		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
		
		<script>
			var socket;
			var socket;
			function connectToServer() {
				socket = new WebSocket("ws://localhost:8080/csci201finalproject/ws");
				socket.onmessage = function(event) {
					var table = document.getElementById("tableFriendsList");
					var checkUser = false;
					for (var i = 0, row; row = table.rows[i]; i++) {
						if (event.data == row.cells[1]) {
							checkUser = true;
						}
					}
					if (!checkUser) {
						var row = table.insertRow(0);
						var imageCell = row.insertCell(0);
						var nameCell = row.insertCell(1);
						var img = document.createElement('img');
						if (event.data == "James Orme-Rogers") {
							img.src = "James.jpg";
						}
						if (event.data == "Tristan Elma") {
							img.src = "Tristan.jpg";
						}
						if (event.data == "Joel Gutierrez") {
							img.src = "Joel.jpg";
						}
						if (event.data == "Yuyang Huang") {
							img.src = "Yuyang.jpg";
						}
						if (event.data == "Luz Camacho") {
							img.src = "Luz.jpg";
						}
						if (event.data == "Kaushik Tandon") {
							img.src = "Kaushik.jpg";
						}
						img.width = "50";
						img.height = "50";
						imageCell.appendChild(img);
						nameCell.innerHTML = event.data;
					}
				}
			}
			
			function RegisterCoursesJS(){
				var userID = sessionStorage.getItem("userID");
				var userName = sessionStorage.getItem("userName"); 
				var userEmail = sessionStorage.getItem("userEmail"); 
				var userPicURL = sessionStorage.getItem("userPicURL");
				console.log(userID);
				console.log(userName); 
				console.log(userEmail); 
				console.log(userPicURL);
				
				socket.send(userName);
				
				var xhttp2 = new XMLHttpRequest();
				xhttp2.open("GET", "RegisterCourses?userID=" + userID + "&userName=" + userName + "&userEmail=" + userEmail + "&userPicURL=" + userPicURL, true);
				xhttp2.onreadystatechange = function() {
					if(this.readyState == 4 && this.status == 200){
					}
				}
				xhttp2.send();
			}
			
		</script>
	</head>
		<!-- html code for modal -->
	 	<!-- The Modal -->
		<div id="myModal" class="modal">
		<div class="modal-dialog modal-dialog-centered" role="document">
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
		  </div>
		</div> <!-- end of modal -->
	 	<!-- end of html code for modal -->
		
		
	<body onload="connectToServer()">
		<script>
		var friendsListGlobalVar = [];

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
		    	FB.api('/me', {locale: 'en_US', fields: 'id,first_name,last_name,email,link,gender,locale,picture'}, function (response) {
		        	document.getElementById('fbButton').setAttribute("onclick","fbLogout()");
		        	document.getElementById('fbButton').setAttribute("href","login.jsp");
		        	document.getElementById('fbButton').innerHTML = '<img src="fblogout.png" class="fb-button" />';
		        	var userID = response.id;
	    	    	sessionStorage.setItem("userID", userID);
	    	    	//check if sessionUserID == null -> if so -> send to DB
					//TODO: uncomment line below to add user to database 
					addUniqueUser(userID);
					/* console.log(sessionStorage.getItem("userID"));
					console.log(sessionStorage.getItem("userName")); 
					console.log(sessionStorage.getItem("userEmail")); 
					console.log(sessionStorage.getItem("userPicURL"));  */
					FB.api("/me/friends", function (response) {
	    	    		if (response && !response.error) { //on success
	    	    			storeFriendsInStorage(response);
	    	    			friendsListGlobalVar = response;
	    	    		} else { // on error 
	    	    		}
					});
		   		});
		   	}
		  
		  	function fbLogout() {
		    	FB.logout(function() {
		    		FB.Auth.setAuthResponse(null, 'unknown');
		    		document.getElementById('fbButton').setAttribute("onclick","fbLogin()");
		          	document.getElementById('fbButton').innerHTML = '<img src="fblogin.png" class="fb-button" />';
		          	//document.getElementById('userData').innerHTML = '';
		          	window.location.href = 'login.jsp';
		      	});
		   	}
		  	
		    var fl = []
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
		  	
		    function addUniqueUser(userID) { 
				var xhttp = new XMLHttpRequest();
			   	xhttp.open("POST", "AddUniqueUserServlet?userID="+userID, true);
			   	xhttp.onreadystatechange = function() { 
			   		//console.log(this.responseText);
			   	}
			   	xhttp.send();
		     }
		    
		    function sleep(ms) {
		    	  return new Promise(resolve => setTimeout(resolve, ms));
		    }
			
			async function removeGroup(group_num){
				if(group_num != -1){
					//for-loop to remove classes from group one-by-one in reverse order
					var nodelist = document.getElementsByClassName("row h-100 class-row " + group_num);
					var numClassesInGroup = nodelist.length;
					var i;
					for (i = numClassesInGroup - 1; i >= 0; i--) { 
					    justRemoveClass(group_num, i);
					    await sleep(100);
					}
					
					// remove the group from the display
					var xhttp = new XMLHttpRequest();
					xhttp.open("GET", "RemoveInfo?action=group&group_num=" + group_num, true);
					xhttp.onreadystatechange = function() {
						if(this.readyState == 4 && this.status == 200){
							document.getElementById("classes_table").innerHTML = this.responseText;
							updateGeneratedSchedulesOnUI("friend");
						}
					}
					xhttp.send();
				}
				else{
					// remove the group from the display
					var xhttp = new XMLHttpRequest();
					xhttp.open("GET", "RemoveInfo?action=group&group_num=" + group_num, true);
					xhttp.onreadystatechange = function() {
						if(this.readyState == 4 && this.status == 200){
							document.getElementById("classes_table").innerHTML = this.responseText;
						}
					}
					xhttp.send();
				}
			}
			
			function addGroup(){
				var xhttp = new XMLHttpRequest();
				xhttp.open("GET", "AddInfo?action=group", true);
				xhttp.onreadystatechange = function() {
					if(this.readyState == 4 && this.status == 200){
						document.getElementById("classes_table").innerHTML = this.responseText;
					}
				}
				xhttp.send();
			}
			
			function addClass(group_num){
				var xhttp = new XMLHttpRequest();
				xhttp.open("GET", "AddInfo?action=class&department=" + document.getElementById("department_input" + group_num).value + "&number=" + document.getElementById("number_input" + group_num).value + "&group_num=" + group_num, true);
				xhttp.onreadystatechange = function() {
					if(this.readyState == 4 && this.status == 200){
						document.getElementById("classes_table").innerHTML = this.responseText;
						updateGeneratedSchedulesOnUI("friend");
					}
				}
				xhttp.send();
			}
			
			function justRemoveClass(group_num, class_num){
				var xhttp = new XMLHttpRequest();
				xhttp.open("GET", "RemoveInfo?action=class&group_num=" + group_num + "&class_num=" + class_num, true);
				xhttp.onreadystatechange = function() {
					if(this.readyState == 4 && this.status == 200){
						//document.getElementById("classes_table").innerHTML = this.responseText;
					}
				}
				xhttp.send();
			}
			
			function removeClass(group_num, class_num){
				var xhttp = new XMLHttpRequest();
				xhttp.open("GET", "RemoveInfo?action=class&group_num=" + group_num + "&class_num=" + class_num, true);
				xhttp.onreadystatechange = function() {
					if(this.readyState == 4 && this.status == 200){
						document.getElementById("classes_table").innerHTML = this.responseText;
						updateGeneratedSchedulesOnUI("friend");
					}
				}
				xhttp.send();
			}
			
			function addConstraint(){
				var xhttp = new XMLHttpRequest();
				var monday = document.getElementById("monday");
				var tuesday = document.getElementById("tuesday");
				var wednesday = document.getElementById("wednesday");
				var thursday = document.getElementById("thursday");
				var friday = document.getElementById("friday");
				var monday_bit = "0";
				var tuesday_bit = "0";
				var wednesday_bit = "0";
				var thursday_bit = "0";
				var friday_bit = "0";
				if(monday.checked == true) monday_bit = "1";
				if(tuesday.checked == true) tuesday_bit = "1";
				if(wednesday.checked == true) wednesday_bit = "1";
				if(thursday.checked == true) thursday_bit = "1";
				if(friday.checked == true) friday_bit = "1";
				xhttp.open("GET", "AddInfo?action=constraint&monday=" + monday_bit + "&tuesday=" + tuesday_bit + "&wednesday=" + wednesday_bit + "&thursday=" + thursday_bit + "&friday=" + friday_bit + "&start_time=" + document.getElementById("start_time").value + "&end_time=" + document.getElementById("end_time").value, true);
				xhttp.onreadystatechange = function() {
					if(this.readyState == 4 && this.status == 200){
						document.getElementById("constraints_table").innerHTML = this.responseText;
						updateGeneratedSchedulesOnUI("friend");
					}
				}
				xhttp.send();
			}
			
			function removeConstraint(constraint_num){
				var xhttp = new XMLHttpRequest();
				xhttp.open("GET", "RemoveInfo?action=constraint&constraint_num=" + constraint_num, true);
				xhttp.onreadystatechange = function() {
					if(this.readyState == 4 && this.status == 200){
						document.getElementById("constraints_table").innerHTML = this.responseText;
						if(constraint_num == -1){
							updateGeneratedSchedulesOnUI("initialize");
						}
						else{
							updateGeneratedSchedulesOnUI("friend");
						}
					}
				}
				xhttp.send();
			}
			
			function updateGeneratedSchedulesOnUI(mode){
				if(mode != "initialize"){
					document.getElementById("schedules_table").innerHTML = "<h1 class=\"schedule-header\">Generating schedules...</h1>";
				}				var xhttp2 = new XMLHttpRequest();
				xhttp2.open("GET", "UpdateSchedulesOnUI?mode=" + mode, true);
				xhttp2.onreadystatechange = function() {
					if(this.readyState == 4 && this.status == 200){
						document.getElementById("schedules_table").innerHTML = this.responseText;
					}
				}
				xhttp2.send();
			}
			
			removeGroup(-1);
			removeConstraint(-1);
			
			
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
			   			console.log("size: " + friendsListGlobalVar.data.length);
			   			console.log("size2: " + length);
			   			//if users in response match our current friends then add them to the table
			   			for (var i = 0; i < length; i++) {
			   				
		    				var usernameStudent = classEnrollmentList[i].username;
		    				var userPicURLStudent = classEnrollmentList[i].userPicURL;
		    				for(var j = 0; j < friendsListGlobalVar.data.length; j++) {
		    					
		    					var friendName = friendsListGlobalVar.data[j].name;
		    					if(friendName == usernameStudent) {
		    						console.log(friendName + " and " + usernameStudent);
		    						var table = document.getElementById("tableFriendsList");
		    				  		var checkUser = false;
		    						for (var i = 0, row; row = table.rows[i]; i++) {
		    							if (usernameStudent == row.cells[1]) {
		    								checkUser = true;
		    							}
		    						}
		    						if (!checkUser) {
		    							var row = table.insertRow(0);
			    						var imageCell = row.insertCell(0);
			    						var nameCell = row.insertCell(1);
			    						var img = document.createElement('img');
			    						if (friendName == "James Orme-Rogers") {
			    							img.src = "James.jpg";
			    						}
			    						if (friendName == "Tristan Elma") {
			    							img.src = "Tristan.jpg";
			    						}
			    						if (friendName == "Joel Gutierrez") {
			    							img.src = "Joel.jpg";
			    						}
			    						if (friendName == "Yuyang Huang") {
			    							img.src = "Yuyang.jpg";
			    						}
			    						if (friendName == "Luz Camacho") {
			    							img.src = "Luz.jpg";
			    						}
			    						if (friendName == "Kaushik Tandon") {
			    							img.src = "Kaushik.jpg";
			    						}
			    						img.width = "50";
			    						img.height = "50";
			    						imageCell.appendChild(img);
			    						nameCell.innerHTML = friendName;
		    						}
		    					}
		    				} //end of inner for loop
						} // end of outer for loop
			   		}
			   	} //end of async call
			   	xhttp.send();		  		
			} //end of function call


		  	
			function modalClicked(id) {
				var modal = document.getElementById('myModal');
			    modal.style.display = "block";
			    fillHeaderForTable();
			    populateFriendsListTable(id); //TODO: need to pass in classID into this function...
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
		

		<div style="height: 100vh">
			<div class="container-fluid h-100">
				<div class="row h-100">
					<div class="col-12">
						<div class="h-100 d-flex flex-column">
							<div class="row button-bar">
								<div class="col-7">
									<h3 class = "title">ScheduleMe</h3>
								</div>
								<div class="col-3">
									<a class="btn btn-primary btn-block fake-button" onclick="RegisterCoursesJS()" href="">
										Register My Courses <i class="fas fa-lock"></i>
									</a>
								</div>
								<div class="col-2">
									<!-- facebook button -->
									<a href="javascript:void(0);" onclick="fbLogin()" id="fbButton"><img src="fblogin.png" class="fb-button" /></a>
								</div>
							</div>
							<div class="row flex-grow-1 h-100">
								<div class="col-6 h-100">
									<div class="row h-50">
										<div class="col-12 h-100 panel-classes">
											<div class="d-flex flex-column h-100">
												<div class="row justify-content-center panel-header">My Classes</div>
												<div class="row flex-grow-1">
													<div class="col-12 panel-content" id="classes_table">
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="row h-50">
										<div class="col-12 h-100 panel-constraints">
											<div class="d-flex flex-column h-100">
												<div class="row justify-content-center panel-header">Other Time Constraints</div>
												<div class="row flex-grow-1">
													<div class="col-12 panel-content" id="constraints_table">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="col-6 panel-schedules h-100">
									<div class="d-flex flex-column h-100">
										<div class="row justify-content-center panel-header">Generated Schedules</div>
										<div class="row flex-grow-1">
											<div class="col-12 panel-content" id="schedules_table">
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
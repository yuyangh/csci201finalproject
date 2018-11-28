<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Single Scheduling</title>
		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
		<link rel="stylesheet" href="single_schedule.css">
	</head>
	<body>
		<script>
		  	
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
							updateGeneratedSchedulesOnUI("single");
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
						updateGeneratedSchedulesOnUI("single");
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
						updateGeneratedSchedulesOnUI("single");
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
						updateGeneratedSchedulesOnUI("single");
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
							updateGeneratedSchedulesOnUI("single");
						}
					}
				}
				xhttp.send();
			}
			
			function updateGeneratedSchedulesOnUI(mode){
				if(mode != "initialize"){
					document.getElementById("schedules_table").innerHTML = "<h1 class=\"waiting\">Generating schedules...</h1>";
				}
				var xhttp2 = new XMLHttpRequest();
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
		</script>
		

		<div style="height: 100vh">
			<div class="container-fluid h-100">
				<div class="row h-100">
					<div class="col-12">
						<div class="h-100 d-flex flex-column">
							<div class="row button-bar">
								<div class="col-3">
								</div>
								<div class="col-3">
									<h3 class = "title">ScheduleMe</h3>
								</div>
								<div class="col-4">
								</div>
								<div class="col-2">
									<a class="btn btn-info btn-block fake-button" href="login.jsp">
										<i class="fas fa-arrow-left"></i> Main Menu
									</a>
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
											<div class="col-11 panel-content" id="schedules_table">
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
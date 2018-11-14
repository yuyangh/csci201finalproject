package csci201finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RetriveSections {
	private String dept;
	private String classCode;
	private ArrayList<Section> totalSections;
	
	public RetriveSections(String dept, String classCode) {
		this.dept = dept;
		this.classCode = classCode;
		totalSections = new ArrayList<Section>();
	}
	
	public ArrayList<ArrayList<Section>> getSections() {
		// giant array list of quizzes, blah 
		ArrayList<ArrayList<Section>> typedSections = new ArrayList<ArrayList<Section>>();
		
		Connection conn = null;
		ResultSet rs = null;
		java.sql.PreparedStatement checkParams = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ScheduleMe?user=root&password=root&useSSL=false&AllowPublicKeyRetrieval=True&serverTimezone=PST");
			checkParams = conn.prepareStatement("SELECT d.departmentName, c.courseName, c.courseID FROM Departments d, Courses c WHERE d.departmentName=? AND d.departmentID=c.departmentID AND c.courseName=?");;
			checkParams.setString(1, this.dept);
			checkParams.setString(2, this.classCode);
			/* Query to ensure that the dept and course actually exists within databse */
			rs = checkParams.executeQuery();
			// triple check this syntax to see if it completely correct
			if(rs.next()) {
				System.out.println("Course " + rs.getString("courseName") + " with ID " + rs.getInt("courseID") + " found in dept " + rs.getString("departmentName"));
				int courseID = rs.getInt("courseID");
				
				java.sql.PreparedStatement getCourseSections = conn.prepareStatement("SELECT * FROM Sections WHERE courseID=?");
				getCourseSections.setInt(1, courseID);
				ResultSet sectionResult = getCourseSections.executeQuery();
				while(sectionResult.next()) {
					/* Returning all necessary info to populate our array list*/
					String sectionID = sectionResult.getString("sectionID");
					String type = sectionResult.getString("type");
					String startTime = sectionResult.getString("startTime");
					String endTime = sectionResult.getString("endTime");
					String days = sectionResult.getString("days");
					String[] daysParsed = days.split(" ");
					ArrayList<Integer> daysInts = new ArrayList<Integer>();
					for(int i  = 0; i < daysParsed.length; i++) {
						daysInts.add(Integer.parseInt(daysParsed[i]));
					}
					totalSections.add(new Section(sectionID, type, startTime, endTime, daysInts));
				}

				java.sql.PreparedStatement getTypes = conn.prepareStatement("SELECT DISTINCT type FROM Sections WHERE courseID=?");
				getTypes.setInt(1, courseID);
				ResultSet ourTypes = getTypes.executeQuery();
				while(ourTypes.next()) {
					 ArrayList<Section> oneType = new ArrayList<Section>();
					 for(int i = 0; i < totalSections.size(); i++) {
						 if(totalSections.get(i).getType().equals(ourTypes.getString("type"))) {
							 oneType.add(totalSections.get(i));
						 }
					 }
					 typedSections.add(oneType);
				}
				
				
				if(getCourseSections != null) {
					getCourseSections.close();
				}
				if(sectionResult != null) {
					sectionResult.close();
				}
				
			} else {
				System.out.println("The deparment or class code does not exists!");
			}
			
			if(checkParams != null) {
				checkParams.close();
			}
			if(rs != null) {
				rs.close();
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("Could not find driver: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Could not find database: " + e.getMessage());
		}
		
		return typedSections;
	}
}

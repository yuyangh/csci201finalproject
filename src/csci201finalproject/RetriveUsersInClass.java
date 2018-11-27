package csci201finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/* NOTE: this class only serves to return ALL users that are enrolled in a certain class. From my 
 * knowledge, work to find the intersection between Facebook friends and the users in a class would
 * be found elsewhere. */

public class RetriveUsersInClass {
	private String courseName;
	private ArrayList<Student> usersInClass;
	
	public RetriveUsersInClass(String courseName) {
		this.courseName = courseName;
		this.usersInClass = new ArrayList<Student>();
	}
	
	public ArrayList<Student> getUsersInClass() {
		
		Connection conn = null;
		ResultSet rs = null;
		java.sql.PreparedStatement getUserIDs = null;
		
		try {
			//Class.forName("com.mysql.cj.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ScheduleMe?user=root&password=elma201&useSSL=false&AllowPublicKeyRetrieval=True&serverTimezone=PST");
			
			ArrayList<Integer> returnedUserIDs = new ArrayList<Integer>();
			getUserIDs = conn.prepareStatement("SELECT sc.userID FROM Schedules sc, Sections s, Courses c WHERE "
					+ "s.sectionID=sc.sectionID AND s.courseID=c.courseID AND c.courseName=?");
			getUserIDs.setString(1, courseName);
			rs = getUserIDs.executeQuery();
			
			while(rs.next()) {
				returnedUserIDs.add(rs.getInt("userID"));
			}
			
			if(!returnedUserIDs.isEmpty()) {
				java.sql.PreparedStatement getUserInfo = null;
				ResultSet userInfo = null;
				for(int i = 0; i < returnedUserIDs.size(); i++) {
					getUserInfo = conn.prepareStatement("SELECT * FROM Users WHERE userID=?");
					getUserInfo.setInt(1, returnedUserIDs.get(i));
					userInfo = getUserInfo.executeQuery();
					
					/* if user was found in database */
					if(userInfo.next()) {
						Student newUser = new Student(userInfo.getString("facebookID"), userInfo.getString("name"),
								userInfo.getString("img"), userInfo.getString("email"));
						usersInClass.add(newUser);
					}
				}
				
				if(getUserInfo != null) {
					getUserInfo.close();
				}
				if(userInfo != null) {
					userInfo.close();
				}
				
			} else {
				System.out.println("Did not return any user ids. Something went wrong. ");
			}
			
			if(getUserIDs != null) {
				getUserIDs.close();
			}
			if(rs != null) {
				rs.close();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC driver was not found! " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("RetriveUsers: SQL Exception. Could not connection to databse. " + e.getErrorCode());
		}
		
		return usersInClass;
	}
}
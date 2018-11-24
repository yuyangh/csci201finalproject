package csci201finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScheduleToDatabase {
	private ArrayList<String> classesAdded;
	/* will hold JUST sections ids */
	private ArrayList<Integer> arbitrarySections;
	private User user;
	private int userDatabaseID;
	
	public ScheduleToDatabase(User user, ArrayList<String> classesAdded) {
		this.user= user;
		this.classesAdded = classesAdded;
		this.arbitrarySections = new ArrayList<Integer>();
	}
	
	public void pushToDatabase() {
		Connection conn = null;
		ResultSet rs = null;
		java.sql.PreparedStatement checkUser = null;
		
		try {
			//Class.forName("com.mysql.cj.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ScheduleMe?user=root&password=root&useSSL=false&AllowPublicKeyRetrieval=True&serverTimezone=PST");
			
			String fbID = user.getFacebookID();
			String email = user.getEmail();
			checkUser = conn.prepareStatement("SELECT * FROM Users WHERE email=? AND facebookID=?");
			checkUser.setString(1, email);
			checkUser.setString(2, fbID);
			rs = checkUser.executeQuery();
			
			/* If block reached, user exists in the database */
			if(rs.next()) {
				userDatabaseID = rs.getInt("userID");
				
				System.out.println("User " + user.getName() + " exists in the database!");
				java.sql.PreparedStatement getSections = null;
				ResultSet ourSection = null;
				
				for(int i = 0; i < classesAdded.size(); i++) {
					getSections = conn.prepareStatement("SELECT s.sectionID FROM Courses c, Sections s WHERE c.courseName=? AND c.courseID = s.courseID LIMIT 1");
					getSections.setString(1, classesAdded.get(i));
					ourSection = getSections.executeQuery();
					
					if(ourSection.next()) {
						arbitrarySections.add(ourSection.getInt("sectionID"));
					}
				}

				java.sql.PreparedStatement pushSection = null;
				
				if(!arbitrarySections.isEmpty()) {
					for(int i = 0; i < arbitrarySections.size(); i++) {
						pushSection = conn.prepareStatement("INSERT INTO Schedules (sectionID, UserID) VALUES (?, ?)");
						pushSection.setInt(1, arbitrarySections.get(i));
						pushSection.setInt(2, userDatabaseID);
						pushSection.executeQuery();
					}
				} else {
					System.out.println("Our section list is empty. Something went wrong. ");
				}
				
				if(getSections != null) {
					getSections.close();
				}
				if(ourSection != null) {
					ourSection.close();
				}
				if(pushSection != null) {
					pushSection.close();
				}
				
			} else {
				System.out.println("User " + user.getName() + " does not exist in the database.");
			}
			
			if(checkUser != null) {
				checkUser.close();
			}
			if(rs != null) {
				rs.close();
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC driver was not found! " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("SQL Exception. Could not connection to databse. " + e.getErrorCode());
		}
		
	}
}

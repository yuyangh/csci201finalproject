package csci201finalproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class AddClassesToDatabase {
	public static void main(String[] args) {
		// Initialize gson
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonFileName = "WebScrapping/result_entire.json";
		File jsonFile = Paths.get(jsonFileName).toFile();

		Schools jsonSchools = new Schools();
		ArrayList<School> schools = new ArrayList<School>();
		jsonSchools.setSchools(schools);

		Connection conn = null;
		Statement st = null;
		ResultSet rsSchools = null;
		ResultSet rsDepartments = null;
		ResultSet rsCourses = null;
		ResultSet rsSections = null;

		try {
			// Parse schools from json into array
			BufferedReader br = new BufferedReader(new FileReader(jsonFile));
			JsonObject jsonObject = gson.fromJson(br, JsonObject.class);
			JsonArray jsonSchoolsArray = jsonObject.getAsJsonArray("Schools");
			for (JsonElement jsonSchool : jsonSchoolsArray) {
				School school = gson.fromJson(jsonSchool, School.class);
				schools.add(school);
			}
			
			// Connect to MySQL database
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/ScheduleMe?user=root&password=root&useSSL=false&AllowPublicKeyRetrieval=True&serverTimezone=PST");
			st = conn.createStatement();
			
			// Loop through schools
			for (School school : schools) {
				// Check if school is already in database
				String schoolName = school.getName();
				rsSchools = st.executeQuery("SELECT * from Schools where schoolName='" + schoolName + "'");
				if (rsSchools.next()) {
					System.out.println("School " + schoolName + " already in database");
				} else {
					// Insert school into database
					st.executeUpdate("INSERT INTO schools (schoolName) VALUES ('" + schoolName + "')");
					System.out.println("Inserted " + schoolName + " into database");
					
					// Get schoolID of last inserted school
					ResultSet rsSchoolID = st
							.executeQuery("SELECT s.schoolID FROM Schools s WHERE schoolName = '" + schoolName + "'");
					rsSchoolID.next();
					int schoolID = rsSchoolID.getInt(1);
					
					// Loop through departments of each school
					ArrayList<Department> departments = school.getDepartments();
					for (Department department : departments) {
						// Check if department is already in database
						String departmentName = department.getName();
						rsDepartments = st.executeQuery(
								"SELECT * from Departments where departmentName='" + departmentName + "'");
						if (rsDepartments.next()) {
							System.out.println("Department " + departmentName + " already in database");
						} else {
							// Insert department into database with schoolID
							st.executeUpdate("INSERT INTO departments (departmentName, schoolID) VALUES ('"
									+ departmentName + "', '" + schoolID + "')");
							System.out.println("Inserted " + departmentName + " into database");
							
							// Get departmentID of last inserted department
							ResultSet rsDepartmentID = st
									.executeQuery("SELECT d.departmentID FROM Departments d WHERE departmentName = '"
											+ departmentName + "'");
							rsDepartmentID.next();
							int departmentID = rsDepartmentID.getInt(1);
							
							// Loop through courses of each department
							ArrayList<Course> courses = department.getCourses();
							for (Course course : courses) {
								// Check if course is already in database
								String courseName = course.getCourseID();
								rsCourses = st
										.executeQuery("SELECT * from Courses where courseName='" + courseName + "'");
								if (rsCourses.next()) {
									System.out.println("Course " + courseName + " already in database");
								} else {
									// Insert course into database with departmentID
									st.executeUpdate("INSERT INTO courses (courseName, departmentID) VALUES ('"
											+ courseName + "', '" + departmentID + "')");
									System.out.println("Inserted " + courseName + " into database");
									
									// Get courseID of last inserted course
									ResultSet rsCourseID = st.executeQuery(
											"SELECT c.courseID FROM Courses c WHERE courseName = '" + courseName + "'");
									rsCourseID.next();
									int courseID = rsCourseID.getInt(1);
									
									// Loop through sections of each course
									ArrayList<Section> sections = course.getSections();
									for (Section section : sections) {
										// Check if section is already in database
										String sectionName = section.getSectionID();
										rsSections = st.executeQuery(
												"SELECT * from Sections where sectionName='" + sectionName + "'");
										if (rsSections.next()) {
											System.out.println("Section " + sectionName + " already in database");
										} else {
											// Get section attributes
											String sectionType = section.getType();
											String sectionStartTime = section.getStartTime();
											String sectionEndTime = section.getEndTime();
											ArrayList<Integer> sectionDays = section.getDays();
											// Parse section times into string
											String sectionDaysStr = "";
											for (Integer sectionDay : sectionDays) {
												sectionDaysStr += Integer.toString(sectionDay) + " ";
											}
											
											// Insert section into database with courseID
											st.executeUpdate(
													"INSERT INTO sections (sectionName, courseID, type, startTime, endTime, days) VALUES "
															+ "('" + sectionName + "', '" + courseID + "', '"
															+ sectionType + "', '" + sectionStartTime + "', '"
															+ sectionEndTime + "', '" + sectionDaysStr + "')");
											System.out.println("Inserted " + sectionName + " into database");
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println(jsonFileName);
			System.out.println("That file could not be found.");
		} catch (JsonSyntaxException jsonse) {
			System.out.println("That file is not a well-formed JSON file - " + jsonse.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Class was not found.cl");
			System.out.print(cnfe.getMessage());
		}
	}
}

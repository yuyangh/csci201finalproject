package csci201finalproject;

import java.util.ArrayList;

public class Department {
	private String DepartmentName;
	private ArrayList<Course> Courses;
	
	public Department(String name, ArrayList<Course> courses) {
		this.DepartmentName = name;
		this.Courses = courses;
	}
	
	public String getName() {
		return DepartmentName;
	}
	public void setName(String name) {
		this.DepartmentName = name;
	}
	public ArrayList<Course> getCourses() {
		return Courses;
	}
	public void setCourses(ArrayList<Course> courses) {
		this.Courses = courses;
	}
	
	public void addCourse(Course course) {
		if (course != null) {
			this.Courses.add(course);
		}
	}
}

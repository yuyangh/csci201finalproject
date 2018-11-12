package csci201finalproject;

import java.util.ArrayList;

public class Course {
	private String CourseID;
	private ArrayList<Section> Sections;
	
	public Course(String courseID, ArrayList<Section> sections) {
		this.CourseID = courseID;
		this.Sections = sections;
	}

	public String getCourseID() {
		return CourseID;
	}
	public void setCourseID(String courseID) {
		this.CourseID = courseID;
	}
	public ArrayList<Section> getSections() {
		return Sections;
	}
	public void setSections(ArrayList<Section> sections) {
		this.Sections = sections;
	}
	
	public void addSection(Section section) {
		this.Sections.add(section);
	}
	
}

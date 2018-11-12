package csci201finalproject;

import java.util.ArrayList;

public class School {
	private String SchoolName;
	private ArrayList<Department> Departments;
	
	public School(String name, ArrayList<Department> departments) {
		this.SchoolName = name;
		this.Departments = departments;
	}
	
	public String getName() {
		return SchoolName;
	}
	public void setName(String name) {
		this.SchoolName = name;
	}
	public ArrayList<Department> getDepartments() {
		return Departments;
	}
	public void setDepartments(ArrayList<Department> departments) {
		this.Departments = departments;
	}
	
	public void addDepartment(Department department) {
		this.Departments.add(department);
	}
}

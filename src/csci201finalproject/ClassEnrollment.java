package csci201finalproject;

import java.util.ArrayList;

public class ClassEnrollment {
	public ArrayList<Student> students;

	public ClassEnrollment() {
		this.students = new ArrayList<Student>();
	}

	public void addStudent(Student s) {
		this.students.add(s);
	}
}

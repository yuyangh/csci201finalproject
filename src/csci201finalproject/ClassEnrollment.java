package csci201finalproject;

public class ClassEnrollment {
	public ArrayList<Student> students;

	public ClassEnrollment() {
		this.students = new ArrayList<Student>();
	}

	public void addStudent(Student s) {
		this.students.add(s);
	}
}

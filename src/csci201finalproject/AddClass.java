package csci201finalproject;

import java.util.ArrayList;

public class AddClass {
	private String className;
	private RetriveSections retriveSections;
	private ArrayList<Section> totalSections;
	
	public AddClass(String deptName, String classCode) {
		this.className = deptName + classCode;
		this.retriveSections = new RetriveSections(deptName, classCode);
		this.totalSections = retriveSections.getSections();
	}
	
	public ArrayList<Section> getTotalSections() {
		return totalSections;
	}
}

package csci201finalproject;

import java.util.ArrayList;

public class AddClass {
	private String className;
	private RetriveSections retriveSections;
	private ArrayList<ArrayList<Section>> totalSections;
	/* private ArrayList<Section> quiz;
	private ArrayList<Section> lab;
	private ArrayList<Section> lecture;
	private ArrayList<Section> discussion; */
	
	public AddClass(String deptName, String classCode) {
		this.className = deptName + classCode;
		this.retriveSections = new RetriveSections(deptName, classCode);
		this.totalSections = retriveSections.getSections();
	}
	
	public ArrayList<ArrayList<Section>> getTotalSections() {
		return totalSections;
	}
}

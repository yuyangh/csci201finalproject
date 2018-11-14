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
	public ArrayList<ArrayList<Section>> generatePermutations(){		
		ArrayList<ArrayList<Section>> permutations = new ArrayList<ArrayList<Section>>();
		int numTypes = totalSections.size();
		if(numTypes == 1) {
			for(int i = 0 ; i < totalSections.get(0).size(); i++) {
				ArrayList<Section> a = new ArrayList<Section>();
				a.add(totalSections.get(0).get(i));
				permutations.add(a);
			}
		}
		else if(numTypes == 2) {
			for(int i = 0; i < totalSections.get(0).size(); i++) {
				for(int j = 0; j < totalSections.get(1).size(); j++) {
					//One element of the list should contain - Lec1 Lab1
					ArrayList<Section> a = new ArrayList<Section>();
					a.add(totalSections.get(0).get(i));
					a.add(totalSections.get(1).get(j));
					permutations.add(a);
				}
			}
		}
		else if(numTypes == 3) {
			for(int i = 0; i < totalSections.get(0).size(); i++) {
				for(int j = 0; j < totalSections.get(1).size(); j++) {
					for(int k = 0; k < totalSections.get(2).size(); k++) {
						ArrayList<Section> a = new ArrayList<Section>();
						a.add(totalSections.get(0).get(i));
						a.add(totalSections.get(1).get(j));
						a.add(totalSections.get(2).get(k));
						permutations.add(a);
					}
				}
			}
		}
		//Lecture, Lab, Quiz, Discussion
		else if(numTypes == 4) {
			for(int i = 0; i < totalSections.get(0).size(); i++) {
				for(int j = 0; j < totalSections.get(1).size(); j++) {
					for(int k = 0; k < totalSections.get(2).size(); k++) {
						for(int l = 0; l < totalSections.get(3).size(); l++) {
							ArrayList<Section> a = new ArrayList<Section>();
							a.add(totalSections.get(0).get(i));
							a.add(totalSections.get(1).get(j));
							a.add(totalSections.get(2).get(k));
							a.add(totalSections.get(3).get(l));
							permutations.add(a);
						}
					}
				}
			}
		}
		return permutations;
	}
}

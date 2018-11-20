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



	public AddClass(String deptName, String className) {
		this.className = className;
		this.retriveSections = new RetriveSections(getDeptPartName(className), className);
		this.totalSections = retriveSections.getSections();
	}

	public AddClass(String className) {
		this.className = className;
		this.retriveSections = new RetriveSections(getDeptPartName(className), className);
		this.totalSections = retriveSections.getSections();
	}

	public String getDeptPartName(String className){
		StringBuilder result=new StringBuilder();
		for (int i = 0; i <className.length() ; i++) {
			if(className.charAt(i)>='A' && className.charAt(i)<='Z'){
				result.append(className.charAt(i));
			}
		}
		return result.toString();
	}

	public String getDeptPartName(){
		StringBuilder result=new StringBuilder();
		for (int i = 0; i <className.length() ; i++) {
			if(className.charAt(i)>='A' && className.charAt(i)<='Z'){
				result.append(className.charAt(i));
			}
		}
		return result.toString();
	}

	public ArrayList<ArrayList<Section>> getTotalSections() {
		return totalSections;
	}

	public String getClassName() {
		return this.className;
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

					Section num1 = totalSections.get(0).get(i);
					Section num2 = totalSections.get(1).get(j);
					if(!num1.doesConflict(num2.getStartTime(), num2.getEndTime(), num2.getDays()) ){
						ArrayList<Section> a = new ArrayList<Section>();
						a.add(totalSections.get(0).get(i));
						a.add(totalSections.get(1).get(j));
						permutations.add(a);
					}
					//One element of the list should contain - Lec1 Lab1

				}
			}
		}
		else if(numTypes == 3) {
			for(int i = 0; i < totalSections.get(0).size(); i++) {
				Section num1 = totalSections.get(0).get(i);
				for(int j = 0; j < totalSections.get(1).size(); j++) {
					Section num2 = totalSections.get(1).get(j);
					for(int k = 0; k < totalSections.get(2).size(); k++) {
						Section num3 = totalSections.get(2).get(k);
						if(!num1.doesConflict(num3.getStartTime(), num3.getEndTime(), num3.getDays()) && !num2.doesConflict(num3.getStartTime(), num3.getEndTime(), num3.getDays()) && !num1.doesConflict(num2.getStartTime(),num2.getEndTime(),num2.getDays())) {							
							ArrayList<Section> a = new ArrayList<Section>();
							a.add(totalSections.get(0).get(i));
							a.add(totalSections.get(1).get(j));
							a.add(totalSections.get(2).get(k));
							permutations.add(a);
						}
						else {
							System.out.println("Conflict");
						}
					}
				}
			}
		}
		//Lecture, Lab, Quiz, Discussion
		else if(numTypes == 4) {
			for(int i = 0; i < totalSections.get(0).size(); i++) {
				Section num1 = totalSections.get(0).get(i);
				for(int j = 0; j < totalSections.get(1).size(); j++) {
					Section num2 = totalSections.get(1).get(j);
					if(!num1.doesConflict(num2.getStartTime(),num2.getEndTime(),num2.getDays())) {
						for(int k = 0; k < totalSections.get(2).size(); k++) {
							Section num3 = totalSections.get(2).get(k);
							if(!num1.doesConflict(num3.getStartTime(), num3.getEndTime(), num3.getDays()) &&
								!num2.doesConflict(num3.getStartTime(), num3.getEndTime(), num3.getDays()) ){
								for(int l = 0; l < totalSections.get(3).size(); l++) {
									Section num4 = totalSections.get(3).get(l);

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
			}
		}
		return permutations;
	}


	public void setClassName(String className) {
		this.className = className;
	}

	public RetriveSections getRetriveSections() {
		return retriveSections;
	}

	public void setRetriveSections(RetriveSections retriveSections) {
		this.retriveSections = retriveSections;
	}

	public void setTotalSections(ArrayList<ArrayList<Section>> totalSections) {
		this.totalSections = totalSections;
	}


}

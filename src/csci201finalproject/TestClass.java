package csci201finalproject;

import java.util.ArrayList;

public class TestClass {

	public static void main(String[] args) {
		//Verify that totalSections works
		AddClass test = new AddClass("CSCI", "CSCI103");
		ArrayList<ArrayList<Section>> totalSections = test.getTotalSections();
		for(int i = 0; i < totalSections.size(); i++) {
			for(int j = 0; j < totalSections.get(i).size(); j++) {
				Section single = totalSections.get(i).get(j);
				System.out.println(j + " " + single.getType() + ": " + single.getSectionID() + " start time: " + single.getStartTime());
			}
		}
		System.out.println("\n\n\n");
		//Verify class of one type results in correct permutations
		AddClass test2 = new AddClass("ALI","ALI224");
		ArrayList<ArrayList<Section>> permutations = test2.generatePermutations();
		for(int i = 0; i < permutations.size(); i++) {
			System.out.print("Permutation: ");
			for(int j = 0; j < permutations.get(i).size(); j++) {
				Section temp = permutations.get(i).get(j);
				System.out.print(temp.getSectionID() + " ");
			}
			System.out.println();
		}
		System.out.println("\n\n\n");
		//Verify class of three types results in correct permutations
		AddClass test3 = new AddClass("PHYS","PHYS152");
		ArrayList<ArrayList<Section>> permutations3 = test3.generatePermutations();
		for(int i = 0; i < permutations3.size(); i++) {
			for(int j = 0; j < permutations3.get(i).size(); j++) {
				Section temp = permutations3.get(i).get(j);
				System.out.print(temp.getSectionID() + " ");
			}
			System.out.println();
		}
		System.out.println("\n\n\n");
		/*
		//Verify class of three types results in correct permutations
		AddClass test4 = new AddClass("ASTR","ASTR100");
		ArrayList<ArrayList<Section>> permutations4 = test4.generatePermutations();
		for(int i = 0; i < permutations4.size(); i++) {
			System.out.print("Permutation: ");
			for(int j = 0; j < permutations4.get(i).size(); j++) {
				Section temp = permutations4.get(i).get(j);
				System.out.print(temp.getSectionID() + " ");
			}
			System.out.println();
		}
		System.out.println("\n\n\n");
		//Verify class of four types results in correct permutations
		AddClass test5 = new AddClass("CHEM","CHEM322");
		ArrayList<ArrayList<Section>> permutations5 = test5.generatePermutations();
		for(int i = 0; i < permutations5.size(); i++) {
			System.out.print("Permutation: ");
			for(int j = 0; j < permutations5.get(i).size(); j++) {
				Section temp = permutations5.get(i).get(j);
				System.out.print(temp.getSectionID() + " ");
			}
			System.out.println();
		}
		*/
	}
}

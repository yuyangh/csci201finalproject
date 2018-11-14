package csci201finalproject;

import java.util.ArrayList;

public class TestClass {
	
	public static void main(String[] args) {
		AddClass test = new AddClass("CSCI", "CSCI103");
		ArrayList<ArrayList<Section>> totalSections = test.getTotalSections();
		for(int i = 0; i < totalSections.size(); i++) {
			for(int j = 0; j < totalSections.get(i).size(); j++) {
				Section single = totalSections.get(i).get(j);
				System.out.println(j + " " + single.getType() + ": " + single.getSectionID() + " start time: " + single.getStartTime());
			}
		}
	}
}

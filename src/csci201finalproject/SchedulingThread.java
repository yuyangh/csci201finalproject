package csci201finalproject;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SchedulingThread extends Thread {

	private ArrayList<AddClass> totalClasses;
	private ArrayList<Constraint> constraints;
	private ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> schedules;

	// will have to create a new instance of the thread with each time the algorithm is called
	public SchedulingThread(ArrayList<AddClass> totalClasses, ArrayList<Constraint> constraints, ConcurrentHashMap schedules) {
		System.out.println("In constructor");
		this.totalClasses = totalClasses;
		this.constraints = constraints;
		this.schedules = schedules;

	}

    /*
    the process for call from the servlet
    front end get call for CSCI103
    we are going to return the schedule Hashtable["csci103"]
    however, the key does not exists
    so condition lock,
    as long as we compute the result,
    signalAll() or notifyAll()
    release the lock()
    */

	public void run() {
        /*calculate the permutation
        get the result set
        suppose we would like to add the class csci 103
        check if the general hashtable has the CSCI103
        if it does not, do the permutation and add to the general hashtable
        check permutation code on test.java
        copy the CSCI103 in the general hashtable and check conflicts with the constraints
        save all non-conflicting result in to the shcedule hashtable


        suppose we would like to add the class CSCI103&CSCI104
        check if the general hashtable has the CSCI103&CSCI104
        if it does not, do the permutation and add to the general hashtable
        add all non-conflicting result to the schedules hashtable*/

		// this step cannot be optimized with executors
		for (AddClass addClass : totalClasses) {
			String className = totalClasses.get(0).getClassName();
			if (!AddInfo.generalHashMap.containsKey(className)) {
				ArrayList<ArrayList<Section>> permutations = totalClasses.get(0).generatePermutations();
				AddInfo.generalHashMap.put(className, permutations);
			}
		}
		for (AddClass addClass : totalClasses) {
			String className = totalClasses.get(0).getClassName();
			if (!schedules.containsKey(className)) {
				ArrayList<ArrayList<Section>> permutations = getPermuationWithConstraints(AddInfo.generalHashMap.get(className), constraints);
				schedules.put(className, permutations);
			}

		}
		//todo may change somehow about how to update the general schedule
		String intendedClassName=getIntendedClassName(totalClasses);
		ArrayList<AddClass> subsetClassList = subsetClassList(intendedClassName);
		schedules.put(intendedClassName,concatPermuations(subsetClassList));

	}

	public static ArrayList<ArrayList<Section>> getPermuationWithConstraints
			(ArrayList<ArrayList<Section>> permutations, ArrayList<Constraint> constraints) {
		// todo go through each element, find conflict or not
		// if find, continue
		//
		ArrayList<ArrayList<Section>> result = null;
		return result;
	}

	public static ArrayList<ArrayList<Section>> concatTwoPermuations
			(ArrayList<ArrayList<Section>> firPermutations, ArrayList<ArrayList<Section>> pSecPrmutations) {
		ArrayList<ArrayList<Section>> result = null;
		return result;
	}

	public static ArrayList<ArrayList<Section>> concatPermuations
			(ArrayList<AddClass> subsetClassList) {
		ArrayList<ArrayList<Section>> result = null;
		return result;
	}

	public static String concatClassNames(String firstGroup, String secondGroup) {
		// e.g. first group= "CSCI103 CSCI270", secondGroup="CSCI104"
		// intended result= "CSCI103 CSCI104 CSCI 270" in alphabetical order
		String result = null;
		return result;
	}

	public static String getIntendedClassName(ArrayList<AddClass> totalClasses) {
		String intendedClassName=null;
		return intendedClassName;
	}

	/**
	 * @param intendedClassName the class name I would like to have ,such as "CSCI103 CSCI104 CSCI 270"
	 * @return a list of class that is in the schedule table, which can compose the intendedClassName
	 */
	public static ArrayList<AddClass> subsetClassList(String intendedClassName) {
		// minimize size subset with given sum
		// https://leetcode.com/problems/minimum-size-subarray-sum/
		ArrayList<AddClass> subsetClassNameList = null;
		return subsetClassNameList;
	}

	public static void main(String[] args) {
		new SchedulingThread(null, null, null);
	}


}

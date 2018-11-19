package csci201finalproject;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SchedulingThread extends Thread {

	static ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> generalSchedules = new ConcurrentHashMap<String, ArrayList<ArrayList<Section>>>();
	private ArrayList<AddClass> totalClasses;
	private ArrayList<Constraint> constraints;
	private ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> schedules;
	private String addClassKey;

	// will have to create a new instance of the thread with each time the algorithm is called
	public SchedulingThread(ArrayList<AddClass> totalClasses, ArrayList<Constraint> constraints,
	                        ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> schedules, String addClassKey) {
		this.totalClasses = totalClasses;
		this.constraints = constraints;
		this.schedules = schedules;
		this.addClassKey = addClassKey;
	}

	// will have to create a new instance of the thread with each time the algorithm is called
	// key must be passed in w/ constructor, run method cannot take parameters
	public SchedulingThread(ArrayList<AddClass> totalClasses, ArrayList<Constraint> constraints, ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> schedules,
	                        ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> generalSchedules, String addClassKey) {
		this.totalClasses = totalClasses;
		this.constraints = constraints;
		// dummy generalSchedules
		this.schedules = schedules;
		this.addClassKey = addClassKey;
	}

    /*
    Process:
    get call from the servlet
    front end get call for CSCI103
    we are going to return the schedules Hashtable["csci103"], this may stored in the Session
    however, the key does not exists
    as long as we compute the result, insert into that
    since ConcurrentHashMap is thread safe, we do not need to consider insertion and iteration fail

    Example
	suppose we would like to add the class csci 103
	check if the generalSchedules hashtable has the CSCI103
	if it does not, do the permutation and add to the generalSchedules hashtable
	// check permutation code on test.java
	copy the CSCI103 in the generalSchedules hashmap and check conflicts with the constraints
	save all non-conflicting result in to the shcedule hashmap

	suppose we already have CSCI103 would like to add the class CSCI104
	check if the generalSchedules hashmap has the CSCI104
	if it does not, do the permutation and add to the generalSchedules hashtable
	add all non-conflicting result to the schedules hashtable
	cancat permutation of schedule[prevClassName] ("CSCI103") and schedule[CSCI104]
	insert into schedule with key, value even if value is null
   */

	/**
	 * @param validPermutations permutations (ex: lec, lab, quiz) of the class
	 * @param constraints       list of Constraint object
	 * @return valid Permutation With Constrains
	 */
	public static ArrayList<ArrayList<Section>> getPermutationWithConstraints
	(ArrayList<ArrayList<Section>> validPermutations, ArrayList<Constraint> constraints) {
		ArrayList<ArrayList<Section>> validPermutationWithConstrains = new ArrayList<ArrayList<Section>>();

		for (ArrayList<Section> singleValidPermutation : validPermutations) {
			/* Extract a single permutation (ex: lec, lab, quiz) of the class we just added */
			boolean stillValid = true;
			/* these for loops check whether each aspect of the permutation is valid against all the constraints.
			 * If even one aspect (a lec, lab, or quiz) does not comply with constraints, we flag that and break
			 * out of our loops accordingly */
			for (Section aSinglePermutation : singleValidPermutation) {
				if (constraints != null) {
					for (Constraint singleConstraint : constraints) {
						if (aSinglePermutation.doesConflict(singleConstraint.getStartTime(), singleConstraint.getEndTime(), singleConstraint.getDays())) {
							stillValid = false;
							break;
						}
					}
				}
				if (!stillValid) {
					break;
				}
			}
			if (stillValid) {
				validPermutationWithConstrains.add(singleValidPermutation);
			}
		}

		return validPermutationWithConstrains;
	}

	/**
	 * @param firstSinglePermutation  a list of Section Object
	 * @param secondSinglePermutation a list of Section Object
	 * @return if two lists' Section Object has time overlap return null, else a concat of two list
	 */
	public static ArrayList<Section> concatTwoSinglePermutation(ArrayList<Section> firstSinglePermutation, ArrayList<Section> secondSinglePermutation) {
		for (Section singlefirstSection : firstSinglePermutation) {
			for (Section singleSecondSection : secondSinglePermutation) {
				// if conflict, break entire loop
				if (singlefirstSection.doesConflict(singleSecondSection.getStartTime(), singleSecondSection.getEndTime(), singleSecondSection.getDays())) {
					/* if even one section conflicts, then it would not be a valid schedule */
					return null;
				}
			}
		}

		// if nothing conflict, we return the combination
		ArrayList<Section> singlePermutationCombination = new ArrayList<Section>();
		singlePermutationCombination.addAll(firstSinglePermutation);
		singlePermutationCombination.addAll(secondSinglePermutation);
		return singlePermutationCombination;

	}

	/**
	 * @param firstPermutations  a list contains a list of Sections (lec, dis, quiz)
	 * @param secondPermutations a list contains a list of Sections (lec, dis, quiz)
	 * @return a non-conflicting permutation contains the (firstPermutations * secondPermutations)
	 */
	public static ArrayList<ArrayList<Section>> concatTwoPermutations
	(ArrayList<ArrayList<Section>> firstPermutations, ArrayList<ArrayList<Section>> secondPermutations) {
		ArrayList<ArrayList<Section>> setIntersection = null;

		/* if prevClass is still an empty string, that means the class we are adding if the first
		 * class added (there would be no "previous" set of classes to create an intersection of).*/
		if (firstPermutations == null) {
			if (secondPermutations == null) {
				return setIntersection;
			} else {
				return secondPermutations;
			}
		} else {
			if (secondPermutations == null) {
				return firstPermutations;
			} else {
				// if two permutations are same
				if (firstPermutations.equals(secondPermutations)) {
					return firstPermutations;
				}
				setIntersection = new ArrayList<ArrayList<Section>>();
				/* extracts the schedule developed for the previous classes (ex: "103 104")*/
				/* we iterate through the valid single course schedule for the class we just added.
				 * ex: this would be the valid schedule that fits all the constraints for a single course
				 * called CSCI201 */
				for (ArrayList<Section> firstSinglePermutation : firstPermutations) {
					/* this array list will add as the container for the new single schedule developed.
					 * For example, when one schedule for the single course CS201 is compared and has no
					 * conflict with a schedule developed for CS103 and CS104, this is the container that
					 * holds that new resulting schedule. NOTE: this also means that this arraylist has a
					 * variety of sections from DIFFERENT courses. Some parsing will be required to organize it
					 * by course again. */
					ArrayList<Section> validSingleSetInersection = new ArrayList<Section>();

					boolean stillValid = true;

					for (ArrayList<Section> secondSinglePermutation : secondPermutations) {
						/* extracts a single schedule for previous classes. ex: one mixture of lec, lab, quiz for
						 * CS103 and CS104 that do not conflict with each other. */
						/* these for loops breakdown the aforementioned schedules back into comparable sections. */

						validSingleSetInersection = concatTwoSinglePermutation(firstSinglePermutation, secondSinglePermutation);
						if (validSingleSetInersection != null) {
							setIntersection.add(validSingleSetInersection);
						}
					}
				}
			}
		}

		return setIntersection;
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

	/**
	 * @param totalClasses all classes that the user would like to have
	 * @param addClassKey  the new key inserted into the totalClasses
	 * @return each class (except addClassKey)'s name with " " between, no " " before or after
	 */
	public static String concatPrevClassNames(ArrayList<AddClass> totalClasses, String addClassKey) {
		StringBuilder prevClass = new StringBuilder();
		for (AddClass totalClass : totalClasses) {
			if (!totalClass.getClassName().equals(addClassKey)) {
				prevClass.append(totalClass.getClassName()).append(" ");
			}
		}
		return prevClass.toString().trim();
	}

	public static String getIntendedClassName(ArrayList<AddClass> totalClasses) {
		String intendedClassName = null;
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

	public static void printPrettyPermutations(ArrayList<ArrayList<Section>> permutations) {
		if (permutations == null) {
			System.out.println("Empty list");
			return;
		}
		for (ArrayList<Section> permutation : permutations) {
			for (Section section : permutation) {
				System.out.print(section.toString() + "\t");
			}
			System.out.println();
		}
	}

	public static void test() {
		ArrayList<AddClass> totalClasses;
		ArrayList<Constraint> constraints;
		ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> schedule;
		ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> general;
		String addClassKey;

		schedule = new ConcurrentHashMap<String, ArrayList<ArrayList<Section>>>();
		general = new ConcurrentHashMap<String, ArrayList<ArrayList<Section>>>();

		Constraint constraint = null;
		constraints = null;

		totalClasses = new ArrayList<AddClass>();


		// // single class case
		// String className1 = "CSCI103";
		// AddClass addClass1 = new AddClass("CSCI", className1);
		// totalClasses.add(addClass1);

		// addClassKey = "CSCI103";
		//
		// SchedulingThread test1 = new SchedulingThread(totalClasses, constraints, schedule, generalSchedules, addClassKey);
		// test1.run();
		// System.out.println(concatPrevClassNames(test1.getTotalClasses(), ""));
		// System.out.println("Result size: " + test1.schedules.get(concatPrevClassNames(test1.getTotalClasses(), "")).size());
		// printPrettyPermutations(test1.schedules.get(className1));


		// two classes case
		System.out.println();
		String className2 = "CSCI201";
		AddClass addClass2 = new AddClass("CSCI", className2);
		totalClasses.add(addClass2);
		addClassKey = "CSCI201";

		SchedulingThread test2 = new SchedulingThread(totalClasses, constraints, schedule, general, addClassKey);
		test2.run();
		System.out.println(concatPrevClassNames(test2.getTotalClasses(), ""));
		System.out.println("Result size: " + test2.schedules.get(concatPrevClassNames(test2.getTotalClasses(), "")).size());
		printPrettyPermutations(test2.schedules.get(concatPrevClassNames(test2.getTotalClasses(), "")));


		// // three classes case
		System.out.println();
		String className3 = "CSCI104";
		AddClass addClass3 = new AddClass("CSCI", className3);
		totalClasses.add(addClass3);
		addClassKey = "CSCI104";
		SchedulingThread test3 = new SchedulingThread(totalClasses, constraints, schedule, general, addClassKey);
		test3.run();
		System.out.println(concatPrevClassNames(test3.getTotalClasses(), ""));
		System.out.println("Result size: " + test3.schedules.get(concatPrevClassNames(test3.getTotalClasses(), "")).size());
		printPrettyPermutations(test3.schedules.get(concatPrevClassNames(test3.getTotalClasses(), "")));

	}

	public static void main(String[] args) {
		test();
	}

	public ArrayList<AddClass> getTotalClasses() {
		return totalClasses;
	}

	public void setTotalClasses(ArrayList<AddClass> totalClasses) {
		this.totalClasses = totalClasses;
	}

	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(ArrayList<Constraint> constraints) {
		this.constraints = constraints;
	}

	public ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> getSchedules() {
		return schedules;
	}

	public void setSchedules(ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> schedules) {
		this.schedules = schedules;
	}

	public String getAddClassKey() {
		return addClassKey;
	}

	public void setAddClassKey(String addClassKey) {
		this.addClassKey = addClassKey;
	}

	// dummy code, just for scratch process
	public void run_2() {
        /*calculate the permutation
        get the result set
        suppose we would like to add the class csci 103
        check if the generalSchedules hashtable has the CSCI103
        if it does not, do the permutation and add to the generalSchedules hashtable
        check permutation code on test.java
        copy the CSCI103 in the generalSchedules hashtable and check conflicts with the constraints
        save all non-conflicting result in to the shcedule hashtable


        suppose we would like to add the class CSCI103&CSCI104
        check if the generalSchedules hashtable has the CSCI103&CSCI104
        if it does not, do the permutation and add to the generalSchedules hashtable
        add all non-conflicting result to the schedules hashtable*/

		// this step cannot be optimized with executors
		for (AddClass singleAddClass : totalClasses) {
			String className = singleAddClass.getClassName();
			if (!SchedulingThread.generalSchedules.containsKey(className)) {
				ArrayList<ArrayList<Section>> permutations = singleAddClass.generatePermutations();
				SchedulingThread.generalSchedules.put(className, permutations);
			}
		}
		for (AddClass singleAddClass : totalClasses) {
			String className = singleAddClass.getClassName();
			if (!schedules.containsKey(className)) {
				ArrayList<ArrayList<Section>> validPermutationWithConstrains = getPermutationWithConstraints(SchedulingThread.generalSchedules.get(className), constraints);
				schedules.put(className, validPermutationWithConstrains);
			}

		}
		//todo may change somehow about how to update the generalSchedules schedule
		String intendedClassName = getIntendedClassName(totalClasses);
		ArrayList<AddClass> subsetClassList = subsetClassList(intendedClassName);
		schedules.put(intendedClassName, concatPermuations(subsetClassList));

	}

	// @SuppressWarnings("unchecked")
	public void run() {

        /* Check if the class is in our generalSchedules hash table; if not, create all valid permutations,
        where a valid permutation is defined as a set of classes (ex: lecture, lab, quiz) that do not conflict with each other. */
		// needs to use containsKey() rather than contains()!
		if (!generalSchedules.containsKey(addClassKey)) {
			for (AddClass totalClass : totalClasses) {
				if (totalClass.getClassName().equals(addClassKey)) {
					generalSchedules.put(addClassKey, totalClass.generatePermutations());
					break;
				}
			}
		}


		ArrayList<ArrayList<Section>> validPermutations = generalSchedules.get(addClassKey);
		/* validAgainstContraints will be the things inserted into our schedule hashtable. This is defined as a
		 * list of permutations that do not conflict with any constraint. Thus, they are considered a valid
		 * schedule for that particular class.  */
		ArrayList<ArrayList<Section>> validAgainstContraints = new ArrayList<ArrayList<Section>>();

		// todo if schedules already contains the key we want, then update such key's contents by:
		// this is a case for constraint update
		if (schedules.containsKey(concatPrevClassNames(totalClasses, ""))) {
			// temp= generalSchedules' key intersect constrainsts
			// result = generalSchedules' key-{temp}
			// push result into schedules
			// return
		}
		validAgainstContraints = getPermutationWithConstraints(validPermutations, constraints);

		schedules.put(addClassKey, validAgainstContraints);

		/* Code to generate the key to find the set which we are finding the intersection of */
		String prevClass = "";
		prevClass = concatPrevClassNames(totalClasses, addClassKey);

		ArrayList<ArrayList<Section>> setIntersection = concatTwoPermutations(schedules.get(prevClass.trim()), validAgainstContraints);

		// even it is null, we need to insert that
		String newIntersectionKey = prevClass + " " + addClassKey;
		schedules.put(newIntersectionKey.trim(), setIntersection);

	}


}
package csci201finalproject;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SchedulingThread extends Thread {

	static ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> generalSchedules = new ConcurrentHashMap<String, ArrayList<ArrayList<Section>>>();

	static {
		generalSchedules.put("", new ArrayList<ArrayList<Section>>());
	}

	private ArrayList<AddClass> totalClasses;
	private ArrayList<Constraint> constraints;
	private ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> schedules;
	private String addClassKey;
	boolean ready = false;
	ArrayList<ArrayList<Section>> result;

	// need to create a new instance of the thread with each time the algorithm is called

	/**
	 * @param totalClasses a list of Section objects, which are users' chosen Section
	 * @param constraints  a list of Constraint objects
	 * @param schedules    ConcurrentHashMap unique to every Session
	 * @param addClassKey  the newest operation the user does. those are following cases:
	 *                     Section name for add a Section,
	 *                     null for delete a class,
	 *                     "" for update constraints
	 */
	public SchedulingThread(ArrayList<AddClass> totalClasses,
	                        ArrayList<Constraint> constraints,
	                        ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> schedules,
	                        String addClassKey) {
		this.totalClasses = totalClasses;
		this.constraints = constraints;
		this.schedules = schedules;
		this.addClassKey = addClassKey;
		start();
	}

	//todo small problem here. if we still need to use a boolean as an indicator to show we update complete or not
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
	 * get list of list of Sections not-conflicting With Constrains
	 *
	 * @param validPermutations permutations (ex: lec, lab, quiz) of the class
	 * @param constraints       list of Constraint object
	 * @return a list of list of Sections e.g. (lec, dis,quiz)
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
	 * besides concatTwoSinglePermutation, print out conflict permutations
	 *
	 * @param firstSinglePermutation  a list of Section Object
	 * @param secondSinglePermutation a list of Section Object
	 * @param dummy                   dummy variable to do overloading
	 * @return if two lists' Section Object has time overlap return null, else a concat of two list
	 */
	public static ArrayList<Section> concatTwoSinglePermutation(ArrayList<Section> firstSinglePermutation,
	                                                            ArrayList<Section> secondSinglePermutation,
	                                                            boolean dummy) {
		ArrayList<Section> result = concatTwoSinglePermutation(firstSinglePermutation, secondSinglePermutation);
		if (result == null) {
			printConflictPermutations(firstSinglePermutation, secondSinglePermutation);
		}
		return result;
	}

	public static void printConflictPermutations(ArrayList<Section> firstSinglePermutation, ArrayList<Section> secondSinglePermutation) {
		System.out.print("Conflict in: ");
		for (Section section : firstSinglePermutation) {
			System.out.print(section.toString() + " ");
		}
		for (Section section : secondSinglePermutation) {
			System.out.print(section.toString() + " ");
		}
		System.out.println();
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

	/**
	 * @param totalClasses a list of AddClass
	 * @return all class names with " " in between
	 */
	public static String getAllClassNames(ArrayList<AddClass> totalClasses) {
		return concatPrevClassNames(totalClasses, "");
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

	public ArrayList<ArrayList<Section>> getResult() {
		return result;
	}

	public void setResult(ArrayList<ArrayList<Section>> result) {
		this.result = result;
	}

	/**
	 * print out list of list of Section's contents
	 *
	 * @param permutations
	 */
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

	public static void printPrettyConstraints(ArrayList<Constraint> constraints) {
		for (Constraint constraint : constraints) {
			System.out.print(constraint + "\t");
		}
		System.out.println();
	}

	public static void printPrettyTotalClasses(ArrayList<AddClass> totalClasses) {
		for (AddClass addClass : totalClasses) {
			System.out.print(addClass.getClassName() + "\t");
		}
		System.out.println();
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
		ArrayList<Integer> days = new ArrayList<Integer>();
		days.add(1);
		days.add(2);
		days.add(3);
		days.add(4);
		days.add(5);
		constraints = new ArrayList<Constraint>();
		constraint = new Constraint("08:00:00", "14:00:00", "test", days);
		constraints.add(constraint);
		// constraints = null;

		totalClasses = new ArrayList<AddClass>();


		// single class case
		String className1 = "CSCI103";
		AddClass addClass1 = new AddClass("CSCI", className1);
		totalClasses.add(addClass1);

		addClassKey = "CSCI103";

		SchedulingThread test1 = new SchedulingThread(totalClasses, constraints, schedule, addClassKey);
		test1.run();
		System.out.println(getAllClassNames(totalClasses));
		System.out.println("Result size: " + test1.schedules.get(concatPrevClassNames(test1.getTotalClasses(), "")).size());
		// printPrettyPermutations(test1.schedules.get(className1));


		// two classes case
		System.out.println();
		String className2 = "EE109";
		AddClass addClass2 = new AddClass("EE", className2);
		totalClasses.add(addClass2);
		addClassKey = className2;

		SchedulingThread test2 = new SchedulingThread(totalClasses, constraints, schedule, addClassKey);
		test2.run();
		System.out.println(getAllClassNames(totalClasses));
		System.out.println("Result size: " + test2.schedules.get(concatPrevClassNames(test2.getTotalClasses(), "")).size());
		// printPrettyPermutations(test2.schedules.get(concatPrevClassNames(test2.getTotalClasses(), "")));


		// // three classes case
		System.out.println();
		String className3 = "WRIT150";
		AddClass addClass3 = new AddClass("WRIT", className3);
		totalClasses.add(addClass3);
		addClassKey = className3;
		SchedulingThread test3 = new SchedulingThread(totalClasses, constraints, schedule, addClassKey);
		test3.run();
		System.out.println(concatPrevClassNames(test3.getTotalClasses(), ""));
		System.out.println("Result size: " + test3.schedules.get(concatPrevClassNames(test3.getTotalClasses(), "")).size());
		// printPrettyPermutations(test3.schedules.get(concatPrevClassNames(test3.getTotalClasses(), "")));

		System.out.println();
		String className4 = "ITP115";
		AddClass addClass4 = new AddClass("ITP", className4);
		totalClasses.add(addClass4);
		addClassKey = className4;
		SchedulingThread test4 = new SchedulingThread(totalClasses, constraints, schedule, addClassKey);
		test4.run();
		System.out.println(getAllClassNames(totalClasses));
		System.out.println("Result size: " + test4.schedules.get(concatPrevClassNames(test4.getTotalClasses(), "")).size());
		// printPrettyPermutations(test4.schedules.get(concatPrevClassNames(test4.getTotalClasses(), "")));

		addTest(totalClasses, constraints, schedule, "PHYS151", "PHYS151");

		// todo empty classname test
	}

	public static void addTest(ArrayList<AddClass> totalClasses,
	                           ArrayList<Constraint> constraints,
	                           ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> schedule,
	                           String className,
	                           String addClassKey) {
		System.out.println();
		AddClass addClass3 = new AddClass(className);
		totalClasses.add(addClass3);
		addClassKey = className;
		SchedulingThread test3 = new SchedulingThread(totalClasses, constraints, schedule, addClassKey);
		test3.run();
		System.out.println(getAllClassNames(totalClasses));
		System.out.println("Result size: " + test3.schedules.get(getAllClassNames(totalClasses)).size());
		// printPrettyPermutations(test3.schedules.get(concatPrevClassNames(test3.getTotalClasses(), "")));
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

	public static ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> getGeneralSchedules() {
		return generalSchedules;
	}

	public static void setGeneralSchedules(ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> generalSchedules) {
		SchedulingThread.generalSchedules = generalSchedules;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public String getAddClassKey() {
		return addClassKey;
	}

	public void setAddClassKey(String addClassKey) {
		this.addClassKey = addClassKey;
	}

	public static void addClassToGeneralSchedule(ArrayList<AddClass> totalClasses, String addClassKey) {
		for (AddClass totalClass : totalClasses) {
			if (!generalSchedules.containsKey(totalClass.getClassName())) {
				generalSchedules.put(addClassKey, totalClass.generatePermutations());
			}
		}
	}

	public static void deleteClass(ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> schedules, ArrayList<AddClass> totalClasses) {
		if(getAllClassNames(totalClasses).equals("")){
			schedules.put("",new ArrayList<ArrayList<Section>>());
		}
		if (schedules.containsKey(getAllClassNames(totalClasses))) {
			// since we already have the value stored in the hashtable, nothing need to do;
			System.out.println("DELETING CLASS. CURRENT totalClasses: " + totalClassesToString(totalClasses));
		} else {
			System.out.println("Error in deletion, current " + totalClassesToString(totalClasses) + " not in hashmap");
		}
	}

	public static ArrayList<AddClass> deleteLastClass(ArrayList<AddClass> totalClasses) {
		if (totalClasses.isEmpty()) {
			return totalClasses;
		}
		totalClasses.remove(totalClasses.size() - 1);
		return totalClasses;
	}

	public static String totalClassesToString(ArrayList<AddClass> totalClasses) {
		StringBuilder result = new StringBuilder("");
		result.append("[");
		for (int i = 0; i < totalClasses.size(); i++) {
			result.append(totalClasses.get(i).getClassName());
			result.append(" ");
		}
		result.append("]");
		return result.toString();
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
		String intendedClassName = getAllClassNames(totalClasses);
		ArrayList<AddClass> subsetClassList = subsetClassList(intendedClassName);
		schedules.put(intendedClassName, concatPermuations(subsetClassList));

	}

	// @SuppressWarnings("unchecked")
	public void run() {
		String allClassNames = getAllClassNames(totalClasses);

		// case for user delete one class
		if (addClassKey == null) {
			// the hashmap does not delete anything actually
			deleteClass(schedules, totalClasses);
			result = schedules.get(allClassNames);
			ready = true;
			return;
		}

		// case for constraint update
		// in default, we deem generalSchedules always have more elements than schedules
		if (schedules.containsKey(allClassNames)) {
			ArrayList<ArrayList<Section>> updatedPermuations = getPermutationWithConstraints(schedules.get(allClassNames), constraints);
			schedules.put(allClassNames, updatedPermuations);
			result = schedules.get(allClassNames);
			ready = true;
			return;
		}

		// Check if the class is in our generalSchedules hashmap; if not, create permutations,
		if (!generalSchedules.containsKey(addClassKey)) {
			addClassToGeneralSchedule(totalClasses, addClassKey);
		}
		ArrayList<ArrayList<Section>> validPermutations = generalSchedules.get(addClassKey);

		/* validPermutationUnderContraints will be the things inserted into our schedule hashtable. This is defined as a
		 * list of permutations that do not conflict with any constraint. Thus, they are considered a valid
		 * schedule for that particular class.  */
		ArrayList<ArrayList<Section>> validPermutationUnderContraints = new ArrayList<ArrayList<Section>>();
		validPermutationUnderContraints = getPermutationWithConstraints(validPermutations, constraints);

		schedules.put(addClassKey, validPermutationUnderContraints);

		/* Code to generate the key to find the set which we are finding the intersection of */
		String prevClass = "";
		prevClass = concatPrevClassNames(totalClasses, addClassKey);

		ArrayList<ArrayList<Section>> setIntersection = concatTwoPermutations(schedules.get(prevClass.trim()), validPermutationUnderContraints);

		// even it is null, we need to insert that
		String newIntersectionKey = prevClass + " " + addClassKey;
		schedules.put(newIntersectionKey.trim(), setIntersection);

		result = schedules.get(allClassNames);
		ready = true;
	}


}

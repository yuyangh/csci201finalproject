package csci201finalproject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

public class SchedulingThread extends Thread {

	private ArrayList<AddClass> totalClasses;
	private ArrayList<Constraint> constraints;
	private ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> concurrentSchedules;

	private Hashtable schedules;
	private Hashtable general;
	private String addClassKey;

	// will have to create a new instance of the thread with each time the algorithm is called
	public SchedulingThread(ArrayList<AddClass> totalClasses, ArrayList<Constraint> constraints, ConcurrentHashMap schedules) {
		System.out.println("In constructor");
		this.totalClasses = totalClasses;
		this.constraints = constraints;
		this.concurrentSchedules = schedules;

	}

	// will have to create a new instance of the thread with each time the algorithm is called
	// key must be passed in w/ constructor, run method cannot take parameters
	public SchedulingThread(ArrayList<AddClass> totalClasses, ArrayList<Constraint> constraints, Hashtable schedules,
	                        Hashtable general, String addClassKey) {
		System.out.println("In constructor");
		this.totalClasses = totalClasses;
		this.constraints = constraints;
		this.schedules = schedules;
		this.addClassKey = addClassKey;
		this.general = general;
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


	// skeleton
	public void run_2() {
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
				concurrentSchedules.put(className, permutations);
			}

		}
		//todo may change somehow about how to update the general schedule
		String intendedClassName = getIntendedClassName(totalClasses);
		ArrayList<AddClass> subsetClassList = subsetClassList(intendedClassName);
		concurrentSchedules.put(intendedClassName, concatPermuations(subsetClassList));

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


	/* TODO: get rid of this line and investigate these warnings */
	@SuppressWarnings("unchecked")
	public void run() {

        /* Check if the class is in our general hash table; if not, create all valid permutations,
        where a valid permutation is defined as a set of classes (ex: lecture, lab, quiz) that do not conflict with each other. */
		if (!general.contains(addClassKey)) {
			for (int i = 0; i < totalClasses.size(); i++) {
				if (totalClasses.get(i).getClassName().equals(addClassKey)) {
					general.put(addClassKey, totalClasses.get(i).generatePermutations());
				}
			}
		}

		ArrayList<ArrayList<Section>> validPermutations = (ArrayList<ArrayList<Section>>) general.get(addClassKey);
		/* validAgainstContraints will be the things inserted into our schedule hashtable. This is defined as a
		 * list of permutations that do not conflict with any constraint. Thus, they are considered a valid
		 * schedule for that particular class.  */
		ArrayList<ArrayList<Section>> validAgainstContraints = new ArrayList<ArrayList<Section>>();

		for (int i = 0; i < validPermutations.size(); i++) {
			/* Extract a single permutation (ex: lec, lab, quiz) of the class we just added */
			ArrayList<Section> singlePermutation = validPermutations.get(i);
			boolean stillValid = true;
			/* these for loops check whether each aspect of the permutation is valid against all the constraints.
			 * If even one aspect (a lec, lab, or quiz) does not comply with constraints, we flag that and break
			 * out of our loops accordingly */
			for (int j = 0; j < singlePermutation.size(); j++) {
				for (int k = 0; k < constraints.size(); k++) {
					Constraint singleConstraint = constraints.get(k);
					if (singlePermutation.get(j).doesConflict(singleConstraint.getStartTime(), singleConstraint.getEndTime(), singleConstraint.getDays())) {
						stillValid = false;
						break;
					}
				}
				if (!stillValid) {
					break;
				}
			}
			if (stillValid) {
				validAgainstContraints.add(singlePermutation);
			}
		}

		schedules.put(addClassKey, validAgainstContraints);

		/* Code to generate the key to find the set which we are finding the intersection of */
		String prevClass = "";
		for (int p = 0; p < totalClasses.size(); p++) {
			if (!totalClasses.get(p).getClassName().equals(addClassKey)) {
				prevClass += totalClasses.get(p).getClassName() + " ";
			}
		}
		/* the string key generated should be concatenation of all the classes inserted into table before addClass */
		/* EX: CS103 was added to our class schedule. Then we add CS104 and that goes into our schedules hashtable. If
		 * we want to insert CS201 next, we need to find the set intersection of the key CS201 with the set "CS103 CS104"*/
		/* This method of key generation will have to changed when it comes to removal. We take advantage of the order of
		 * and arraylist to generate this key */

		ArrayList<ArrayList<Section>> prevClassesSet = null;
		ArrayList<ArrayList<Section>> setIntersection = new ArrayList<ArrayList<Section>>();

		/* if prevClass is still an empty string, that means the class we are adding if the first
		 * class added (there would be no "previous" set of classes to create an intersection of).
		 * TODO: write code to handle that case. Should be simple enough. */
		if (schedules.containsKey(prevClass) && prevClass != "") {
			/* extracts the schedule developed for the previous classes (ex: "103 104")*/
			prevClassesSet = (ArrayList<ArrayList<Section>>) schedules.get(prevClass);
			/* we iterate through the valid single course schedule for the class we just added.
			 * ex: this would be the valid schedule that fits all the constraints for a single course
			 * called CSCI201 */
			for (int i = 0; i < validAgainstContraints.size(); i++) {
				/* this array list will add as the container for the new single schedule developed.
				 * For example, when one schedule for the single course CS201 is compared and has no
				 * conflict with a schedule developed for CS103 and CS104, this is the container that
				 * holds that new resulting schedule. NOTE: this also means that this arraylist has a
				 * variety of sections from DIFFERENT courses. Some parsing will be required to organize it
				 * by course again. */
				ArrayList<Section> validSingleSetInersection = new ArrayList<Section>();

				ArrayList<Section> singleCourseSchedule = validAgainstContraints.get(i);
				boolean stillValid = true;

				for (int k = 0; k < prevClassesSet.size(); k++) {
					/* extracts a single schedule for previous classes. ex: one mixture of lec, lab, quiz for
					 * CS103 and CS104 that do not conflict with each other. */
					ArrayList<Section> singlePrevClassSchedule = prevClassesSet.get(k);

					/* these for loops breakdown the aforementioned schedules back into comparable sections. */
					for (int oneSec = 0; oneSec < singleCourseSchedule.size(); oneSec++) {
						Section singleCourse = singleCourseSchedule.get(oneSec);
						for (int onePrev = 0; onePrev < singlePrevClassSchedule.size(); onePrev++) {
							Section singlePrev = singlePrevClassSchedule.get(onePrev);
							if (singleCourse.doesConflict(singlePrev.getStartTime(), singlePrev.getEndTime(), singlePrev.getDays())) {
								/* if even one section conflicts, then it would not be a valid schedule */
								stillValid = false;
								break;
							} else {
								/* otherwise, everything is fine for now and we want to push the section that
								 * works into the data set */
								validSingleSetInersection.add(singlePrev);
							}
						}
						if (stillValid) {
							/* if there were no conflicts at by the end, we can go ahead and push this section too. */
							validSingleSetInersection.add(singleCourse);
						} else {
							break;
						}
					}
					if (stillValid) {
						/* if all the sections from both schedules are compatabile, then the resulting
						 * schedule is part of the set intersection. */
						setIntersection.add(validSingleSetInersection);
					} else {
						break;
					}
				}
			}
			String newIntersectionKey = prevClass + " " + addClassKey;
			schedules.put(newIntersectionKey, setIntersection);
			/* this should be the end of the operation. to extract the new schedule, you just need the
			 * key, which is a combination of the names of all the courses. */
		} else {
			/* Develop this code to handle not finding the previous classes intersection; the problem
			 * will most likely be that we are not generating the right key for it to begin with. */
			System.out.println("Schedule for previous classes not found.");
		}
	}


	public static void main(String[] args) {
		new SchedulingThread(null, null, null);
	}


//        suppose we would like to add the class csci 103
//        check if the general hashtable has the CSCI103
//        if it does not, do the permutation and add to the general hashtable
//        // check permutation code on test.java
//        copy the CSCI103 in the general hashtable and check conflicts with the constraints
//        save all non-conflicting result in to the shcedule hashtable
//
//
//        suppose we would like to add the class CSCI103&CSCI104
//        check if the general hashtable has the CSCI103&CSCI104
//        if it does not, do the permutation and add to the general hashtable
//        add all non-conflicting result to the schedules hashtable


}

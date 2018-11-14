package csci201finalproject;

import java.util.ArrayList;
import java.util.Hashtable;

public class SchedulingThread extends Thread {

    private ArrayList<AddClass> totalClasses;
    private ArrayList<Constraint> constraints;
    private Hashtable schedules;

    // totalClasses.get(i).getTotalSections().get(i).doesConflict(string, string, arraylist)
    public SchedulingThread(ArrayList<AddClass> totalClasses, ArrayList<Constraint> constraints, Hashtable schedules) {
        System.out.println("In constructor");
        this.totalClasses = totalClasses;
        this.constraints = constraints;
        this.schedules = schedules;
    }

    public void run() {
        // calculate the permutation
        // get the result set
        synchronized(schedules){
            // insert into the hashtable
        }
    }

    
}

package csci201finalproject;

import java.util.ArrayList;
import java.util.Hashtable;

public class SchedulingThread extends Thread {

    private ArrayList<AddClass> totalClasses;
    private ArrayList<Constraint> constraints;
    private Hashtable schedules;

    // will have to create a new instance of the thread with each time the algorithm is called
    public SchedulingThread(ArrayList<AddClass> totalClasses, ArrayList<Constraint> constraints, Hashtable schedules) {
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
        // calculate the permutation
        // get the result set
        suppose we would like to add the class csci 103
        check if the general hashtable has the CSCI103
        if it does not, do the permutation and add to the general hashtable
        // check permutation code on test.java
        copy the CSCI103 in the general hashtable and check conflicts with the constraints
        save all non-conflicting result in to the shcedule hashtable


        suppose we would like to add the class CSCI103&CSCI104
        check if the general hashtable has the CSCI103&CSCI104
        if it does not, do the permutation and add to the general hashtable
        add all non-conflicting result to the schedules hashtable




        synchronized(schedules){
            // insert into the hashtable
        }
    }


}

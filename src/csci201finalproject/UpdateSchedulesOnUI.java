package csci201finalproject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

@WebServlet("/UpdateSchedulesOnUI")
public class UpdateSchedulesOnUI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// principle structures for storing our info and sending it to thread 
	private static ArrayList<AddClass> totalClasses;
	private static ArrayList<Constraint> totalConstraints;
	
	
    public UpdateSchedulesOnUI() {
        super();
        totalClasses = new ArrayList<AddClass>();
        totalConstraints = new ArrayList<Constraint>();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		// Retrieve session variables
		if(session.getAttribute("constraints") == null) {
			session.setAttribute("constraints", new ArrayList<ArrayList<String>>());
		}
		if(session.getAttribute("groups") == null) {
			session.setAttribute("groups", new ArrayList<ArrayList<ArrayList<String>>>());
		}
		ArrayList<ArrayList<String>> constraints = (ArrayList<ArrayList<String>>)session.getAttribute("constraints");
		ArrayList<ArrayList<ArrayList<String>>> groups = (ArrayList<ArrayList<ArrayList<String>>>)session.getAttribute("groups");
		
		// Convert session variables to parameters to pass into SchedulingThread's constructor
		
		
		// Start a new SchedulingThread thread and get the Hashtable of schedules from it
		
		
		// Output the returned schedules to the UI
	}
}

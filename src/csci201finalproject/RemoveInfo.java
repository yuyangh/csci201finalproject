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
import java.util.concurrent.ConcurrentHashMap;

/*
Fixing:
weird layout of buttons on friend scheduling

implementing:
navigating between login page, solo schedule page, and friend schedule page
database interactions in both pages
buttons on friend scheduling page
display generated schedules
*/

@WebServlet("/RemoveInfo")
public class RemoveInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public RemoveInfo() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		if(request.getParameter("action").equals("constraint")){
			// Update 'constraints' session variable
			if(session.getAttribute("constraints") == null) {
				session.setAttribute("constraints", new ArrayList<ArrayList<String>>());
			}
			ArrayList<ArrayList<String>> constraints = (ArrayList<ArrayList<String>>)session.getAttribute("constraints");
			if((Integer.parseInt(request.getParameter("constraint_num")) >= 0) && (Integer.parseInt(request.getParameter("constraint_num")) <= constraints.size()-1)) {
				constraints.remove(Integer.parseInt(request.getParameter("constraint_num")));
			}
			// Re-print UI
			for(int j = 0; j < constraints.size(); j++) {
				if(j == 0) {
					out.println("<div class=\"row h-100 first-constraint-row\">");
				}
				else {
					out.println("<div class=\"row h-100 constraint-row\">");
				}
        		out.println("<div class=\"col-3 h-100 class-entry\">");
        		out.println(buildDayString(constraints.get(j).get(0), constraints.get(j).get(1), constraints.get(j).get(2), constraints.get(j).get(3), constraints.get(j).get(4)));
        		out.println("</div>");
        		out.println("<div class=\"col-3 h-100 class-entry\">");
        		out.println(constraints.get(j).get(5));
        		out.println("</div>");
        		out.println("<div class=\"col-3 h-100 class-entry\">");
        		out.println(constraints.get(j).get(6));
        		out.println("</div>");
        		out.println("<div class=\"col-3 no-padding\">");
        		out.println("<button type=\"button\" class=\"btn btn-danger constraint-remove\" onclick=\"removeConstraint(" + j + ")\">Remove Constraint</button>");
        		out.println("</div>");
        		out.println("</div>");
        	}
			if(constraints.size() <= 0) {
				out.println("<div class=\"row first-constraint-row-add\">");
			}
			else {
				out.println("<div class=\"row constraint-row-add\">");
			}
        	out.println("<div class=\"col-3 days-block\">");
        	out.println("<div class=\"row\">");
        	out.println("<div class=\"col-1 no-padding\">");
        	out.println("</div>");
        	out.println("<div class=\"col-4 no-padding\">");
        	out.println("<label class=\"checkbox-label\"><input class=\"day-checkbox\" type=\"checkbox\" id=\"monday\"> Mon.</label>");
        	out.println("<label class=\"checkbox-label\"><input class=\"day-checkbox\" type=\"checkbox\" id=\"tuesday\"> Tues.</label><br>");
        	out.println("</div>");
        	out.println("<div class=\"col-4 no-padding\">");
        	out.println("<label class=\"checkbox-label\"><input class=\"day-checkbox\" type=\"checkbox\" id=\"wednesday\"> Wed.</label><br>");
        	out.println("<label class=\"checkbox-label\"><input class=\"day-checkbox\" type=\"checkbox\" id=\"thursday\"> Thurs.</label><br>");
        	out.println("</div>");
        	out.println("<div class=\"col-3 no-padding\">");
        	out.println("<label class=\"checkbox-label\"><input class=\"day-checkbox\" type=\"checkbox\" id=\"friday\"> Fri.</label><br>");
        	out.println("</div>");
        	out.println("</div>");
        	out.println("</div>");
        	out.println("<div class=\"col-3 no-padding\">");
        	out.println("<input type=\"text\" onfocus=\"(this.type='time')\" placeholder=\" Start Time\" class=\"class-entry\" id=\"start_time\">");
        	out.println("</div>");
        	out.println("<div class=\"col-3 no-padding\">");
        	out.println("<input type=\"text\" onfocus=\"(this.type='time')\" placeholder=\" End Time\" class=\"class-entry\" id=\"end_time\">");
        	out.println("</div>");
        	out.println("<div class=\"col-3 no-padding\">");
        	out.println("<button type=\"button\" class=\"btn btn-success constraint-add\" onclick=\"addConstraint()\">Add Constraint</button>");
        	out.println("</div>");
        	out.println("</div>");
        }
		else {
			// Update 'groups' session variable
			if(session.getAttribute("groups") == null) {
				session.setAttribute("groups", new ArrayList<ArrayList<ArrayList<String>>>());
			}
			ArrayList<ArrayList<ArrayList<String>>> groups = (ArrayList<ArrayList<ArrayList<String>>>)session.getAttribute("groups");
			if((Integer.parseInt(request.getParameter("group_num")) >= 0) && (Integer.parseInt(request.getParameter("group_num")) <= groups.size()-1)) {
				if(request.getParameter("action").equals("group")) {
		        	// Remove group from 'groups' session variable
					groups.remove(Integer.parseInt(request.getParameter("group_num")));
		        }
				else if(request.getParameter("action").equals("class")) {
		        	// Remove class from 'groups' session variable
					if(Integer.parseInt(request.getParameter("class_num")) < groups.get(Integer.parseInt(request.getParameter("group_num"))).size()) {
						groups.get(Integer.parseInt(request.getParameter("group_num"))).remove(Integer.parseInt(request.getParameter("class_num")));
					}
		        }
			}
			// Re-print UI
			for (int i = 0; i < groups.size() - 1; i++) {
				out.println("<div class=\"row h-100 header-row\">");
				out.println("<div class=\"col-4 h-100 group-header\">");
				out.println("Group " + (i + 1));
				out.println("</div>");
				out.println("<div class=\"col-4 no-padding\">");
				//out.println("<button type=\"button\" class=\"btn btn-danger button-remove-group\" onclick=\"removeGroup(" + i + ")\">Remove Group</button>");
				out.println("</div>");
				out.println("</div>");
				for (int j = 0; j < groups.get(i).size(); j++) {
					out.println("<div class=\"row h-100 class-row " + i + " \">");
					out.println("<div class=\"col-4 h-100 class-entry\">");
					out.println(groups.get(i).get(j).get(0));
					out.println("</div>");
					out.println("<div class=\"col-4 h-100 class-entry\">");
					out.println(groups.get(i).get(j).get(1));
					out.println("</div>");
					out.println("<div class=\"col-4 no-padding\">");
					//out.println("<button type=\"button\" class=\"btn btn-danger button-remove-class\" onclick=\"removeClass(" + i + "," + j + ")\">Remove Class</button>");
					out.println("</div>");
					out.println("</div>");
				}
				/*
				out.println("<div class=\"row class-row\">");
				out.println("<div class=\"col-4 no-padding\">");
				out.println("<input type=\"text\" class=\"class-input\" id=\"department_input" + i + "\" placeholder=\"Department Code\">");
				out.println("</div>");
				out.println("<div class=\"col-4 no-padding\">");
				out.println("<input type=\"text\" class=\"class-input\" id=\"number_input" + i + "\" placeholder=\"Course Number\">");
				out.println("</div>");
				out.println("<div class=\"col-4 no-padding\">");
				out.println("<button type=\"button\" class=\"btn btn-success button-add-class\" onclick=\"addClass(" + i + ")\">Add Class</button>");
				out.println("</div>");
				*/
				out.println("</div>");
			}
			if(groups.size() > 0) {
				int i = groups.size() - 1;
				out.println("<div class=\"row h-100 header-row\">");
				out.println("<div class=\"col-4 h-100 group-header\">");
				out.println("Group " + (i + 1));
				out.println("</div>");
				out.println("<div class=\"col-4 no-padding\">");
				out.println("<button type=\"button\" class=\"btn btn-danger button-remove-group\" onclick=\"removeGroup(" + i + ")\">Remove Group</button>");
				out.println("</div>");
				out.println("</div>");
				for (int j = 0; j < groups.get(i).size() - 1; j++) {
					out.println("<div class=\"row h-100 class-row " + i + " \">");
					out.println("<div class=\"col-4 h-100 class-entry\">");
					out.println(groups.get(i).get(j).get(0));
					out.println("</div>");
					out.println("<div class=\"col-4 h-100 class-entry\">");
					out.println(groups.get(i).get(j).get(1));
					out.println("</div>");
					out.println("<div class=\"col-4 no-padding\">");
					//out.println("<button type=\"button\" class=\"btn btn-danger button-remove-class\" onclick=\"removeClass(" + i + "," + j + ")\">Remove Class</button>");
					out.println("</div>");
					out.println("</div>");
				}
				if(groups.get(i).size() > 0) {
					int j = groups.get(i).size() - 1;
					out.println("<div class=\"row h-100 class-row " + i + " \">");
					out.println("<div class=\"col-4 h-100 class-entry\">");
					out.println(groups.get(i).get(j).get(0));
					out.println("</div>");
					out.println("<div class=\"col-4 h-100 class-entry\">");
					out.println(groups.get(i).get(j).get(1));
					out.println("</div>");
					out.println("<div class=\"col-4 no-padding\">");
					out.println("<button type=\"button\" class=\"btn btn-danger button-remove-class\" onclick=\"removeClass(" + i + "," + j + ")\">Remove Class</button>");
					out.println("</div>");
					out.println("</div>");
				}
				out.println("<div class=\"row class-row\">");
				out.println("<div class=\"col-4 no-padding\">");
				out.println("<input type=\"text\" class=\"class-input\" id=\"department_input" + i + "\" placeholder=\"Department Code\">");
				out.println("</div>");
				out.println("<div class=\"col-4 no-padding\">");
				out.println("<input type=\"text\" class=\"class-input\" id=\"number_input" + i + "\" placeholder=\"Course Number\">");
				out.println("</div>");
				out.println("<div class=\"col-4 no-padding\">");
				out.println("<button type=\"button\" class=\"btn btn-success button-add-class\" onclick=\"addClass(" + i + ")\">Add Class</button>");
				out.println("</div>");
				out.println("</div>");
			}
			out.println("<div class=\"row group-add-row\">");
			out.println("<div class=\"col-4 no-padding\">");
			out.println("<button type=\"button\" class=\"btn btn-success button-add-group\" onclick=\"addGroup()\">Add Group</button>");
			out.println("</div>");
			out.println("<div class=\"col-8 no-padding\">");
			out.println("</div>");
			out.println("</div>");
		}
		
		if(!((request.getParameter("constraint_num") != null && request.getParameter("constraint_num").equals("-1")) || (request.getParameter("group_num") != null && request.getParameter("group_num").equals("-1")))) {
			
			// todo start of Tristan's code
	
			if (session.getAttribute("schedules") == null) {
				ConcurrentHashMap<String, ArrayList<ArrayList<Section>>> schedules = new ConcurrentHashMap<String, ArrayList<ArrayList<Section>>>();
				session.setAttribute("schedules", schedules);
			}
			if(session.getAttribute("totalClasses") == null) {
				ArrayList<AddClass> totalClasses = new ArrayList<AddClass>();
				session.setAttribute("totalClasses", totalClasses);
			}
			if(session.getAttribute("constraintArrayList") == null) {
				ArrayList<Constraint> constraintArrayList = new ArrayList<Constraint>();
				session.setAttribute("constraintArrayList", constraintArrayList);
			}
			if(session.getAttribute("result") == null) {
				ArrayList<ArrayList<Section>> result = new ArrayList<ArrayList<Section>>();
				session.setAttribute("result", result);
			}
			
			String addClassKey = null;
			
			if(!request.getParameter("action").equals("group")) {
				
				// CASE FOR CONSTRAINT
				if (request.getParameter("action").equals("constraint") && Integer.parseInt(request.getParameter("constraint_num")) >= 0) {
					addClassKey = "";
					ArrayList<Constraint> constraintArrayList = (ArrayList<Constraint>) session.getAttribute("constraintArrayList");
	
					if(!constraintArrayList.isEmpty()) {
						constraintArrayList.remove(constraintArrayList.size()-1);
					}
					
					//SchedulingThread.printPrettyConstraints(constraintArrayList);
					
					session.setAttribute("constraintArrayList", constraintArrayList);
				}
				
				// CASE FOR REMOVE CLASSES
				else if(request.getParameter("action").equals("class")){
					ArrayList<AddClass> totalClasses = ((ArrayList<AddClass>)session.getAttribute("totalClasses"));
					totalClasses = SchedulingThread.deleteLastClass(totalClasses);
					session.setAttribute("totalClasses", totalClasses);
					System.out.println("totalClasses: "+totalClasses.size());
					System.out.print("Classes in totalClasses: ");
					SchedulingThread.printPrettyTotalClasses(totalClasses);
					
				}
				
				// with constructor, it will start to compute
				SchedulingThread schedulingThread = new SchedulingThread((ArrayList<AddClass>)session.getAttribute("totalClasses"), (ArrayList<Constraint>)session.getAttribute("constraintArrayList"),
						(ConcurrentHashMap<String, ArrayList<ArrayList<Section>>>) session.getAttribute("schedules"), addClassKey);
				System.out.println("Constraints in schedulingThread");
				SchedulingThread.printPrettyConstraints(schedulingThread.getConstraints());
				
				// we may optimize the code below to make it easier to access
				//System.out.println("total classes before calling thread"+(ArrayList<AddClass>)session.getAttribute("totalClasses"));
				//System.out.println("Key: " + schedulingThread.getAllClassNames((ArrayList<AddClass>)session.getAttribute("totalClasses")));
				
	//			while(!schedulingThread.isReady()) {
	//				Thread.yield();
	//			}
				
				while (!((ConcurrentHashMap<String, ArrayList<ArrayList<Section>>>)
						session.getAttribute("schedules")).containsKey(SchedulingThread.concatPrevClassNames((ArrayList<AddClass>)session.getAttribute("totalClasses"), ""))) {
		//			schedulingThread.yield();
					Thread.yield();
				}
				//System.out.println("isReady"+schedulingThread.isReady());
				//System.out.println("Result"+schedulingThread.getResult());
				
				
				// result is stored in the result
				//System.out.println("Constraint array: " + schedulingThread.getConstraints());
				//ArrayList<ArrayList<Section>> ourResults = schedulingThread.getSchedules().get(SchedulingThread.concatPrevClassNames((ArrayList<AddClass>)session.getAttribute("totalClasses"), ""));
				//System.out.println("Result set size: " + ourResults.size());
				//SchedulingThread.printPrettyPermutations(ourResults);
				session.setAttribute("result", schedulingThread.getSchedules().get(SchedulingThread.concatPrevClassNames((ArrayList<AddClass>)session.getAttribute("totalClasses"), "")));
				
				session.setAttribute("schedules", schedulingThread.getSchedules());
		
				// end of Tristan's code
			}
		}
	}
	public String buildDayString(String monday, String tuesday, String wednesday, String thursday, String friday) {
		String str = "";
		if(monday.equals("1")) {
			str += "M";
		}
		if(tuesday.equals("1")) {
			if(str != "") str += "/";
			str += "T";
		}
		if(wednesday.equals("1")) {
			if(str != "") str += "/";
			str += "W";
		}
		if(thursday.equals("1")) {
			if(str != "") str += "/";
			str += "Th";
		}
		if(friday.equals("1")) {
			if(str != "") str += "/";
			str += "F";
		}
		return str;
	}
}

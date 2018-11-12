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

/*
Fixing:

implementing:
adding new constraint
removing constraint
friend scheduling page
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
			
			
			
			// Re-print UI
			
			
			
        }
		else {
			// Update 'groups' session variable
			if(session.getAttribute("groups") == null) {
				session.setAttribute("groups", new ArrayList<ArrayList<ArrayList<String>>>());
			}
			ArrayList<ArrayList<ArrayList<String>>> groups = (ArrayList<ArrayList<ArrayList<String>>>)session.getAttribute("groups");
			if(request.getParameter("action").equals("group")) {
	        	// Remove group from 'groups' session variable
				groups.remove(Integer.parseInt(request.getParameter("group_num")));
	        }
			else if(request.getParameter("action").equals("class")) {
	        	// Remove class from 'groups' session variable
				groups.get(Integer.parseInt(request.getParameter("group_num"))).remove(Integer.parseInt(request.getParameter("class_num")));
	        }
			// Re-print UI
	        for(int i = 0; i < groups.size(); i++) {
	        	out.println("<div class=\"row h-100 header-row\">");
	        	out.println("<div class=\"col-4 h-100 group-header\">");
	        	out.println("Group " + (i+1));
	        	out.println("</div>");
	        	out.println("<div class=\"col-4 no-padding\">");
	        	out.println("<button type=\"button\" class=\"btn btn-danger button-remove\" onclick=\"removeGroup(" + i + ")\">Remove Group</button>");
	        	out.println("</div>");
	        	out.println("</div>");
	        	for(int j = 0; j < groups.get(i).size(); j++) {
	        		out.println("<div class=\"row h-100 class-row\">");
	        		out.println("<div class=\"col-4 h-100 class-entry\">");
	        		out.println(groups.get(i).get(j).get(0));
	        		out.println("</div>");
	        		out.println("<div class=\"col-4 h-100 class-entry\">");
	        		out.println(groups.get(i).get(j).get(1));
	        		out.println("</div>");
	        		out.println("<div class=\"col-4 no-padding\">");
	        		out.println("<button type=\"button\" class=\"btn btn-danger button-remove\" onclick=\"removeClass(" + i + "," + j + ")\">Remove Class</button>");
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
        		out.println("<button type=\"button\" class=\"btn btn-success button-add\" onclick=\"addClass(" + i + ")\">Add Class</button>");
        		out.println("</div>");
        		out.println("</div>");
	        }
        	out.println("<div class=\"row group-add-row\">");
        	out.println("<div class=\"col-4 no-padding\">");
        	out.println("<button type=\"button\" class=\"btn btn-success button-add\" onclick=\"addGroup()\">Add Group</button>");
        	out.println("</div>");
        	out.println("<div class=\"col-8 no-padding\">");
        	out.println("</div>");
        	out.println("</div>");
		}
	}

}

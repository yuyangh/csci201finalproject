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

@WebServlet("/UpdateSchedulesOnUI")
public class UpdateSchedulesOnUI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public UpdateSchedulesOnUI() {
        super();
    }
    
    private String formatTime(String raw) {
    	String result = raw;
    	if(!raw.equals("None")) {
    		result = result.substring(0, 5);
    		String am_pm = "AM";
			int hour = Integer.parseInt(result.substring(0,2));
			if(hour > 11){
				if(hour != 12) hour-=12;
				am_pm = "PM";
			}
			result = Integer.toString(hour) + ":" + result.substring(3,5) + " " + am_pm;
    	}
    	return result;
    }
    
    private String formatDays(ArrayList<Integer> list) {
    	String result = "";
    	if(list.contains(1)) {
    		result += "M";
    	}
    	if(list.contains(2)) {
    		if(!result.equals("")) result += "/";
    		result += "T";
    	}
    	if(list.contains(3)) {
    		if(!result.equals("")) result += "/";
    		result += "W";
    	}
    	if(list.contains(4)) {
    		if(!result.equals("")) result += "/";
    		result += "Th";
    	}
    	if(list.contains(5)) {
    		if(!result.equals("")) result += "/";
    		result += "F";
    	}
    	return result;
    }
    
    private String formatCode(String raw) {
    	return (raw.substring(0, raw.length()-3) + " " + raw.substring(raw.length()-3));
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		if (session.getAttribute("totalClasses") == null) {
			ArrayList<AddClass> totalClasses = new ArrayList<AddClass>();
			session.setAttribute("totalClasses", totalClasses);
		}
		if (session.getAttribute("result") == null) {
			ArrayList<ArrayList<Section>> result = new ArrayList<ArrayList<Section>>();
			session.setAttribute("result", result);
		}
		
		ArrayList<AddClass> totalClasses = (ArrayList<AddClass>) session.getAttribute("totalClasses");
		
		ArrayList<ArrayList<Section>> result = (ArrayList<ArrayList<Section>>) session.getAttribute("result");
		
		if(!request.getParameter("mode").equals("single")) {
			int numClasses = 0;
			numClasses = totalClasses.size();
			out.println("<div class=\"row h-100 header-row\">");
			out.println("<div class=\"col-12 h-100 no-padding\">");
			out.println("<u class=\"section-header\"> My Courses</u>");
			if(numClasses == 0) {
				out.println("<p class=\"schedule-header\">Add classes to begin generating schedules!</p>");
			}
			else if(numClasses == 1) {
				out.println("<p class=\"schedule-header\">You have added " + numClasses + " class.</p>");
			}
			else{
				out.println("<p class=\"schedule-header\">You have added " + numClasses + " classes.</p>");
			}
			out.println("</div>");
			out.println("</div>");
			// Output the number of friends in each class
			if(totalClasses.size() > 0) {
				for(AddClass addClass : totalClasses) {
					out.println("<div class=\"row h-100 schedule-key-row\">");
					out.println("<div class=\"col-8 h-100 no-padding\">");
					out.println("<button type=\"button\" class=\"schedule-class\">" + addClass.getClassName() + "</button>");
					out.println("</div>");
					out.println("<div class=\"col-4 h-100 no-padding\">");
					out.println("<button id=\"" + addClass.getClassName() + "\" type=\"button\" data-toggle=\"modal\" data-target=\"myModal\" class=\"btn btn-info schedule-class-button\" onclick=\"modalClicked(this.id);\">See Friends</button>");
					out.println("</div>");
					out.println("</div>");
				}
			}
		}
		
		int numSchedulesGenerated = 0;
		numSchedulesGenerated = result.size();
		
		out.println("<div class=\"row h-100 header-row\">");
		out.println("<div class=\"col-12 h-100 no-padding\">");
		out.println("<u class=\"section-header\"> My Schedules</u>");
		if(numSchedulesGenerated == 1) {
			out.println("<p class=\"schedule-header\">" + numSchedulesGenerated + " possible schedule fits your requirements.</p>");
		}
		else{
			out.println("<p class=\"schedule-header\">" + numSchedulesGenerated + " possible schedules fit your requirements.</p>");
		}
		out.println("</div>");
		out.println("</div>");
		// Output the returned schedules to the UI
		int counter = 1;
		for(ArrayList<Section> schedule : result) {
			out.println("<div class=\"row h-100 schedule-number-row\">");
			out.println("<div class=\"col-4 h-100 schedule-number\">");
			out.println("Schedule " + counter);
			out.println("</div>");
			out.println("</div>");
			out.println("<div class=\"row h-100 schedule-key-row\">");
			out.println("<div class=\"col-2 h-100 no-padding\">");
			out.println("<button type=\"button\" class=\"schedule-key\">Section #</button>");
			out.println("</div>");
			out.println("<div class=\"col-2 h-100 no-padding\">");
			out.println("<button type=\"button\" class=\"schedule-key\">Code</button>");
			out.println("</div>");
			out.println("<div class=\"col-2 h-100 no-padding\">");
			out.println("<button type=\"button\" class=\"schedule-key\">Type</button>");
			out.println("</div>");
			out.println("<div class=\"col-2 h-100 no-padding\">");
			out.println("<button type=\"button\" class=\"schedule-key\">Start Time</button>");
			out.println("</div>");
			out.println("<div class=\"col-2 h-100 no-padding\">");
			out.println("<button type=\"button\" class=\"schedule-key\">End Time</button>");
			out.println("</div>");
			out.println("<div class=\"col-2 h-100 no-padding\">");
			out.println("<button type=\"button\" class=\"schedule-key-last\">Days</button>");
			out.println("</div>");
			out.println("</div>");
			for(Section section : schedule) {
				out.println("<div class=\"row h-100 schedule-body-row\">");
				out.println("<div class=\"col-2 h-100 no-padding\">");
				out.println("<button type=\"button\" class=\"schedule-body\">" + section.getName() + "</button>");
				out.println("</div>");
				out.println("<div class=\"col-2 h-100 no-padding\">");
				out.println("<button type=\"button\" class=\"schedule-body\">" + formatCode(section.getClassCode()) + "</button>");
				out.println("</div>");
				out.println("<div class=\"col-2 h-100 no-padding\">");
				out.println("<button type=\"button\" class=\"schedule-body\">" + section.getType() + "</button>");
				out.println("</div>");
				out.println("<div class=\"col-2 h-100 no-padding\">");
				out.println("<button type=\"button\" class=\"schedule-body\">" + formatTime(section.getStartTime()) + "</button>");
				out.println("</div>");
				out.println("<div class=\"col-2 h-100 no-padding\">");
				out.println("<button type=\"button\" class=\"schedule-body\">" + formatTime(section.getEndTime()) + "</button>");
				out.println("</div>");
				out.println("<div class=\"col-2 h-100 no-padding\">");
				out.println("<button type=\"button\" class=\"schedule-body-last\">" + formatDays(section.getDays()) + "</button>");
				out.println("</div>");
				out.println("</div>");
			}
			counter++;
		}
	}
}

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
		
		out.println("<div class=\"row h-100 header-row\">");
		out.println("<div class=\"col-12 h-100 group-header\">");
		out.println("Friends in my classes");
		out.println("</div>");
		out.println("</div>");
		// Output the number of friends in each class
		if(totalClasses.size() > 0) {
			for(AddClass addClass : totalClasses) {
				out.println("<div class=\"row h-100 header-row\">");
				out.println("<div class=\"col-8 h-100 group-header\">");
				out.println(addClass.getClassName());
				out.println("</div>");
				out.println("<div class=\"col-4 h-100 no-padding\">");
				out.println("<button type=\"button\" class=\"btn btn-default\" >Num</button>");
				out.println("</div>");
				out.println("</div>");
			}
		}
		
		out.println("<div class=\"row h-100 header-row\">");
		out.println("<div class=\"col-12 h-100 group-header\">");
		out.println("Schedules");
		out.println("</div>");
		out.println("</div>");
		// Output the returned schedules to the UI
		int counter = 1;
		for(ArrayList<Section> schedule : result) {
			out.println("<div class=\"row h-100 header-row\">");
			out.println("<div class=\"col-4 h-100 group-header\">");
			out.println(counter);
			out.println("</div>");
			out.println("</div>");
			out.println("<div class=\"row h-100 header-row\">");
			out.println("<div class=\"col-2 h-100 group-header\">");
			out.println("Name");
			out.println("</div>");
			out.println("<div class=\"col-2 h-100 group-header\">");
			out.println("Type");
			out.println("</div>");
			out.println("<div class=\"col-2 h-100 group-header\">");
			out.println("Start Time");
			out.println("</div>");
			out.println("<div class=\"col-2 h-100 group-header\">");
			out.println("End Time");
			out.println("</div>");
			out.println("<div class=\"col-2 h-100 group-header\">");
			out.println("Days");
			out.println("</div>");
			out.println("<div class=\"col-2 h-100 group-header\">");
			out.println("Section #");
			out.println("</div>");
			out.println("</div>");
			for(Section section : schedule) {
				out.println("<div class=\"row h-100 header-row\">");
				out.println("<div class=\"col-12 h-100 group-header\">");
				out.println(section.toString());
				out.println("</div>");
				out.println("</div>");
			}
			counter++;
		}
	}
}

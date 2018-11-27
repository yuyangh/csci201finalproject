package csci201finalproject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class RegisterCourses
 */
@WebServlet("/RegisterCourses")
public class RegisterCourses extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterCourses() {
        super();
        // TODO Auto-generated constructor stub
    }

//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		doGet(request, response);
//	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("In register course servlet!!!");
		
		//Connection conn = null;
		//Statement st = null;
		//ResultSet rs = null;
		//Class.forName("com.mysql.cj.jdbc.Driver");
		//conn = DriverManager.getConnection(
		//		"jdbc:mysql://localhost/ScheduleMe?user=root&password=elma201&useSSL=false&AllowPublicKeyRetrieval=True&serverTimezone=PST");
		//st = conn.createStatement();
		
		HttpSession session = request.getSession();
//		String facebookID = (String)session.getAttribute("userID");
//		String name = (String)session.getAttribute("userName");
//		String email = (String)session.getAttribute("userEmail");
//		String img = (String)session.getAttribute("userPicURL");
		
		String email = request.getParameter("userEmail");
		String facebookID = request.getParameter("userID");
		String name = request.getParameter("userName");
		String img = request.getParameter("userPicURL");
		
		System.out.println("facebookID: " + facebookID);
		System.out.println("email: " + email);
		System.out.println("name: " + name);
		System.out.println("Pic url: " + img);
		
		User currentUser = new User(facebookID, name, img, email);
		
		ArrayList<AddClass> totalClasses = (ArrayList<AddClass>) session.getAttribute("totalClasses");
		ArrayList<String> strClasses = new ArrayList<String>();
		
		for(int i = 0; i < totalClasses.size(); i++) {
			System.out.println("Class: " + totalClasses.get(i).getClassName());
			strClasses.add(totalClasses.get(i).getClassName());
		}

	}

}

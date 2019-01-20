package csci201finalproject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetUsersTakingClassServlet
 */
@WebServlet("/GetUsersTakingClassServlet")
public class GetUsersTakingClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetUsersTakingClassServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("In get users servlet. ");
		PrintWriter out = response.getWriter();
		String classID = request.getParameter("classID");
		RetriveUsersInClass retrive = new RetriveUsersInClass(classID);
		ArrayList<Student> usersInClass = retrive.getUsersInClass();
		for(int i = 0; i < usersInClass.size(); i++) {
			Student single = usersInClass.get(i);
			System.out.println(single.username);
		}
		Gson gson = new Gson();
		String jsonData = gson.toJson(usersInClass);
		out.println(jsonData);
	}

}
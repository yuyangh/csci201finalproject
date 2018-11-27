package csci201finalproject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddUniqueUserServlet
 */
@WebServlet("/AddUniqueUserServlet")
public class AddUniqueUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddUniqueUserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userEmail = request.getParameter("userEmail");
		String userID = request.getParameter("userID");
		String userName = request.getParameter("userName");
		String userPicURL = request.getParameter("userPicURL");
		
		System.out.println("URL: " + userPicURL);

		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/ScheduleMe?user=root&password=elma201&useSSL=false&AllowPublicKeyRetrieval=True&serverTimezone=PST");
			ps = conn.prepareStatement("SELECT * FROM Users WHERE facebookID=? AND email=?");

			ps.setString(1, userID); // set first variable in prepared statement
			ps.setString(2, userEmail);
			rs = ps.executeQuery();
			if (!(rs.next())) {
				ps = conn.prepareStatement("INSERT INTO Users(facebookID,name,email,img) VALUES(?,?,?,?)");

				ps.setString(1, userID); // set first variable in prepared statement
				ps.setString(2, userName);
				ps.setString(3, userEmail);
				ps.setString(4, userPicURL);
				ps.executeUpdate();
			} else {
				System.out.println("User is already in database.");
			}
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ClassNotFoundException: " + cnfe.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
	}
}
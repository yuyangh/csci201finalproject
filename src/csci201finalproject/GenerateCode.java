package csci201finalproject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GenerateCode
 */
@WebServlet("/GenerateCode")
public class GenerateCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GenerateCode() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	System.out.println("Generating random code...");
    	String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	String digits = "0123456789";
    	
    	String values = letters + digits;
    	
    	Random rand = new Random();
    	
    	char[] code = new char[6];
    	
    	for (int i = 0; i < 6; i++) {
    		code[i] = values.charAt(rand.nextInt(values.length()));
    	}
    	
    	System.out.println(code);    	
    }

}

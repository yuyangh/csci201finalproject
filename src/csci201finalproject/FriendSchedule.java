package csci201finalproject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class FriendSchedule {
	private Vector<ServerThread> serverThreads;
	public FriendSchedule(int port) {
		try {
			System.out.println("Binding to port " + port);
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Bound to port " + port);
			serverThreads = new Vector<ServerThread>();
			while (true) {
				Socket s = ss.accept();		// blocking
				System.out.println("Connection from: " + s.getInetAddress());
				ServerThread st = new ServerThread(s, this);
				serverThreads.add(st);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in FriendSchedule constructor: " + ioe.getMessage());
		}
	}
	
	public void broadcast(User user, ServerThread st) {
		if (user != null) {
			System.out.println(user.getFacebookID() + " - " + user.getName());
			for (ServerThread serverThread : serverThreads) {
				if (st != serverThread) {
					
				}
			}
		}
	}
	
	public static void main(String[] args) {
		FriendSchedule fs = new FriendSchedule(6789);
	}
}

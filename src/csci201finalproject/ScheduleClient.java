package csci201finalproject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ScheduleClient extends Thread {
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	public ScheduleClient(String hostname, int port) {
		try {
			System.out.println("Trying to connect to " + hostname + ":" + port);
			Socket s = new Socket(hostname, port);
			System.out.println("Connected to " + hostname + ":" + port);
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe in ScheduleClient constructor: " + ioe.getMessage());
		}
	}
	
	public static void main(String[] args) {
		ScheduleClient sc = new ScheduleClient("localhost", 6789);
	}
}

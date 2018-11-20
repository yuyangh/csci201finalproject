package csci201finalproject;

public class Student {
	public String userID;
	public String username;
	public String userPicURL;
	public String userEmail;

	public Student(String userID, String username, String userPicURL, String userEmail) {
		this.userID = userID;
		this.username = username;
		this.userPicURL = userPicURL;
		this.userEmail = userEmail;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUserPicURL(String userPicURL) {
		this.userPicURL = userPicURL;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}

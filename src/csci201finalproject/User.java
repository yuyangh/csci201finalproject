package csci201finalproject;

public class User {
	private String facebookID;
	private String name;
	private String img;
	private String email;
	private int userDatabaseID;
	
	public User(String facebookID, String name, String img, String email) {
		this.facebookID = facebookID;
		this.name = name;
		this.img = img;
		this.email = email;
		this.userDatabaseID = -1;
	}

	public String getFacebookID() {
		return facebookID;
	}

	public void setFacebookID(String facebookID) {
		this.facebookID = facebookID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setUserDatabaseID(int id) {
		this.userDatabaseID = id;
	}
	
	public int getUserDatabaseID() {
		return userDatabaseID;
	}
 }
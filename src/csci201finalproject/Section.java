package csci201finalproject;

import java.util.ArrayList;

public class Section {
	private String SectionID;
	private String Type;
	private String StartTime;
	private String EndTime;
	private ArrayList<Integer> Days;
	
	public Section(String sectionID, String type, String startTime, String endTime, ArrayList<Integer> days) {
		this.SectionID = sectionID;
		this.Type = type;
		this.StartTime = startTime;
		this.EndTime = endTime;
		this.Days = days;
	}
	
	public String getSectionID() {
		return SectionID;
	}
	public void setSectionID(String sectionID) {
		this.SectionID = sectionID;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		this.Type = type;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		this.StartTime = startTime;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		this.EndTime = endTime;
	}
	public ArrayList<Integer> getDays() {
		return Days;
	}
	public void setDays(ArrayList<Integer> days) {
		this.Days = days;
	}
}

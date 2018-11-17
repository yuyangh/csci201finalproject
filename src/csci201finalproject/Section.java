package csci201finalproject;

import java.util.ArrayList;

public class Section {
	private String SectionID;
	private String SectionName;// sectionName on classes.usc
	private String Type;
	private String StartTime;
	private String EndTime;
	private ArrayList<Integer> Days;

	public Section(String sectionID, String SectionName,String type, String startTime, String endTime, ArrayList<Integer> days) {
			this.SectionID = sectionID;
			this.SectionName=SectionName;
			this.Type = type;
			this.StartTime = startTime;
			this.EndTime = endTime;
			this.Days = days;
		}

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

	public boolean doesConflict(String otherStartTime, String otherEndTime, ArrayList<Integer> otherDays) {
		for(int i = 0; i < this.Days.size(); i++) {
			for(int k = 0; k < otherDays.size(); k++) {
				// identify potential conflict
				if(otherDays.get(k) == this.Days.get(i)) {
					if(this.StartTime.compareTo(otherStartTime) == 0) return true;
					if(this.EndTime.compareTo(otherEndTime) == 0) return true;
					// if the otherStarTime is greater than the startTime but less than the end time => conflict
					if(this.StartTime.compareTo(otherStartTime) < 0 && this.EndTime.compareTo(otherStartTime) > 0) return true;
					if(this.StartTime.compareTo(otherEndTime) < 0 && this.EndTime.compareTo(otherEndTime) > 0) return true;
					if(this.StartTime.compareTo(otherStartTime)>0 && this.EndTime.compareTo(otherEndTime)<0) return true;
				}
			}
		}
		return false;
	}
}

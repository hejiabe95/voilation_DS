package com.zc.intf.entity;

import java.io.Serializable;

public class BasicTrainTeam implements Serializable {
   public int number; 
   public int id;
   public String teamName;
   public String teamNote;
   public String teamParentID;
   
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getTeamNote() {
		return teamNote;
	}
	public void setTeamNote(String teamNote) {
		this.teamNote = teamNote;
	}
	public String getTeamParentID() {
		return teamParentID;
	}
	public void setTeamParentID(String teamParentID) {
		this.teamParentID = teamParentID;
	} 
	   
   
 
   
   
   
  
}
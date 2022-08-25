package com.semanticsquare.thrillio.constants;

public enum UserType {

	USER("user"),
	EDITOR("editor"),
	CHIEF_EDITOR("chiefeditor");

	private UserType (String userType) {
		this.userType = userType;
	}
	
	private String userType;

	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
}

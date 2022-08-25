package com.semanticsquare.thrillio.constants;

public enum KidFriendlyStatus {

	APPROVED("approved"),
	REJECTED("rejected"),
	UNKNOWN("unknown");

	private KidFriendlyStatus (String status) {
		this.setStatus(status);
	}

	private String status;
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	
	
}

package com.semanticsquare.thrillio.entities;

import com.semanticsquare.thrillio.constants.KidFriendlyStatus;

public abstract class Bookmark {
	private long id;
	private String title;
	private String profileUrl;
	private KidFriendlyStatus kidFriendlyStatus = KidFriendlyStatus.UNKNOWN;
	private User kidFriendlyMarkedBy; 
	private User sharedBy;
	
	public long getId() {
		return id;
	}

	public KidFriendlyStatus getKidFriendlyStatus() {
		return kidFriendlyStatus;
	}

	public void setKidFriendlyStatus(KidFriendlyStatus kidFriendlyStatus) {
		this.kidFriendlyStatus = kidFriendlyStatus;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public abstract boolean isKidFriendlyEligible();

	public void setSharedBy(User user) {
		this.sharedBy = user;
		
	}
	public User getSharedBy () {
		return sharedBy;
	}

	public void setKidFriendlyMarkedBy(User user) {
		this.kidFriendlyMarkedBy = user;
		
	}

	public User getKidFriendlyMarkedBy() {
		return kidFriendlyMarkedBy;
	}

}

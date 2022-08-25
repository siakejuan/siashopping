package com.semanticsquare.thrillio;

import java.sql.SQLException;
import java.util.List;

import com.semanticsquare.thrillio.bgjobs.WebpageDownloaderTask;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.managers.BookmarkManager;
import com.semanticsquare.thrillio.managers.UserManager;

public class Launch {

	private static List<User> users;
	private static List<List<Bookmark>> bookmarks;

	private static void loadData() {
		System.out.println("1. Loading Data...");
		DataStore.loadData();

		users = UserManager.getInstance().getUsers();
		bookmarks = BookmarkManager.getInstance().getBookmarks();

//		System.out.println("Printing data...");
//		printUserData();
//		printBookmarkData();

	}

	private static void printBookmarkData() {
		for (List<Bookmark> bookmarkList : bookmarks) {
			for (Bookmark bookmark : bookmarkList) {
				System.out.println(bookmark);
			}
		}

	}

	private static void printUserData() {
		for (User user : users) {
			System.out.println(user);
		}

	}

	private static void start() throws SQLException {
		// System.out.println("\n2. Bookmarking...");
		for (User user : users) {
			// View.bookmark(user, bookmarks);
			View.browse(user, bookmarks);
		}
	}

	/*
	 * private static void startBookmarking() {
	 * System.out.println("\n2. Bookmarking..."); for (User user: users) { //
	 * View.bookmark(user, bookmarks); View.browse(user, bookmarks); } }
	 */
	private static void runDownloaderJob() {
		WebpageDownloaderTask task = new WebpageDownloaderTask(true);
		(new Thread(task)).start();
	}

	public static void main(String[] args) throws SQLException {
		loadData();

		// startBookmarking();
		System.out.println("\n bookmarking starts...");
		start();

		// Background jobs
		// runDownloaderJob();
	}

}

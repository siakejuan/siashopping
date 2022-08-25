package com.semanticsquare.thrillio;

import java.sql.SQLException;
import java.util.List;

import com.semanticsquare.thrillio.constants.KidFriendlyStatus;
import com.semanticsquare.thrillio.constants.UserType;
import com.semanticsquare.thrillio.controllers.BookmarkController;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.partner.Shareable;

public class View {
	/*
	 * public static void bookmark(User user, Bookmark[][] bookmarks) {
	 * System.out.println("\n" + user.getEmail() + " is bookmarking"); for (int i =
	 * 0; i < DataStore.TOTAL_USER_COUNT; i++) { int typeOffset = (int)
	 * (Math.random() * DataStore.BOOKMARK_TYPES_COUNT); int bookmarkOffset = (int)
	 * (Math.random() * DataStore.BOOKMARK_COUNT_PER_TYPE);
	 *
	 * Bookmark bookmark = bookmarks[typeOffset][bookmarkOffset];
	 *
	 * BookmarkController.getInstance().saveUserBookmark(user, bookmark);
	 *
	 * System.out.println(bookmark); } }
	 */

	public static void browse(User user, List<List<Bookmark>> bookmarks) throws SQLException {
		System.out.println("\n" + user.getEmail() + " is browsing items...");
		int bookmarkCount = 0;

		for (List<Bookmark> bookmarkList : bookmarks) {
			for (Bookmark bookmark : bookmarkList) {
				// Bookmarking!!
				// if (bookmarkCount < DataStore.USER_BOOKMARK_LIMIT) {
				boolean isBookmarked = getBookmarkDecision(bookmark);
				if (isBookmarked) {
					bookmarkCount++;

					// Controller in the backend receives the request to save as bookmark
					BookmarkController.getInstance().saveUserBookmark(user, bookmark);

					System.out.println("New item bookmarked -- " + bookmark);
				}
				// }

				if (user.getUserType().equals(UserType.CHIEF_EDITOR) || user.getUserType().equals(UserType.EDITOR)) {

					// Mark as kid-friendly
					if (bookmark.isKidFriendlyEligible()
							&& bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.UNKNOWN)) {
						KidFriendlyStatus kidFriendlyStatus = getKidFriendlyStatusDecision();

						if (!kidFriendlyStatus.equals(KidFriendlyStatus.UNKNOWN)) {
							BookmarkController.getInstance().setKidFriendlyStatus(user, kidFriendlyStatus, bookmark);
						}
					}
				}

				// Sharing
				if(bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.APPROVED) 
						&& bookmark instanceof Shareable) {
					boolean isShared = getShareDecision();
					if(isShared) {
						BookmarkController.getInstance().share(user, bookmark);
					}
				}
			}
		}

	}

	private static KidFriendlyStatus getKidFriendlyStatusDecision() {

		double decision = Math.random();

		return decision < 0.4 ? KidFriendlyStatus.APPROVED
				: (decision > 0.4 && decision < 0.8) ? KidFriendlyStatus.REJECTED : KidFriendlyStatus.UNKNOWN;

	}

	private static boolean getBookmarkDecision(Bookmark bookmark) {
		return Math.random() < 0.5 ? true : false;

	}

	private static boolean getShareDecision() {
		return Math.random() < 0.5 ? true : false;
	}
}

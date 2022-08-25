package com.semanticsquare.thrillio;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.semanticsquare.thrillio.constants.BookGenre;
import com.semanticsquare.thrillio.constants.Gender;
import com.semanticsquare.thrillio.constants.MovieGenre;
import com.semanticsquare.thrillio.constants.UserType;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.entities.UserBookmark;
import com.semanticsquare.thrillio.managers.BookmarkManager;
import com.semanticsquare.thrillio.managers.UserManager;
import com.semanticsquare.thrillio.util.IOUtil;

public class DataStore {
	public static final int USER_BOOKMARK_LIMIT = 5;
	public static final int BOOKMARK_COUNT_PER_TYPE = 5;
	public static final int BOOKMARK_TYPES_COUNT = 3;
	public static final int TOTAL_USER_COUNT = 5;
	public static List<User> users = new ArrayList<>();

	public static List<User> getUsers() {
		return users;
	}

//	public static Bookmark[][] bookmarks = new Bookmark[BOOKMARK_TYPES_COUNT][BOOKMARK_COUNT_PER_TYPE];
	public static List<List<Bookmark>> bookmarks = new ArrayList<>();

	public static List<List<Bookmark>> getBookmarks() {
		return bookmarks;
	}

//	public static UserBookmark[] userBookmarks = new UserBookmark[TOTAL_USER_COUNT * USER_BOOKMARK_LIMIT];
	public static List<UserBookmark> userBookmarks = new ArrayList<>();
//	private static int bookmarkIndex;

	public static void loadData() {

		// load from text files
		/*
		 * loadUsers(); loadWebLinks(); loadMovies(); loadBooks();
		 */

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// new com.mysql.jdbc.Driver();
			// OR
			// System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
			// OR java.sql.DriverManager
			// DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?userSSL=false",
				"root", "root"); Statement stmt = conn.createStatement();) {
			loadUsers(stmt);
			loadWebLinks(stmt);
			loadMovies(stmt);
			loadBooks(stmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void loadWebLinks(Statement stmt) throws SQLException {

//		String[] data = new String[BOOKMARK_COUNT_PER_TYPE];
		List<String> data = new ArrayList<>();
//		IOUtil.read(data, "WebLink");
//    	int colNum = 0;
		
		String query = "select id, title, url, host from weblink";
		ResultSet rs = stmt.executeQuery(query);
		
		List<Bookmark> bookmarkList = new ArrayList<>();
		while (rs.next()) {
//			String[] values = row.split("\t");
			// parameters required : long id, String title, String url, String host
//			Bookmark bookmark = BookmarkManager.getInstance().createWebLink(Long.parseLong(values[0]), values[1],
//					values[2], values[3]/* , values[4] */);
			int id = rs.getInt("id");
			String title = rs.getString("title");
			String url = rs.getString("url");
			String host = rs.getString("host");
			Bookmark bookmark = BookmarkManager.getInstance().createWebLink(id, title, url, host);
			bookmarkList.add(bookmark);
		}

		bookmarks.add(bookmarkList);
	}

	private static void loadMovies(Statement stmt) throws SQLException {

//		String[] data = new String[BOOKMARK_COUNT_PER_TYPE];
//		List<String> data = new ArrayList<>();
		List<Bookmark> listBookmark = new ArrayList<>();
//		IOUtil.read(data, "Movie");
//    	int colNum = 0;
		
		String query = "Select m.id, title, release_year, GROUP_CONCAT(DISTINCT a.name SEPARATOR ',') AS cast, GROUP_CONCAT(DISTINCT d.name SEPARATOR ',') AS directors, movie_genre_id, imdb_rating"
				+ " from Movie m, Actor a, Movie_Actor ma, Director d, Movie_Director md "
				+ "where m.id = ma.movie_id and ma.actor_id = a.id and "
				      + "m.id = md.movie_id and md.director_id = d.id group by m.id";
		ResultSet rs = stmt.executeQuery(query);
		
		while (rs.next()) {
//			String[] values = row.split("\t");
//			String[] cast = values[3].split(",");
//			String[] directors = values[4].split(",");
//			Bookmark bookmark = BookmarkManager.getInstance().createMovie(Long.parseLong(values[0]), values[1], "",
//					Integer.parseInt(values[2]), cast, directors, MovieGenre.valueOf(values[5]),
//					Double.parseDouble(values[6])/* , values[7] */);
			long id = rs.getLong("id");
			String title = rs.getString("title");
			int releaseYear = rs.getInt("release_year");
			String[] cast = rs.getString("cast").split(",");
			String[] directors = rs.getString("directors").split(",");			
			int genre_id = rs.getInt("movie_genre_id");
			MovieGenre genre = MovieGenre.values()[genre_id];
			double imdbRating = rs.getDouble("imdb_rating");
			
			Bookmark bookmark = BookmarkManager.getInstance().createMovie(id, title, "", releaseYear, cast, directors, genre, imdbRating/*, values[7]*/);
    		
			listBookmark.add(bookmark);
		}

		bookmarks.add(listBookmark);
	}

	private static void loadBooks(Statement stmt) throws SQLException {

//		String[] data = new String[BOOKMARK_COUNT_PER_TYPE];
		/*
		 * List<String> data = new ArrayList<>(); List<Bookmark> listBookmark = new
		 * ArrayList<>(); IOUtil.read(data, "Book");
		 */
//    	int colNum = 0;
		System.out.println("\nLoading books....");
		String query = "Select b.id, title, publication_year, p.name, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, amazon_rating, created_date"
				+ " from Book b, Publisher p, Author a, Book_Author ba "
				+ "where b.publisher_id = p.id and b.id = ba.book_id and ba.author_id = a.id group by b.id";
		ResultSet rs = stmt.executeQuery(query);

		List<Bookmark> bookmarkList = new ArrayList<>();
		while (rs.next()) {
			/*
			 * String[] values = row.split("\t"); String[] authors = values[4].split(",");
			 * Bookmark bookmark =
			 * BookmarkManager.getInstance().createBook(Long.parseLong(values[0]),
			 * values[1], Integer.parseInt(values[2]), values[3], authors, values[5],
			 * Double.parseDouble(values[6]) , values[7] ); listBookmark.add(bookmark);
			 */
			long id = rs.getLong("id");
			String title = rs.getString("title");
			int publicationYear = rs.getInt("publication_year");
			String publisher = rs.getString("name");
			String[] authors = rs.getString("authors").split(",");
			int genre_id = rs.getInt("book_genre_id");
			BookGenre genre = BookGenre.values()[genre_id];
			double amazonRating = rs.getDouble("amazon_rating");

			Date createdDate = rs.getDate("created_date");
			System.out.println("createdDate: " + createdDate);
			Timestamp timeStamp = rs.getTimestamp(8);
			System.out.println("timeStamp: " + timeStamp);
			System.out.println("localDateTime: " + timeStamp.toLocalDateTime());

			System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear
					+ ", publisher: " + publisher + ", authors: " + String.join(", ", authors) + ", genre: " + genre
					+ ", amazonRating: " + amazonRating);

			System.out
					.println("_______________________________________________________________________________________");
			Bookmark bookmark = BookmarkManager.getInstance().createBook(id, title, publicationYear, publisher, authors,
					genre, amazonRating/* , values[7] */);
			bookmarkList.add(bookmark);
		}
		bookmarks.add(bookmarkList);
	}

	private static void loadUsers(Statement stmt) throws SQLException {

//		String[] data = new String[TOTAL_USER_COUNT];
//		List<String> data = new ArrayList<>();
//		IOUtil.read(data, "User");

		String query = "Select * from User";

		ResultSet rs = stmt.executeQuery(query);

//		int rowNum = 0;

		while (rs.next()) {
			// String[] values = row.split("\t");
			int gender_id = rs.getInt("gender_id");
			Gender gender = Gender.values()[gender_id];
			if (gender.equals("f")) {
				gender = Gender.FEMALE;
			} else if (gender.equals("t")) {
				gender = Gender.TRANSGENDER;
			}
			int id = rs.getInt("id");
			String email = rs.getString("email");
			String password = rs.getString("password");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			int userTypeId = rs.getInt("user_type_id");
			UserType userType = UserType.values()[userTypeId];
			// long id, String email, String password,
			// String firstName, String lastName, Gender gender, String userType
			User user = UserManager.getInstance().createUser(id, email, password, firstName,
					lastName, gender, userType);

			users.add(user);
		}

	}

	public static void add(UserBookmark userBookmark) {
//		userBookmarks[bookmarkIndex] = userBookmark;
		userBookmarks.add(userBookmark);
//		bookmarkIndex++;
	}

}

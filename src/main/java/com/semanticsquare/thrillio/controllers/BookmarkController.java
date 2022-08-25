package com.semanticsquare.thrillio.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.semanticsquare.thrillio.constants.KidFriendlyStatus;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.managers.BookmarkManager;
import com.semanticsquare.thrillio.managers.UserManager;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/bookmark", "/bookmark/save", "/bookmark/mybooks" })
public class BookmarkController extends HttpServlet {
	/*
	 * // Tomcat creates a singleton for us private static BookmarkController
	 * instance = new BookmarkController();
	 * 
	 * private BookmarkController() { }
	 * 
	 * public static BookmarkController getInstance() { return instance; }
	 */
	public BookmarkController() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// initialize RequestDispatcher to null
		RequestDispatcher dispatcher = null;
		System.out.println("Servlet path : " + request.getServletPath());

		// check if session is still valid
		if (request.getSession().getAttribute("userId") != null) {
			long userId = (long) request.getSession().getAttribute("userId");

			// clicked on 'save'
			if (request.getServletPath().contains("save")) {
				dispatcher = request.getRequestDispatcher("/mybooks.jsp");
				// get value passed in .jsp
				String bid = request.getParameter("bid");

				// get data from database
				User user = UserManager.getInstance().getUser(userId);
				Bookmark book = BookmarkManager.getInstance().getBook(Long.parseLong(bid));
				BookmarkManager.getInstance().saveUserBookmark(user, book);

				Collection<Bookmark> list = BookmarkManager.getInstance().getBooks(true, userId);
				request.setAttribute("books", list);

			} else if (request.getServletPath().contains("mybooks")) { // clicked on 'mybooks'
				dispatcher = request.getRequestDispatcher("/mybooks.jsp");
				// pass true: get only books that are saved by the user
				Collection<Bookmark> list = BookmarkManager.getInstance().getBooks(true, userId);
				request.setAttribute("books", list);
			} else { // clicked on 'browse'
				dispatcher = request.getRequestDispatcher("/browse.jsp");
				// pass false: get books that are not saved by user
				Collection<Bookmark> list = BookmarkManager.getInstance().getBooks(false, userId);
				request.setAttribute("books", list);
			}

			
		} else {
			dispatcher = request.getRequestDispatcher("/login.jsp");
		}
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	// Bookmark manager will receive input from Controller layer to save bookmark
	// into DAO
	public void saveUserBookmark(User user, Bookmark bookmark) throws SQLException {
		BookmarkManager.getInstance().saveUserBookmark(user, bookmark);

	}

	public void setKidFriendlyStatus(User user, KidFriendlyStatus kidFriendlyStatus, Bookmark bookmark)
			throws SQLException {
		BookmarkManager.getInstance().setKidFriendlyStatus(user, kidFriendlyStatus, bookmark);
	}

	public void share(User user, Bookmark bookmark) {
		BookmarkManager.getInstance().share(user, bookmark);
	}
}

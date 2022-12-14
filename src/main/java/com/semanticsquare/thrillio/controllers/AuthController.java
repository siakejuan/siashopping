package com.semanticsquare.thrillio.controllers;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.semanticsquare.thrillio.managers.UserManager;

/**
 * Servlet implementation class Login
 */
@WebServlet(urlPatterns = {"/auth", "/auth/logout"})
public class AuthController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = null;
		if (!request.getServletPath().contains("logout")) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			if (Objects.equals(password, null)) {
				password="";
			}
			
			long userId = UserManager.getInstance().authenticate(email, password); 
			if(userId != -1) {
				// session default timeout 30 minutes 
				// user will be directed to login page after 30 minutes 
				// session.setMaxTimeInterval(value) to change session time
				HttpSession session = request.getSession();
				session.setAttribute("userId", userId);
				// forwarding to servlet
				request.getRequestDispatcher("bookmark/mybooks").forward(request, response);
			}else {
				
				// forwarding to .jsp
				request.getRequestDispatcher("/login.jsp").forward(request, response);
				
			}
		} else {
			request.getSession().invalidate();
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

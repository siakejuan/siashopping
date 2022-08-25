package com.semanticsquare.thrillio.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.semanticsquare.thrillio.DataStore;
import com.semanticsquare.thrillio.constants.Gender;
import com.semanticsquare.thrillio.constants.UserType;
import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.managers.UserManager;

public class UserDao {
	public List<User> getUsers() {
		return DataStore.getUsers();
	}

	public User getUser(long userId) {

		User user = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/siashopping?userSSL=false",
				"root", "root"); Statement stmt = conn.createStatement();) {

			String query = "Select * from User where id = " + userId;

			ResultSet rs = stmt.executeQuery(query);

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
				user = UserManager.getInstance().createUser(id, email, password, firstName, lastName, gender, userType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public long authenticate(String email, String encodePassword) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/siashopping?useSSL=false", "root", "root");
				Statement stmt = conn.createStatement();) {	
			String query = "Select id from User where email = '" + email + "' and password = '" + encodePassword + "'";
			System.out.println("query: " + query);
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				return rs.getLong("id");				
	    	}			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return -1;
	}

}

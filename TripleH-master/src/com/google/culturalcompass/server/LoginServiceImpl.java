package com.google.culturalcompass.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.culturalcompass.client.LoginInfo;
import com.google.culturalcompass.client.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.culturalcompass.client.NotLoggedInException;
import com.google.culturalcompass.server.LoginServiceImpl;
import com.google.culturalcompass.shared.Place;

public class LoginServiceImpl extends RemoteServiceServlet implements
LoginService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService = UserServiceFactory.getUserService();
	static DatastoreService data= DatastoreServiceFactory.getDatastoreService();
	@Override
	public LoginInfo loginAdmin(String requestUri) {
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();

		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setAdmin(userService.isUserAdmin());
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
	}
	
	@Override
	public boolean login(String username, String password) {
		try {
			Entity user = findUser(username);
			return (password.equals(user.getProperty("Password")));
		} catch (EntityNotFoundException e) {
			return false;
		}
	}
	@Override
	public boolean register(String username, String password, String email, String gender,String question,String answer) {
		
		try {
			Entity user =findUser(username);
			return false;
		} catch (EntityNotFoundException e) {
			Entity newUser = new Entity(KeyFactory.createKey("User", username));
			newUser.setProperty("Password", password);
			newUser.setProperty("Email", email);
			newUser.setProperty("Gender", gender);
			newUser.setProperty("Question", question);
			newUser.setProperty("Answer", answer);
			data.put(newUser);
			return true; 
		}
	}

	
	
	public static Entity findUser(String username) throws EntityNotFoundException {
	
		Entity user =data.get(KeyFactory.createKey("User", username));
		return user;
	}
	
	public boolean addPlace(String username, ArrayList<String> p) {
	
		Entity user;
		try {
			user = findUser(username);
			user.setProperty("My Places", p);
			data.put(user);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		
		return true;
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> showmyPlace(String username) {
		Entity user;
		ArrayList<String> p = null;
		try {
			user = findUser(username);
			p = (ArrayList<String>) user.getProperty("My Places");
			
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		return p;
		
	}
	
	public boolean removePlaces(String username){
		Entity user;
		try {
			user = findUser(username);
			user.removeProperty("My Places");
		}
		catch (EntityNotFoundException e){
			e.printStackTrace();
		}
		return true;
	}
	

	
	

}
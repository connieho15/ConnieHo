package com.google.culturalcompass.client;

import java.util.ArrayList;

import com.google.culturalcompass.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	void login(String username, String password, AsyncCallback<Boolean> async);

	void register(String username, String password, String email, String gender, String question,String answer,AsyncCallback<Boolean> callback);
	
	public void loginAdmin(String requestUri, AsyncCallback<LoginInfo> async);
	
	public void addPlace(String username, ArrayList<String> place, AsyncCallback<Boolean> async);
	public void showmyPlace(String username, AsyncCallback<ArrayList<String>> async);
	public void removePlaces(String username, AsyncCallback<Boolean> async);
}


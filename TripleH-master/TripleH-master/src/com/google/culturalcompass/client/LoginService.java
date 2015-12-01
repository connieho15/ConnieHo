package com.google.culturalcompass.client;

import java.util.ArrayList;

import com.google.culturalcompass.shared.Place;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
  
boolean login(String username, String password);
boolean register(String username, String password, String email, String gender,String question,String answer);

public LoginInfo loginAdmin(String requestUri);
boolean addPlace(String username, ArrayList<String> place);
ArrayList<String> showmyPlace(String username);
boolean removePlaces(String username);

  
  
  
}
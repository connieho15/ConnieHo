package com.google.culturalcompass.client;

import java.util.List;

import com.google.culturalcompass.shared.Place;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("place")
public interface PlaceService extends RemoteService {
   Place getPlace(String input);
   public void importData();
   public List<Place> searchingPlace(String input);
}
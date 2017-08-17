package com.google.culturalcompass.client;

import java.util.List;

import com.google.culturalcompass.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PlaceServiceAsync {
	
	public void getPlace(String input, AsyncCallback<Place> callback);
	void importData(AsyncCallback<Void> callback);
	public void searchingPlace(String input, AsyncCallback<List<Place>> callback);
}

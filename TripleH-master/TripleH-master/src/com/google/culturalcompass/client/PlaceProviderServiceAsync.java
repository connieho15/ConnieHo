package com.google.culturalcompass.client;

import com.google.culturalcompass.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PlaceProviderServiceAsync {
	void getPlaceProviders(AsyncCallback<Place[]> callback);
	void setPlaceProvider(Place place, AsyncCallback<Void> callback);
	
}

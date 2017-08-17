package com.google.culturalcompass.client;

import com.google.culturalcompass.client.NotLoggedInException;
import com.google.culturalcompass.shared.Place;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("provider")
public interface PlaceProviderService extends RemoteService {
	public Place[] getPlaceProviders() throws NotLoggedInException;
	public void setPlaceProvider(Place place);
}

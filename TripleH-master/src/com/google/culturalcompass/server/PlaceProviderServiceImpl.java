package com.google.culturalcompass.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.culturalcompass.client.NotLoggedInException;
import com.google.culturalcompass.client.PlaceProviderService;
import com.google.culturalcompass.server.PMF;
import com.google.culturalcompass.shared.Place;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class PlaceProviderServiceImpl extends RemoteServiceServlet implements
		PlaceProviderService {

	@Override
	public Place[] getPlaceProviders() throws NotLoggedInException {
		ArrayList<Place> detachedPlaces = new ArrayList<Place>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.getFetchPlan().setGroup(FetchPlan.ALL);
		Query q = pm.newQuery(Place.class);
		q.setOrdering("name ascending");
		try {
			@SuppressWarnings("unchecked")
			List<Place> results = (List<Place>) q.execute();
			detachedPlaces.addAll(pm.detachCopyAll(results));
		} finally {
			q.closeAll();
			pm.close();
		}
		return (Place[]) detachedPlaces.toArray(new Place[0]);
	}

	@Override
	public void setPlaceProvider(Place place) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(place);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
	}

}

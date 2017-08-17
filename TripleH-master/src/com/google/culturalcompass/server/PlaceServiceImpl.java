package com.google.culturalcompass.server;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import com.google.culturalcompass.client.PlaceService;
import com.google.culturalcompass.server.PMF;
import com.google.culturalcompass.shared.Place;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PlaceServiceImpl extends RemoteServiceServlet implements
		PlaceService {

	private static final long serialVersionUID = 6717835196177737876L;

	private URL url;
	private InputStream inputStream;
	private Workbook workbook;
	private Sheet sheet;
	private ArrayList<Place> placesToStore;

	public PlaceServiceImpl() {
		placesToStore = new ArrayList<Place>();
		try {
			url = new URL(
					"http://www.ugrad.cs.ubc.ca/~n1y8/2015CulturalSpaces.xls");
			inputStream = url.openStream();
			workbook = WorkbookFactory.create(inputStream);
			sheet = workbook.getSheetAt(0);
			sheet.removeRow(sheet.getRow(0));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void importData() {
		for (Row row : sheet) {
			String dataKey = "";
			String CULTURAL_SPACE_NAME = "";
			String WEBSITE = "";
			String ADDRESS = "";
			double LONGITUDE = 0;
			double LATITUDE = 0;
			for (int i = 0; i < 6; i++) {
				Cell cell = row.getCell(i, Row.RETURN_BLANK_AS_NULL);
				if (i == 0 && cell != null)
					dataKey = cell.getRichStringCellValue().getString();
				else if (i == 1 && cell != null)
					CULTURAL_SPACE_NAME = cell.getRichStringCellValue()
							.getString();
				else if (i == 2 && cell != null)
					WEBSITE = cell.getRichStringCellValue().getString();
				else if (i == 3 && cell != null)
					ADDRESS = cell.getRichStringCellValue().getString();
				else if (i == 4 && cell != null)
					LONGITUDE = cell.getNumericCellValue();
				else if (i == 5 && cell != null)
					LATITUDE = cell.getNumericCellValue();
			}
			// Update Section A
//			if (row.getCell(0).getRichStringCellValue().getString()
//					.contains("A")) {
//				Place place = new Place(dataKey, CULTURAL_SPACE_NAME, WEBSITE,
//						ADDRESS, LATITUDE, LONGITUDE);
//				placesToStore.add(place);
//			}
			// Update section B
//			if (row.getCell(0).getRichStringCellValue().getString()
//					.contains("B")) {
//				Place place = new Place(dataKey, CULTURAL_SPACE_NAME, WEBSITE,
//						ADDRESS, LATITUDE, LONGITUDE);
//				placesToStore.add(place);
//			}
			// Update section C
//			if (row.getCell(0).getRichStringCellValue().getString()
//					.contains("C")) {
//				Place place = new Place(dataKey, CULTURAL_SPACE_NAME, WEBSITE,
//						ADDRESS, LATITUDE, LONGITUDE);
//				placesToStore.add(place);
//			}
			// Update section D
//			if (row.getCell(0).getRichStringCellValue().getString()
//					.contains("D")) {
//				Place place = new Place(dataKey, CULTURAL_SPACE_NAME, WEBSITE,
//						ADDRESS, LATITUDE, LONGITUDE);
//				placesToStore.add(place);
//			}
			// Update section E
//			if (row.getCell(0).getRichStringCellValue().getString()
//					.contains("E")) {
//				Place place = new Place(dataKey, CULTURAL_SPACE_NAME, WEBSITE,
//						ADDRESS, LATITUDE, LONGITUDE);
//				placesToStore.add(place);
//			}
			// Update section F
			if (row.getCell(0).getRichStringCellValue().getString()
					.contains("F")) {
				Place place = new Place(dataKey, CULTURAL_SPACE_NAME, WEBSITE,
						ADDRESS, LATITUDE, LONGITUDE);
				placesToStore.add(place);
			}
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				pm.makePersistentAll(placesToStore);
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				pm.close();
			}
		}
	}
	
	public Place getPlace(String input) {
		Place place = new Place();
		place.setName(input);
		place.setLat(50);
		place.setLon(50);
		
		
/*		try {
			places = parser.parseCulturalPlace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Iterator<Place> iterator = placesToStore.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getName().contains(input)) {
				return iterator.next();
			}

		}
		return place;
	}
	
	public List<Place> searchingPlace(String input) {
		ArrayList<Place> detachedPlaces = new ArrayList<Place>();
		List<Place> result = new ArrayList<Place>();
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

		for (Place p: detachedPlaces) {
			if (p.getName().contains(input) || input == null) {
				result.add(p);
			}
		}
		
		return result;
	}


}
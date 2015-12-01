package com.google.culturalcompass;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import com.google.culturalcompass.shared.Place;

public class Tester {

	private ArrayList<Place> places;

	@Before
	public void init() throws Exception {
		Reader r = new Reader();
		places = r.importData();
	}

	@Test
	public void testExistingObject() {
		assert (places.contains(new Place("D143", "Railtown Studios",
				"http://railtownstudios.com/",
				"321 Railway St, Vancouver, BC, V6A 1A4", 49.2846363,
				-123.097645)));
	}

	@Test
	public void testNonExistingObject() {
		assert (!places.contains(new Place("A599", "abcdefg", "www.abc.com",
				"1234 abc street", 49.2344352, -123.134319)));
	}

	@Test
	public void testFirstObject() {
		Place place = places.get(0);
		assertEquals(place.getName(),
				"15th Field Artillery Regiment Museum and Archives");
		assertEquals(place.getWebsite(),
				"www.memorybc.ca/museum-of-15th-field-artillery-regiment");
		assertEquals(place.getAddress(),
				("2025 W 11th Av, Vancouver, BC, V6J 2C7"));
		assert (place.getLon() == -123.151123);
		assert (place.getLat() == 49.261938);
	}

	@Test
	public void testMidObject() {
		Place place = places.get(8);
		assertEquals(place.getName(), "Alliance Française de Vancouver");
		assertEquals(place.getWebsite(), "http://www.alliancefrancaise.ca/");
		assertEquals(place.getAddress(),
				("6161 Cambie St, Vancouver, BC, V5Z 3B2"));
		assert (place.getLon() == -123.1169818);
		assert (place.getLat() == 49.228879);
	}

	@Test
	public void testLastObject() {
		Place place = places.get(places.size() - 1);
		assertEquals(place.getName(), "Yuk Yuk's Comedy Club");
		assertEquals(place.getWebsite(), "http://www.yukyuks.com");
		assertEquals(place.getAddress(),
				("2837 Cambie St, Vancouver, BC, V5Z 3Y8"));
		assert (place.getLon() == -123.1151069);
		assert (place.getLat() == 49.2600654);
	}

}

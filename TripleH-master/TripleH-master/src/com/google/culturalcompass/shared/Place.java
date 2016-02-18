package com.google.culturalcompass.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

//import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;
import com.google.maps.gwt.client.LatLng;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Place implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6766423489920479558L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String encodedKey;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.pk-name", value = "true")
	private String dataKey;
	
	@Persistent
	private String name;
	
	@Persistent
	private String website;

	@Persistent
	private String address;

	@Persistent
	private double lat;

	@Persistent
	private double lon;
	
	public Place() {
		this("", "", "", "", 0.0, 0.0);
	}

	public Place(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	public Place(String dataKey, String name, String website, String address, double lat,
			double lon) {
		this.dataKey = dataKey;
		this.name = name;
		this.website = website;
		this.address = address;
		this.lat = lat;
		this.lon = lon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public LatLng getLatLng() {
		return LatLng.create(lat, lon);
	}

	@Override
	public String toString() {
		return "Place [dataKey=" + dataKey + ", name=" + name + ", website=" + website + ", address="
				+ address + ", latitude=" + lat + ", longitude=" + lon + "]";
	}

	public static final ProvidesKey<Place> KEY_PROVIDER = new ProvidesKey<Place>() {
		public Object getKey(Place item) {
			return item == null ? null : item.getName();
		}
	};

}
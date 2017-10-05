package it.unical.its.gpsUtil;

import java.time.LocalTime;
import java.util.Random;

public class StopTime {

	int durf;
	String name;
	double lon, lat;
	LocalTime time;
	boolean active;
	
	public StopTime(int durf, String name, double lon, double lat, LocalTime time, boolean active) {
		this.durf = durf;
		this.name = name;
		this.lon = lon;
		this.lat = lat;
		this.time = time;
		this.active = active;
	}
	
	public StopTime() {
		// Empty object
	}

	public void clone( StopTime st) {
		this.durf = st.durf;
		this.name = st.name;
		this.lon = st.lon;
		this.lat = st.lat;
		this.time = st.time;
		this.active = st.active;
	}
	
	public int getDurf() {
		return durf;
	}

	public void setDurf(int durf) {
		this.durf = durf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public double getRndLat() {
		return lat + 0.00005;
	}
	
	public double getRndLon() {
		return lon + 0.00005;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "StopTime [durf=" + durf + ", name=" + name + ", lon=" + lon + ", lat=" + lat + ", time=" + time
				+ ", active=" + active + "]";
	}	
	
}

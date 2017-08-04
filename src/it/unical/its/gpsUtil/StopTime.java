package it.unical.its.gpsUtil;

import java.time.LocalTime;

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

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
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

package it.unical.its.gpsUtil;

public class Stop {

	int durf;
	String name;
	double lon, lat;
	
	public Stop(int durf, String name, double lon, double lat) {
		this.durf = durf;
		this.name = name;
		this.lon = lon;
		this.lat = lat;
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
	
}

package it.unical.its.gpsUtil;

import java.util.LinkedList;

public class Journey {
	
	int journeyCode;
	int lineCode;
	LinkedList<StopTime> stopList = new LinkedList<StopTime>();

	public Journey() {
		// TODO Auto-generated constructor stub
	}
	
	public Journey(int journeyCode, int lineCode, LinkedList<StopTime> list) {
		// TODO Auto-generated constructor stub
		this.journeyCode = journeyCode;
		this.lineCode = lineCode;
		for (StopTime st: list) {
			stopList.add(new StopTime(st.durf, st.name, st.lon, st.lat, st.time,st.active));
		}
	}

	@Override
	public String toString() {
		return "Journey [journeyCode=" + journeyCode + ", lineCode=" + lineCode + ", stopList=" + stopList + "]";
	}

	public int getJourneyCode() {
		return journeyCode;
	}

	public void setJourneyCode(int journeyCode) {
		this.journeyCode = journeyCode;
	}

	public int getLineCode() {
		return lineCode;
	}

	public void setLineCode(int lineCode) {
		this.lineCode = lineCode;
	}

	public LinkedList<StopTime> getStopList() {
		return stopList;
	}

	public void setStopList(LinkedList<StopTime> stopList) {
		this.stopList = stopList;
	}
	
	

}

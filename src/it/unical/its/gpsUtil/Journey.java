package it.unical.its.gpsUtil;

import java.util.LinkedList;

public class Journey {
	
	int journeyCode;
	int lineCode;
	LinkedList<StopTime> stopList;

	public Journey() {
		// TODO Auto-generated constructor stub
	}
	
	public Journey(int journeyCode, int lineCode, LinkedList<StopTime> stopList) {
		// TODO Auto-generated constructor stub
		this.journeyCode = journeyCode;
		this.lineCode = lineCode;
		this.stopList = stopList;
	}

	@Override
	public String toString() {
		return "Journey [journeyCode=" + journeyCode + ", lineCode=" + lineCode + ", stopList=" + stopList + "]";
	}
	
	

}

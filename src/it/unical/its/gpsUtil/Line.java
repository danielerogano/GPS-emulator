
package it.unical.its.gpsUtil;

public class Line {
	
	int lineCode;
	String lineName;
	int lineJourneyNumber;
	
	public Line(int lineCode, String lineName, int lineJourneyNumber) {
		// TODO Auto-generated constructor stub
		this.lineCode = lineCode;
		this.lineName = lineName;
		this.lineJourneyNumber =lineJourneyNumber;
	}

	@Override
	public String toString() {
		return "Line [lineCode=" + lineCode + ", lineName=" + lineName + ", lineJourneyNumber=" + lineJourneyNumber
				+ "]";
	}

	public int getLineCode() {
		return lineCode;
	}

	public void setLineCode(int lineCode) {
		this.lineCode = lineCode;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public int getLineJourneyNumber() {
		return lineJourneyNumber;
	}

	public void setLineJourneyNumber(int lineJourneyNumber) {
		this.lineJourneyNumber = lineJourneyNumber;
	}
}

package it.unical.its.gpsUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Scanner;

/** UTF-8 Encoding SBV */
public class Parser {
	
	private final Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	  
	int operatorCode;
	String operatorName;
	int SBVCode;
	Operator op = new Operator();
	LinkedList<Line> lines = new LinkedList<Line>();
	static LinkedList<Journey> journeys = new LinkedList<Journey>();
	LinkedList<Stop> stops = new LinkedList<Stop>();
	LinkedList<StopTime> stopTimes = new LinkedList<StopTime>();
	boolean isFirstJourney;
	int currentLineCode = 0;

  public static void main(String... aArgs) throws IOException {
    Parser parser = new Parser("C:\\Users\\Daniele\\Desktop\\Romano.sbv");
    parser.processLineByLine();
    log("Total Journeys: " + journeys.size());
    log("Done.");
  }
  
  /**
   Constructor.
  */
  public Parser(String aFileName){
    fFilePath = Paths.get(aFileName);
  }
  
  /** Method to process line by line  */
  public final void processLineByLine() throws IOException {
    try (Scanner scanner =  new Scanner(fFilePath, ENCODING.name())){
    	int lines = 0;
      while (scanner.hasNextLine()){
    	lines++;
        processLine(scanner.nextLine());
      }
      //log("Lines processed: " + lines);
    }
  }

  protected void processLine(String aLine){
    //use a second Scanner to parse the content of each line 
    Scanner scanner = new Scanner(aLine);
    scanner.useDelimiter(";");
    int count = 0;
    while (scanner.hasNext()) {
    	//log(scanner.next());
    	scanner.next();
    	count++;
    }
    // log("Element in line: " + count);
    String check;
    switch(count) {
    case 1: check = "Code";
    	parseSBV(aLine);
    	break;
    case 2: check = "Operator";
    	parseOperator(aLine);
    	break;
    case 4: check = "Line";
    	parseLine(aLine);
    	break;
    default: check = "Journey";
    	parseJourney(aLine, count);
    	break;
    }
    //log(check);
  }
  
  private void parseJourney(String aLine, int count) {
	// TODO Auto-generated method stub
	  
	  Scanner scan = new Scanner(aLine);
	  scan.useDelimiter(";");
	  if(isFirstJourney) {
		// Durf, Stop name, Coordinates
		  //log("Stops: " + count);
		  while (scan.hasNext()) {
			  Scanner stopScan = new Scanner(scan.next());
			  stopScan.useDelimiter("§");
			  int durf = Integer.parseInt(stopScan.next());
			  String stopName = stopScan.next();
			  	Scanner coordScan = new Scanner(stopScan.next());
			  	coordScan.useDelimiter(":");
			  	double lon = Double.parseDouble(coordScan.next());
			  	double lat = Double.parseDouble(coordScan.next());
			  //log("Durf: " + durf + ", Name: " + stopName + ", Coord: " + lon + " - " + lat);
			  stops.add(new Stop(durf,stopName, lon, lat));
			  count--;
			  if (count == 0) {
				  isFirstJourney = false;
			  }
		  }
	  }	  else {
		  // Times, activeCheck
		  int it = 0;
		  int journeyCode = Integer.parseInt(scan.next());
		  log("Journey: " + journeyCode);
			  while (scan.hasNext()) {
				 if (it<count-3) { 
				  boolean activeStatus;
				  LocalTime time;
				  String current = scan.next();
				  if (current.isEmpty()) {
					  activeStatus = false;
					  time = LocalTime.parse("00:00");
					  } else {
						  activeStatus = true;
						  time = LocalTime.parse(current);
					  }
				  Stop currentStop = stops.get(it);
				  it++;
				  stopTimes.add(new StopTime(currentStop.durf, currentStop.name, currentStop.lon, currentStop.lat, time,activeStatus));
				  //log(stopTimes.getLast().toString());
				 } else {
					 scan.next();
				 }
				 }
			  journeys.add(new Journey(journeyCode, currentLineCode, stopTimes));
	  }
	  }

private void parseOperator(String aLine) {
	// TODO Auto-generated method stub
	  Scanner scan = new Scanner(aLine);
	  scan.useDelimiter(";");
	  while(scan.hasNext()) {
		   operatorCode = Integer.parseInt(scan.next());
		   operatorName = scan.next();
		   op.setCode(operatorCode);
		   op.setName(operatorName);
	   }
	  log("Operator: " + op.getName());
}
  
  private void parseSBV(String aLine) {
		// TODO Auto-generated method stub
		  Scanner scan = new Scanner(aLine);
		   scan.useDelimiter(";");
		   while(scan.hasNext()) {
			   SBVCode = Integer.parseInt(scan.next());
		   }
		   log("Total Lines: " + SBVCode);
	}
  
  private void parseLine(String aLine) {
		// TODO Auto-generated method stub
		  Scanner scan = new Scanner(aLine);
		  scan.useDelimiter(";");
		  
		  int lineCode = 0, lineJourneyNumber = 0;
		  String line = "";
		   
		   while(scan.hasNext()) {
			   lineCode = Integer.parseInt(scan.next());
			   line = scan.next();
			   scan.next();
			   lineJourneyNumber = Integer.parseInt(scan.next());
		   }
		   isFirstJourney = true;
		   lines.add(new Line(lineCode,line,lineJourneyNumber));
		   currentLineCode = lineCode;
		   //log(lines.getLast().toString());
	}
  
  private static void log(Object aObject){
    System.out.println(String.valueOf(aObject));
  }

} 

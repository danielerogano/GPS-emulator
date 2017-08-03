package it.unical.its.gpsUtil;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/** UTF-8 Encoding SBV */
public class Parser {
	
	private final Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	  
	int operatorCode;
	String operatorName;
	int SBVCode;
	static Operator op;
	static Journey randomJourney;
	
	static LinkedList<Line> lines = new LinkedList<Line>();
	static LinkedList<Journey> journeys = new LinkedList<Journey>();
	static LinkedList<Stop> stops = new LinkedList<Stop>();
	static LinkedList<StopTime> stopTimes = new LinkedList<StopTime>();
	
	boolean isFirstJourney;
	int currentLineCode = 0;
	static String randomLineName;
	static Random r;
	static int Low;
	static int High;
	
	private Scanner  scan, scan2, scan3, scan4, scanner,stopScan, coordScan;
	
	private Socket socket;

  public static void main(String... aArgs) throws IOException {
	  
    Parser parser = new Parser("C:\\Users\\Daniele\\Desktop\\Perrone.sbv");
    parser.processLineByLine();
    
    log("Total Journeys: " + journeys.size());
    
    Low = 0;
    High = journeys.size();
    
    selectJourney();
    
    runSimulation(randomJourney);
    
    log("Done.");
  }
 
private static void runSimulation(Journey randomJourney2) {

	LinkedList<StopTime> stopList = randomJourney2.getStopList();
	//log("List objects: " + stopList.size());	
	for (StopTime st: stopList) {
		if (st.isActive()) {
		log(st.getDurf() + ", " + st.getName() + ", " + st.getLon() + ", " + st.getLat() + ", " + st.getTime());
			  }
	}
	stopList.clear();
}

	private static void selectJourney() {
	
		  log("Choosing random Journey from SBV file...");
		  
		  r = new Random();
		  randomJourney= new Journey();
		  randomJourney = journeys.get(r.nextInt(High-Low));
		  
		  for (Line line: lines) {
			  if (line.getLineCode() == randomJourney.getLineCode()) {
				  randomLineName = line.getLineName();
			  }
		  }
		  log("Journey selected: " + randomJourney.getJourneyCode() + ", Line: " + randomLineName);
	}

  /*
   Constructor.
  */
	public Parser(String aFileName){
	    fFilePath = Paths.get(aFileName);
	 }
  
  /** Method to process line by line  */
  public final void processLineByLine() throws IOException {
    try (Scanner scanner =  new Scanner(fFilePath, ENCODING.name())){
    	while (scanner.hasNextLine()){
    	processLine(scanner.nextLine());
      }
      //log("Lines processed: " + lines);
    }
  }

	protected void processLine(String aLine){
	    scanner = new Scanner(aLine);
	    scanner.useDelimiter(";");
	    int count = 0;
	    while (scanner.hasNext()) {
	    	//log(scanner.next());
	    	scanner.next();
	    	count++;
	    }
	    switch(count) {
	    case 1: 
	    	parseSBV(aLine);
	    	break;
	    case 2: 
	    	parseOperator(aLine);
	    	break;
	    case 4: 
	    	parseLine(aLine);
	    	break;
	    default: 
	    	parseJourney(aLine, count);
	    	break;
	    }
	    //log(check);
	}
  
private void parseJourney(String aLine, int count) {	  
	  
	  scan = new Scanner(aLine);
	  scan.useDelimiter(";");
	  if(isFirstJourney) {
		  stops.clear();
		// Durf, Stop name, Coordinates
		  //log("Stops: " + count);
		  while (scan.hasNext()) {
			  stopScan = new Scanner(scan.next());
			  stopScan.useDelimiter("§");
			  int durf = Integer.parseInt(stopScan.next());
			  String stopName = stopScan.next();
			  	coordScan = new Scanner(stopScan.next());
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
		  stopTimes.clear();
		  int it = 0;
		  int journeyCode = Integer.parseInt(scan.next());
		  //log("Journey: " + journeyCode);
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
		
		op = new Operator();
		scan2 = new Scanner(aLine);
		scan2.useDelimiter(";");
			while(scan2.hasNext()) {
			   operatorCode = Integer.parseInt(scan2.next());
			   operatorName = scan2.next();
			   op.setCode(operatorCode);
			   op.setName(operatorName);
			}
		log("Operator: " + op.getName());
	}
  
	private void parseSBV(String aLine) {
			scan3 = new Scanner(aLine);
			   scan3.useDelimiter(";");
			   while(scan3.hasNext()) {
				   SBVCode = Integer.parseInt(scan3.next());
			   }
			   log("Total Lines: " + SBVCode);
	}
  
  private void parseLine(String aLine) {
		scan4 = new Scanner(aLine);
		  scan4.useDelimiter(";");
		  
		  int lineCode = 0, lineJourneyNumber = 0;
		  String line = "";
		   
		   while(scan4.hasNext()) {
			   lineCode = Integer.parseInt(scan4.next());
			   line = scan4.next();
			   scan4.next();
			   lineJourneyNumber = Integer.parseInt(scan4.next());
		   }
		   isFirstJourney = true;
		   lines.add(new Line(lineCode,line,lineJourneyNumber));
		   currentLineCode = lineCode;
		   //log(lines.getLast().toString());
	}
  
  public void transfer(final File f, final String host, final int port) throws IOException {
	    socket = new Socket(host, port);
	    final BufferedOutputStream outStream = new BufferedOutputStream(socket.getOutputStream());
	    final BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(f));
	    final byte[] buffer = new byte[4096];
	    for (int read = inStream.read(buffer); read >= 0; read = inStream.read(buffer))
	        outStream.write(buffer, 0, read);
	    inStream.close();
	    outStream.close();
	}
  
  private static void log(Object aObject){
    System.out.println(String.valueOf(aObject));
  }

}

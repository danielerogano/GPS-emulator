//Update 09/09/2017

package it.unical.its.gpsUtil;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/** UTF-8 Encoding SBV */
public class Parser {
	
	private final Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	
	static String SBV_FILE_PATH = "./file.sbv";
	static String OUTPUT_FILE_PATH = "./tracking_packets.DAT";
	
	static int SPEED_LOWER_BOUND = 5;
	static int SPEED_UPPER_BOUND = 50;
	
	static int ALTITUDE_LOWER_BOUND = 100;
	static int ALTITUDE_UPPER_BOUND = 300;
	
	static int HEADING_LOWER_BOUND = 1;
	static int HEADING_UPPER_BOUND = 359;
	
	static int ACCURACY_LOWER_BOUND = 5;
	static int ACCURACY_UPPER_BOUND = 20;

	int operatorCode;
	String operatorName;
	int SBVCode;
	static Operator op;
	static Journey randomJourney;
	
	static LinkedList<Line> lines = new LinkedList<Line>();
	static LinkedList<Journey> journeys = new LinkedList<Journey>();
	static LinkedList<Stop> stops = new LinkedList<Stop>();
	static LinkedList<StopTime> stopTimes = new LinkedList<StopTime>();
	static LinkedList<ByteArray> byteArrays = new LinkedList<ByteArray>();
	
	boolean isFirstJourney;
	int currentLineCode = 0;
	static String randomLineName;
	static Random r;
	static int Low;
	static int High;
	
	private Scanner  scan, scan2, scan3, scan4, scanner,stopScan, coordScan;
	
	static SimpleDateFormat jTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Socket socket;
	
	/*
	Constructor.
	*/
	public Parser(String aFileName){
	    fFilePath = Paths.get(aFileName);
	 }

	public static void main(String[] args) throws IOException {
		
		File f = new File(SBV_FILE_PATH);
		if(f.exists() && !f.isDirectory()) { 
			Parser parser = new Parser(SBV_FILE_PATH);
		    parser.processLineByLine();
		
		    log("Total Journeys: " + journeys.size());
		
			Low = 0;
			High = journeys.size();
		
			selectJourney();
			
			runSimulation(randomJourney);
			
			writeToFile();
			log("Output file created.");
		} else {
			System.out.println("Please provide a SBV file (rename file.sbv) in the same folder");
		}
		
		// log("Test:");
		// ByteArray ba = new ByteArray(1504797708, true, false, false, (float)39.3265, (float)16.5567, (float)12.2, (float)312.1, (float)34.5, 12, 1, 1, 26523);
		// ba.arrayComposer();
		// System.out.println(Arrays.toString(ba.trackingPacket));	    
	  }
 
	private static void runSimulation(Journey randomJourney2) throws IOException {
	
		LinkedList<StopTime> stopList = randomJourney2.getStopList();
		//log("List objects: " + stopList.size());	
		for (StopTime st: stopList) {
			if (st.isActive()) {
			// log(st.getDurf() + ", " + st.getName() + ", " + st.getLon() + ", " + st.getLat() + ", " + st.getTime());
			
			packetGenerator(st);
			
			}
		}
		stopList.clear();
	}
	/* Select random Journey from SBV */
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
	} // close function

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
  
	private static void writeToFile() throws IOException {
		
		FileOutputStream fileOuputStream = new FileOutputStream(OUTPUT_FILE_PATH, true); 
				
		for (ByteArray ba: byteArrays) {
		    fileOuputStream.write(ba.trackingPacket);
			}
		
		fileOuputStream.close();
	}
	
	private static void packetGenerator(StopTime st) throws IOException {

		// Timestamp calculator
		Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, st.getTime().getHour());
        now.set(Calendar.MINUTE, st.getTime().getMinute());
        // random values generator
        r = new Random();
        
        ByteArray ba = new ByteArray(
        		now.getTime().getTime(),															// timestamp
        		false,																				// degraded
        		false,																				// deadReckoned
        		false,																				// notFixed
        		(float)st.getLat(),																	// latitude
        		(float)st.getLon(),																	// longitude
        		(float) r.nextInt(ALTITUDE_UPPER_BOUND-ALTITUDE_LOWER_BOUND)+ALTITUDE_LOWER_BOUND,	// altitude
        		(float) r.nextInt(HEADING_UPPER_BOUND-HEADING_LOWER_BOUND)+HEADING_LOWER_BOUND, 	// heading
        		(float) r.nextInt(SPEED_UPPER_BOUND-SPEED_LOWER_BOUND)+SPEED_LOWER_BOUND, 			// speed
        		r.nextInt(ACCURACY_UPPER_BOUND-ACCURACY_LOWER_BOUND)+ACCURACY_LOWER_BOUND, 			// accuracy
        		1, 																					// type
        		1, 																					// event
        		randomJourney.getJourneyCode()														// vehicleJourney
        		);
        
		byteArrays.add(ba);

	}
  
	private static void log(Object aObject){
		System.out.println(String.valueOf(aObject));
	}
}

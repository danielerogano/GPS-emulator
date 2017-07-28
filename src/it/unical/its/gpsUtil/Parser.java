package it.unical.its.gpsUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/** UTF-8 Encoding SBV */
public class Parser {
	
	int operatorCode;
	String operatorName;
	int SBVCode;
	Operator op = new Operator();

  public static void main(String... aArgs) throws IOException {
    Parser parser = new Parser("/home/dr/eclipse-workspace/file.sbv");
    parser.processLineByLine();
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
      log("Lines processed: " + lines);
    }
  }

  protected void processLine(String aLine){
    //use a second Scanner to parse the content of each line 
    Scanner scanner = new Scanner(aLine);
    scanner.useDelimiter(";");
    int count2 = 0;
    while (scanner.hasNext()) {
    	//log(scanner.next());
    	scanner.next();
    	count2++;
    }
    //log("Element in line: " + count2);
    String check;
    switch(count2) {
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
    }
    //log(check);
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
		   log("SBV Code: " + SBVCode);
	}
  
  private void parseLine(String aLine) {
		// TODO Auto-generated method stub
		  Scanner scan = new Scanner(aLine);
		   scan.useDelimiter(";");
		   while(scan.hasNext()) {
			   int lineCode = Integer.parseInt(scan.next());
			   String line = scan.next();
			   scan.next();
			   int lineJourneyNumber = Integer.parseInt(scan.next());			   
		   }
		   log("Line: ");
	}

// PRIVATE 
  private final Path fFilePath;
  private final static Charset ENCODING = StandardCharsets.UTF_8;  
  
  private static void log(Object aObject){
    System.out.println(String.valueOf(aObject));
  }
  
  private String quote(String aText){
    String QUOTE = "'";
    return QUOTE + aText + QUOTE;
  }
} 

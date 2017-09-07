package it.unical.its.gpsUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

public class ByteArray {
	
	byte[] trackingPacket = new byte[42];
	
	byte[] version = new byte[1];
	byte[] timestamp = new byte[8];
	byte[] flags = new byte[1];
	byte[] location = new byte[12];
	byte[] heading = new byte[4];
	byte[] speed = new byte[4];
	byte[] accuracy = new byte[1];
	byte[] trackingInfo = new byte[1];
	byte[] vehicleJourney = new byte[4];
	byte[] journeyOrdinal = new byte[2];
	byte[] parameter = new byte[4];
	
	// int[] bits = new int[]{1, 0, 1, 0, 1, 1, 0, 1, 0, 1};
	// int[] bitsType;
	// int[] bitsEvent;
	
	public ByteArray() {
		// TODO Auto-generated constructor stub
	}
	
	public ByteArray(int timestamp, boolean degraded, boolean deadReckoned, boolean notFixed, double lat, double lon, double alt, double heading, double speed, int accuracy, int type, int event, int vehicleJourney) throws IOException {

		int deg, dea, nf = 0;
		
		if (degraded) {
			deg = 1;
		} else {
			deg = 0;
			}
			
		if (deadReckoned){
			dea = 1;
		} else {
			dea = 0;
			}
		if (notFixed){
			nf = 1;
		} else {
			nf = 0;
			}
		
		int[] bitsFlag = new int[]{deg, dea, nf, 0, 0, 0, 0, 0, 0, 0};
		
		Arrays.fill(this.version, (byte)1);
		Arrays.fill(this.timestamp, (byte)timestamp);
		
		this.flags = flagComposer(bitsFlag);
		this.location = locationComposer(lat, lon, alt);
		this.trackingInfo = trackingInfoComposer(type, event);
		

		Arrays.fill(this.heading, (byte)heading);
		Arrays.fill(this.speed, (byte)speed);
		Arrays.fill(this.accuracy, (byte)accuracy);
		Arrays.fill(this.trackingInfo, (byte)1);
		Arrays.fill(this.vehicleJourney, (byte)vehicleJourney);
		Arrays.fill(this.journeyOrdinal, (byte)0);
		Arrays.fill(this.parameter, (byte)0);
		
	}
	
	private static byte[]  flagComposer (int[] bits) {
		
		BitSet bitSet = new BitSet(bits.length);
	    for (int index = 0; index < bits.length; index++) {
	        bitSet.set(index, bits[index] > 0);
	    }
	    return bitSet.toByteArray();
    }
	
	public static byte[] trackingInfoComposer(int type, int event) {
		String sType = String.format("%4s", Integer.toBinaryString(type)).replace(' ', '0');
	    String sEvent = String.format("%4s", Integer.toBinaryString(event)).replace(' ', '0');
	    sType += sEvent;
	 	return sType.getBytes();
	}
	
	public byte[] locationComposer(double lat, double lon, double alt) throws IOException {
		
		byte[] loc = new byte[12];
		
		byte[] latitude = new byte[4];
		byte[] longitude = new byte[4];
		byte[] altitude = new byte[4];
		
		Arrays.fill(latitude, (byte)lat);
		Arrays.fill(longitude, (byte)lon);
		Arrays.fill(altitude, (byte)alt);
		
		ByteBuffer target = ByteBuffer.wrap(location);
	    target.put(latitude);
	    target.put(longitude);
	    target.put(altitude);
	    
	    return loc;
	    
	}
	
	public void arrayComposer() throws IOException {
		ByteBuffer target = ByteBuffer.wrap(trackingPacket);
	    target.put(version);
	    target.put(timestamp);
	    target.put(flags);
	    target.put(location);
	    target.put(heading);
	    target.put(speed);
	    target.put(accuracy);
	    target.put(trackingInfo);
	    target.put(vehicleJourney);
	    target.put(journeyOrdinal);	 
	    target.put(parameter);
	}
	
	
	public static void main(String[] args) {
		log("Test!");
	}
	
	private static void log(Object aObject){
	    System.out.println(String.valueOf(aObject));
	  }
	
}
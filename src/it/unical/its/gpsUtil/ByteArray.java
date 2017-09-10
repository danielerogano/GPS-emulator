package it.unical.its.gpsUtil;

import java.io.File;
import java.io.FileOutputStream;
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
	
	public ByteArray() {
		// TODO Auto-generated constructor stub
	}
	
	public ByteArray(long timestamp, boolean degraded, boolean deadReckoned, boolean notFixed, float lat, float lon, float alt, float heading, float speed, int accuracy, int type, int event, int vehicleJourney) throws IOException {

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
		
		int[] bitsVersion = new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int[] bitsFlag = new int[]{deg, dea, nf, 0, 0, 0, 0, 0, 0, 0};
		int[] bitsjourneyOrdinal = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int[] bitsparameter = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		
		this.version = byteComposer(bitsVersion);
		//this.timestamp = ByteBuffer.allocate(8).putLong(timestamp).array();
		this.timestamp = this.longToBytes(timestamp);
		
		this.flags = byteComposer(bitsFlag);
		this.location = locationComposer(lat, lon, alt);
		
		
		this.heading = ByteBuffer.allocate(4).putFloat((float) heading).array();
		this.speed = ByteBuffer.allocate(4).putFloat((float) speed).array();
		this.accuracy = ByteBuffer.allocate(1).put((byte)accuracy).array();

		// this.trackingInfo = trackingInfoComposer(type, event);
		this.trackingInfo = byteComposer(bitsFlag);
		
		this.vehicleJourney = ByteBuffer.allocate(4).putFloat((float) vehicleJourney).array();
		this.journeyOrdinal = byteComposer(bitsjourneyOrdinal);
		this.parameter = byteComposer(bitsparameter);
		
		this.arrayComposer();
	}
	
	private static byte[]  byteComposer (int[] bits) {
		
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
	
	public byte[] locationComposer(float lat, float lon, float alt) throws IOException {
		
		byte[] loc = new byte[12];
		
		byte[] latitude = new byte[4];
		byte[] longitude = new byte[4];
		byte[] altitude = new byte[4];
		
		latitude = ByteBuffer.allocate(4).putFloat((float) lat).array();
		longitude = ByteBuffer.allocate(4).putFloat((float) lon).array();
		altitude = ByteBuffer.allocate(4).putFloat((float) alt).array();
		
		ByteBuffer target = ByteBuffer.wrap(loc);
	    target.put(latitude);
	    target.put(longitude);
	    target.put(altitude);
	    return loc;
	    
	}
	
	public byte[] longToBytes(long x) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(x);
	    return buffer.array();
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
	
	public static byte[] convertToByteArray(double value, int length) {
	      byte[] bytes = new byte[length];
	      ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
	      buffer.putDouble(value);
	      return buffer.array();
	  }
	
	/*
	
	public static void main(String[] args) throws IOException {
		log("Test!");
		ByteArray ba = new ByteArray(1504797708, true, false, false, (float)39.3265, (float)16.5567, (float)12.2, (float)312.1, (float)34.5, 12, 1, 1, 26523);
	    
	    System.out.println("Version: " + Arrays.toString(ba.version));
	    System.out.println("Timestamp: " + Arrays.toString(ba.timestamp));
	    System.out.println("flags: " + Arrays.toString(ba.flags));
	    System.out.println("location: " + Arrays.toString(ba.location));
	    System.out.println("heading: " + Arrays.toString(ba.heading));
	    System.out.println("speed: " + Arrays.toString(ba.speed));
	    System.out.println("accuracy: " + Arrays.toString(ba.accuracy));
	    System.out.println("trackingInfo: " + Arrays.toString(ba.trackingInfo));
	    System.out.println("vehicleJourney: " + Arrays.toString(ba.vehicleJourney));
	    System.out.println("journeyOrdinal: " + Arrays.toString(ba.journeyOrdinal));
	    System.out.println("parameter: " + Arrays.toString(ba.parameter));
	    
	    ba.arrayComposer();
	    
	    System.out.println(Arrays.toString(ba.trackingPacket));

	    writeToFile(ba.trackingPacket);

	}
	
	*/
	private static void writeToFile(byte[] arr ) throws IOException {
		FileOutputStream fileOuputStream = new FileOutputStream("C:\\Users\\dr\\Desktop\\output.DAT"); 
        fileOuputStream.write(arr);
    	fileOuputStream.close();
	}
	
	private static void log(Object aObject){
	    System.out.println(String.valueOf(aObject));
	  }
	
}
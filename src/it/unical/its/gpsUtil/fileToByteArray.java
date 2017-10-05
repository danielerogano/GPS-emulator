package it.unical.its.gpsUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

public class fileToByteArray {
	
	static String OUTPUT_FILE_PATH = "./";

    public static void run() {

        // convert file to byte[]
    	
    	File [] files = finder(OUTPUT_FILE_PATH);
    	
		if(files.length > 0) {
			byte[] bFile = readBytesFromFile(files[0].getName());
			int[] output = bytearray2intarray(bFile);
			
			System.out.println(files[0].getName() + " found!");
			/*
			//Print bytes[]
			for (int i = 0; i < bFile.length; i++) {
			    System.out.print((char) bFile[i]);
			}
			*/
			System.out.print("\r\n ");
			
			for (int i = 0; i < output.length; i++) {
			    for (int j = 0; j< 42; j++) {
			    	System.out.print(output[j+i]);
			        System.out.print("||");
			    }
			    i+=41;
			    System.out.print("\r\n ");
			}			
		} else {
			System.out.println("File DAT not found");
		}
    	
    }
    
    public static File[] finder(String dirName){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() { 
                 public boolean accept(File dir, String filename)
                      { return filename.endsWith(".dat"); }
        } );

    }
    
    public static int[] bytearray2intarray(byte[] barray)
    {
      int[] iarray = new int[barray.length];
      int i = 0;
      for (byte b : barray)
          iarray[i++] = b & 0xff;
      return iarray;
    }

    private static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }

}
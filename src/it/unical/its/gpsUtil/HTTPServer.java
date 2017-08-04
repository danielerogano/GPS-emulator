//HTTP server
package it.unical.its.gpsUtil;

import java.io.*;
import java.net.*;

public class HTTPServer { 
	public static void main(String args[] ) throws IOException { 
		ServerSocket server = new ServerSocket(8888);
		System.out.println("Listening for connection on port 8888 ....");
		while (true) { 
			Socket clientSocket = server.accept();
			InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			String line = reader.readLine();
				while (!line.isEmpty()) {
					System.out.println(line);
					line = reader.readLine();
					}
				}
		}
	}

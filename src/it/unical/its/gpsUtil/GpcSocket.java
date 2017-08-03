package it.unical.its.gpsUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class GpcSocket {

    private Socket socket;

    private static final int SERVERPORT = 8888;
    private static final String SERVER_IP = "192.168.1.78";

    public void run() {
        new Thread(new ClientThread()).start();
    }

    public int send(byte[] str) throws IOException {
        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
        socket = new Socket(serverAddr, SERVERPORT);
        OutputStream out = socket.getOutputStream();
        out.write(str);
        out.flush();
        out.close();
        socket.close();
        return str.length;
    }

    class ClientThread implements Runnable {
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
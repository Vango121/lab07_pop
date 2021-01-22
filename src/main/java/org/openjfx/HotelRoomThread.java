package org.openjfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HotelRoomThread extends Thread{
    private int port;
    private PrintWriter out;

    public HotelRoomThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", port);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void changeKey(){
        out.println("newKey");
    }
    public void close(){
        out.println("close");
    }
}

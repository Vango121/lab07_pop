package org.openjfx.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRoomModel extends Thread {
    private String id;
    private int rozmiar;
    private String key;
    private int port;
    private boolean isSomeone = false;
    public PrintWriter out1;
    public static boolean running = true;

    public ClientRoomModel(String id, int rozmiar, String key, int port) {
        this.id = id;
        this.rozmiar = rozmiar-48;
        this.key = key;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", port);
            out1 = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;
            out1.println("client to Room");
            while ((inputLine = in1.readLine()) != null && running) {
                System.out.println(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enterRoom() {
        out1.println("doors " + key);
        isSomeone=!isSomeone;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIdd() {
        return id;
    }

    public void setIdd(String id) {
        this.id = id;
    }

    public int getRozmiar() {
        return rozmiar;
    }

    public void setRozmiar(int rozmiar) {
        this.rozmiar = rozmiar;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isSomeone() {
        return isSomeone;
    }

    public void setSomeone(boolean someone) {
        isSomeone = someone;
    }
}

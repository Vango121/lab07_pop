package org.openjfx.models;

import org.openjfx.HotelThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Room extends Thread {
    private int id;
    private int port;
    private int size;
    private boolean empty;
    private String key;
    private boolean isSomeone = false;

    public Room(int id, int port, int size, boolean empty) {
        this.id = id;
        this.port = port;
        this.size = size;
        this.empty = empty;
        connect();
        key = generateKey();
        System.out.println(key);
    }

    public int getIdd() {
        return id;
    }

    public void setIdd(int id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public String getKey() {
        return key;
    }

    public void setKey() {
        this.key = generateKey();
    }

    private Socket socket;

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port, 50);
                while (true) {
                    try {
                        socket = serverSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Thread t = new Thread(this::roomThread);
                    t.setDaemon(true);
                    t.start();
                }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void roomThread(){
        PrintWriter out1;
        BufferedReader in;
        try {
            out1 = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine())!= null){
                System.out.println(inputLine+" room");
                if(inputLine.startsWith("doors")&& inputLine.contains(key)) {
                    isSomeone = !isSomeone;
                }else if(inputLine.equals("endReservation")) {
                    empty=true;
                    System.out.println("closed room from client");
                    out1.println("closed room"+id);
                    socket.close();
                    break;
                }else if(inputLine.equals("close")){
                    socket.close();
                    break;
                }else if(inputLine.equals("newKey")){
                    key=generateKey();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private PrintWriter out;

    private void connect() {

        Thread t = new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 80);
                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("room" + id + " size" + size);
                //Thread.sleep(3000);
                //out.println("close"+id);
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }
    private void sendStatus(){

    }
    public void closeRoom() {
        out.println("close" + id);
    }

    private String generateKey() {
        Random random = new Random();
        return random.ints(48, 122 + 1) //48 ASCII 0 122 ASCII z
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }
}

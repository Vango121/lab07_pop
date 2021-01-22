package org.openjfx;

import org.openjfx.models.Room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class HotelThread extends Thread{
    protected Socket socket;
    private UiInterface uiInterface;

    public HotelThread(Socket socket,UiInterface uiInterface) {
        this.socket = socket;
        this.uiInterface = uiInterface;
    }
    PrintWriter out;
    BufferedReader in;
    @Override
    public void run(){

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //out.println("Hello there General Kenobi");
            String inputLine;
            while ((inputLine = in.readLine())!= null){
                System.out.println(inputLine);
                if(inputLine.startsWith("close")){
                    conClose(inputLine);
                    break;
                }
                else if(inputLine.startsWith("R")){
                    String[] tab = inputLine.split("/");
                    List<Room> roomList = uiInterface.findRoom(tab[0],Integer.parseInt(tab[1]));
                    if(roomList.size()>0){
                        for (Room room: roomList){
                            out.println("R"+room.getSize()+"/"+room.getKey()+"/"+room.getIdd()+"/"+room.getPort());
                        }
                    }else{
                        out.println("cant get room");
                    }

                }
                else if(inputLine.startsWith("end")){
                    endReservation();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void endReservation(){
        uiInterface.cancelReservation();
    }
    private void conClose(String inputLine) throws IOException {
        System.out.println("close room id"+inputLine.substring(5));
        uiInterface.removeRoom(Integer.parseInt(inputLine.substring(5)));
        out.println("connection closed");
        socket.close();
    }
}

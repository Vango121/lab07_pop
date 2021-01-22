package org.openjfx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import org.openjfx.models.ClientRoomModel;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GuestScene implements Initializable {
    @FXML
    TextField oneRoom;
    @FXML
    TextField twoRoom;
    @FXML
    TextField threeRoom;
    @FXML
    ListView<String> rooms;
    ObservableList<String> itemList = FXCollections.observableArrayList();
    List<ClientRoomModel> roomsList = new ArrayList<>();
    List<Pair<Thread,PrintWriter>> threadsRoomList = new ArrayList<>();
    boolean running = true;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rooms.setItems(itemList);
    }

    PrintWriter out;
    int count = 0;

    public void start(ActionEvent actionEvent) {
        if(count ==0){
            Thread t = new Thread(() -> {
                try {
                    Socket socket = new Socket("localhost", 80);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String inputLine;
                    //out.println("client");
                    getRoom();
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);
                        if (inputLine.startsWith("R")) {
                            setList(inputLine);
//                            String[] tab = inputLine.split("/");
//                            itemList.add("Room nr " + tab[2] + " rozmiar " + inputLine.charAt(1));
//                            ClientRoomModel clientRoomModel = new ClientRoomModel(tab[2], inputLine.charAt(1), tab[1], Integer.parseInt(tab[3]));
//                            roomsList.add(clientRoomModel);
//                            clientRoomModel.setDaemon(true);
//                            clientRoomModel.start();
                            //Pair<Thread,PrintWriter> pair = new Pair<>(rt,out);
                            //threadsRoomList.add(pair);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.setDaemon(true);
            t.start();
            count++;
        }else{
            getRoom();
        }

    }
    private void setList(String inputLine){
        Platform.runLater(()->{
            String[] tab = inputLine.split("/");
            itemList.add("Room nr " + tab[2] + " rozmiar " + inputLine.charAt(1));
            ClientRoomModel clientRoomModel = new ClientRoomModel(tab[2], inputLine.charAt(1), tab[1], Integer.parseInt(tab[3]));
            roomsList.add(clientRoomModel);
            clientRoomModel.setDaemon(true);
            clientRoomModel.start();
        });
    }
    //PrintWriter out1;
//    private Pair<Thread,PrintWriter> connectToRoom(ClientRoomModel clientRoomModel) {
//         Thread t = new Thread(() -> {
//            try {
//                Socket socket = new Socket("localhost", clientRoomModel.getPort());
//                out1 = new PrintWriter(socket.getOutputStream(), true);
//                BufferedReader in1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String inputLine;
//                out1.println("client to Room");
//                while ((inputLine = in1.readLine()) != null && running) {
//                    System.out.println(inputLine);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }, clientRoomModel.getIdd());
//         t.setDaemon(true);
//         t.start();
//         return new Pair<>(t,out1);
//    }

    private void getRoom() {
        Platform.runLater(() -> {
            out.println("R1/" + oneRoom.getText());
            out.println("R2/" + twoRoom.getText());
            out.println("R3/" + threeRoom.getText());
        });
    }
    public void endReservation(ActionEvent actionEvent){
        StringBuilder stringBuilder = new StringBuilder("end");
        for(ClientRoomModel room : roomsList){
            stringBuilder.append("/").append(room.getIdd());
        }
        for(ClientRoomModel room : roomsList){
            room.out1.println("endReservation");
        }
        System.out.println(stringBuilder+" sb");
        out.println(stringBuilder.toString());
        roomsList.clear();
        running = false;
        threadsRoomList.clear();
        itemList.clear();
        rooms.refresh();
    }
}

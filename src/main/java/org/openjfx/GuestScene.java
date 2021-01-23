package org.openjfx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    boolean running = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rooms.setItems(itemList);
    }

    PrintWriter out;
    int count = 0;

    public void start(ActionEvent actionEvent) {
        if (count == 0) {
            Thread t = new Thread(() -> {
                try {
                    Socket socket = new Socket("localhost", 80);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String inputLine;
                    getRoom();
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);
                        if (inputLine.startsWith("R")) {
                            setList(inputLine);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.setDaemon(true);
            t.start();
            count++;
        } else {
            getRoom();
        }

    }

    private void setList(String inputLine) {
        Platform.runLater(() -> {
            String[] tab = inputLine.split("/");
            itemList.add("Room nr " + tab[2] + " rozmiar " + inputLine.charAt(1) + " gosc w srodku " + false);
            ClientRoomModel clientRoomModel = new ClientRoomModel(tab[2], inputLine.charAt(1), tab[1], Integer.parseInt(tab[3]));
            roomsList.add(clientRoomModel);
            clientRoomModel.setDaemon(true);
            clientRoomModel.start();
        });
    }

    private void getRoom() {
        Platform.runLater(() -> {
            out.println("R1/" + oneRoom.getText());
            out.println("R2/" + twoRoom.getText());
            out.println("R3/" + threeRoom.getText());
        });
    }

    public void endReservation(ActionEvent actionEvent) {
        if(roomsList.stream().noneMatch(ClientRoomModel::isSomeone)){
            StringBuilder stringBuilder = new StringBuilder("end");
            for (ClientRoomModel room : roomsList) {
                stringBuilder.append("/").append(room.getIdd());
            }
            for (ClientRoomModel room : roomsList) {
                room.out1.println("endReservation");
            }
            System.out.println(stringBuilder + " sb");
            out.println(stringBuilder.toString());
            roomsList.clear();
            running = false;
            itemList.clear();
            rooms.refresh();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Koniec rezerwacji");
            alert.setHeaderText("Blad");
            alert.setContentText("Wszyscy goscie musza opuscic pokoje przed zakonczeniem rezerwacji");
            alert.showAndWait();
        }

    }

    public void roomOnClick(MouseEvent arg0) {
        int id = rooms.getSelectionModel().getSelectedIndex();
        ClientRoomModel clientRoom = roomsList.get(id);
        clientRoom.enterRoom();
        Platform.runLater(() -> {
            itemList.set(id,"Room nr " + clientRoom.getIdd() + " rozmiar " + clientRoom.getRozmiar() + " gosc w srodku " + clientRoom.isSomeone());
            rooms.refresh();
        });
    }
}

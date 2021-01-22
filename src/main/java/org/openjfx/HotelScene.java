package org.openjfx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.openjfx.models.Room;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.*;

public class HotelScene implements Initializable, UiInterface {
    @FXML
    TextField port;
    @FXML
    TextField oneTextField;
    @FXML
    TextField twoTextField;
    @FXML
    TextField threeTextField;
    @FXML
    ListView<String> roomListView;
    ObservableList<String> itemList = FXCollections.observableArrayList();
    Socket socket;

    List<Room> oneRoom = new ArrayList<>();
    List<Room> twoRoom = new ArrayList<>();
    List<Room> threeRoom = new ArrayList<>();
    List<HotelRoomThread> hotelRoomThreads = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomListView.setItems(itemList);
    }

    public void start(ActionEvent actionEvent) {
        int portId = Integer.parseInt(port.getText());
        startServerSocket(portId);
        createRoom(Integer.parseInt(oneTextField.getText()), Integer.parseInt(twoTextField.getText()), Integer.parseInt(threeTextField.getText()));
    }

    private void startServerSocket(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port, 50);
            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        socket = serverSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    HotelThread hotelThread = new HotelThread(socket, this);
                    hotelThread.setDaemon(true);
                    hotelThread.start();
                }
            });
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int findPort() {
        int startport = 100;
        int stopport = 65535;
        for (int port = startport; port <= stopport; port++) {
            try {
                ServerSocket ss = new ServerSocket(port);
                ss.close();
                return port;
            } catch (IOException ex) {
                System.out.println("Port " + port + " is occupied.");
            }
        }
        return 0;
    }

    private void createRoom(int one, int two, int three) {
        int id = 0;
        for (int i = 0; i < one; i++) {
            Room room = new Room(id, findPort(), 1, true);
            room.setDaemon(true);
            room.start();
            oneRoom.add(room);
            itemList.add("id: " + room.getIdd() + " romiar: " + room.getSize() + " wolny: " + room.isEmpty());
            id++;
            HotelRoomThread hotelRoomThread = new HotelRoomThread(room.getPort());
            hotelRoomThreads.add(hotelRoomThread);
            hotelRoomThread.setDaemon(true);
            hotelRoomThread.start();
        }
        for (int i = 0; i < two; i++) {
            Room room = new Room(id, findPort(), 2, true);
            room.setDaemon(true);
            room.start();
            twoRoom.add(room);
            itemList.add("id: " + room.getIdd() + " romiar: " + room.getSize() + " wolny: " + room.isEmpty());
            id++;
            HotelRoomThread hotelRoomThread = new HotelRoomThread(room.getPort());
            hotelRoomThreads.add(hotelRoomThread);
            hotelRoomThread.setDaemon(true);
            hotelRoomThread.start();
        }
        for (int i = 0; i < three; i++) {
            Room room = new Room(id, findPort(), 3, true);
            room.setDaemon(true);
            room.start();
            threeRoom.add(room);
            itemList.add("id: " + room.getIdd() + " romiar: " + room.getSize() + " wolny: " + room.isEmpty());
            id++;
            HotelRoomThread hotelRoomThread = new HotelRoomThread(room.getPort());
            hotelRoomThreads.add(hotelRoomThread);
            hotelRoomThread.setDaemon(true);
            hotelRoomThread.start();
        }
    }

    public void onClick(MouseEvent arg0) {
        int id = Integer.parseInt(roomListView.getSelectionModel().getSelectedItem().split(" ")[1]);
        Room room = findRoomById(id);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Room ");
        alert.setHeaderText("Room id " + room.getIdd());
        alert.setContentText("is Empty " + room.isEmpty() + " size " + room.getSize());

        ButtonType buttonTypeOne = new ButtonType("Delete room");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            room.closeRoom();
        } else {
            alert.close();
        }
    }

    private Room findRoomById(int id) {
        for (Room room : oneRoom) {
            if (room.getIdd() == id) return room;
        }
        for (Room room : twoRoom) {
            if (room.getIdd() == id) return room;
        }
        for (Room room : threeRoom) {
            if (room.getIdd() == id) return room;
        }
        return oneRoom.get(0);
    }

    private void setDataForJavaFX(Room room, int i) {
        Platform.runLater(()->{
            itemList.set(i, "id: " + room.getIdd() + " romiar: " + room.getSize() + " wolny: " + room.isEmpty());
            roomListView.refresh();
        });

    }

    @Override
    public void removeRoom(int id) {
        Platform.runLater(() -> {
            int length = String.valueOf(id).length();
            System.out.println(id + " do usuniecia" + length);
            itemList.removeIf(item -> item.substring(4, 4 + length).equals(String.valueOf(id)));
        });

    }

    @Override
    public void cancelReservation() {
        for (int i = 0; i <oneRoom.size(); i++) {
            if(oneRoom.get(i).isEmpty()) {
                setDataForJavaFX(oneRoom.get(i), i);
                hotelRoomThreads.get(i).changeKey();
            }
        }
        for (int i = 0; i <twoRoom.size(); i++) {
            if(twoRoom.get(i).isEmpty()) {
                setDataForJavaFX(twoRoom.get(i), i + oneRoom.size());
                hotelRoomThreads.get(i+oneRoom.size()).changeKey();
            }
        }
        for (int i = 0; i <threeRoom.size(); i++) {
            if(threeRoom.get(i).isEmpty()) {
                setDataForJavaFX(threeRoom.get(i), i + oneRoom.size() + twoRoom.size());
                hotelRoomThreads.get(i+oneRoom.size()+twoRoom.size()).changeKey();
            }
        }
    }

    @Override
    public List<Room> findRoom(String size, int count) {
        List<Room> list = new ArrayList<>();
        List<Integer> listIndexes = new ArrayList<>();
        if (size.equals("R1")) {
            for (int i = 0; i < oneRoom.size(); i++) {
                if (oneRoom.get(i).isEmpty()) {
                    if (listIndexes.size() != count) {
                        listIndexes.add(i);
                    }
                }
            }
            if (listIndexes.size() == count) {
                for (int i = 0; i < listIndexes.size(); i++) {
                    oneRoom.get(i).setEmpty(false);
                    list.add(oneRoom.get(i));
//                    itemList.set(i, "id: " + oneRoom.get(i).getIdd() + " romiar: " + oneRoom.get(i).getSize() + " wolny: " + oneRoom.get(i).isEmpty());
//                    roomListView.refresh();
                    setDataForJavaFX(oneRoom.get(i),i);
                }
            }
        } else if (size.equals("R2")) {
            for (int i = 0; i < twoRoom.size(); i++) {
                if (twoRoom.get(i).isEmpty()) {
                    if (listIndexes.size() != count) {
                        listIndexes.add(i);

                    }
                }
            }
            if (listIndexes.size() == count) {
                for (int i = 0; i < listIndexes.size(); i++) {
                    twoRoom.get(i).setEmpty(false);
                    list.add(twoRoom.get(i));
//                    itemList.set(i + oneRoom.size(), "id: " + twoRoom.get(i).getIdd() + " romiar: " + twoRoom.get(i).getSize() + " wolny: " + twoRoom.get(i).isEmpty());
//                    roomListView.refresh();
                    setDataForJavaFX(twoRoom.get(i),i + oneRoom.size());
                }
            }
        } else {
            for (int i = 0; i < threeRoom.size(); i++) {
                if (threeRoom.get(i).isEmpty()) {
                    if (listIndexes.size() != count) {
                        listIndexes.add(i);
                    }
                }
            }
            if (listIndexes.size() == count) {
                for (int i = 0; i < listIndexes.size(); i++) {
                    threeRoom.get(i).setEmpty(false);
                    list.add(threeRoom.get(i));
//                    itemList.set(i + oneRoom.size() + twoRoom.size(), "id: " + threeRoom.get(i).getIdd() + " romiar: " + threeRoom.get(i).getSize() + " wolny: " + threeRoom.get(i).isEmpty());
//                    roomListView.refresh();
                    setDataForJavaFX(threeRoom.get(i),i + oneRoom.size() + twoRoom.size());
                }
            }
        }
        return list;
    }
}

package org.openjfx;

import javafx.fxml.Initializable;
import org.openjfx.models.Room;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomScene implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setRoom(Room room){
        System.out.println(room.getSize());
    }
}

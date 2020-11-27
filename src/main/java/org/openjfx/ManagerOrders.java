package org.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagerOrders implements Initializable {
    @FXML
    ListView<String> ordersList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onClickList(MouseEvent mouseEvent) {
    }
}

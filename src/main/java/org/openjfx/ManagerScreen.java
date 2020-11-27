package org.openjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerScreen implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void buttonOffer(ActionEvent actionEvent) throws IOException {
        Parent registerPageParent = FXMLLoader.load(getClass().getResource("offers.fxml"));
        Scene registerScene = new Scene(registerPageParent);
        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(registerScene);
        appStage.show();
    }

}

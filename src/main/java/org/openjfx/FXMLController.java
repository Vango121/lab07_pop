package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLController implements Initializable {

    @FXML
    public TextField userName;
    @FXML
    public TextField password;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void hotel(ActionEvent actionEvent) throws IOException {
        changeScene(actionEvent, "hotelScene.fxml");
    }

    @FXML
    public void guest(ActionEvent actionEvent) throws IOException {
        changeScene(actionEvent, "guestScene.fxml");
    }

    @FXML
    public void room(ActionEvent actionEvent) throws IOException {
        changeScene(actionEvent, "roomScene.fxml");
    }

    private void changeScene(ActionEvent actionEvent, String name) throws IOException {
        Parent registerPageParent = FXMLLoader.load(getClass().getResource(name));
        Scene registerScene = new Scene(registerPageParent);
        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(registerScene);
        appStage.show();
    }


}

package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.openjfx.models.User;
import org.openjfx.repository.LoginRepository;

public class FXMLController implements Initializable {

    @FXML
    public TextField userName;
    @FXML
    public TextField password;
    LoginRepository loginRepository;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginRepository = new LoginRepository();
    }

    @FXML
    public void register(ActionEvent actionEvent) throws IOException {
        Parent registerPageParent = FXMLLoader.load(getClass().getResource("register.fxml"));
        Scene registerScene = new Scene(registerPageParent);
        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(registerScene);
        appStage.show();
    }

    public void login(ActionEvent actionEvent) {
        try {
            ResultSet resultSet = loginRepository.logInDB(userName.getText(), password.getText());
            if (resultSet.next()) {
                AccountManager.logIn(new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getInt("acctype")));
                switch (AccountManager.user.getAccType()) {
                    case 1: {
                        Parent registerPageParent = FXMLLoader.load(getClass().getResource("userScreen.fxml"));
                        Scene registerScene = new Scene(registerPageParent);
                        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        appStage.hide();
                        appStage.setScene(registerScene);
                        appStage.show();
                        break;
                    }
                    case 2: {
                        Parent registerPageParent = FXMLLoader.load(getClass().getResource("employeeScreen.fxml"));
                        Scene registerScene = new Scene(registerPageParent);
                        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        appStage.hide();
                        appStage.setScene(registerScene);
                        appStage.show();
                        break;
                    }
                    case 3: {
                        Parent registerPageParent = FXMLLoader.load(getClass().getResource("managerScreen.fxml"));
                        Scene registerScene = new Scene(registerPageParent);
                        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        appStage.hide();
                        appStage.setScene(registerScene);
                        appStage.show();
                        break;
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Blad");
                alert.setContentText("Bledne dane logowania");
                alert.show();
                userName.setText("");
                password.setText("");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

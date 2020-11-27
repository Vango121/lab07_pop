package org.openjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    TextField loginText;
    @FXML
    PasswordField passwordText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void register(ActionEvent actionEvent) {
        DatabaseConn databaseConn = new DatabaseConn();
        Connection connection = databaseConn.getConnection();

        try {
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(1) FROM users WHERE username = '" + loginText.getText() + "'";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            if (resultSet.getInt("COUNT(1)") >= 1) {
                System.out.println("zarejestr");
            } else {
                System.out.println("nie zarejestrw");
                query = "INSERT INTO `users`(`id`, `username`, `password`, `acctype`) VALUES (NULL,'" + loginText.getText() + "','" + passwordText.getText() + "','1')";
                int update = statement.executeUpdate(query);
                if (update == 1) {
                    System.out.println("registered");
                    Parent registerPageParent = FXMLLoader.load(getClass().getResource("scene.fxml"));
                    Scene registerScene = new Scene(registerPageParent);
                    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    appStage.hide();
                    appStage.setScene(registerScene);
                    appStage.show();
                } else {
                    System.out.println("failed");
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

package org.openjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Offers implements Initializable {
    @FXML
    public ListView<String> offerList;
    ObservableList<String> itemList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        offerList.setItems(itemList);
        DatabaseConn databaseConn = new DatabaseConn();
        Connection connection = databaseConn.getConnection();
        String query = "SELECT * FROM `offer`";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println(resultSet.getString("description"));
                itemList.add(resultSet.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void listOnCLick(MouseEvent arg0) throws IOException {
        System.out.println("clicked on " + offerList.getSelectionModel().getSelectedIndex());
        FXMLLoader loader =  new FXMLLoader(getClass().getResource("offerChange.fxml"));
        Parent registerPageParent = loader.load();
        Scene registerScene = new Scene(registerPageParent);
        Stage appStage = (Stage) ((Node) arg0.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(registerScene);
        OfferChange offerChange = loader.getController();
        offerChange.setItemId(offerList.getSelectionModel().getSelectedIndex()+1);
        appStage.show();
    }
}

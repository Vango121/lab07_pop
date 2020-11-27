package org.openjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class UserPrevOrders implements Initializable {

    @FXML
    public ListView<String> orderList;
    ObservableList<String> itemList = FXCollections.observableArrayList();
    DatabaseConn databaseConn;
    Connection connection;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderList.setItems(itemList);
        databaseConn = new DatabaseConn();
        connection = databaseConn.getConnection();
        try {
            getOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void getOrders() throws SQLException {
        String query = "SELECT * FROM orders INNER JOIN offer ON orders.offerId = offer.id WHERE orders.userId = "+AccountManager.user.getId();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            String result = "";
            String offerDescription = resultSet.getString("description");
            int status = resultSet.getInt("status");
            System.out.println(status);
            switch (status){
                case 0: {
                    result = offerDescription + " status zamowienia: oplacone";
                    itemList.add(result);
                    break;
                }
                case 1: {
                    result = offerDescription + " status zamowienia: zaakceptowane";
                    itemList.add(result);
                    break;
                }
                case 2: {
                    result = offerDescription + " status zamowienia: zrealizowane";
                    itemList.add(result);
                    break;
                }
            }

        }
    }
    public void backButton(ActionEvent actionEvent) throws IOException {
        Parent registerPageParent = FXMLLoader.load(getClass().getResource("userScreen.fxml"));
        Scene registerScene = new Scene(registerPageParent);
        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(registerScene);
        appStage.show();
    }
}

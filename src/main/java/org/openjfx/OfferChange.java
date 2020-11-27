package org.openjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class OfferChange implements Initializable {
    DatabaseConn databaseConn;
    @FXML
    TextField offer;
    private int id;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setItemId(int id){
        this.id=id;
        databaseConn = new DatabaseConn();
        Connection connection = databaseConn.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM `offer` WHERE `id` = "+ id;
            System.out.println(query);
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            offer.setText(resultSet.getString("description"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void buttonAktualizuj(ActionEvent actionEvent) throws SQLException {
        String query = "UPDATE `offer` SET `description` = '"+offer.getText()+"' WHERE `offer`.`id` = "+id;
        Connection connection = databaseConn.getConnection();
        Statement statement = connection.createStatement();
        int update = statement.executeUpdate(query);
        if(update==1){
            System.out.println("udane");
        }else{
            System.out.println("failed");
        }
    }
}

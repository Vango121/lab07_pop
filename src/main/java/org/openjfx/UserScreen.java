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
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.openjfx.models.Offer;
import org.openjfx.models.Order;
import org.openjfx.repository.UserRepository;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserScreen implements Initializable {
    @FXML
    public ListView<String> offerList;
    @FXML
    public ListView<String> bagListView;
    ObservableList<String> itemList = FXCollections.observableArrayList();
    ObservableList<String> bagitemList = FXCollections.observableArrayList();
    List<Offer> objectItemList = new ArrayList<>();
    List<Order> orderList = new ArrayList<>();
    TextInputDialog td;
    TextInputDialog tdPayment;
    UserRepository userRepository;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userRepository=new UserRepository();
        td = new TextInputDialog("");
        tdPayment = new TextInputDialog();
        td.setHeaderText("Podaj ilosc ");
        td.setTitle("Dodawanie do koszyka");
        tdPayment.setHeaderText("Platnosc");
        offerList.setItems(itemList);
        bagListView.setItems(bagitemList);
        try {
            objectItemList = userRepository.getOffers();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Offer offer : objectItemList) {
            itemList.add(offer.toString());
        }
    }

    public void listOnCLick(MouseEvent mouseEvent) {
        Order order = new Order();
        td.showAndWait();
        if (!td.getEditor().getText().equals("")){
            order.setOffer(objectItemList.get(offerList.getSelectionModel().getSelectedIndex()));
            order.setQuantity(Integer.parseInt(td.getEditor().getText()));
            orderList.add(order);
            System.out.println(AccountManager.user.getId());
        }

    }

    public void makeOrder(ActionEvent actionEvent) throws SQLException {
        if(orderList.size()>0) {
            int price = 0;
            for (Order i : orderList) {
                price = price + (int) (i.getQuantity() * i.getOffer().getPrice());
            }
            tdPayment.setHeaderText("Zaplac wpisujac cene: " + price);
            tdPayment.showAndWait();
            int value = Integer.parseInt(tdPayment.getEditor().getText());
            if (value == price) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Platnosc");
                alert.setContentText("Platnosc udana");
                alert.show();
                for (Order order : orderList) {
                    userRepository.saveOrder(order);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Blad");
                alert.setContentText("Platnosc nieudana");
                alert.show();
            }
        }
    }
    public void previousOrders(ActionEvent actionEvent) throws IOException {
        Parent registerPageParent = FXMLLoader.load(getClass().getResource("userPrevOrders.fxml"));
        Scene registerScene = new Scene(registerPageParent);
        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(registerScene);
        appStage.show();
    }
}

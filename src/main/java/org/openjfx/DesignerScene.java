package org.openjfx;

import communication.ICenter;
import communication.IDesigner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import support.Answer;
import support.Question;


import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;

public class DesignerScene implements Initializable {

    @FXML
    TextField answerTextField;
    @FXML
    TextField questionTextField;
    @FXML
    TextField descriptionTextField;
    @FXML
    ComboBox<Integer> comboBox;

    private Designer d;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost",3000);
            ICenter ic = (ICenter) reg.lookup("Center");
             d = new Designer(comboBox);
            IDesigner id = (IDesigner) UnicastRemoteObject.exportObject(d,0);
            System.out.println("Designer is ready");
            d.setInterfaces(ic,id);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
    public void buttonQA(){
        Question question = new Question();
        question.question = questionTextField.getText();
        Answer answer = new Answer();
        answer.answer= answerTextField.getText();
        Answer[] a = {answer};
        Question[] questions = { question};
        int check = d.addQA(questions,a);
        if(check == 1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("User in game");
            alert.setContentText("You can't add qa when user is in game");
            alert.showAndWait();
        }
    }
    public void buttonDescription(){
        d.passDescription(descriptionTextField.getText(),comboBox.getSelectionModel().getSelectedIndex());
    }
}

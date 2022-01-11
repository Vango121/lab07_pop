package org.openjfx;

import communication.ICenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import support.Answer;
import support.CustomException;
import support.Question;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class VisitorScene implements Initializable {
    @FXML
    GridPane gridPane;
    private ArrayList<TextField> textFields = new ArrayList<>();
    private int click = 0;
    ICenter ic;
    private int visitorId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 3000);
            ic = (ICenter) reg.lookup("Center");
            setContent(ic.getQuestions());
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Visitor");
            dialog.setContentText("Please enter your name:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                try {
                    visitorId = ic.signIn(name);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void setContent(Question[] questions) {
        for (int i = 0; i < questions.length; i++) {
            Label label = new Label(questions[i].question);
            TextField textField = new TextField();
            gridPane.add(label, 0, i);
            GridPane.setMargin(label, new Insets(5));
            gridPane.add(textField, 1, i);
            GridPane.setMargin(textField, new Insets(5));
            textFields.add(textField);
        }
        Button button = new Button(" Send Answers");
        button.setOnAction(event -> {
                Answer[] answers = new Answer[questions.length];
                for (int i = 0; i < textFields.size(); i++) {
                    answers[i] = new Answer();
                    answers[i].answer = textFields.get(i).getText();
                }
                try {
                    boolean[] results = ic.checkAnswers(visitorId,answers);
                    for (int i = 0; i < results.length; i++) {
                        Label label = new Label(results[i]+"");
                        gridPane.add(label,3,i);
                    }

                } catch (RemoteException | CustomException e) {
                    if(e.getMessage().equals("Answers have already been sent")){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Answers have already been sent");
                        alert.setContentText("You can't send answers twice");
                        alert.showAndWait();
                    }
                    e.printStackTrace();
                }

        });
        gridPane.add(button, 4, questions.length + 2);
        Button buttonSignOut = new Button("Sign out");
        buttonSignOut.setOnAction(event -> {
            try {
                ic.signOut(visitorId);
            } catch (RemoteException | CustomException e) {
                e.printStackTrace();
            }

        });
        gridPane.add(buttonSignOut, 4, questions.length + 3);
    }
}

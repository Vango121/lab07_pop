package org.openjfx;

import communication.ICenter;
import communication.IMonitor;
import communication.IStand;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;

public class MonitorScene implements Initializable, IMonitor {
    int count = 0;
    @FXML
    GridPane gridPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 3000);
            ICenter ic = (ICenter) reg.lookup("Center");
            IMonitor im = (IMonitor) UnicastRemoteObject.exportObject(this, 0);
            ic.connect(im);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setScore(String s, int i) throws RemoteException {
        Platform.runLater(()->{
            Label label = new Label("Name "+s+ " score "+ i);
            gridPane.add(label,1,count++);
            System.out.println("name " + s + " score " + i);
        });

    }
}

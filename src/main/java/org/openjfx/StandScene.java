package org.openjfx;

import communication.ICenter;
import communication.IStand;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import support.CustomException;
import support.Description;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;

public class StandScene implements Initializable, IStand {
    private Stand stand;
    private int id;
    ICenter ic;
    @FXML
    Label descriptionLabel;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Registry reg = LocateRegistry.getRegistry("localhost",3000);
            ic = (ICenter) reg.lookup("Center");
            IStand is = (IStand) UnicastRemoteObject.exportObject(this,0);
            System.out.println("Stand is ready");
            id = ic.connect(is);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setContent(Description description) throws RemoteException {
        Platform.runLater(()->{
            descriptionLabel.setText(description.description);
        });
        int a;
    }

    @Override
    public int getId() throws RemoteException {
        return id;
    }
    @FXML
    public void buttonOnClick(){
        try {
            ic.disconnect(id);
        } catch (RemoteException | CustomException e) {
            e.printStackTrace();
        }
    }
}

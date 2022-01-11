package org.openjfx;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

import communication.ICenter;
import communication.IDesigner;
import communication.IStand;
import javafx.scene.control.ComboBox;
import support.Answer;
import support.CustomException;
import support.Description;
import support.Question;


public class Designer implements IDesigner {
    ICenter ic;
    IDesigner id;
    ArrayList<IStand> iStands;
    ComboBox<Integer> comboBox;

    public Designer(ComboBox<Integer> comboBox) {
        this.comboBox = comboBox;
    }

    public void setInterfaces(ICenter ic, IDesigner id) throws RemoteException {
        this.ic = ic;
        ic.connect(id);
    }

    @Override
    public void notify(int standId, boolean isConnected) throws RemoteException {
        System.out.println("standId=" + standId + " isConnected=" + isConnected);
        iStands = new ArrayList<>(Arrays.asList(ic.getStands()));
        System.out.println(iStands.size());
        if(isConnected){
            comboBox.getItems().add(standId);
        }else{
            comboBox.getItems().removeIf(integer -> integer == standId);
        }


    }

    public int addQA(Question[] questions, Answer[] answers) {
        try {
            ic.addQA(questions, answers);
            return 0;
        } catch (RemoteException | CustomException e) {
            if (e.getMessage().equals("User in game")) return 1;
            e.printStackTrace();
        }
        return 0;
    }

    public void passDescription(String text, int id) {
        Description description = new Description();
        description.description = text;
        try {
            iStands.get(id).setContent(description);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

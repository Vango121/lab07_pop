package org.openjfx;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import communication.ICenter;
import communication.IStand;
import support.Description;

public class Stand implements IStand{

	int id;
	private ICenter iCenter;

	public void setInterface(ICenter iCenter){
		this.iCenter = iCenter;
	}

	@Override
	public void setContent(Description d) throws RemoteException {
		System.out.println(d.description);
	}

	@Override
	public int getId() throws RemoteException {
		return id;
	}

}

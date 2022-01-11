package org.openjfx;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;


import communication.ICenter;
import communication.IDesigner;
import communication.IMonitor;
import communication.IStand;
import support.Answer;
import support.CustomException;
import support.Question;

public class Center implements ICenter {
   
	private static IDesigner id = null;
	private static IStand is = null;
	private static int identifier = 0;
	private ArrayList<IStand> iStands = new ArrayList<>();
	private ArrayList<Question> questions = new ArrayList<>();
	private ArrayList<Answer> answers = new ArrayList<>();
	private ArrayList<IMonitor> iMonitors = new ArrayList<>();
	private int visitorID=0;
	Map<Integer,Integer> resultMap = new TreeMap<>();
	Map<Integer,String > usersMap = new LinkedHashMap<>();
	Map<Integer,Boolean> userInGame = new LinkedHashMap<>();

	public Center() {
	}

	public static void main(String[] args) {
		try {
			Registry reg = LocateRegistry.createRegistry(3000);
			reg.rebind("Center", UnicastRemoteObject.exportObject(new Center(), 0));
			System.out.println("Center is ready");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int signIn(String visitorName) throws RemoteException {
		usersMap.put(visitorID,visitorName);
		userInGame.put(visitorID,true);
		return visitorID++;
	}

	@Override
	public void signOut(int visitorId) throws RemoteException, CustomException {
		usersMap.remove(visitorId);
		resultMap.remove(visitorId);
		userInGame.remove(visitorId);
	}

	@Override
	public Question[] getQuestions() throws RemoteException {
		return questions.toArray(new Question[0]);
	}

	@Override
	public boolean[] checkAnswers(int userId, Answer[] a) throws RemoteException, CustomException {
		if(!resultMap.containsKey(userId)){
			resultMap.put(userId,0);
		}
		if(resultMap.get(userId)==0){
			resultMap.put(userId, resultMap.get(userId) + 1);
			userInGame.put(userId,false);
			boolean[] results = new boolean[a.length];
			for (int i = 0; i < a.length; i++) {
				results[i]= a[i].answer.equals(answers.get(i).answer);
			}
			Stream<Boolean> stream = IntStream.range(0, results.length)
					.mapToObj(idx -> results[idx]);

			iMonitors.forEach(iMonitor -> {
				try {
					iMonitor.setScore(usersMap.get(userId), (int)(((int) stream.filter(value -> value==Boolean.TRUE).count()/(double)questions.size())*100));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			});
			return results;
		}else{
			throw new CustomException("Answers have already been sent");
		}
	}

	@Override
	public void addQA(Question[] q, Answer[] a) throws RemoteException, CustomException {
		if(!userInGame.containsValue(true)){
			questions.addAll(Arrays.asList(q));
			answers.addAll(Arrays.asList(a));
		}
		else throw new CustomException("User in game");
	}

	@Override
	public IStand[] getStands() throws RemoteException {
		return iStands.toArray(new IStand[0]);
	}

	@Override
	public int connect(IDesigner id) throws RemoteException {
		Center.id = id;
		return identifier++;
	}

	@Override
	public int connect(IStand is) throws RemoteException {
		Center.is = is;
		identifier++;
		iStands.add(is);
		id.notify(identifier, true);
		return identifier;
	}

	@Override
	public int connect(IMonitor im) throws RemoteException {
		identifier++;
		iMonitors.add(im);
		return identifier;
	}

	@Override
	public void disconnect(int identifier) throws RemoteException, CustomException {
		boolean removed = iStands.removeIf(iStand -> {
			try {
				return iStand.getId()==identifier;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return false;
		});
		if(removed)id.notify(identifier,false);
	}


}

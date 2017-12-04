package assignment7;

import java.util.Observable;

public class TheUnObservables extends Observable {
	
	
	public void Change_message(String message){
		setChanged();
		notifyObservers(message);
	}
}

package Model;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;

public class ConnectionReader implements Runnable {
	private String readText;
	private String oldText;
	BufferedReader in;
	PropertyChangeSupport support;
	PropertyChangeListener controller;
	
	ConnectionReader(BufferedReader i, PropertyChangeListener inController) {
		in = new BufferedReader(i);
		support = new PropertyChangeSupport(this);
		controller = inController;
		support.addPropertyChangeListener(controller);
	}

	@Override
	public void run() {
		boolean done = false;
		while(!done) {
			System.out.println("ConnectionReader listening.");
			try {
				readText = in.readLine();
				if(readText==null) {
					System.out.println("ConnectionReader1: "+ readText);
					done = true;
					String newText = Message.readXML(Message.createXML(
									"TERMINAL", "BLACK", "Connection ended.")).get();
					support.firePropertyChange("message", oldText, newText);
					oldText = newText;
				} else {
					System.out.println("ConnectionReader2: "+readText);
					String newText = Message.readXML(readText).orElseGet(() -> 
							Message.createXML(
									"TERMINAL", "BLACK", "Uninterpreted message."));
					support.firePropertyChange("message", oldText, newText);
					oldText = newText;
				}
			} catch(IOException e) {
				done = true;
				support.removePropertyChangeListener(controller);
				e.printStackTrace();
			}
			
			
		}
		System.out.println("ConnectionReader ended.");
	}
}
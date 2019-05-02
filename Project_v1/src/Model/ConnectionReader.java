package model;

import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import model.ChatEvent;
import model.ChatEvent.Type;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ConnectionReader implements Runnable {
	private int id;
	private String 		readText;
	private ChatEvent 	oldEvent;
	private BufferedReader 	in;
	private PrintWriter 	out;
	private Socket s;
	private PropertyChangeSupport support;
	private boolean active;
	
	ConnectionReader(int index) {
		support = new PropertyChangeSupport(this);
		active = false;
		id = index;
		System.out.println("ConnectionReader spawned with support "+support);
	}

	void bind(Socket s) throws IOException {
		this.s	= s;
		out		= new PrintWriter(s.getOutputStream(), true);
		in		= new BufferedReader(new InputStreamReader(
				  s.getInputStream()));
		active 	= true;
		
		ChatEvent newEvent = ChatEvent.from(id).connected(s.getInetAddress().toString());
		support.firePropertyChange("connect", oldEvent, newEvent);
		oldEvent = newEvent;
	}
	
	PropertyChangeSupport getSupport() {
		return support;
	}
	
	void writeOut(String s) {
		System.out.println("Sending message: "+s);
		out.println(s);
	}
	
	boolean isActive() {
		return active;
	}
	
	int getID() {
		return id;
	}
	
	void close() {
		active = false;
		out.close();
		try {
			in.close();
		} catch (IOException e) {}
		try {
			s.close();
		} catch (IOException e) {}
		
	}
	
	@Override
	public void run() {
		while(active) {
			try {
				readText = in.readLine();
				if(readText==null) {
					active = false;
				} else {
					ChatEvent newEvent;
					try {
						newEvent = Message.readXML(id, readText);
						
						support.firePropertyChange("message", oldEvent, newEvent);
						oldEvent = newEvent;
						
					} catch (SAXException|ParserConfigurationException e) {
						// Silently swallow malformed messages
						e.printStackTrace();
					}
				}
			} catch(IOException e) {
				if(active == true) {
					ChatEvent newEvent = ChatEvent.from(id).disconnected(oldEvent.getSender());
					support.firePropertyChange("disconnect", oldEvent, newEvent);
					oldEvent = newEvent;
				}
				active = false;
			}
		}
	}
}
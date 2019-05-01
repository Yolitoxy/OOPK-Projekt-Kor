package model;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Connection {
	private ServerSocket listen;
	private List<ConnectionReader> readers;
	private List<PropertyChangeListener> subscribers;
	private boolean active;
	
	private String name;
	private Color c;
	
	private int readerIndex;
	
	public Connection() {
		readers 	= new ArrayList<>();
		subscribers = new ArrayList<>();
		active		= false;
		listen		= null;
		name		= "Default";
		c			= Color.black;
		readerIndex = 0;
	}
	
	
	public void connect(String IPAdress, int port) {
		try {
			connect(new Socket(IPAdress, port));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void host(int port) {
		try {
			listen = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ListenOnce l = new ListenOnce(listen);
		for( PropertyChangeListener sub : subscribers ) {
			l.support.addPropertyChangeListener(sub);
		}
		
		Thread t = new Thread(l);
		t.start();
	}
	
	void hostRoom(int port) {
		try {
			listen = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ListenAlways l = new ListenAlways(listen);
		for( PropertyChangeListener sub : subscribers ) {
			l.support.addPropertyChangeListener(sub);
		}
		
		Thread t = new Thread(l);
		t.start();
	}
	
	private void connect(Socket s) throws IOException {
		
		//	add an observable that listens to the incoming stream
		//	and notifies controller when it receive incoming text

		readerIndex++;
		ConnectionReader reader = new ConnectionReader(readerIndex);
		readers.add(reader);
		
		for(PropertyChangeListener sub: subscribers) {
			System.out.println("Adding subscriber "+sub+" to "+reader.getSupport());
			reader.getSupport()
				  .addPropertyChangeListener(sub);
		}
		
		reader.bind(s);
		Thread t = new Thread(reader);
		t.start();
	}
	
	public int getHostPort() {
		if(listen == null) throw new IllegalStateException("Not hosting.");
		return listen.getLocalPort();
	}
	
	public InetAddress getHostAdress() {
		if(listen == null) throw new IllegalStateException("Not hosting.");
		return listen.getInetAddress();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public void setColor(Color c) {
		this.c = c;
	}
	
	public boolean isHosting() {
		return !(listen == null);
	}
	
	public boolean isActive() {
		for (ConnectionReader r: readers) {
			if (r.isActive()) {
				return true;
			}
		}
		return false;
	}
	
	public Message messageFromText(String s) {
		return new Message(name, c, s);
	}
	
	public void sendText(String s) {
		Message m = new Message(name, c, s);
		sendString(m.toXML());
	}
	
	public void sendString(String raw) {
		for(ConnectionReader r : readers) {
			r.writeOut(raw);
		}
	}
	
	
	
	public void subscribe(PropertyChangeListener sub) {
		subscribers.add(sub);
	}
	
	List<ConnectionReader> getReaders() {
		return readers;
	}
	
	public void close() {
		System.out.println("---Closing connections.");
		sendString(Message.logoutXml(name));
		for(ConnectionReader r: readers) {
			r.close();
		}
		
		try {
			listen.close();
		} catch(NullPointerException e) {
			// listen not initialized
		} catch (IOException e) {
			// listen currently listening, swallow
		}
	}
	
	private class ListenOnce implements Runnable {
		private ServerSocket s;
		private PropertyChangeSupport support;
		
		ListenOnce(ServerSocket s) {
			this.s=s;
			support = new PropertyChangeSupport(this);
		}
		
		@Override
		public void run() {
			support.firePropertyChange("connect", null, ChatEvent.from(0).hosting(name));
			try {
				connect(s.accept());
			} catch (IOException e) {
				// swallow
				e.printStackTrace();
			}
		}
	}
	
	private class ListenAlways implements Runnable {
		private ServerSocket s;
		private PropertyChangeSupport support;
		
		ListenAlways(ServerSocket s) {
			this.s = s;
			support = new PropertyChangeSupport(this);
		}
		
		@Override
		public void run() {
			support.firePropertyChange("connect", null, ChatEvent.from(0).hosting(name));
			while(active) {
				try {
					connect(s.accept());
				} catch (IOException e) {
					// swallow
					e.printStackTrace();
				}
			}
		}
	}

	public void logout() {
		// TODO Auto-generated method stub
		
	}

}

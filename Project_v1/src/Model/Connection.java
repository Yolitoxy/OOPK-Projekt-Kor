package Model;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Connection {
	private ServerSocket listen;
	private List<ConnectionReader> readers;
	private List<PropertyChangeListener> subscribers;
	public boolean active;
	
	
	public Connection() {
		readers 	= new ArrayList<>();
		subscribers = new ArrayList<>();
		active		= false;
		listen		= null;
	}
	
	
	void connect(String IPAdress, int port) {
		try {
			connect(new Socket(IPAdress, port));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	void host(int port) {
		try {
			listen = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread t = new Thread(new ListenOnce(listen));
		t.start();
	}
	
	void hostRoom(int port) {
		try {
			listen = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread t = new Thread(new ListenAlways(listen));
		t.start();
	}
	
	private void connect(Socket s) throws IOException {
		
		//	add an observable that listens to the incoming stream
		//	and notifies controller when it receive incoming text
		ConnectionReader reader = new ConnectionReader(s);
		readers.add(reader);
		
		for(PropertyChangeListener sub: subscribers) {
			System.out.println("Adding subscriber "+sub+" to "+reader.getSupport());
			reader.getSupport()
				  .addPropertyChangeListener(sub);
		}
		
		Thread t = new Thread(reader);
		t.start();
	}
	

	public void sendText(String s) {
		for(ConnectionReader r : readers) {
			r.writeOut(s);
		}
	}
	
	void subscribe(PropertyChangeListener sub) {
		subscribers.add(sub);
	}
	
	List<ConnectionReader> getReaders() {
		return readers;
	}
	
	void close() {
		System.out.println("---Closing connections.");
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
		
		ListenOnce(ServerSocket s) {
			this.s=s;
		}
		
		@Override
		public void run() {
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
		
		ListenAlways(ServerSocket s) {
			this.s = s;
		}
		
		@Override
		public void run() {
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
}

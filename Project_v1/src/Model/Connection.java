package Model;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.*;

public class Connection {
	Socket MainSocket = null;
	ServerSocket ServerListenSocket = null;
	private PrintWriter out;
	private BufferedReader in;
	private PropertyChangeListener controller;
	
	public void sendText(String s) {
		System.out.println("Sending message: "+s);
		out.println(s);
		return;
	}
	
	// Client instance
	public Connection(String IPadress, int port, PropertyChangeListener inController) {
		controller = inController;
		try {
			setup(new Socket(IPadress,port));
		} catch(IOException e) {
			//	TODO error: not found
			e.printStackTrace();
		}
	}
	
	// Host instance
	public Connection(int port, PropertyChangeListener inController) {
		controller = inController;
		try {
			ServerListenSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread t = new Thread(new ConnectionHostListener(ServerListenSocket));
		t.start();
	}
	
	private void setup(Socket s) throws IOException {
		//	connect this component to the given socket
		MainSocket = s;
		out = new PrintWriter(MainSocket.getOutputStream(), true);
		in = new BufferedReader(
	    		new InputStreamReader(MainSocket.getInputStream()));
		
		//	add an observable that listens to the incoming stream
		//	and notifies controller when it receive incoming text
		Thread t = new Thread(new ConnectionReader(in,controller));
		t.start();
	}
	
	void close() {
		try {
			System.out.println("---Closing connection.");
			in.close();
			out.close();
			MainSocket.close();
		} catch (IOException|NullPointerException e) {
			//	if the sockets are not found, they cannot be closed
			//	this is fine as this means that the sockets aren't initialized
			e.printStackTrace();
		}
	}
	
	private class ConnectionHostListener implements Runnable {
		private ServerSocket s;
		
		ConnectionHostListener(ServerSocket s) {
			this.s=s;
		}
		
		@Override
		public void run() {
			try {
				setup(s.accept());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}

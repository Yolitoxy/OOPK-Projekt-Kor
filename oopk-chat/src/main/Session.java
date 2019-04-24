package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

public class Session {
	List<SEvent> events;
	List<Connection> connections;
	
	private Boolean open = true;
	
	List<SEvent> getEvents() {
		return events;
	}
	
	void close() {
		open = false;
		for (Connection c: connections) {
			try {
				c.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	void sendMessage(Message m) {
		var out = m.toXMLString();
		for (Connection c: connections) {
			
		}
	}
	
	private class HostOnce implements Runnable {
		int port;
		
		HostOnce(int port) {
			this.port = port;
		}
		
		@Override
		public void run() {
			try (var s = new ServerSocket(port)) {
				Connection c = new Connection(s.accept());
				connections.add(c);
				events.add(new SEvent(SEvent.Type.CONNECT));
				
				Thread t = new Thread(
						new Reader(c));
				t.start();
			} catch (IOException e) {
				// port already occupied mb?
				e.printStackTrace();
			}
		}
	}
	
	private class HostOpen implements Runnable {
		int port;
		
		HostOpen(int port) {
			this.port = port;
		}
		
		@Override
		public void run() {
			try (var s = new ServerSocket(port)) {
				while(open) {
					Connection c = new Connection(s.accept());
					connections.add(c);
					events.add(new SEvent(SEvent.Type.CONNECT));
					
					Thread t = new Thread(
							new Reader(c));
					t.start();
				}
			} catch (IOException e) {
				// port already occupied mb?
				e.printStackTrace();
			}
		}
	}
	
	private class Reader implements Runnable {
		Connection c;
		
		Reader(Connection c){
			this.c = c;
		}
		
		@Override
		public void run() {
			while(open) {
				try {
					var line = c.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}

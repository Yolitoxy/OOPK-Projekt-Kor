package Model;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;

public class ConnectionReader implements Runnable {
	private String readText;
	private String oldText;
	BufferedReader in;
	PrintWriter out;
	Socket s;
	PropertyChangeSupport support;
	boolean active;
	
	ConnectionReader() {
		support = new PropertyChangeSupport(this);
		active = false;
		System.out.println("ConnectionReader spawned with support "+support);
	}
	
	ConnectionReader(Socket s) throws IOException {
		this();
		System.out.println("ConnectionReader spawned with socket "+s);
		bind(s);
	}

	void bind(Socket s) throws IOException {
		this.s	= s;
		out		= new PrintWriter(s.getOutputStream(), true);
		in		= new BufferedReader(new InputStreamReader(
				  s.getInputStream()));
		active 	= true;
	}
	
	PropertyChangeSupport getSupport() {
		return support;
	}
	
	void writeOut(String s) {
		System.out.println("Sending message: "+s);
		out.println(s);
	}
	
	void close() {
		out.close();
		
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(active) {
			System.out.println("ConnectionReader listening.");
			System.out.println("Currently hooked: "+support.getPropertyChangeListeners());
			try {
				readText = in.readLine();
				if(readText==null) {
					System.out.println("ConnectionReader1: "+ readText);
					active = false;
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
				active = false;
				e.printStackTrace();
			}
			
			
		}
		System.out.println("ConnectionReader ended.");
	}
}
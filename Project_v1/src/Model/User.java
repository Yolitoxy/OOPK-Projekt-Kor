package Model;
import java.beans.PropertyChangeListener;

public class User {
	private String username;
	private String color;
	private Connection connection;
	private PropertyChangeListener controller;
	
	public void sendMessage(String m) {
		String send = Message.createXML(username, color, m);
		connection.sendText(send);
	}
	
	public void connectTo(String IP, int port) {
		connection = new Connection(IP, port, controller);
	}
	
	public void hostAt(int port) {
		connection = new Connection(port, controller);
	}
	
	public void closeConnection() {
		connection.close();
	}
	
	public void setColor(String newColor) {
		color = newColor;
		return;
	}
	
	public void setUsername(String newUsername) {
		username = newUsername;
		return;
	}
	
	public String getColor() {
		return color;
	}
	
	public String getUsername() {
		return username;
	}
	
	public User(String inUsername, String inColor, PropertyChangeListener inController) {
		username = inUsername;
		color = inColor;
		controller = inController;
	}
	
	public String toString() {
		return "Name: " + getUsername() + "\n"
				+ "Color: " + getColor();
	}
}

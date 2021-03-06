package Model;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import View.ChatFrame;
import View.ChatPanel;
import View.SessionView;

public class User
implements PropertyChangeListener,
		   ActionListener {
	private String username;
	private Color color;
	private Connection connection;
	private SessionView view;
	
	public enum Button {
		ENTER,
		COLOR,
		LOGOUT;
	}
	
	public User(String inUsername, Color inColor, ChatFrame v) {
		username = inUsername;
		color = inColor;
		view = v.spawnChatPanel();
		connection = new Connection();
		connection.subscribe(this);
		
        for(Button b: Button.values()) {
        	view.getButton(b).addActionListener(this);
        }
	}
	
	public void sendMessage(String m) {
		String send = Message.createXML(username, color, m);
		connection.sendText(send);
	}
	
	public void connectTo(String IP, int port) {
		connection.connect(IP, port);
	}
	
	public void hostOnce(int port) {
		connection.host(port);
	}
	
	public void closeConnection() {
		connection.close();
	}
	
	public void setColor(Color newColor) {
		color = newColor;
		return;
	}
	
	public void setUsername(String newUsername) {
		username = newUsername;
		return;
	}
	
	public String getUsername() {
		return username;
	}

	public Color getColor() {
		return color;
	}
	

	
	public void propertyChange(PropertyChangeEvent evt) {
		String inMessage = ((String)evt.getNewValue());
		String ID = ((String)evt.getPropertyName());
		System.out.println("User recieved command "+ID);
		switch(ID) {
			case "message":
			// 	a bonafide message that is supposed to be displayed
				System.out.println("User: "+inMessage);
				view.displayMessage(inMessage);
				break;
			case "disconnect":
				System.out.println("what m8");
			//	in this case, the other user disconnected, and we
			//	display that we received this information and close the socket
				view.displayMessage(inMessage);
				closeConnection();
				break;
			default:
			//	undiagnosed error
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//	Chat window: The enter button causes a message to be sent.
		if(e.getSource() == view.getButton(Button.ENTER)) {
			sendMessage(view.getNewSentMessage());
			view.displayMessage(
					Message.readXML(
						Message.createXML(this,
							view.getNewSentMessage())
					).get());
		
		//	Chat window: Change color if color button is pressed.
		}
		else if(e.getSource() == view.getButton(Button.COLOR)) {
			Color newColor=JColorChooser.showDialog(
				null,
				"Choose your textcolor",
				Color.black
			);
			setColor(newColor);
		}
		
		else if(e.getSource()==view.getButton(Button.LOGOUT)) {
			String logOutMessage = Message.createXML(
				"System",
				Color.RED,
				username +" has logged out."
			);
        	sendMessage(logOutMessage); 
        	view.displayMessage(
        			Model.Message.readXML(logOutMessage).get());
        	
        	closeConnection();
		}
		
	}
}

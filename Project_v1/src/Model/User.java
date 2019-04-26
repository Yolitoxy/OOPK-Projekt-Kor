package Model;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JColorChooser;

import View.ChatPanel;

public class User
implements PropertyChangeListener,
		   ActionListener {
	private String username;
	private String color;
	private Connection connection;
	private ChatPanel view;
	
	public User(String inUsername, String inColor, ChatPanel view) {
		username = inUsername;
		color = inColor;
		this.view = view;
		connection = new Connection();
		connection.subscribe(this);
		
        view.getEnterButton().addActionListener((ActionListener)this);
        view.getColorButton().addActionListener((ActionListener)this);
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
	
	public void setColor(String newColor) {
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

	public String getColor() {
		return color;
	}
	

	
	public void propertyChange(PropertyChangeEvent evt) {
		String inMessage = ((String)evt.getNewValue());
		String ID = ((String)evt.getPropertyName());
		System.out.println("User recieved command "+ID);
		switch(ID) {
			case "message":
			// 	a bonafide message that is supposed to be displayed
				System.out.println(view);
				System.out.println(inMessage);
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
		if(e.getSource()==view.getEnterButton()) {
			sendMessage(view.getNewSentMessage());
			view.displayMessage(
					Message.readXML(
						Message.createXML(this,
							view.getNewSentMessage())
					).get());
		
		//	Chat window: Change color if color button is pressed.
		}
		else if(e.getSource()==view.getColorButton()) {
			Color newColor=JColorChooser.showDialog(
				null,
				"Choose your textcolor",
				Color.black
			);
			setColor(newColor.toString());
		}
		
	}
}

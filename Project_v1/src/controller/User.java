package controller;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import model.ChatEvent;
import model.Connection;
import model.Message;
import view.ChatFrame;
import view.ChatPanel;
import view.SessionView;

public class User
implements PropertyChangeListener,
		   ActionListener {
	private Connection connection;
	private SessionView view;
	
	
	public enum Button {
		ENTER,
		COLOR,
		LOGOUT,
		CLOSE;
	}
	
	public User(String username, Color color, ChatFrame v) {
		connection = new Connection();
		connection.subscribe(this);
		view = v.spawnChatPanel(connection);
		
        for(Button b: Button.values()) {
        	view.getButton(b).addActionListener(this);
        }
        connection.setName(username);
        connection.setColor(color);
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
	

	
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("User recieved command "+evt.getPropertyName());
		ChatEvent newEvent = (ChatEvent)evt.getNewValue();
		switch(newEvent.getType()) {
			case MESSAGE:
			case DISCONNECT:
			case CONNECT:
				view.display(newEvent);
				view.display();
				break;
			case HOSTING:
				view.display();
				break;
			default:
			//	undiagnosed error
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//	Chat window: The enter button causes a message to be sent.
		if(e.getSource() == view.getButton(Button.ENTER)) {
			String txt = view.getNewSentMessage();
			connection.sendText(txt);
			view.display(ChatEvent
					.from(0)
					.message(connection.messageFromText(txt)));
		
		//	Chat window: Change color if color button is pressed.
		}
		else if(e.getSource() == view.getButton(Button.COLOR)) {
			Color newColor=JColorChooser.showDialog(
				null,
				"Choose your textcolor",
				Color.black
			);
			connection.setColor(newColor);
		}
		
		else if(e.getSource()==view.getButton(Button.LOGOUT)) {
			view.display(ChatEvent
					.from(0)
					.disconnected(connection.getName()));
        	closeConnection();
		}
		else if(e.getSource() == view.getButton(Button.CLOSE)) {
			view.display(ChatEvent
					.from(0)
					.disconnected(connection.getName()));
			closeConnection();
		}
	}
}

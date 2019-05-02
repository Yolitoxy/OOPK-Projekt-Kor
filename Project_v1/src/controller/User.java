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


/*
 *  Tar hand om en chatsession genom att lyssna p� events, dels fr�n uppkopplingen (connection)
 *  och dels fr�n gr�nssnittet som tillh�r den h�r chatsessionen (SessionView, implementeras i ChatPanel).
 */
public class User
implements PropertyChangeListener,
		   ActionListener {
	private Connection connection;
	private SessionView view;
	
	// H�r definieras vilka knappar som User f�rv�ntar sig kunna hitta i gr�nssnittet (SessionView)
	public enum Button {
		ENTER,
		COLOR,
		LOGOUT,
		CLOSE;
	}
	
	/* 
	 * Konstruktorn initialiserar connection och l�gger till sig sj�lv som listener till det som
	 * h�nder d�ri. Dessutom ber den ChatFrame om en SessionView via spawnChatPanel, och lyssnar
	 * till knapparna d�ri.
	 */
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
	
	/*
	 * Instruerar connection att koppla upp sig mot en f�rv�ntad host vid den givna adressen.
	 */
	public void connectTo(String IP, int port) {
		connection.connect(IP, port);
	}
	
	/*
	 * Instruerar connection att b�rja hosta genom givna porten.
	 */
	public void hostOnce(int port) {
		connection.host(port);
	}
	
	/*
	 * S�ger �t connection att frig�ra och st�nga ned.
	 */
	public void closeConnection() {
		connection.close();
	}
	

	/*
	 * Det �r connection som skickar PropertyChangeEvent. De inneh�ller ChatEvents. Om det
	 * �r n�got som ska visas i meddelanderutan s� notifieras view specifikt om det.
	 */
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
	
	/*
	 * ActionEvents kommer ifr�n knapparna i view.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Skicka meddelande.
		if(e.getSource() == view.getButton(Button.ENTER)) {
			String txt = view.getNewSentMessage();
			connection.sendText(txt);
			view.display(ChatEvent
					.from(0)
					.message(connection.messageFromText(txt)));
		
		// �ndra skickad textf�rg.
		}
		else if(e.getSource() == view.getButton(Button.COLOR)) {
			Color newColor=JColorChooser.showDialog(
				null,
				"Choose your textcolor",
				Color.black
			);
			connection.setColor(newColor);
		}
		
		// Logga ut.
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

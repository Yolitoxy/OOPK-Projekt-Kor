package View;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ChatFrame extends JFrame implements ActionListener {
	private static ChatPanel myChatPanel;
	private static PopUpPanel myPopUpPanel;
	private Model.User myUser;
	
	
	public ChatFrame(Model.User inUser)
			throws IOException {
		myUser = inUser;
		
        setPreferredSize(new Dimension(900, 900));
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        myPopUpPanel= new PopUpPanel((ActionListener)this);
        myPopUpPanel.setVisible(true);
        add(myPopUpPanel);
        
        myChatPanel= new ChatPanel((ActionListener)this);
        myChatPanel.setVisible(true);
        
        pack();

	}
	
	public void updateMessage(String newMessage) {
		myChatPanel.displayMessage(newMessage);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//	If Client button was pressed, read the user
		//	entered text from the relevant lines and
		//	try to connect.
		if(e.getSource() == myPopUpPanel.getClientButton()) {
			String userName = myPopUpPanel.getUserName();
			String IP = myPopUpPanel.getIP();
			int portCode = myPopUpPanel.getPortcode();
			
			myUser.setUsername(userName);
			System.out.println("connect to: "+userName);
			myUser.connectTo(IP, portCode);
			
			myChatPanel.setInformationBar(IP, portCode);
			remove(myPopUpPanel);
			add(myChatPanel);
		}
		
		//	If Server button was pressed, read the user
		//	entered text and try to set up a server.
		else if(e.getSource() == myPopUpPanel.getServerButton()) {
			String userName = myPopUpPanel.getUserName();
			int portCode = myPopUpPanel.getPortcode();
			
			myUser.setUsername(userName);
			System.out.println("host name "+userName+" at port "+portCode);
			myUser.hostAt(portCode);
			
			myChatPanel.setInformationBar("Host. ", portCode);
			remove(myPopUpPanel);
			add(myChatPanel);
			pack();
		}
		
		//	Chat window: The enter button causes a message to be sent.
		else if(e.getSource()==myChatPanel.getEnterButton()) {
			myUser.sendMessage(myChatPanel.getNewSentMessage());
			myChatPanel.displayMessage(
					Model.Message.readXML(
						Model.Message.createXML(myUser,
							myChatPanel.getNewSentMessage())
					).get());
		
		//	Chat window: Change color if color button is pressed.
		}
		else if(e.getSource()==myChatPanel.getColorButton()) {
			Color newColor=JColorChooser.showDialog(
				null,
				"Choose your textcolor",
				Color.black
			);
			myUser.setColor(newColor.toString());
		}
		
		//	Chat window: Close down the current chat session.
		else if(e.getSource()==myChatPanel.getCloseButton()) {
			String logOutMessage = Model.Message.createXML(
				"System",
				"RED",
				myUser.getUsername()+" has logged out."
			);
        	myUser.sendMessage(logOutMessage); 
        	myChatPanel.displayMessage(
        			Model.Message.readXML(logOutMessage).get());
        	
        	myUser.closeConnection(); 
        	remove(myChatPanel);
        	add(myPopUpPanel);
        	pack();
		}
		
	}
	
};


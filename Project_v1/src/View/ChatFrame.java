package View;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ChatFrame extends JFrame {
	private static ChatPanel myChatPanel;
	private static PopUpPanel myPopUpPanel;
	
	
	public ChatFrame()
			throws IOException {
		
        setPreferredSize(new Dimension(900, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        myPopUpPanel= new PopUpPanel();
        myPopUpPanel.setVisible(true);
        add(myPopUpPanel);
        
        myChatPanel= new ChatPanel();
        myChatPanel.setVisible(true);
        
        pack();

	}
	
	public PopUpPanel getPopUpPanel() {
		return myPopUpPanel;
	}
	
	public void updateMessage(String newMessage) {
		myChatPanel.displayMessage(newMessage);
	}

	public ChatPanel getChatPanel() {
		return myChatPanel;
	}
	
};


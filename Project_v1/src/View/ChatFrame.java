package View;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatFrame extends JFrame {
	private PopUpPanel p;
	private ChatPanel myChatPanel;
	private List<ActionListener> subscribers;
	
	
	public ChatFrame()
			throws IOException {
		subscribers = new ArrayList<>();
		
        setPreferredSize(new Dimension(900, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        p = new PopUpPanel();
        p.setVisible(true);
        add(p);
        
        pack();

	}
	
	public PopUpPanel getPopUpPanel() {
		return p;
	}
	
	public void subscribe(ActionListener sub) {
		if(myChatPanel != null) {
			for (JButton b: myChatPanel.buttons()) {
				b.addActionListener(sub);
			}
		}
		p.getClientButton().addActionListener(sub);
		p.getServerButton().addActionListener(sub);
	}
	
	public ChatPanel spawnChatPanel() {
		myChatPanel = new ChatPanel();
		myChatPanel.setVisible(true);
		
		for (ActionListener sub: subscribers) {
			for (JButton b: myChatPanel.buttons()) {
				b.addActionListener(sub);
			}
		}
		return myChatPanel;
	}

	public ChatPanel getChatPanel() {
		return myChatPanel;
	};
	
};


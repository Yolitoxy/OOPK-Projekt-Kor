package View;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
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
		
        //setPreferredSize(new Dimension(900, 350));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        myChatPanel = new ChatPanel();
        add(myChatPanel);
        
        p = new PopUpPanel();
        add(p);
        
        
        layoutInit();

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
		remove(myChatPanel);
		myChatPanel = new ChatPanel();
		myChatPanel.setVisible(true);
		add(myChatPanel);
		
		pack();
		
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
	
	private void layoutInit() {
		BoxLayout l = new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS); 
		setLayout(l);
		
		
        pack();
	}
};


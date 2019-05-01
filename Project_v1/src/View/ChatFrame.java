package view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import controller.User;

import java.awt.event.*;
import java.io.IOException;

public class ChatFrame extends JFrame {
	private PopUpPanel p;
	private ChatPanel myChatPanel;
	private JTabbedPane tabs;
	
	public ChatFrame()
			throws IOException {
		
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        p = new PopUpPanel();
        
        tabs = new JTabbedPane();
        add(tabs);
        tabs.addTab("Login",p);
        
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
	
	public ChatPanel spawnChatPanel(model.Connection c) {
		JPanel tab = new JPanel();
		ChatPanel temp = new ChatPanel(c,tab);
        tabs.addTab("", temp);
        tabs.setTabComponentAt(tabs.indexOfComponent(temp), tab);
        temp.getButton(User.Button.CLOSE).addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			tabs.removeTabAt(tabs.indexOfComponent(temp));
        		}
        	}
        );
        
		pack();
		
		return temp;
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


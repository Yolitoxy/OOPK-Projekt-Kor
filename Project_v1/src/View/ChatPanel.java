package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import controller.User;
import model.ChatEvent;
import model.Connection;
import model.HTMLSerializer;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;


	
public class ChatPanel extends JPanel implements SessionView {
    private JButton colorButton;
    private JButton logoutButton;
    private JButton enterButton;
    
    private JTextField chatTextField;
    private JLabel informationBar;
    
    private JTextPane displayField;
    private List<JButton> buttons;
    private Connection connection;
    
    private JPanel tabPanel;
    private JLabel tabText;
    private JButton closeButton1;
    
    public ChatPanel(Connection c, JPanel tab){
    	connection = c;
    	tabPanel = tab;
    	tabText = new JLabel();
    	closeButton1 = defaultCtrlButton();
    	
    	
    	tabPanel.add(tabText);
    	tabText.setAlignmentX(LEFT_ALIGNMENT);
    	tabPanel.add(closeButton1);
    	closeButton1.setAlignmentX(RIGHT_ALIGNMENT);
    	
        BoxLayout panelLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(panelLayout);
        
        enterButton = defaultCtrlButton();
        colorButton = defaultCtrlButton();
        logoutButton = defaultCtrlButton();
        
        enterButton.setText(">");
        colorButton.setText("o");
        logoutButton.setText("x");
        
        enterButton.setBackground(Color.GREEN);
        colorButton.setBackground(Color.LIGHT_GRAY);
        logoutButton.setBackground(Color.RED);
        chatTextField= new JTextField(20);
        chatTextField.setMaximumSize(new Dimension(
        		Integer.MAX_VALUE,
        		chatTextField.getPreferredSize().height));
        chatTextField.setText("Write your message here");
        
        JPanel controls = new JPanel();
        BoxLayout controlsLayout = new BoxLayout(controls, BoxLayout.LINE_AXIS);
        controls.setLayout(controlsLayout);
        controls.add(chatTextField);
        controls.add(Box.createRigidArea(new Dimension(5,0)));
        controls.add(enterButton);
        controls.add(Box.createRigidArea(new Dimension(5,0)));
        controls.add(colorButton);
        controls.add(Box.createRigidArea(new Dimension(5,0)));
        controls.add(logoutButton);
        
        controls.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        buttons = new ArrayList<>();
        buttons.add(enterButton);
        buttons.add(logoutButton);
        buttons.add(colorButton);

        displayField= new JTextPane();
        informationBar= new JLabel();
        informationBar.setBorder(BorderFactory.createEmptyBorder(5,5,5,15));
        
        JScrollPane displayFieldContainer = new JScrollPane(displayField);
        displayField.setContentType("text/html");
        displayField.setEditable(false);
        displayFieldContainer.setPreferredSize(new Dimension(300,180));
        

        add(displayFieldContainer);
        add(controls);
        add(informationBar);
        
        displayFieldContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        controls.setAlignmentX(Component.LEFT_ALIGNMENT);
        informationBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        display();
    }
    
    public JButton getEnterButton() {
    	return enterButton;
    }
    
    public JButton getColorButton() {
    	return colorButton;
    }
    
    public JButton getLogoutButton() {
    	return logoutButton;
    }
    
   public String getNewSentMessage() {
	   String newSentMessage = chatTextField.getText();
	   return newSentMessage;
   }

    
    private void setInformationBar() {
    	if (connection == null) {
    		System.out.println("ChatPanel: connection null");
    		informationBar.setText("Not connected");
    	} else if( connection.isHosting() ) {
    		System.out.println("ChatPanel: hosting");
        	informationBar.setText("Hosting at "+connection.getHostAdress()+" at port "+connection.getHostPort());
    	} else if ( connection.isActive() ) {
    		System.out.println("ChatPanel: connected");
    		informationBar.setText("Connected");
    	} else {
    		System.out.println("ChatPanel: not connected");
    		informationBar.setText("Not connected");
    	}
    }
    
    private void setTab() {
    	tabText.setText(connection.getName());
    	closeButton1.setText("x");
    	closeButton1.setForeground(Color.BLACK);
    }

    private JButton defaultCtrlButton() {
    	JButton b = new JButton();
    	Dimension bDim = new Dimension(20,20);
        b.setPreferredSize(bDim);
        b.setForeground(Color.WHITE);
        b.setMargin(new Insets(0,0,0,0));
        b.setHorizontalTextPosition(SwingConstants.CENTER);
        b.setVerticalTextPosition(SwingConstants.CENTER);
        
        return b;
    }

    public void displayMessage(String newMessage){
    	HTMLDocument d = (HTMLDocument)displayField.getDocument();
    	try {
			d.insertBeforeEnd(d.getDefaultRootElement().getElement(0), newMessage);
		} catch (BadLocationException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
			// this should never be reachable as we always have a body
		}
    }
    
    public void display() {
    	setInformationBar();
    	setTab();
    	repaint();
    }

	public List<JButton> buttons() {
		return buttons;
	}

	public AbstractButton getButton(controller.User.Button b) {
		switch(b) {
		case ENTER:
			return enterButton;
		case COLOR:
			return colorButton;
		case LOGOUT:
			return logoutButton;
		case CLOSE:
			return closeButton1;
		}
		throw new IllegalArgumentException(
			"Did not correspond to any instantiated button.");
	}

	@Override
	public void display(ChatEvent evt) {
		HTMLDocument d = (HTMLDocument)displayField.getDocument();
    	try {
			d.insertBeforeEnd(d.getDefaultRootElement().getElement(0), HTMLSerializer.toHtml(evt));
		} catch (BadLocationException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
			// this should never be reachable as we always have a body
		}
		
	}
    
}
  
	



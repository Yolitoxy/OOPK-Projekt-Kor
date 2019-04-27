package View;

import java.awt.*;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import Model.User.Button;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;


	
public class ChatPanel extends JPanel implements SessionView {
	private static final int X_SIZE=700;
    private static final int Y_SIZE=700;
    private JButton colorButton;
    private JButton closeButton;
    private JButton enterButton;
    private JTextField chatTextField;
    private JLabel informationBar;
    private JTextPane displayField;
    private ArrayList<String> messageList= new ArrayList<String>();
    private List<JButton> buttons;
  
    
    public ChatPanel(){
    	
    	//setLayout(null);
    	//setSize(X_SIZE,Y_SIZE);

        BoxLayout panelLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(panelLayout);
        
        enterButton = defaultCtrlButton();
        colorButton = defaultCtrlButton();
        closeButton = defaultCtrlButton();
        
        enterButton.setText(">");
        colorButton.setText("o");
        closeButton.setText("x");
        
        enterButton.setBackground(Color.GREEN);
        colorButton.setBackground(Color.LIGHT_GRAY);
        closeButton.setBackground(Color.RED);
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
        controls.add(closeButton);
        
        controls.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        buttons = new ArrayList<>();
        buttons.add(enterButton);
        buttons.add(closeButton);
        buttons.add(colorButton);
        

        displayField= new JTextPane();
        informationBar= new JLabel("IP-adress:");
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
    }
    
    public JButton getEnterButton() {
    	return enterButton;
    }
    
    public JButton getColorButton() {
    	return colorButton;
    }
    
    public JButton getCloseButton() {
    	return closeButton;
    }
    
   public String getNewSentMessage() {
	   String newSentMessage = chatTextField.getText();
	   return newSentMessage;
   }

    
    public void setInformationBar(String IPAdress,int portCode) {
    	informationBar.setText("IP-adress:"+IPAdress+"\n"+"  Port code: "+portCode);
        informationBar.setBounds(420,200,600,300);
        this.repaint();    
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
    	messageList.add(newMessage); 
    	HTMLDocument d = (HTMLDocument)displayField.getDocument();
    	try {
			d.insertBeforeEnd(d.getDefaultRootElement().getElement(0), newMessage);
		} catch (BadLocationException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
			// this should never be reachable as we always have a body
		}
    }

	public List<JButton> buttons() {
		return buttons;
	}

	public AbstractButton getButton(Model.User.Button b) {
		switch(b) {
		case ENTER:
			return enterButton;
		case COLOR:
			return colorButton;
		case LOGOUT:
			return closeButton;
		}
		throw new IllegalArgumentException(
			"Did not correspond to any instantiated button.");
	}
    
}
  
	



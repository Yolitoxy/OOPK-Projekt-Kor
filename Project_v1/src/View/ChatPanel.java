package View;

import java.awt.*;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import Model.Message;

import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.*;


	
public class ChatPanel extends JPanel{
	private static final int X_SIZE=700;
    private static final int Y_SIZE=700;
    private JButton colorButton;
    private JButton closeButton;
    private JButton enterButton;
    private JTextField chatTextField;
    private JLabel informationBar;
    private JTextPane displayField;
    private ArrayList<String> messageList= new ArrayList<String>();
    
  
    
    public ChatPanel(){
    	
    	setLayout(null);
    	setSize(X_SIZE,Y_SIZE);

        
        enterButton = new JButton("Enter");
        closeButton= new JButton("Log out");
        colorButton=new JButton("Choose text-color");
        colorButton.setBackground(Color.CYAN);
        displayField= new JTextPane();
        chatTextField= new JTextField("Write your message here");
        informationBar= new JLabel("IP-adress:");
        
        chatTextField.setBounds(20,450,380,150);
        colorButton.setBounds(20,410,150,30);
        enterButton.setBounds(420,450,100,100);
        closeButton.setBounds(420,50,100,100);
        informationBar.setBounds(470,100,100,100);
        
        JScrollPane displayFieldContainer = new JScrollPane(displayField);
        displayField.setContentType("text/html");
        displayField.setEditable(false);
        displayField.setSize(400,400);
        
        
        add(enterButton);
        add(chatTextField);
        add(informationBar);
        add(chatTextField);
        add(colorButton);
        add(closeButton);
        add(displayField);
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
    
}
  
	



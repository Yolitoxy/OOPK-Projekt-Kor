package view;

import javax.swing.AbstractButton;

import model.ChatEvent;

public interface SessionView {
	public AbstractButton getButton(controller.User.Button b);
	
	public void displayMessage(String m);
	
	public String getNewSentMessage();
	
	public void display();
	
	public void display(ChatEvent e);
}

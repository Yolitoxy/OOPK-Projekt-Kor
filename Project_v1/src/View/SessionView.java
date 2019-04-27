package View;

import javax.swing.AbstractButton;

public interface SessionView {
	public AbstractButton getButton(Model.User.Button b);
	public void displayMessage(String m);
	public String getNewSentMessage();
}

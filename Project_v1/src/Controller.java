import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import Model.*;
import View.ChatFrame;



public class Controller extends WindowAdapter implements PropertyChangeListener {
	private User myUser;
	private ChatFrame myFrame;
	
	public void propertyChange(PropertyChangeEvent evt) {
		String inMessage = ((String)evt.getNewValue());
		String ID = ((String)evt.getPropertyName());
		System.out.println("Controller recieved command "+ID);
		switch(ID) {
			case "message":
			// 	a bonafide message that is supposed to be displayed
				myFrame.updateMessage(inMessage);
				break;
			case "disconnect":
				System.out.println("what m8");
			//	in this case, the other user disconnected, and we
			//	display that we received this information and close the socket
				myFrame.updateMessage(inMessage);
				myUser.closeConnection();
				break;
			default:
			//	undiagnosed error
		}
		
	}
	
	Controller() {
		myUser = new Model.User("DefaultUser","BLACK",(PropertyChangeListener)this);
		try {
			myFrame = new View.ChatFrame(myUser);
			myFrame.addWindowListener(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Controller myController = new Controller();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		myUser.closeConnection();
		System.exit(0);
	}

}

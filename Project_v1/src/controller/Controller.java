package controller;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


// The runnable class.
public class Controller 
	extends WindowAdapter 
	implements ActionListener {
	private List<User> sessions;
	private view.ChatFrame myFrame;
	
	Controller() {
		try {
			myFrame = new view.ChatFrame();
			myFrame.addWindowListener(this);
			myFrame.subscribe((ActionListener)this);
			
			sessions = new ArrayList<>();
			
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
		for(User u: sessions) {
			u.closeConnection();
		}
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		

		System.out.println("Controller: poked");
		
		//	If Client button was pressed, read the user
		//	entered text from the relevant lines and
		//	try to connect.
		if(e.getSource() == myFrame.getPopUpPanel().getClientButton()) {
			String userName = myFrame.getPopUpPanel().getUserName();
			String IP = myFrame.getPopUpPanel().getIP();
			int portCode = myFrame.getPopUpPanel().getPortcode();
			
			System.out.println("connecting as "+userName);
			User u = new User(userName, Color.BLACK, myFrame);
			u.connectTo(IP,portCode);
			sessions.add(u);
			
		}
		
		//	If Server button was pressed, read the user
		//	entered text and try to set up a server.
		else if(e.getSource() == myFrame.getPopUpPanel().getServerButton()) {
			String userName = myFrame.getPopUpPanel().getUserName();
			int portCode = myFrame.getPopUpPanel().getPortcode();
			
			User u = new User(userName, Color.BLACK, myFrame);
			u.hostOnce(portCode);
			sessions.add(u);
			System.out.println("hosting as "+userName+" at port "+portCode);
			
		}
		
		
	}

}

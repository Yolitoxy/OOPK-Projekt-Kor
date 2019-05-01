package model;

import java.awt.Color;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import model.ChatEvent;

public class HTMLSerializer {
	boolean messageBroken = false;
	private int id;
	
	HTMLSerializer(int id) {
		this.id = id;
	};
	
	public int getID() {
		return id;
	}
	
	ChatEvent interpretMessage(Document doc) {
		Element msg  = (Element)doc.getFirstChild();
		Element txt = (Element)msg.getElementsByTagName("text").item(0);
		Element dc = (Element)msg.getElementsByTagName("disconnect").item(0);
				
		String sender = msg.getAttribute("sender");
		Color color = null;
		try {
			color = Message.fromHex(txt.getAttribute("color"));
		} catch (IllegalArgumentException e) {
			// It does not matter if the color is missing at this point.
		}
		
		// serializeTextContent is the most substantial method called here.
		String returnText = "";
		for(int i = 0; i < txt.getChildNodes().getLength(); i++) {
			returnText += serializeTextContent(txt.getChildNodes().item(i));
		}
		ChatEvent evt = null;
		if(dc != null) {
			evt = ChatEvent.from(id).disconnected(sender);
		} else if (msg != null) {
			Message m = new Message(sender, color, returnText);
			evt = ChatEvent.from(id).message(m);
		}
		
		if(messageBroken == true) {
			evt.broken();
		}
		System.out.println("Message broken stats: "+messageBroken);
		return evt;
	}
	
	String serializeTextContent(Node txt) {
		if (txt.getNodeType()==Node.TEXT_NODE) {
			return txt.getTextContent();
		}
		else if (txt.getNodeName() == "fetstil") {
			String formatBold = "<b>";
			for(int i = 0; i < txt.getChildNodes().getLength(); i++) {
				formatBold += serializeTextContent(txt.getChildNodes().item(i));
			}
			formatBold += "</b>";
			return formatBold;
		}
		else if (txt.getNodeName() == "kursiv") {
			String formatItalic = "<i>";
			for(int i = 0; i < txt.getChildNodes().getLength(); i++) {
				formatItalic += serializeTextContent(txt.getChildNodes().item(i));
			}
			formatItalic += "</i>";
			return formatItalic;
		}
		System.out.println("message found to be broken");
		messageBroken = true;
		return "";
	}
	
	static String errorMessageOnBroken() {
		return "<div style = 'color:red;'>"
				+ "Last message carries uninterpreted content."
				+ "</div>";
	}
	
	
	
	String displayNickname(String nick) {
    	if (nick == null) {
    		return "";
    	} else {
    		return "["+nick+"]: ";
    	}
    }
	
	public static String toHtml(ChatEvent e) {
		String myHtml = "";
		switch(e.getType()) {
		case CONNECT:
			myHtml ="<div>"
					+	Message.escapeXml(e.getSender())+" has connected."
					+"</div>";
			break;
		case DISCONNECT:
			myHtml = "<div>"
					+	Message.escapeXml(e.getSender())+" has disconnected."
					+"</div>";
			break;
		case MESSAGE:
			myHtml = e.getMessage().toHTML();
			break;
		}
		if(e.isBroken()) {
			myHtml += errorMessageOnBroken();
		}
		return myHtml;
	}
}
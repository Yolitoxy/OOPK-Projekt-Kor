package main;

import java.awt.Color;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MessageParser {
	
	String sender;
	Color c;
	String body;
	Status s = Status.SUCCESSFUL;
	SEvent.Type type;
	
	enum Status {
		SUCCESSFUL,
		BROKEN;
	}
	
	MessageParser() {};
	
	Status getStatus() {
		return s;
	}
	
	void readContents(Document doc) {
		Element msg  = (Element)doc.getFirstChild();
		
		// First make sure that the message is of proper form.
		// If it is, gather appropriate attributes and start
		// recursing on its content.
		if (msg.getNodeName() == "message") {
			sender = msg.getAttribute("sender");
			Element content = (Element)msg.getFirstChild();
			
			if (content.getNodeName() == "text") {
				try {
					c = MessageProtocol.fromHex(content.getAttribute("color"));
				} catch(IllegalArgumentException e) {
					s = Status.BROKEN;
				}
				for(int i = 0; i < content.getChildNodes().getLength(); i++) {
					readTextContents(content.getChildNodes().item(i));
				}
				type = SEvent.Type.MESSAGE;
			} else if (content.getNodeName() == "disconnect") {
				type = SEvent.Type.DISCONNECT;
			}
		}
	}
	
	void readTextContents(Node txt) {
		if (txt.getNodeType() == Node.TEXT_NODE) {
			body += txt.getTextContent();
		}
		else if (txt.getNodeType() == Node.ELEMENT_NODE) {
			if (!(txt.getNodeName() == "fetstil") 
				&& !(txt.getNodeName() == "kursiv")) {
				s = Status.BROKEN;
			}
		} else {
			s = Status.BROKEN;
		}
	}
	
	
}
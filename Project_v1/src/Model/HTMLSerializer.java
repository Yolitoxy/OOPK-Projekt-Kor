package Model;

import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class HTMLSerializer {
	boolean messageFound = false;
	boolean messageBroken = false;
	
	HTMLSerializer() {};
	
	Optional<String> serializeMessage(Document doc) {
		Element msg  = (Element)doc.getFirstChild();
		Element txt = (Element)msg.getElementsByTagName("text").item(0);
		
		// First make sure that the message is of proper form.
		// If it is, gather appropriate attributes and start
		// recursing on its content.
		if (msg.getNodeName() == "message" && txt != null) {
			messageFound = true;
			String nick = msg.getAttribute("sender");
			String color = txt.getAttribute("color");
			
			// serializeTextContent is the most substantial method called here.
			String returnText = "";
			for(int i = 0; i < txt.getChildNodes().getLength(); i++) {
				returnText += serializeTextContent(txt.getChildNodes().item(i));
			}
			
			if (messageBroken == true) {
				return Optional.of("<div style='color:"+color+"'>"+
    					displayNickname(nick)+
    					returnText+
    					"</div>"+
    					errorMessageOnBroken());
			} else {
				return Optional.of("<div style='color:"+color+";'>"+
    					displayNickname(nick)+
    					returnText+
    					"</div>");
			}
		} else {
			messageFound = false;
			return Optional.empty();
		}
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
		messageBroken = true;
		return "";
	}
	
	String errorMessageOnBroken() {
		return "<div style = 'color:red; font-style:red;'>"
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
}
package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Message {
	private String sender;
	private Color c;
	private String body;
	
	Message(String sender, Color c, String body) {
		this.sender = sender;
		this.c = c;
		this.body = body;
	}
	
	String getSender() {
		return sender;
	}
	
	Color getColor() {
		return c;
	}
	
	String getBody( ) {
		return body;
	}
	
	String toXML() {
		String myXML = "<message sender='"+escapeXml(sender)+"'>"
                + "<text color='"+hexString(c)+"'>"
                + escapeXml(body)+"</text></message>";
		return myXML;
	}
	
    static Pattern colorPattern =
            Pattern.compile("#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})");
 
    
    public static String createXML(String username, Color color, String message){
        String myXML = "<message sender='"+escapeXml(username)+"'>"
                + "<text color='"+hexString(color)+"'>"
                + escapeXml(message)+"</text></message>";
        return myXML; 
    }
    
    public static String hexString(Color c) {
    	String r = Integer.toHexString(c.getRed());
    	if (r.length()<2) r = "0"+r;
    	String g = Integer.toHexString(c.getGreen());
    	if (g.length()<2) g = "0"+g;
    	String b = Integer.toHexString(c.getBlue());
    	if (b.length()<2) b = "0"+b;
        return "#"+r+g+b;
    }
    
    public String toHTML() {
    	String myHTML =
    		"<div style='color:"+hexString(this.getColor())+";'>"
    		+	senderToString(escapeXml(this.getSender()))
    		+	escapeXml(getBody())
    		+"</div>";
    	return myHTML;
    }
    
    public static Color fromHex(String s) {
		Matcher m = colorPattern.matcher(s);
		if (m.matches()) {
			int r = Integer.parseInt(m.group(1), 16);
			int g = Integer.parseInt(m.group(2), 16);
			int b = Integer.parseInt(m.group(3), 16);
			return new Color(r,g,b);
		} else {
			throw new IllegalArgumentException("String in illegal format");
		}
	}
    
    private static String senderToString(String sender) {
		return "["+escapeXml(sender)+"]: ";
	}
    
    public static String logoutXml(String sender) {
    	return "<message sender='"+escapeXml(sender)+"'><disconnect/></message>";
    }
    
    public static String escapeXml(String s) {
        String outString = "";
        for (int i=0; i<s.length(); i++) {
            outString += escapeXml(s.charAt(i));
        }
        return outString;
    }
    
    public static String escapeXml(char c) {
        switch (c) {
        case '&':
            return "&amp;";
        case '"':
            return "&quot;";
        case '<':
            return "&lt;";
        case '>':
            return "&gt;";
        default:
            return String.valueOf(c);
        }
    }
    
    public static ChatEvent readXML(int id, String XMLIn)
    		throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder db =
        		DocumentBuilderFactory.newInstance().newDocumentBuilder();
        SchemaFactory sf = 
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema sch =
        		sf.newSchema(new StreamSource(new File("src/model/MessageSchema.xsd")));
        Validator val = sch.newValidator();
        
        Document messageDoc = db.parse(new InputSource(new StringReader(XMLIn)));
        val.validate(new DOMSource(messageDoc));
        
        HTMLSerializer ser = new HTMLSerializer(id);
        return ser.interpretMessage(messageDoc);
    }
    
    
}

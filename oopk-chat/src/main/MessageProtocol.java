package main;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class MessageProtocol {
	static Pattern colorPattern =
			Pattern.compile("#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})");
	static String msch = "MessageSchema.xsd"; 
	
	public static String serialize(Message m){
		String out =
			"<message sender='"+escapeXML(m.getSender())+"'>"
			+	"<text color='#"+hexString(m.getColor())+"'>"
			+		escapeXML(m.getBody())
			+	"</text>"
			+"</message>";
		return out; 
    }
	
	public static String escapeXML(String s) {
    	String out = "";
    	for (int i=0; i<s.length(); i++) {
    		out += escapeXML(s.charAt(i));
    	}
    	return out;
    }
	
	public static String escapeXML(char c) {
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
	
	public static String hexString(Color c) {
		var out = Integer.toHexString(c.getRed())
				+ Integer.toHexString(c.getGreen())
				+ Integer.toHexString(c.getBlue());
		return out;
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
	
	void parse(String s) {
		SchemaFactory msf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema ms = msf.newSchema(new URL(msch));
			SAXParserFactory jpf = SAXParserFactory.newInstance();
			jpf.setSchema(ms);
			SAXParser p = jpf.newSAXParser();
		} catch (SAXException e) {
			System.out.println("Failed to interpret MessageSchema.xsd");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("Failed to find MessageSchema.xsd");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			System.out.println("Failed to configure parser");
			e.printStackTrace();
		}
	}
}

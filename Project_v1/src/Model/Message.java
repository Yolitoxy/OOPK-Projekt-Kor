package Model;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Message {
    static Pattern colorPattern =
            Pattern.compile("#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})");
 
    public Message() {
    }
    
    public static String createXML(String username, Color color, String message){
        String myXML = "<message sender='"+escapeXml(username)+"'>"
                + "<text color='"+hexString(color)+"'>"
                + escapeXml(message)+"</text></message>";
        return myXML; 
    }
    
    public static String hexString(Color c) {
        String out = "#" + Integer.toHexString(c.getRed())
                + Integer.toHexString(c.getGreen())
                + Integer.toHexString(c.getBlue());
        return out;
    }
    

    public static String createXML(User user, String message) {
        return createXML(user.getUsername(),user.getColor(),message);
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
    
    public static Optional<String> readXML(String XMLIn) {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(
                    new InputSource(new StringReader(XMLIn)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        HTMLSerializer s = new HTMLSerializer();
        Optional<String> html = s.serializeMessage(doc);
        return html;
    }
    
    public static void main(String[] args) {
        Optional<String> html = readXML(
                "<message sender='aaa'>"
                + "<text color='black'>Write your message here</text>"
                + "</message>");
        System.out.println(html);
    }
}

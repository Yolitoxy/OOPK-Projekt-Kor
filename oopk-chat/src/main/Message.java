package main;

import java.awt.Color;

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
	
	String getBody() {
		return body;
	}
}

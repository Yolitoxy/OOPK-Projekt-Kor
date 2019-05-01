package model;


public class ChatEvent {
	public enum Type {
		CONNECT,
		DISCONNECT,
		HOSTING,
		MESSAGE,
	}
	
	private int sourceID;
	private Message m;
	private Type t;
	private boolean broken;
	private String sender;
	
	private ChatEvent(int id) {
		sourceID = id;
		sender = "?";
		broken = false;
	}
	
	public static ChatEvent from(int id) {
		return new ChatEvent(id);
	}
	
	public ChatEvent message(Message m) {
		this.t = Type.MESSAGE;
		this.m = m;
		sender = m.getSender();
		return this;
	}
	
	public ChatEvent disconnected(String sender) {
		this.t = Type.DISCONNECT;
		this.sender = sender;
		return this;
	}
	
	public ChatEvent connected(String sender) {
		this.t = Type.CONNECT;
		this.sender = sender;
		return this;
	}
	
	public ChatEvent broken() {
		this.broken = true;
		return this;
	}
	
	public int getID() {
		return sourceID;
	}
	
	public Type getType() {
		return t;
	}
	
	public Message getMessage() {
		return m;
	}
	
	public String getSender() {
		return sender;
	}
	
	public boolean isBroken( ) {
		return broken;
	}

	public ChatEvent hosting(String sender) {
		this.t = Type.HOSTING;
		return this;
	}
}

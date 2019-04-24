package main;

public class SEvent {
	private Type t;
	private Message m;
	
	enum Type {
		MESSAGE,
		CONNECT,
		DISCONNECT,
	}
	
	SEvent(Type t) {
		switch (t) {
		case MESSAGE: throw new IllegalArgumentException(); 
		case CONNECT: this.t = t;
		case DISCONNECT: this.t = t;
		}
	}
	
	SEvent(Type t, Message m) {
		switch (t) {
		case MESSAGE: {
			this.m = m;
			this.t = t;
		} 
		case CONNECT: throw new IllegalArgumentException();
		case DISCONNECT: throw new IllegalArgumentException();
		}
	}
	
	Type getType() {
		return t;
	}
	
	Message getMessage() {
		switch (t) {
		case MESSAGE: ;
		case CONNECT: throw new IllegalArgumentException();
		case DISCONNECT: throw new IllegalArgumentException();
		}
		return m;
	}
}

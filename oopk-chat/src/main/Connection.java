package main;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection implements Closeable {
	Socket s;
	BufferedReader r;
	PrintWriter w;
	
	Connection(Socket s) throws IOException {
		this.s = s;
		r = new BufferedReader(
			new InputStreamReader(
			s.getInputStream()));
		w = new PrintWriter(
			s.getOutputStream(), true);
	}
	
	void send(String m) {
		w.println(m);
	}
	
	String readLine() throws IOException {
		return r.readLine();
	}
	
	public void close() throws IOException {
		s.close();
		r.close();
		w.close();
	}
}

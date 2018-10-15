package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocket servSocket = new ServerSocket(2500);
		System.out.println("Starting Up...");
		
		try (Socket s = servSocket.accept()) {
			System.out.println("Connection recieved");
			
			InputStream sockIn = s.getInputStream();
			BufferedReader sockReader = new BufferedReader(new InputStreamReader(sockIn));
			String line = sockReader.readLine();
			System.out.println("Read the line \"" + line + "\"");
			
			PrintStream sockOut = new PrintStream(s.getOutputStream());
			sockOut.println(line);
			System.out.println("Echoed the line back to the client");
			Thread.sleep(1000);
			sockOut.flush();
			
			System.out.println("Closing connection");
			servSocket.close();
		}
	}
}

package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

	public static void main(String[] args) throws IOException, InterruptedException {	
		for (int i = 0; i < 5; i++) {
			try (Socket sock = new Socket("localhost", 2500)) {
				System.out.println("Connection established");
				Thread.sleep(1000);
//				try (PrintStream out = new PrintStream(sock.getOutputStream())) {
//					System.out.println("Sending text: \"Hello\"");
//					out.println("Hello");
//					BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//					String line = in.readLine();
//					System.out.println("Server said: \"" + line + "\"");
//				}
			}
		}
	}

}

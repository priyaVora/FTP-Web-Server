package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
	private static ExecutorService fileService;
	public static final int MAX_REQUEST= 19;
	public static Random random = new Random();

	public static void main(String[] args) throws IOException, InterruptedException {
//		for (int i = 0; i < 5; i++) {
//			try (Socket sock = new Socket("localhost", 2500)) {
//				System.out.println("Connection established");
//				Thread.sleep(1000);
//				
////				try (PrintStream out = new PrintStream(sock.getOutputStream())) {
////					System.out.println("Sending text: \"Hello\"");
////					out.println("Hello");
////					BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
////					String line = in.readLine();
////					System.out.println("Server said: \"" + line + "\"");
////				}
//			}

		// Establish the Connections
		try (Socket sock = new Socket("localhost", 2500)) {
			System.out.println("Connection established");
			sleep();
		}
		fileRequest();
	}

	private static void fileRequest() throws UnknownHostException, IOException {
		fileService = Executors.newFixedThreadPool(MAX_REQUEST);
		boolean run = true;
		int counter = 0;

		while (run)

		{
			Request r = new Request();
			fileService.submit(() -> r.run());

			if (counter == 20) {
				run = false;
			}
			counter++;
		}
		fileService.shutdownNow();

	}

	private static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

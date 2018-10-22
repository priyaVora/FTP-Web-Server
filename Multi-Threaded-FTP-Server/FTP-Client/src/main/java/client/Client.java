package client;

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
	public static int max_Request = 19;
	public static Random random = new Random();

	public Client(int request) {
		this.max_Request = request;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Client: make connection with Server");
		try (Socket sock = new Socket("localhost", 2500)) {
			System.out.println("Client: connection established");
			sleep();
			fileRequest(sock);
			System.out.println("Client Finished Session...");
		}
	}

	private static void fileRequest(Socket socket) throws UnknownHostException, IOException {
		fileService = Executors.newFixedThreadPool(max_Request);
		boolean run = true;
		int counter = 0;

		while (run)

		{
			Request r = new Request(socket);
			fileService.submit(() -> r.run());
			System.out.println();
			if (r.sessionEnded == true) {
				System.out.println("# " + r.getId() + " - request is processed.");
			}

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
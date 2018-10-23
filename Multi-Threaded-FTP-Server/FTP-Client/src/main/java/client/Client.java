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
	public static int max_Request = 20;
	public static Random random = new Random();
	public static BufferedReader sockReader;

	public Client(int request) {
		this.max_Request = request;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Client: make connection with Server");
		Socket sock = new Socket("localhost", 2500);

		// while(sock.isConnected() != false) {
		System.out.println("Client: connection established");
		sleep(1000);

		Thread sendingRequest = new Thread(new Runnable() {

			@Override
			public void run() {
				int counter = 0;
				try {

					fileRequest(sock);
					System.out.println("Client Finished Sending Request Session...");
					counter++;

				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				sleep(3500);

			}
		});

		Thread responseChecking = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						checkifResponse(sock);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		sendingRequest.start();
		responseChecking.start();
	}

	public static void checkifResponse(Socket s) throws IOException {
		InputStream sockIn = s.getInputStream();
		sockReader = new BufferedReader(new InputStreamReader(sockIn));
		readResponse(sockIn);
	}

	public static String readResponse(InputStream sockIn) throws IOException {
		String requestLine = "";
		String line = "";
		String c = "";
	
		while (sockReader.ready()) {
			c = sockReader.readLine();
			System.out.println(c);
			if (c.length() != 0) {
				line = c;
				requestLine += "\n";
				requestLine += line;
			}
		}
		return requestLine;
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
//			if (r.sessionEnded == true) {
//				System.out.println("# " + r.getId() + " - request is processed.");
//			}

			if (counter == max_Request) {
				run = false;
			}
			counter++;
		}
		fileService.shutdownNow();

	}

	private static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
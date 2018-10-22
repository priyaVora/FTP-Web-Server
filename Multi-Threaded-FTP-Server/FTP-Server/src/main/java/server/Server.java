package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Server {

	final static int CLIENTS = 20;
	final static int MAX_FILE_PROCESS = 25;

	private static ExecutorService FTP_service;
	private static Semaphore serverAcceptedFiles = new Semaphore(MAX_FILE_PROCESS);

	public static Random random = new Random();
	InputStream sockIn;
	public static BufferedReader sockReader;

	public static void main(String[] args) throws IOException, InterruptedException {
		process();
	}

	public static void process() throws IOException {
		ServerSocket servSocket = new ServerSocket(2500);
		FTP_service = Executors.newFixedThreadPool(MAX_FILE_PROCESS);
		System.out.println("Starting Up...");
		Socket s = servSocket.accept();
		System.out.println("Server Accepts Connection");
		while (true) {
			InputStream sockIn = s.getInputStream();
			sockReader = new BufferedReader(new InputStreamReader(sockIn));
			String request = readRequest(sockIn);

			if (request != null && request != "") {
				System.out.println("\nRequest Received:\n\t  " + request);

				Response response = new Response(s, "");

				FTP_service.submit(() -> {
					try {
						serverAcceptedFiles.acquire();
						
						//call responses's run method
						response.run();
						System.out.println("Response sent to Client...");
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
//				sockOut.flush();
//				System.out.println("Closing connection");
			// servSocket.close();
		}
	}

	public static String readRequest(InputStream sockIn) throws IOException {
		BufferedReader sockReader = new BufferedReader(new InputStreamReader(sockIn));
		String requestLine = "";
		String line = "";
		String c = "";

		while ((c = sockReader.readLine()) != null) {
			if (c.length() != 0) {
				line = c;
				requestLine += "\n";
				requestLine += line;
				//System.out.println(line);
			}

		}

		return requestLine;
	}

//	public static String readFile(String filePath) {
//		String fileContent = "";
//		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//			String sCurrentLine;
//			while ((sCurrentLine = br.readLine()) != null) {
//				fileContent += sCurrentLine;
//				fileContent += "\n";
//				System.out.println("sCurrentLine: " + sCurrentLine);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return fileContent;
//	}

	private static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
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
	public static Socket s;
	public InputStream sockIn;
	public static BufferedReader sockReader;

	public static void main(String[] args) throws IOException, InterruptedException {
		process();
	}

	public static void process() throws IOException {
		ServerSocket servSocket = new ServerSocket(2500);
		FTP_service = Executors.newFixedThreadPool(MAX_FILE_PROCESS);
		System.out.println("Starting Up...");
		s = servSocket.accept();
		System.out.println("Server Accepts Connection");

		InputStream sockIn = s.getInputStream();
		sockReader = new BufferedReader(new InputStreamReader(sockIn));

		ReadRequest readRequest = new ReadRequest(s, sockIn, sockReader, FTP_service);
		FTP_service.submit(() -> {
			System.out.println("Entered...");
//			try {
//				serverAcceptedFiles.acquire();
			
			sleep(4000);
			readRequest.start();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
		});
		// });
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

	// try {
//		serverAcceptedFiles.acquire();
//
//		// call responses's run method
//		response.run();
//		System.out.println("Response sent to Client...");
//
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}

	private static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
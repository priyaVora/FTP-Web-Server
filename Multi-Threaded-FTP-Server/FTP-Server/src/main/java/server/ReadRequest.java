package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class ReadRequest extends Thread {
	private static Object Locked = new Object();
	private Random gen = new Random();

	private Socket socket;
	public InputStream sockIn;
	public static BufferedReader sockReader;
	public static ExecutorService FTP_service;
	public Semaphore serverAcceptedFiles;

	public ReadRequest(Socket s, InputStream sockIn, BufferedReader sockReader, ExecutorService FTP_service,
			Semaphore serverAcceptedFiles) {
		this.socket = s;
		this.sockIn = sockIn;
		this.sockReader = sockReader;
		this.FTP_service = FTP_service;
		this.serverAcceptedFiles = serverAcceptedFiles;
	}

	@Override
	public void run() {
		super.run();

		try {
			process();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void process() throws IOException {
		String returnRequest = null;
		String requestLine = null;
		String line = "";
		String c = "";
		if (sockReader.ready()) {
			requestLine = "";
			int counter = 0;
			while ((c = sockReader.readLine()) != null) {
				// while ((c = sockReader.readLine()) != null) {

				if (c.length() != 0) {
					if (!c.equals("------------")) {
						line = c;
						requestLine += "\n";
						requestLine += line;
					} else {
						counter++;
						returnRequest = requestLine;

						if (returnRequest != null) {
							System.out.println(returnRequest);
						}

						if (returnRequest != null && returnRequest != "") {
							System.out.println("\nRequest Received:\n\t  " + counter + " " + returnRequest);
						}
						requestLine = "";
						Response response = new Response(socket, "");
						FTP_service.submit(() -> {
							try {
								serverAcceptedFiles.acquire();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("SUBMIT");
							response.start();
							serverAcceptedFiles.release();
						});

					}
				}

			}
			System.out.println("Server ended While Loop");

		}
	}
}

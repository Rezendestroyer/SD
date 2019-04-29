package br.ufu.sd;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Hello world!
 *
 */
public class Client {
	private static Socket socket = null;

	public static void main(String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {
			System.err.println("Não foi possivel se conectar ao servidor: " + host + ':' + port);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread menu = null;
		try {
			menu = new Thread(new MenuThread(new ObjectOutputStream(socket.getOutputStream())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Thread response = new Thread(new ResponseThread(socket));

		menu.start();
		response.start();

		try {
			menu.join();
			response.join(5000, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package br.ufu.sd;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MenuThread implements Runnable {
	private Scanner scn;
	private String command;
	ObjectOutputStream outputStream;

	public MenuThread(ObjectOutputStream _outputStream) {
		this.outputStream = _outputStream;
	}

	public void run() {
		scn = new Scanner(System.in);
		while (true) {
			System.out.println("\n----------- Lista de Comandos ------------");
			System.out.println("1. Insert <values>");
			System.out.println("2. Select <id>");
			System.out.println("3. Update <id> values <values>");
			System.out.println("4. Delete <id>");
			System.out.println("5. Exit");
			System.out.println("------------------------------------------");
			command = scn.nextLine();

			if (command.equals("Exit")) {
				break;
			} else {
				if (ValidateCommand(command)) {
					try {
						outputStream.flush();
						outputStream.writeObject(command);
						Thread.sleep(1000);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else
					System.out.println("\nComando inválido!");
			}
		}
		scn.close();
	}

	public boolean ValidateCommand(String text) {
		String[] elements = text.split(" ");

		switch (elements[0].toLowerCase()) {
		case "insert":
			if (elements.length < 2)
				return false;
			break;
		case "select":
			if (elements.length < 2)
				return false;
			break;
		case "delete":
			if (elements.length != 2)
				return false;
			try {
				Integer.parseInt(elements[1]);
			} catch (NumberFormatException e) {
				return false;
			}
			break;
		case "update":
			if (elements.length < 4 || !elements[2].equals("values"))
				return false;
			try {
				Integer.parseInt(elements[1]);
			} catch (NumberFormatException e) {
				return false;
			}
			break;
		default:
			return false;
		}
		return true;
	}
}

package br.ufu.sd;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

public class ExecutionThread implements Runnable {
	private final BlockingQueue<Input> queue;
	static private Map<BigInteger, byte[]> dataBase = new HashMap<BigInteger, byte[]>();
	long nextKey = 0;

	public ExecutionThread(BlockingQueue<Input> _queue) {
		this.queue = _queue;
	}

	public void run() {
		try {
			while (true) {
				Input input = queue.take();
				String[] cmdArgs = input.getCommand().split(" ");
				String cmd = cmdArgs[0].toLowerCase();
				String arg = cmdArgs[1];
				String response = "\n";

				if (cmd.compareTo("insert") == 0) {
					BigInteger key = BigInteger.valueOf(nextKey++);
					dataBase.put(key, arg.getBytes());
				} else if (cmd.compareTo("select") == 0) {
					if (arg.compareTo("*") == 0) {
						for (Map.Entry<BigInteger, byte[]> pair : dataBase.entrySet()) {
							response += pair.getKey().toString() + " " + new String(pair.getValue()) + "\n";
						}
					} else {
						BigInteger key = BigInteger.valueOf(Long.parseLong(arg));
						if (dataBase.containsKey(key)) {
							response += key + " " + new String(dataBase.get(key)) + "\n";
						} else {
							response += "Não existe valores para o id: " + key.toString() + "\n";
						}
					}
				} else if (cmd.compareTo("update") == 0) {
				} else if (cmd.compareTo("delete") == 0) {
					
				}

				System.out.println("Comando executado: " + cmd);

				ObjectOutputStream outputStream = input.getOutputStream();

				if (outputStream != null) {
					outputStream.flush();
					outputStream.writeObject(response);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

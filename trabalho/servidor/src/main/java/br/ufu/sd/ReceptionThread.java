package br.ufu.sd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

public class ReceptionThread implements Runnable {
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final BlockingQueue<Input> queue;

    public ReceptionThread(ObjectInputStream _inputStream, ObjectOutputStream _outputStream, BlockingQueue<Input> _queue) {
        this.inputStream = _inputStream;
        this.outputStream = _outputStream;
        this.queue = _queue;
    }

    public void run() {
        try {
            while (true) {
                queue.put(new Input(outputStream, inputStream.readObject().toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

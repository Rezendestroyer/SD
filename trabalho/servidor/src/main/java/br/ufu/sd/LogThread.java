package br.ufu.sd;

import java.util.concurrent.BlockingQueue;

public class LogThread implements Runnable {
    private final BlockingQueue<Input> queue;
    private Log log = new Log();
    private String command;

    public LogThread(BlockingQueue<Input> _queue) {
        this.queue = _queue;
    }

    public void run() {
        try {
            log.open();
            while (true) {
                command = queue.take().getCommand();
                if (!command.contains("Select")) log.write(command);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            log.close();
        }
    }
}

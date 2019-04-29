package br.ufu.sd;

import java.util.concurrent.BlockingQueue;

public class OrganizerThread implements Runnable {
    private final BlockingQueue<Input> queue1;
    private final BlockingQueue<Input> queue2;
    private final BlockingQueue<Input> queue3;

    private Input input;

    public OrganizerThread(BlockingQueue<Input> _queue1, BlockingQueue<Input> _queue2, BlockingQueue<Input> _queue3) {
        queue1 = _queue1;
        queue2 = _queue2;
        queue3 = _queue3;
    }

    public void run() {
        try {
            while (true) {
                input = queue1.take();
                queue2.add(input);
                queue3.add(input);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

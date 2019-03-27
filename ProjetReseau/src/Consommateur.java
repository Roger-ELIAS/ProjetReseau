import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class Consommateur implements Runnable {

    private ArrayBlockingQueue<String> queue;
    private Socket client;

    public Consommateur(ArrayBlockingQueue<String> queue, Socket client) {
        this.queue = queue;
        this.client = client;
    }

    @Override
    public void run() {
        String message;
        while((message = queue.poll())!=null){
            try {
                this.client.getOutputStream().write(message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

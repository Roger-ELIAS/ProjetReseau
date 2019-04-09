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
        while(true){
            try {
                message = queue.take();
                if(client.isClosed())
                    break;
                this.client.getOutputStream().write(message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class ClientClavardamuHandler implements Runnable{

    private Socket client;
    private ArrayBlockingQueue<String> queue;


    public ClientClavardamuHandler(Socket client, ArrayBlockingQueue<String> queue) {
        this.client = client;
        this.queue = queue;
    }

    @Override
    public void run() {

    }
}

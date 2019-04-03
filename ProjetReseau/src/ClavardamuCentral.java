import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClavardamuCentral {
    private int port;
    private ArrayList<ArrayBlockingQueue<String>> queueList;


    public ClavardamuCentral(int port) {
        this.port = port;
        this.queueList = new ArrayList<ArrayBlockingQueue<String>>();
    }

    public void startServer(){
        try {
            ServerSocket server = new ServerSocket(port);
            server.setReuseAddress(true);
            ExecutorService pool = Executors.newCachedThreadPool();
            int i = 0;
            while(true){
                ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
                Socket socket = server.accept();
                this.queueList.add(queue);
                pool.execute(new Consommateur(queue,socket));
                pool.execute(new Producteur(this.queueList, i, socket));
                ++i;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayBlockingQueue<String>> getQueueList() {
        return queueList;
    }
}

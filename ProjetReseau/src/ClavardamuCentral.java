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
    private ArrayList<Socket> socketList;

    public ClavardamuCentral(int port) {
        this.port = port;
        this.queueList = new ArrayList<ArrayBlockingQueue<String>>();
        this.socketList = new ArrayList<>();
    }

    public void startServer(){
        try {
            ServerSocket server = new ServerSocket(port);
            server.setReuseAddress(true);
            ExecutorService pool = Executors.newCachedThreadPool();
            while(true){
                ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
                pool.execute(new Consommateur(new ArrayBlockingQueue<String>(100),server.accept()));
                pool.execute(new Producteur());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

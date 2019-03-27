import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SalonCentral {
    private int port;

    public SalonCentral(int port) {
        this.port = port;
    }

    public void startServer(){
        try {
            ServerSocket server = new ServerSocket(port);
            server.setReuseAddress(true);
            ExecutorService pool = Executors.newCachedThreadPool();
            while(true){
                pool.execute(new ClientHandler(server.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

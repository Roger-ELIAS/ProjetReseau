import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class ServerInHandler implements Runnable {
    private Socket socket;
    private ArrayBlockingQueue<String> masterQueue;

    public ServerInHandler(Socket socket, ArrayBlockingQueue<String> masterQueue) {
        this.masterQueue = masterQueue;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            String message;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true){
                message = in.readLine();
                masterQueue.put(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

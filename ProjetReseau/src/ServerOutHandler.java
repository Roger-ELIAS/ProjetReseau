import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class ServerOutHandler implements Runnable {

    private ArrayList<Socket> serverArray;
    private ArrayBlockingQueue<String> masterQueue;

    public ServerOutHandler(ArrayList<Socket> serverArray, ArrayBlockingQueue<String> masterQueue) {
        this.serverArray = serverArray;
        this.masterQueue = masterQueue;
    }

    @Override
    public void run() {
        String message;
        while(true){
            try {
                message = masterQueue.take();
                for(Socket tmpSocket : serverArray){
                    tmpSocket.getOutputStream().write(message.getBytes());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

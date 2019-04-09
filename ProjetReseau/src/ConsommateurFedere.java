import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class ConsommateurFedere implements Runnable {

    private Socket socket;
    private ArrayList<ArrayBlockingQueue<String>> queueList;

    public ConsommateurFedere(Socket socket, ArrayList<ArrayBlockingQueue<String>> queueList) {
        this.socket = socket;
        this.queueList = queueList;
    }

    @Override
    public void run() {
        String message;
        while(true){
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                message = in.readLine();
                if(socket.isClosed())
                    break;
                for(ArrayBlockingQueue tmpQueue : queueList)
                    tmpQueue.put(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

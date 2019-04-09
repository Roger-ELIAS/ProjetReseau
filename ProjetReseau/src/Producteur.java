import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class  Producteur implements Runnable{

    private ArrayList<ArrayBlockingQueue<String>> queueList;
    private int position;
    private Socket socket;

    public Producteur(ArrayList<ArrayBlockingQueue<String>> queueList, int position, Socket socket) {
        this.queueList = queueList;
        this.position = position;
        this.socket = socket;
    }

    @Override
    public void run() {
        String message;
        String errorMessage;
        String pseudo;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            message = in.readLine();
            if(message != "" && message.substring(0,8).equals("CONNECT ")) {
                pseudo = message.substring(8);
                while (true) {
                    message = in.readLine();
                    if(message != "" && message.substring(0,4).equals("MSG ")) {
                        message = message.substring(4);
                        for (int i = 0; i < queueList.size(); ++i) {
                            if (i != this.position) {
                                queueList.get(i).add(pseudo + "> " + message + "\n");
                            }
                        }
                    }else{
                        errorMessage = "ERROR clavardamu. \n";
                        socket.getOutputStream().write(errorMessage.getBytes());
                    }
                }
            }
            else{
                errorMessage = "ERROR CONNECT aborting clavardamu protocol. \n";
                socket.getOutputStream().write(errorMessage.getBytes());
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

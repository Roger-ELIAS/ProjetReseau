import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class  ProducteurFedere implements Runnable{

    private ArrayList<ArrayBlockingQueue<String>> queueList;
    private Socket socket;
    private Socket masterSocket;

    public ProducteurFedere(Socket socket, Socket masterSocket) {
        this.socket = socket;
        this.masterSocket = masterSocket;
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
                        message = pseudo + "> " + message + "\n";
                        masterSocket.getOutputStream().write(message.getBytes());
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

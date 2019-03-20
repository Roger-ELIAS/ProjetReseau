import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private Socket client;
    private String pseudo;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        String message;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            message = in.readLine();
            String errorMessage;
            if(message != "" && message.substring(0,8).equals("CONNECT ")) {
                pseudo = message.substring(8); //On supprime CONNECT et sauvegarde le reste dans pseudo
                while(true){
                    message = in.readLine();
                    if(message != "" && message.substring(0,4).equals("MSG ")){
                        System.out.println(pseudo + ">" + message.substring(4));
                    }
                    else{
                        errorMessage = "ERROR clavardamu. \n";
                        client.getOutputStream().write(errorMessage.getBytes());
                    }
                }
            }
            else {
                errorMessage = "ERROR CONNECT aborting clavardamu protocol. \n";
                client.getOutputStream().write(errorMessage.getBytes());
                client.close();
            }


    } catch (IOException e) {
        e.printStackTrace();
    }
}
}

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class OutputHandler implements Runnable {
    private Socket client;

    public OutputHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while(true) {
                Scanner sc = new Scanner(System.in);
                String message;
                message = sc.nextLine();
                message += "\n";
                client.getOutputStream().write(message.getBytes());
            }
        }catch(IOException e ) {
            e.printStackTrace();
        }
    }
}

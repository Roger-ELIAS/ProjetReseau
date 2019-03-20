import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public void startClient() {
        Scanner sc = new Scanner(System.in);
        try {
            Socket client = new Socket("localhost",12345);
            String message;
            System.out.println("Saisir un pseudo à l'aide de la commande CONNECT pseudo");

            message = sc.nextLine();
            message += "\n";

            client.getOutputStream().write(message.getBytes());

            Thread outputThread = new Thread(new InputHandler(client));
            Thread inputThread = new Thread(new OutputHandler(client));
            outputThread.start();
            inputThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class InputHandler implements Runnable {
    private Socket client;

    public InputHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while(true) {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String inputString;
                if(in.ready()) {
                    inputString = in.readLine() + "\n";
                    System.out.println(inputString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

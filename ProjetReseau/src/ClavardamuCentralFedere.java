import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClavardamuCentralFedere {
    private int port;
    private ArrayList<ArrayBlockingQueue<String>> queueList;
    private Socket masterSocket;
    private ArrayList<Socket> socketArray;
    private boolean isMaster = true;
    private ArrayBlockingQueue<String> masterQueue;
    public ClavardamuCentralFedere(int port) {
        this.port = port;
        this.queueList = new ArrayList<ArrayBlockingQueue<String>>();
        this.socketArray = new ArrayList<Socket>();

    }

    public void startServer(String confFileName){
        try {
            ServerSocket server = new ServerSocket(port);
            server.setReuseAddress(true);
            ExecutorService pool = Executors.newCachedThreadPool();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(confFileName)));
            String line;
            int pos = 0;
            int spacePos = 0;
            String connectIP;
            String connectPort;
            while((line=bufferedReader.readLine())!=null){
                if(line.substring(0,6).equals("master")){
                    isMaster = false;
                    line = line.substring(9);
                    for(int i = 0; i < line.length(); ++i){
                        if(line.charAt(i)==' ') {
                            spacePos = i;
                            break;
                        }
                    }
                    connectIP = line.substring(0,spacePos);
                    connectPort = line.substring(spacePos+1,line.length());
                    this.masterSocket = new Socket(connectIP, Integer.parseInt(connectPort));
                }
                else{
                    line = line.substring(7);
                    for(int i = 0; i < line.length(); ++i){
                        if(line.charAt(i)==' ') {
                            spacePos = i;
                            break;
                        }
                    }
                    connectIP = line.substring(0,spacePos);
                    connectPort = line.substring(spacePos+1,line.length());
                    socketArray.add(new Socket(connectIP,Integer.parseInt(connectPort)));
                }

            }
            int i = 0;
            while(true){
                ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
                Socket socket = server.accept();
                this.queueList.add(queue);
                pool.execute(new Consommateur(queue,socket));
                pool.execute(new Producteur(this.queueList, i, socket));
                ++i;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayBlockingQueue<String>> getQueueList() {
        return queueList;
    }
}

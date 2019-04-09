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
    private ServerSocket server;
    private int port;
    private ArrayList<ArrayBlockingQueue<String>> queueList;
    private Socket masterSocket;
    private ArrayList<Socket> serverArray;
    private boolean isMaster = true;
    private ArrayBlockingQueue<String> masterQueue;
    public ClavardamuCentralFedere(int port) {
        this.port = port;
        this.queueList = new ArrayList<ArrayBlockingQueue<String>>();
        this.serverArray = new ArrayList<Socket>();

    }

    public void startServer(){
        try {
            server = new ServerSocket(port);
            server.setReuseAddress(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void interconnectServer(String confFileName){
        try {
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
                    serverArray.add(new Socket(connectIP,Integer.parseInt(connectPort)));
                }

            }
            int i = 0;
            if(isMaster){
                this.masterQueue = new ArrayBlockingQueue<>(100);
                for(Socket tmpSocket : serverArray){
                    pool.execute(new ServerInHandler(tmpSocket,masterQueue)); //Permet au master de récupérer tous les messages et de les placer dans sa queue
                }
                pool.execute(new ServerOutHandler(serverArray, masterQueue)); //Le master envoie les messages contenus dans sa queue dans chacun des serveurs
            }
            while(true){
                ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
                Socket socket = server.accept();
                this.queueList.add(queue);
                pool.execute(new ConsommateurFedere(socket,queueList));
                pool.execute(new ProducteurFedere(socket, masterSocket));
                pool.execute(new Consommateur(queueList.get(i),socket));
                pool.execute(new Producteur(queueList,i,socket));
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

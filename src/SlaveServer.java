import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SlaveServer implements Runnable {

    private int port;
    protected static List<Socket> slavesList = Collections.synchronizedList(new LinkedList<Socket>());

    public SlaveServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            // Infinite loop. Accepting slave connections and adding them to the list.
            while (true) {
                Socket slave = serverSocket.accept();
                slavesList.add(slave);
                new Thread(new SlaveHandler(slave)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static synchronized boolean isSlavesListEmpty() {

        return slavesList.isEmpty() ? true : false;
    }
}

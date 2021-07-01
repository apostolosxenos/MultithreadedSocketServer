import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MasterServer {

    private static final int CLIENTS_PORT = 11111;
    private static final int SLAVES_PORT = 55555;
    protected static LinkedList<Socket> clientsList = new LinkedList<>();
    protected static List<Integer> receivedNumbers = Collections.synchronizedList(new LinkedList<>());
    protected static List<Integer> duplicateNumbers = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(CLIENTS_PORT)) {

            serverSocket.setReuseAddress(true);

            System.out.println("Master Server is up and waiting client connections...");

            // Thread for Slave Server
            new Thread(new SlaveServer(SLAVES_PORT)).start();

            // Thread for broadcasting duplicate numbers
            new Thread(new ServerBroadcast()).start();

            // Infinite loop. Accepting client connections and adding them to the list.
            try {
                while (true) {
                    Socket client = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(client);
                    clientsList.add(client);
                    new Thread(clientHandler).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Synchronized method used to avoid threads' interference
    // when each of them check the availability of numbers' list, get and remove the first number
    protected static synchronized int pop() {

        if (!receivedNumbers.isEmpty()) {
            int firstNumber = receivedNumbers.get(0);
            receivedNumbers.remove(0);
            return firstNumber;
        }

        return -1;
    }
}
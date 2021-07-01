import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientHandler implements Runnable {

    private final Socket client;
    private BufferedReader in;
    protected PrintWriter out;
    private final SocketAddress clientId;

    public ClientHandler(Socket clientSocket) {
        client = clientSocket;
        clientId = client.getRemoteSocketAddress();
    }

    @Override
    public void run() {

        System.out.println(String.format("Client %s connected.", clientId));

        try {

            // Open streams
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

            while (true) {

                // Read input from client socket and parses it to integer
                String line = in.readLine();
                int number = Integer.parseInt(line);

                // If slaves list is not empty add the number to received numbers list.
                // Otherwise 'ignores' the number as there is no slave available to process it.
                if (!SlaveServer.isSlavesListEmpty()) {
                    addNumberToList(number);
                    System.out.println(String.format("Received %d from Client %s", number, clientId));
                }
            }
        } catch (IOException e) {
            // Removes a client socket from the list once it is closed
            MasterServer.clientsList.remove(client);
            closeSocket();
            System.err.println(String.format("Client %s closed.",client.getRemoteSocketAddress()));
        }
    }

    private void addNumberToList(int number) {
        MasterServer.receivedNumbers.add(number);
    }

    private void closeSocket() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (client != null) client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
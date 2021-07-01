import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SlaveHandler implements Runnable {

    private final Socket slave;
    private BufferedReader in;
    private PrintWriter out;
    private int slaveId;
    private static int slaveConnections = 0;


    public SlaveHandler(Socket slaveSocket) {
        slave = slaveSocket;
        slaveId = ++slaveConnections;
    }

    @Override
    public void run() {

        System.out.println(String.format("Slave #%d connected.", slaveId));

        try {

            // Open streams
            in = new BufferedReader(new InputStreamReader(slave.getInputStream()));
            out = new PrintWriter(slave.getOutputStream(), true);

            while (true) {

                // The first number from Server's synchronized received numbers list
                int number = MasterServer.pop();

                // If list was empty at that moment, jump to next loop
                if (number == -1) continue;

                // Writes number to slave socket
                out.println(number);

                // Read response from slave socket and converts it to integer
                String slaveResponse = in.readLine();
                int slaveResponseInt = Integer.parseInt(slaveResponse);

                // If slave's response is 1 means that slave has received the same number before
                if (slaveResponseInt == 1) {

                    // Saves the duplicate number to Server's duplicate numbers lists
                    MasterServer.duplicateNumbers.add(number);
                    System.out.println(number + " is duplicate on slave #" + slaveId);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    System.err.println(ie.getMessage());
                }
            }

        } catch (IOException e) {
            // Removes a slave socket once it is closed
            SlaveServer.slavesList.remove(slave);
            System.err.println("Slave " + slave.getRemoteSocketAddress() + " closed.");
            closeSlaveSocket();
        }

    }

    private void closeSlaveSocket() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (slave != null) slave.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

// Thread class used to broadcast duplicate numbers to all clients that are connected.
public class ServerBroadcast implements Runnable {

    @Override
    public void run() {
        try {
            while (true) {

                // If duplicates list is empty jumps to next loop
                if (MasterServer.duplicateNumbers.isEmpty()) continue;

                // Gets and removes the first number of the duplicates list
                broadcast(MasterServer.duplicateNumbers.get(0));
                MasterServer.duplicateNumbers.remove(0);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcast(int number) {

        PrintWriter out;

        for (Socket socket : MasterServer.clientsList) {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(String.format("[SERVER BROADCAST] Number %s has already been sent ", number));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

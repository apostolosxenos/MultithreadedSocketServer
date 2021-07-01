import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Client {

    private final static String SERVER_ADDRESS = "127.0.0.1";
    private final static int SERVER_PORT = 11111;

    public static void main(String[] args) {

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Thread for non-blocking input from Server. Mainly used to read Server's broadcast for duplicate numbers
            new Thread(new SocketReader(socket)).start();

            while (true) {

                // Sends a random integer [0-100] to Server
                int randomNumber = new Random().nextInt(101);
                out.println(randomNumber);
                System.out.println("Sent " + randomNumber);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {

            System.exit(0);
            e.printStackTrace();

        }
    }
}
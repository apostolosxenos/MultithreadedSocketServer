import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

public class Slave {

    private final static String SERVER_ADDRESS = "127.0.0.1";
    private final static int SERVER_PORT = 55555;
    private static HashSet<Integer> numbers = new HashSet<Integer>();

    public static void main(String[] args) {

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

             // Open streams
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            try {

                while (true) {

                    // Read Server's input and parses it to number
                    String serverInput = in.readLine();
                    int number = Integer.parseInt(serverInput);
                    System.out.println("Received " + number);

                    // If number exists in the HashSet responds 1, otherwise adds it to the HashSet and responds 0
                    if (!numbers.contains(number)) {
                        numbers.add(number);
                        out.println(0);
                    } else
                        out.println(1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
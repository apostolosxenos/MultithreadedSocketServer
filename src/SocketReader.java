import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

// Thread class used for non-blocking input stream
public class SocketReader implements Runnable {

    private Socket socket;
    private BufferedReader in;

    public SocketReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {

                String input = in.readLine();
                System.err.println(input);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
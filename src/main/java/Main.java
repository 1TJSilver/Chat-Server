import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Main {
    public static final int PORT = 8080;
    public static LinkedList<ChatService> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server Started");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ChatService(socket));
                } catch (IOException e) {

                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}
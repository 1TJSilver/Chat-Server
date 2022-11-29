import java.io.*;
import java.net.*;

public class ChatService extends Thread {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedWriter logger;
    File logFile;

    public ChatService(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        logFile = new File("file.log");
        logFile.createNewFile();
        logger = new BufferedWriter(new FileWriter(logFile));
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            try {
                while (true) {
                    word = in.readLine();
                    if (word.equals("stop")) {
                        downService();
                        break;
                    }
                    System.out.println("Echoing: " + word);
                    for (ChatService chat : Main.serverList) {
                        chat.send(word);
                    }
                }
            } catch (NullPointerException ignored) {
            }

        } catch (IOException e) {
            this.downService();
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }

    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ChatService chat : Main.serverList) {
                    if (chat.equals(this)) chat.interrupt();
                    Main.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {
        }
    }
    public void log(String message){
        try {
            logger.write(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

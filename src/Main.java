import fr.jima.service.client.ChatClient;
import fr.jima.service.server.ChatServer;

import javax.swing.*;
import java.io.IOException;

public class Main {


    public static void main(String[] args) {

        // Start Server
        ChatServer server = startServer(500);

        // Creating Clients
        startClient("127.0.0.1", 500, "alex");
        startClient("127.0.0.1", 500, "bob");
        startClient("127.0.0.1", 500, "Armin");
        startClient("127.0.0.1", 500, "Jana");

        // Listening entry event to Stop Server
        waitForExitRequest(server);

    }

    public static ChatServer startServer(int portNumber) {
        try {
            // Starting Server on given Port
            ChatServer server = new ChatServer(portNumber);
            server.start();
            return server;
        }
        catch (Throwable ex) {
            // Catching Error
            String msg = String.format("Not possible to Start server. Port %s is used.\n%s - %s",
                    portNumber, ex.getClass().getSimpleName(), ex.getMessage());
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static void startClient(final String hostName, final int hostPortNumber, String userName) {
        // Creating Client
        final ChatClient client = new ChatClient(userName);

        // Turning on
        // When creation is finish we connect Client
        client.start(() -> client.connect(hostName, hostPortNumber));
    }


    private static void waitForExitRequest(ChatServer server) {
        System.out.println("Press Enter to Stop the Server.");
        try {
            System.in.read();
        }
        catch (IOException e) {}
        System.out.println("Shutting down Server...");
        server.interrupt();
    }

}
